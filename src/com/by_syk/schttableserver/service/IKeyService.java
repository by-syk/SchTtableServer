package com.by_syk.schttableserver.service;

import com.by_syk.schttableserver.bean.KeyBean;

public interface IKeyService {
    KeyBean addUser(String schoolCode, String stuNo, String password);
    
    boolean isSigned(String userKey);
    
    boolean isSigned(String schoolCode, String stuNo, String password);
    
    KeyBean getUserInfo(String userKey);
    
    KeyBean getUserInfo(String schoolCode, String stuNo, String password);
    
    boolean deleteUser(String userKey);
}
