package com.cloudSeckill.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBean {

    public String returnCode;
    public String returnDesc;
    public String returnData;
}
