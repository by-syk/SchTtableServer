package com.by_syk.schttableserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.KeyBean;
import com.by_syk.schttableserver.dao.IKeyDao;

@Service("keyService")
public class KeyServiceImpl implements IKeyService {
    @Autowired
    @Qualifier("keyDao")
    private IKeyDao keyDao;
    
    @Override
    public KeyBean addUser(String schoolCode, String stuNo, String password) {
        KeyBean keyBean = new KeyBean();
        keyBean.setSchoolCode(schoolCode);
        keyBean.setStuNo(stuNo);
        keyBean.setPassword(password);
        boolean ok = keyDao.add(keyBean);
        if (ok) {
            return keyBean;
        }
        return null;
    }

    @Override
    public boolean isSigned(String userKey) {
        return keyDao.contains(userKey);
    }

    @Override
    public boolean isSigned(String schoolCode, String stuNo, String password) {
        return keyDao.contains(schoolCode, stuNo, password);
    }

    @Override
    public KeyBean getUserInfo(String userKey) {
        return keyDao.get(userKey);
    }

    @Override
    public KeyBean getUserInfo(String schoolCode, String stuNo, String password) {
        return keyDao.get(schoolCode, stuNo, password);
    }

    @Override
    public boolean deleteUser(String userKey) {
        if (userKey == null) {
            return false;
        }
        return keyDao.delete(new KeyBean(userKey));
    }
}
