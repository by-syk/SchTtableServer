package com.by_syk.schttableserver.dao;

import java.util.List;

import com.by_syk.schttableserver.bean.SchoolTodoBean;

public interface ISchoolTodoDao {
    List<SchoolTodoBean> getAll();
}
