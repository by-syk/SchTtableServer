package com.by_syk.schttableserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.AppVerBean;
import com.by_syk.schttableserver.dao.IAppVerDao;
import com.by_syk.schttableserver.vo.AppVerVo;

@Service("appVerService")
public class AppVerServiceImpl implements IAppVerService {
    @Autowired
    @Qualifier("appVerDao")
    private IAppVerDao appVerDao;

    @Override
    public AppVerVo getLatestApp() {
        final String CLIENT_APP_PKG_NAME = "com.by_syk.schttable";
        
        List<AppVerBean> list = appVerDao.getAll(CLIENT_APP_PKG_NAME);
        if (list != null && !list.isEmpty()) {
            return new AppVerVo(list.get(0));
        }
        return null;
    }
}