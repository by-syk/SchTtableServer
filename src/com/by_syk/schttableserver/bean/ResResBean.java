package com.by_syk.schttableserver.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResResBean<T> {
    private int status = STATUS_ERROR;
    
    private String msg;
    
    private T result;

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = 1;

    private ResResBean() {}
    
    private void setStatus(int status) {
        this.status = status;
    }
    
    private void setMsg(String msg) {
        this.msg = msg;
    }
    
    private void setResult(T result) {
        this.result = result;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public T getResult() {
        return result;
    }
    
    @JsonIgnore
    public boolean isStatusSuccess() {
        return status == STATUS_SUCCESS;
    }
    
    public static class Builder<T> {
        private ResResBean<T> bean = new ResResBean<>();
        
        public Builder<T> status(int status) {
            bean.setStatus(status);
            return this;
        }
        
        public Builder<T> msg(String msg) {
            bean.setMsg(msg);
            return this;
        }
        
        public Builder<T> result(T result) {
            bean.setResult(result);
            return this;
        }
        
        public ResResBean<T> build() {
            return bean;
        }
    }
}
