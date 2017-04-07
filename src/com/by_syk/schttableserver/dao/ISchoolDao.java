package com.by_syk.schttableserver.dao;

import java.util.List;

import com.by_syk.schttableserver.bean.SchoolBean;

public interface ISchoolDao {
    List<SchoolBean> getAll();
    
    SchoolBean get(String code, int area);
    
    SchoolBean get(String code);
}
