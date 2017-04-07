package com.by_syk.schttableserver.dao;

import java.util.List;

import com.by_syk.schttableserver.bean.TimetableBean;

public interface ITimetableDao {
    List<TimetableBean> get(String schoolCode);
}
