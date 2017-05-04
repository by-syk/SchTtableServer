package com.by_syk.schttableserver.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.BugBean;
import com.by_syk.schttableserver.bean.MonitorBean;
import com.by_syk.schttableserver.dao.IBugDao;
import com.by_syk.schttableserver.dao.IMonitorDao;

@Service("bugService")
public class BugServiceImpl implements IBugService {
    @Autowired
    @Qualifier("bugDao")
    private IBugDao bugDao;
    
    @Override
    public boolean addBugReport(String schoolCode, String stuNo,
            Long courseDate, Integer courseOrder, String desc) {
        BugBean bean = new BugBean();
        bean.setSchoolCode(schoolCode);
        bean.setStuNo(stuNo);
        if (courseDate != null) {
            bean.setCourseDate(new Date(courseDate));
        }
        if (courseOrder != null) {
            bean.setCourseOrder(courseOrder);
        }
        bean.setDesc(desc);
        
        return bugDao.add(bean);
    }
}
