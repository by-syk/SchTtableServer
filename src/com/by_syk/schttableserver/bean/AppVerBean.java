package com.by_syk.schttableserver.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE appver(
  pkg_name VARCHAR(64),
  ver_name VARCHAR(32) NOT NULL,
  ver_code INT,
  min_sdk TINYINT,
  apk_size INT,
  description VARCHAR(512),
  url VARCHAR(256) NOT NULL,
  date DATE NOT NULL,
  pub TINYINT(1) DEFAULT 0,
  PRIMARY KEY(pkg_name, ver_code)
);
 *
 * APP版本
 * 
 * @author By_syk
 */
@Entity
@Table(name = "appver")
public class AppVerBean implements Serializable {
    // Composite-id class must implement Serializable
    private static final long serialVersionUID = 1L;

    // 包名
    @Id
    @Column(name = "pkg_name")
    private String pkgName;
    
    // 版本名
    @Column(name = "ver_name", nullable = false)
    private String verName;

    // 版本号
    @Id
    @Column(name = "ver_code")
    private int verCode;

    // 兼容SDK版本
    @Column(name = "min_sdk")
    private int minSdk;

    // 安装包大小
    @Column(name = "apk_size")
    private int apkSize;

    // 更新日志
    @Column(name = "description")
    private String desc;

    // 下载链接
    @Column(name = "url", nullable = false)
    private String url;

    // 更新日期
    @Column(name = "date", nullable = false)
    private Date date;
    
    // 开放
    @Column(name = "pub")
    public boolean pub;

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
    
    public void setPub(boolean pub) {
        this.pub = pub;
    }
    
    public boolean isPub() {
        return pub;
    }
}
