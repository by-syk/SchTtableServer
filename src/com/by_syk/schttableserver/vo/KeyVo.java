package com.by_syk.schttableserver.vo;

import java.io.Serializable;

import com.by_syk.schttableserver.bean.KeyBean;

public class KeyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 用户KEY
    private String userKey;
    
    // 学院
    private String academy;
    
    // 专业
    private String major;
    
    // 姓名
    private String userName;
    
    public KeyVo() {}
    
    public KeyVo(KeyBean keyBean) {
        parse(keyBean);
    }
    
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void parse(KeyBean keyBean) {
        if (keyBean == null) {
            return;
        }
        
        setUserKey(keyBean.getUserKey());
        setAcademy(keyBean.getAcademy());
        setMajor(keyBean.getMajor());
        setUserName(keyBean.getUserName());
    }
}
