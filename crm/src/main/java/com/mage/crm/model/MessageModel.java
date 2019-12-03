package com.mage.crm.model;

import com.mage.crm.base.CrmConstant;

public class MessageModel {
    private Integer code= CrmConstant.OPS_SUCCESS_CODE;//状态码
    private String msg=CrmConstant.OPS_SUCCESS_MSG;//提示信息
    private Object result;//返回值

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
