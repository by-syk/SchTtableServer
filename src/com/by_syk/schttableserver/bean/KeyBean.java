package com.by_syk.schttableserver.bean;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.by_syk.schttableserver.util.net.NetEncryptUtil;

/**
 * SQL:
CREATE TABLE userkey(
  user_key CHAR(36) PRIMARY KEY,
  school_code CHAR(5) NOT NULL,
  academy VARCHAR(16),
  major VARCHAR(16),
  user_name VARCHAR(16),
  stu_no VARCHAR(16) NOT NULL,
  password VARCHAR(128) NOT NULL,
  evil TINYINT(1) NOT NULL DEFAULT 0,
  sign_time TIMESTAMP NOT NULL
);
 *
 * 用户KEY
 * 
 * @author By_syk
 */
@Entity
@Table(name = "userkey")
public class KeyBean {
    // 用户KEY
    @Id
    @Column(name = "user_key", unique = true, nullable = false)
    private String userKey;
    
    // 学校代码
    @Column(name = "school_code", nullable = false)
    private String schoolCode;
    
    // 学院
    @Column(name = "academy")
    private String academy;
    
    // 专业
    @Column(name = "major")
    private String major;
    
    // 姓名
    @Column(name = "user_name")
    private String userName;
    
    // 学号
    @Column(name = "stu_no", nullable = false)
    private String stuNo;
    
    // 密码（学号+密码，并加密）
    @Column(name = "password", nullable = false)
    private String password;
    
    // 黑名单用户，用于控制访问
    @Column(name = "evil")
    private boolean evil;
    
    // 登记时间
    @Column(name = "sign_time", nullable = false)
    private Date signTime;

    public KeyBean(String userKey) {
        setUserKey(userKey);
        setSignTime(new Date());
    }
    
    public KeyBean() {
        this(UUID.randomUUID().toString());
    }
    
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
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

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }
    
    public boolean isEvil() {
        return evil;
    }

    public void setEvil(boolean evil) {
        this.evil = evil;
    }

    public String getRealPassword() {
        if (stuNo == null || password == null) {
            return null;
        }
        
        String decrypted = NetEncryptUtil.decrypt(password);
        if (decrypted != null && decrypted.startsWith(stuNo)) {
            return decrypted.substring(stuNo.length());
        }
        return null;
    }
}