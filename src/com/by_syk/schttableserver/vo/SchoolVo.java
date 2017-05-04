package com.by_syk.schttableserver.vo;

import java.io.Serializable;

import com.by_syk.schttableserver.bean.SchoolBean;

public class SchoolVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 学校代码
    private String code;
    
    // 学校名称
    private String name;
    
    // 学号长度
    private int stuNoLen;

    // 学号字符限制匹配，为null则默认匹配数字
    private String stuNoRegex;

    // 学校教务网站链接
    private String url;
    
    // 用户数
    private int userNum;

    public SchoolVo() {}
    
    public SchoolVo(SchoolBean bean) {
        parse(bean);
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStuNoLen() {
        return stuNoLen;
    }

    public void setStuNoLen(int stuNoLen) {
        this.stuNoLen = stuNoLen;
    }

    public String getStuNoRegex() {
        return stuNoRegex;
    }

    public void setStuNoRegex(String stuNoRegex) {
        this.stuNoRegex = stuNoRegex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getUserNum() {
        return userNum;
    }
    
    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public void parse(SchoolBean bean) {
        if (bean == null) {
            return;
        }
        
        setCode(bean.getCode());
        setName(bean.getName());
        setStuNoLen(bean.getStuNoLen());
        setStuNoRegex(bean.getStuNoRegex());
        setUrl(bean.getUrl());
    }
}
