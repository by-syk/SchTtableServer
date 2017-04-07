package com.by_syk.schttableserver.bean;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE status(
  id VARCHAR(32) PRIMARY KEY,
  code INT NOT NULL,
  time TIMESTAMP NOT NULL
);
 *
 * 流程状态
 * 
 * @author By_syk
 */
@Entity
@Table(name = "status")
public class StatusBean {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    
    @Column(name = "code")
    private int code;
    
    @Column(name = "time", nullable = false)
    private Date time;
    
    public static final int CODE_UNDEFINED = 0;
    // 1xx 成功
    public static final int CODE_SUCCESS = 1;
    // 2xx 等待
    public static final int CODE_WAIT = 2;
    // 3xx 出错
    public static final int CODE_ERR = 3;
    // 301xx 登录环节出错
    public static final int CODE_ERR_LOGIN = 301;
    public static final int CODE_ERR_ACCOUNT = 30101;
    public static final int CODE_ERR_PASSWORD = 30102;
    public static final int CODE_ERR_ACCT_OR_PWD = 30103;
    public static final int CODE_ERR_GRAPHIC_CODE = 30104;
    // 302xx 抓取用户信息环节出错
    public static final int CODE_ERR_GRAB_USER_INFO = 302;
    // 303xx 抓取课表环节出错
    public static final int CODE_ERR_GRAB_COURSES = 303;
    public static final int CODE_ERR_TERM = 30301;
    public static final int CODE_ERR_REGEX_COURSES = 30302;
    // 304xx 保存数据环节出错
    public static final int CODE_ERR_DAO = 304;
    // 305xx 用户出错
    public static final int CODE_ERR_USER = 305;
    public static final int CODE_ERR_EVIL_USER = 30501;
    // 306xx 学校出错
    public static final int CODE_ERR_SCHOOL = 306;
    
    public StatusBean() {
        setId(String.valueOf(UUID.randomUUID().getMostSignificantBits()));
        setCode(CODE_UNDEFINED);
        setTime(new Date());
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
    public StatusBean copy() {
        StatusBean bean = new StatusBean();
        bean.setId(id);
        bean.setCode(code);
        bean.setTime(time);
        return bean;
    }
}
