package com.cloudSeckill.service;

import com.cloudSeckill.dao.domain.User;
import com.cloudSeckill.dao.domain.UserExample;
import com.cloudSeckill.dao.mapper.UserMapper;
import com.cloudSeckill.data.response.QueryRechargeCodeStatusBean;
import com.proxy.dao.RechargeCodeDaoMapper;
import com.proxy.entity.RechargeCodeEntity;
import com.proxy.entity.RechargeCodeTypeEntity;
import com.proxy.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {
    
    @Autowired UserMapper userMapper;
    @Autowired BusinessService businessService;
    @Autowired RechargeCodeDaoMapper rechargeCodeDaoMapper;

    public List<User> getKengList(String userName) {
        UserExample example = new UserExample();
        example.createCriteria().andFromUserNameEqualTo(userName);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    public User addItem(User user) {
        userMapper.insertSelective(user);
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(user.getId());
        List<User> userList = userMapper.selectByExample(example);
        return userList.get(0);
    }

    /**
     * 查询充值码状态
     */
    public QueryRechargeCodeStatusBean queryRechargeCodeStatus(String rechargeCode){
        List<RechargeCodeEntity> rechargeCodeEntities = rechargeCodeDaoMapper.queryRechargeCodeList(0, 1, 0, 0, -1, 0, 0, rechargeCode, null, null, null, null, null, null, 0, 0);
        if(rechargeCodeEntities == null || rechargeCodeEntities.size() == 0){
            return null;
        }
        RechargeCodeEntity rechargeCodeEntity = rechargeCodeEntities.get(0);
        List<RechargeCodeTypeEntity> rechargeCodeTypeList = businessService.getRechargeCodeTypeList();
        for (int i = 0; i < rechargeCodeTypeList.size(); i++) {
            RechargeCodeTypeEntity rechargeCodeTypeEntity = rechargeCodeTypeList.get(i);
            if(rechargeCodeTypeEntity.type == rechargeCodeEntity.recharge_type){
                QueryRechargeCodeStatusBean queryRechargeCodeStatusBean = new QueryRechargeCodeStatusBean();
                queryRechargeCodeStatusBean.type = rechargeCodeTypeEntity.tips;
                queryRechargeCodeStatusBean.status = rechargeCodeEntity.recharge_status == 1 ? "已充值" : "未充值";
                return queryRechargeCodeStatusBean;
            }
        }
        return null;
    }
}
