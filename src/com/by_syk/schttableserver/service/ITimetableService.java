package com.by_syk.schttableserver.service;

import java.util.List;

import com.by_syk.schttableserver.bean.TimetableBean;

public interface ITimetableService {
    List<TimetableBean> getTimetable(String schoolCode);
}
