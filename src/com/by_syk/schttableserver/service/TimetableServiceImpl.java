package com.by_syk.schttableserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.TimetableBean;
import com.by_syk.schttableserver.dao.ITimetableDao;

@Service("timetableService")
public class TimetableServiceImpl implements ITimetableService {
    @Autowired
    @Qualifier("timetableDao")
    private ITimetableDao timetableDao;
    
    @Override
    public List<TimetableBean> getTimetable(String schoolCode) {
        List<TimetableBean> list = timetableDao.get(schoolCode);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }
}
