package com.by_syk.schttableserver.vo;

import java.io.Serializable;
import java.util.Date;

import com.by_syk.schttableserver.bean.AppVerBean;

public class AppVerVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 包名
    private String pkgName;
    
    // 版本名
    private String verName;

    // 版本号
    private int verCode;

    // 兼容SDK版本
    private int minSdk;

    // 安装包大小
    private int apkSize;

    // 更新日志
    private String desc;

    // 下载链接
    private String url;

    // 更新日期
    private Date date;

    public AppVerVo() {}
    
    public AppVerVo(AppVerBean bean) {
        parse(bean);
    }
    
    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public int getMinSdk() {
        return minSdk;
    }

    public void setMinSdk(int minSdk) {
        this.minSdk = minSdk;
    }

    public int getApkSize() {
        return apkSize;
    }

    public void setApkSize(int apkSize) {
        this.apkSize = apkSize;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public void parse(AppVerBean bean) {
        if (bean == null) {
            return;
        }
        
        setPkgName(bean.getPkgName());
        setVerName(bean.getVerName());
        setVerCode(bean.getVerCode());
        setMinSdk(bean.getMinSdk());
        setApkSize(bean.getApkSize());
        setDesc(bean.getDesc());
        setUrl(bean.getUrl());
        setDate(bean.getDate());
    }
}
