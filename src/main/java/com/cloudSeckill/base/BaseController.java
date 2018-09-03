package com.cloudSeckill.base;

import com.cloudSeckill.data.response.ResponseBean;

public abstract class BaseController {
    protected final static String CODE_SUCCESS = "M01001";
    protected final static String CODE_ERROR = "M01002";

    public ResponseBean resultResponseSuccessObj(String data) {
        ResponseBean ResponseBean = new ResponseBean();
        ResponseBean.returnCode = CODE_SUCCESS;
        ResponseBean.returnData = data;
        return ResponseBean;
    }
    
    public ResponseBean resultResponseErrorObj(String data) {
        ResponseBean ResponseBean = new ResponseBean();
        ResponseBean.returnCode = CODE_ERROR;
        ResponseBean.returnData = data;
        return ResponseBean;
    }
    
}
