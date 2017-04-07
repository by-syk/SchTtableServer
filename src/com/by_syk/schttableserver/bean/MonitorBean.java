package com.by_syk.schttableserver.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SQL:
CREATE TABLE monitor(
  _id INT AUTO_INCREMENT PRIMARY KEY,
  user_key CHAR(36),
  ip VARCHAR(15),
  device_brand VARCHAR(32),
  device_model VARCHAR(32),
  device_sdk TINYINT,
  app_ver_name VARCHAR(32),
  app_ver_code INT,
  time TIMESTAMP NOT NULL
);
 *
 * 访问日志
 * 
 * @author By_syk
 */
@Entity
@Table(name = "monitor")
public class MonitorBean {
    @Id @GeneratedValue
    @Column(name = "_id")
    private int id;
    
    // 用户KEY
    @Column(name = "user_key")
    private String userKey;
    
    // IP地址
    @Column(name = "ip")
    private String ip;
    
    // 设备品牌
    @Column(name = "device_brand")
    private String deviceBrand;
    
    // 设备型号
    @Column(name = "device_model")
    private String deviceModel;
    
    // 设备系统版本
    @Column(name = "device_sdk")
    private int deviceSdk;
    
    // App版本名
    @Column(name = "app_ver_name")
    private String appVerName;
    
    // App版本号
    @Column(name = "app_ver_code")
    private int appVerCode;
    
    // 访问时间
    @Column(name = "time")
    private Date time;
    
    public MonitorBean() {
        setTime(new Date());
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        if (userKey != null && userKey.length() <= 36) {
            this.userKey = userKey;
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        if (deviceBrand != null && deviceBrand.length() <= 36) {
            this.deviceBrand = deviceBrand;
        }
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        if (deviceModel != null && deviceModel.length() <= 36) {
            this.deviceModel = deviceModel;
        }
    }

    public int getDeviceSdk() {
        return deviceSdk;
    }

    public void setDeviceSdk(int deviceSdk) {
        this.deviceSdk = deviceSdk;
    }

    public String getAppVerName() {
        return appVerName;
    }

    public void setAppVerName(String appVerName) {
        if (appVerName != null && appVerName.length() <= 36) {
            this.appVerName = appVerName;
        }
    }

    public int getAppVerCode() {
        return appVerCode;
    }

    public void setAppVerCode(int appVerCode) {
        this.appVerCode = appVerCode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}