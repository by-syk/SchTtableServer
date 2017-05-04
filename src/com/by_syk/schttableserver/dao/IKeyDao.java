package com.by_syk.schttableserver.dao;

import java.util.Map;

import com.by_syk.schttableserver.bean.KeyBean;

public interface IKeyDao {
    boolean add(KeyBean keyBean);
    
    boolean update(KeyBean keyBean);
    
    boolean addOrUpdate(KeyBean keyBean);
    
    boolean contains(String userKey);
    
    boolean contains(String schoolCode, String stuNo, String password);
    
    KeyBean get(String userKey);
    
    KeyBean get(String schoolCode, String stuNo, String password);
    
    boolean delete(KeyBean keyBean);
    
    boolean delete(String schoolCode, String stuNo);
    
    /**
     * 统计各学校用户数
     * k：学校代码
     * v：用户数
     */
    Map<String, Long> countSchoolUser();
}
