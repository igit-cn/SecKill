package com.proxy.action;

import com.cloudSeckill.base.BaseController;
import com.cloudSeckill.data.response.ResponseBean;
import com.cloudSeckill.service.UserService;
import com.google.gson.Gson;
import com.proxy.constant.ResponseCode;
import com.proxy.entity.*;
import com.proxy.service.BusinessService;
import com.proxy.service.ProxyUserService;
import com.proxy.service.RechargeCodeService;
import com.proxy.utils.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("api")
public class BusinessAction{
    
    @Autowired private CheckUtils checkUtils;
    @Autowired private BusinessService businessService;
    @Autowired private RechargeCodeService rechargeCodeService;
    @Autowired private ProxyUserService proxyUserService;
    
    /**
     * 生成代理卡充值码
     */
    @RequestMapping("pushRechargeCard")
    public @ResponseBody String pushRechargeCard(String token, String fromProxyName, String firstStr, int rechargeType, int count, String remark){
        String checkParamsIsEmpty = checkUtils.checkParamsIsEmpty(token,fromProxyName);
        if(checkParamsIsEmpty != null){
            return checkParamsIsEmpty;
        }
        
        String checkPermissionEntity = checkUtils.checkPermission(token);
        if(checkPermissionEntity != null){
            return checkPermissionEntity;
        }
        
        if(rechargeType == 0){
            ResultEntity resultEntity = new ResultEntity();
            resultEntity.code = ResponseCode.DATA_ERROR;
            resultEntity.msg = "参数错误";
            resultEntity.data = new NullEntity();
            return new Gson().toJson(resultEntity);
        }
        
        businessService.pushRechargeCard(fromProxyName,firstStr,rechargeType,count,remark);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "出卡成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 添加注释
     */
    @RequestMapping("addRemark")
    public @ResponseBody
    String addRemark(String token, String proxyName, String remark){
        String checkParams = checkUtils.checkParamsIsEmpty(token,proxyName,remark);
        if(checkParams != null){
            return checkParams;
        }
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkTokenReturnId(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        
        businessService.addRemark(proxyInfoEntity.id,proxyName,remark);
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "添加备注成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }

    /**
     * 指定父代理名字查询旗下的子代理列表(分页)
     */
    @RequestMapping("queryProxyListByPreviousProxyName")
    public @ResponseBody
    String queryProxyListByPreviousProxyName(String token, int page, int size){
        String checkParams = checkUtils.checkParamsIsEmpty(token);
        if(checkParams != null){
            return checkParams;
        }
        
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkTokenReturnId(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "查询成功";
        ListEntity listEntity = new ListEntity();
        
        listEntity.total = businessService.queryProxyListCountByPreviousProxyName(proxyInfoEntity.proxy_name);
        listEntity.list = businessService.queryProxyListByPreviousProxyName(proxyInfoEntity.proxy_name,page,size);
        
        resultEntity.data = listEntity;
        
        return new Gson().toJson(resultEntity);
    }

    /**
     * 批量转让充值卡
     */
    @RequestMapping("makeOverRechargeCode")
    public @ResponseBody
    String makeOverRechargeCode(String token, String[] rechargeCodes, String toProxyName, String remark){
        String checkParams = checkUtils.checkParamsIsEmpty(token,toProxyName);
        if(checkParams != null){
            return checkParams;
        }
        
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        
        businessService.makeOverRechargeCode(proxyInfoEntity.proxy_name,rechargeCodes,toProxyName,remark);
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.data = new NullEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "转让成功";
        return new Gson().toJson(resultEntity);
    }

    /**
     * 批量转让充值卡
     */
    @RequestMapping("makeOverRechargeCodeByProxy")
    public @ResponseBody
    String makeOverRechargeCodeByProxy(String token, String rechargeCodes, String toProxyName, String remark){
        String checkParams = checkUtils.checkParamsIsEmpty(token,toProxyName,rechargeCodes);
        if(checkParams != null){
            return checkParams;
        }

        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        
        businessService.makeOverRechargeCode(proxyInfoEntity.proxy_name,rechargeCodes.split(","),toProxyName,remark);

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.data = new NullEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "转让成功";
        return new Gson().toJson(resultEntity);
    }
    

    /**
     * 批量转让充值卡根据数量和充值码类型
     */
    @RequestMapping("makeOverRechargeCodeByType")
    public @ResponseBody
    String makeOverRechargeCodeByType(String token, String rechargeCodeType, String count, String toProxyName, String remark){
        String checkParams = checkUtils.checkParamsIsEmpty(token,toProxyName,rechargeCodeType,count);
        if(checkParams != null){
            return checkParams;
        }

        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        
        int status = businessService.makeOverRechargeCodeByType(proxyInfoEntity.proxy_name,rechargeCodeType,count,toProxyName,remark);
        ResultEntity resultEntity = new ResultEntity();
        if(status == 1){
            resultEntity.data = new NullEntity();
            resultEntity.code = ResponseCode.RECHARGE_CODE_TYPE_EXCEPTION;
            resultEntity.msg = "充值码类型错误";
        } else if(status == 2){
            resultEntity.data = new NullEntity();
            resultEntity.code = ResponseCode.RECHARGE_CODE_INSUFFICIENT;
            resultEntity.msg = "充值码剩余数量不足";
        } else {
            resultEntity.data = new NullEntity();
            resultEntity.code = ResponseCode.RESPONSE_OK;
            resultEntity.msg = "转让成功";
        }
        
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 获取充值卡类型
     */
    @RequestMapping("getRechargeCodeTypeList")
    public @ResponseBody String getRechargeCodeTypeList(){
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "获取列表成功";
        resultEntity.data = businessService.getRechargeCodeTypeList();
        return new Gson().toJson(resultEntity);
    }

    /**
     * 获取转让给子代理的充值码数量列表
     */
    @RequestMapping("getMakeOverChildDetail")
    public @ResponseBody
    String getMakeOverChildDetail(String token , String childProxyName){
        String checkParams = checkUtils.checkParamsIsEmpty(token,childProxyName);
        if(checkParams != null){
            return checkParams;
        }
        
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "获取数据成功";
        resultEntity.data = businessService.getMakeOverChildDetail(proxyInfoEntity.proxy_name,childProxyName);
        return new Gson().toJson(resultEntity);
    }
    
    /**
     * 获取剩余充值码数量列表
     */
    @RequestMapping("getSurplusRechargeCountList")
    public @ResponseBody
    String getSurplusRechargeCountList(String token){
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if (proxyInfoEntity == null) {
            return checkUtils.getTokenTimeOutStr();
        }

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "获取列表成功";
        resultEntity.data = businessService.getSurplusRechargeCountList(proxyInfoEntity.proxy_name);
        return new Gson().toJson(resultEntity);
    }

    /**
     * 根据充值码类型获取剩余充值码列表
     */
    @RequestMapping("getSurplusRechargeList")
    public @ResponseBody
    String getSurplusRechargeList(String token, int type, int page, int size){
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }

        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "获取列表成功";
        ListEntity listEntity = new ListEntity();
        listEntity.total = businessService.getSurplusRechargeListCount(proxyInfoEntity.proxy_name, type);
        listEntity.list = businessService.getSurplusRechargeList(proxyInfoEntity.proxy_name, type, page, size);
        resultEntity.data = listEntity;
        return new Gson().toJson(resultEntity);
    }

    /**
     * 获取已经转让或充值的充值码
     */
    @RequestMapping("getRechargeCodeTransferOrUsed")
    public @ResponseBody
    String getRechargeCodeTransferOrUsed(String token, int page , int size, String buyBeginTime, String buyEndTime, String sellBeginTime, String sellEndTime, String rechargeBeginTime,
                                         String rechargeEndTime, String rechargeCode, String remark, String childProxyName, String rechargeType, String status, String fromType, String rechargeStatus, String sellStatus){
        String paramsCheck = checkUtils.checkParamsIsEmpty(fromType,token);
        if( ! StringUtils.isEmpty(paramsCheck)){
            return paramsCheck;
        }
        
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        
        RechargeCodeSellOrUsedParamEntity rechargeCodeSellOrUsedParamEntity = new RechargeCodeSellOrUsedParamEntity();
        rechargeCodeSellOrUsedParamEntity.page = page;
        rechargeCodeSellOrUsedParamEntity.size = size;
        rechargeCodeSellOrUsedParamEntity.buyBeginTime = buyBeginTime;
        rechargeCodeSellOrUsedParamEntity.buyEndTime = buyEndTime;
        rechargeCodeSellOrUsedParamEntity.sellBeginTime = sellBeginTime;
        rechargeCodeSellOrUsedParamEntity.sellEndTime = sellEndTime;
        rechargeCodeSellOrUsedParamEntity.rechargeBeginTime = rechargeBeginTime;
        rechargeCodeSellOrUsedParamEntity.rechargeEndTime = rechargeEndTime;
        rechargeCodeSellOrUsedParamEntity.rechargeCode = rechargeCode;
        rechargeCodeSellOrUsedParamEntity.remark = remark;
        rechargeCodeSellOrUsedParamEntity.childProxyName = childProxyName;
        rechargeCodeSellOrUsedParamEntity.rechargeType = rechargeType;
        rechargeCodeSellOrUsedParamEntity.status = status;
        rechargeCodeSellOrUsedParamEntity.fromType = fromType;
        rechargeCodeSellOrUsedParamEntity.rechargeStatus = rechargeStatus;
        rechargeCodeSellOrUsedParamEntity.sellStatus = sellStatus;
        
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "获取列表成功";
        resultEntity.data = businessService.getRechargeCodeTransferOrUsed(rechargeCodeSellOrUsedParamEntity,proxyInfoEntity);
        return new Gson().toJson(resultEntity);
    }

    /**
     * 打上出售标记,不可再次出售
     */
    @RequestMapping("sellRechargeCodeSign")
    public @ResponseBody
    String sellRechargeCodeSign(String token, String rechargeCode, String sellRemark){
        String paramsCheck = checkUtils.checkParamsIsEmpty(token,rechargeCode);
        if( ! StringUtils.isEmpty(paramsCheck)){
            return paramsCheck;
        }
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }

        ResultEntity resultEntity = new ResultEntity();
        int status = businessService.sellRechargeCodeSign(proxyInfoEntity.proxy_name,rechargeCode.split(","),sellRemark);
        if(status == 1 || rechargeCode.split(",").length == 0){
            resultEntity.msg = "充值码不存在";
            resultEntity.code = ResponseCode.RECHARGE_CODE_NOT_EXIST;
            resultEntity.data = new NullEntity();
        } else if(status == 2){
            resultEntity.msg = "充值码已被冻结";
            resultEntity.code = ResponseCode.RECHARGE_CODE_STATUS_ERR;
            resultEntity.data = new NullEntity();
        }  else if(status == 3){
            resultEntity.msg = "充值码已被删除";
            resultEntity.code = ResponseCode.RECHARGE_CODE_STATUS_ERR;
            resultEntity.data = new NullEntity();
        } else if(status == 4){
            resultEntity.msg = "出售充值码成功";
            resultEntity.code = ResponseCode.RESPONSE_OK;
            resultEntity.data = new NullEntity();
        }
        return new Gson().toJson(resultEntity);
    }

    /**
     * 意见反馈
     */
    @RequestMapping("addFeedback")
    public @ResponseBody
    String addFeedback(String token, String content){
        String checkParams = checkUtils.checkParamsIsEmpty(token,content);
        if(checkParams != null){
            return checkParams;
        }
        
        ProxyInfoEntity proxyInfoEntity = checkUtils.checkToken(token);
        if(proxyInfoEntity == null){
            return checkUtils.getTokenTimeOutStr();
        }
        businessService.addFeedback(proxyInfoEntity.proxy_name,content);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.code = ResponseCode.RESPONSE_OK;
        resultEntity.msg = "意见反馈提交成功";
        resultEntity.data = new NullEntity();
        return new Gson().toJson(resultEntity);
    }
}
