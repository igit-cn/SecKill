package com.cloudSeckill.service;

import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.info.UserInfo;
import com.cloudSeckill.data.request.LoginRequest;
import com.cloudSeckill.data.request.RegisterRequest;
import com.cloudSeckill.data.response.ProxyResultBean;
import com.cloudSeckill.utils.DataTransitionUtils;
import com.cloudSeckill.utils.RedisUtil;
import com.cloudSeckill.utils.SessionUtils;
import com.cloudSeckill.utils.ValidateImageUtils;
import com.proxy.constant.ResponseCode;
import com.proxy.dao.ProxyUserDaoMapper;
import com.proxy.entity.EmailEntity;
import com.proxy.entity.RechargeCodeEntity;
import com.proxy.entity.RechargeCodeTypeEntity;
import com.proxy.entity.UserEntity;
import com.proxy.service.BusinessService;
import com.proxy.service.EmailService;
import com.proxy.service.ProxyUserService;
import com.proxy.service.RechargeCodeService;
import com.proxy.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    
    @Autowired private RedisUtil redisUtil;
    @Autowired private UserMapper userMapper;
    @Autowired private HomeService homeService;
    @Autowired private ProxyUserService proxyUserService;
    @Autowired private EmailService emailService;
    @Autowired private RechargeCodeService rechargeCodeService;
    @Autowired private BusinessService businessService;
    @Autowired private ProxyUserDaoMapper proxyUserDaoMapper;
    /**
     * 登录
     */
    public UserInfo login(LoginRequest loginRequest, HttpSession session) {
        UserEntity userEntity = proxyUserDaoMapper.userLogin(loginRequest.userName, MD5Util.digest(loginRequest.userPass));
        UserInfo userInfo = DataTransitionUtils.proxyUserEntity2UserInfo(userEntity);
        
        if(userInfo.server_msg == null){
            //登录成功,缓存用户信息
            session.setAttribute(SessionUtils.USER_INFO, userInfo);
            session.setAttribute(SessionUtils.SESSION_ID, session.getId());
        }
        return userInfo;
    }
    
    /**
     * 生成验证码
     */
    public String createVerifyCode(HttpSession session) {
        ValidateImageUtils validateImageUtils = new ValidateImageUtils();
        validateImageUtils.getValidateImage();
        redisUtil.set(RedisUtil.VALIDATE_CODE + session.getId(), validateImageUtils.getValidateCode(), 1000L * 60 * 10);
        return validateImageUtils.getValidateCode();
    }

    /**
     * 校验验证码
     */
    public boolean checkVerifyCode(String verifyCode, HttpSession session) {
        String redisVerifyCode = redisUtil.getStr(RedisUtil.VALIDATE_CODE + session.getId());
        return verifyCode.equalsIgnoreCase(redisVerifyCode);
    }

    
    /**
     * 查询用户是否注册,邮箱是否存在
     */
    public List<UserEntity> queryUserRegister(String userName ,String email){
        return proxyUserDaoMapper.queryUserRegister(userName,email);
    }

    /**
     * 查询用户是否注册
     */
    public List<UserEntity> queryUserRegisterByName(String userName){
        return proxyUserDaoMapper.queryUserRegisterByName(userName);
    }
    
    /**
     * 发送邮箱
     */
    public ProxyResultBean sendEmailCode(String userName) {
        UserEntity userInfoByName = proxyUserService.getUserInfoByName(userName);
        ProxyResultBean proxyResultBean = new ProxyResultBean();
        if(userInfoByName == null){
            proxyResultBean.code = -1;
            proxyResultBean.msg = "用户名不存在 ,请检查";
            return proxyResultBean;
        }
        
        try {
            emailService.sendEmailCode(userInfoByName.email);
        } catch (Exception e) {
            proxyResultBean.code = -1;
            proxyResultBean.msg = "邮件发送失败";
            return proxyResultBean;
        }
        
        proxyResultBean.code = ProxyResultBean.response_code_success;
        proxyResultBean.msg = "发送成功";
        
        return proxyResultBean;
    }

    /**
     * 重置密码
     */
    public ProxyResultBean resetPassword(UserInfo userInfo) throws Exception{
        ProxyResultBean proxyResultBean = new ProxyResultBean();
        
        UserEntity userEntity = proxyUserService.getUserInfoByName(userInfo.userName);
        if (userEntity == null) {
            proxyResultBean.code = ResponseCode.EMAIL_NOT_EXIST;
            proxyResultBean.msg = "用户不存在";
            return proxyResultBean;
        }
        
        EmailEntity emailEntity = emailService.emailAlterPasswordForUser(userEntity, userInfo.emailVerifyCode, MD5Util.digest(userInfo.userPass));
        if (emailEntity == null) {
            proxyResultBean.code = ResponseCode.RANDOM_CODE_TIMEOUT;
            proxyResultBean.msg = "邮箱验证码错误";
            return proxyResultBean;
        }
        
        proxyResultBean.code = ResponseCode.RESPONSE_OK;
        proxyResultBean.msg = "修改成功";
        return proxyResultBean;
    }

    /**
     * 退出登录
     */
    public void userLogout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * 注册
     */
    public ProxyResultBean UserRegister(RegisterRequest registerRequest) {
        ProxyResultBean proxyResultBean = new ProxyResultBean();
        
        List<UserEntity> userEntities = queryUserRegisterByName(registerRequest.userName);
        if (userEntities.size() != 0) {
            proxyResultBean.code = ResponseCode.CURRENT_PROXY_ACCOUNT_EXIST;
            proxyResultBean.msg = userEntities.get(0).user_name.equals(registerRequest.userName) ? "用户名已存在" : "邮箱已经注册";
            return proxyResultBean;
        }
        
        //获取充值码信息
        List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeService.queryRechargeCodeList(1, 10, null, null, null, null, null, registerRequest.userActiveCode, null, null, null, null, null, null);
        //充值码不存在
        if (rechargeCodeEntities.size() == 0 || registerRequest.userActiveCode.length() != 10) {
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_NOT_EXIST;
            proxyResultBean.msg = "充值码不存在";
            return proxyResultBean;
        }
        
        //充值码状态不符合
        RechargeCodeEntity rechargeCodeEntity = rechargeCodeEntities.get(0);
        if (rechargeCodeEntity.sell_status == 0) {
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_NOT_EXIST;
            proxyResultBean.msg = "充值码未出售";
            return proxyResultBean;
        } else if (rechargeCodeEntity.recharge_status == 1) {
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_NOT_EXIST;
            proxyResultBean.msg = "充值码已被充值";
            return proxyResultBean;
        }  else if(rechargeCodeEntity.status == 2){
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_NOT_EXIST;
            proxyResultBean.msg = "充值码已被删除";
            return proxyResultBean;
        } else if(rechargeCodeEntity.status == 1){
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_NOT_EXIST;
            proxyResultBean.msg = "充值码已被冻结";
            return proxyResultBean;
        }
        
        //标记充值
        rechargeCodeService.rechargeToUser(registerRequest.userActiveCode,registerRequest.userName);
        //注册,增加用户使用时间
        long addUseTime = 0 ;
        List<RechargeCodeTypeEntity> rechargeCodeTypeList = businessService.getRechargeCodeTypeList();
        for (RechargeCodeTypeEntity rechargeCodeTypeEntity : rechargeCodeTypeList){
            if(rechargeCodeTypeEntity.type == rechargeCodeEntities.get(0).recharge_type){
                addUseTime = rechargeCodeTypeEntity.use_time;
                proxyUserDaoMapper.userRegister(registerRequest.userName, MD5Util.digest(registerRequest.userPass),registerRequest.userEmail,rechargeCodeTypeEntity.use_time + System.currentTimeMillis(),System.currentTimeMillis());
            }
        }
        
        //添加一个新的坑
        User user = new User();
        user.setFromUserName(registerRequest.userName);
        user.setExpirTime(new Date(System.currentTimeMillis() + addUseTime));
        //增加以下三个默认值，避免登陆后就时上线状态
        user.setHeadImg("");
        user.setName("");
        user.setOnlineStatus(2);
        homeService.addItem(user);
        proxyResultBean.code = ResponseCode.RESPONSE_OK; 
        proxyResultBean.msg = "注册成功";
        return proxyResultBean;
    }

    /**
     * 充值,没坑就添加一个坑
     */
    public ProxyResultBean rechargeCodeUp(String userName, Integer id, String rechargeCode) {
        
        //获取充值码信息
        List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeService.queryRechargeCodeList(1, 10, null, null, null, null, null, rechargeCode, null, null, null, null, null, null);
        
        ProxyResultBean proxyResultBean = new ProxyResultBean();
        RechargeCodeEntity rechargeCodeEntity = rechargeCodeEntities.get(0);
        if (rechargeCodeEntity.sell_status == 0) {
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_NOT_SELL;
            proxyResultBean.msg = "充值码未出售,无法充值";
            return proxyResultBean;
        } else if (rechargeCodeEntity.recharge_status == 1) {
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_RECHARGEED;
            proxyResultBean.msg = "充值码已被充值,无法使用";
            return proxyResultBean;
        } else if(rechargeCodeEntity.status == 1){
            proxyResultBean.msg = "充值码已被冻结";
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_STATUS_ERR;
            return proxyResultBean;
        }  else if(rechargeCodeEntity.status == 2){
            proxyResultBean.msg = "充值码已被删除";
            proxyResultBean.code = ResponseCode.RECHARGE_CODE_STATUS_ERR;
            return proxyResultBean;
        }
        
        //先出售
        businessService.sellRechargeCode(rechargeCode.split(","),userName);
        //后充值 , 改变充值吗状态
        rechargeCodeService.rechargeToUser(rechargeCode,userName);
        //添加充值使用时间
        long addUseTime = proxyUserService.addUserUseTime(rechargeCodeEntity.recharge_type, userName);
        if(id == -1){ //-1 代表注册
            User user = new User();
            user.setFromUserName(userName);
            user.setExpirTime(new Date(System.currentTimeMillis() + addUseTime));
            homeService.addItem(user);
            proxyResultBean.msg = "注册成功";
            proxyResultBean.code = ResponseCode.RESPONSE_OK;
            return proxyResultBean;
        }
        
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(id);
        List<User> userList = userMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userList)) {
            User user = userList.get(0);
            Date expirTime = user.getExpirTime();
            if (expirTime.getTime() < new Date().getTime()) {
                //小于说明用户已经不能使用了 ,从当前时间累加
                addUseTime = System.currentTimeMillis() + addUseTime;
            } else {
                //大于说明用户还有使用时间 ,从使用过期的时间累加
                addUseTime = expirTime.getTime() + addUseTime;
            }
            user.setExpirTime(new Date(addUseTime));
            userMapper.updateByExample(user, example);
        }

        proxyResultBean.msg = "充值成功";
        proxyResultBean.code = ResponseCode.RESPONSE_OK;
        return proxyResultBean;
    }

}
