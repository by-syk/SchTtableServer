package com.by_syk.schttableserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.MonitorBean;
import com.by_syk.schttableserver.dao.IMonitorDao;

@Service("monitorService")
public class MonitorServiceImpl implements IMonitorService {
    @Autowired
    @Qualifier("monitorDao")
    private IMonitorDao monitorDao;
    
    @Override
    public boolean addLog(String userKey, String ip, String brand, String model, Integer sdk,
            String appVerName, Integer appVerCode) {
        MonitorBean monitorBean = new MonitorBean();
        monitorBean.setUserKey(userKey);
        monitorBean.setIp(ip);
        monitorBean.setDeviceBrand(brand);
        monitorBean.setDeviceModel(model);
        if (sdk != null) {
            monitorBean.setDeviceSdk(sdk);
        }
        monitorBean.setAppVerName(appVerName);
        if (appVerCode != null) {
            monitorBean.setAppVerCode(appVerCode);
        }
        
        return monitorDao.add(monitorBean);
    }
}
