package com.by_syk.schttableserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.SchoolBean;
import com.by_syk.schttableserver.bean.SchoolTodoBean;
import com.by_syk.schttableserver.dao.IKeyDao;
import com.by_syk.schttableserver.dao.ISchoolDao;
import com.by_syk.schttableserver.dao.ISchoolTodoDao;
import com.by_syk.schttableserver.vo.SchoolTodoVo;
import com.by_syk.schttableserver.vo.SchoolVo;

@Service("schoolService")
public class SchoolServiceImpl implements ISchoolService {
    @Autowired
    @Qualifier("schoolDao")
    private ISchoolDao schoolDao;
    
    @Autowired
    @Qualifier("schoolTodoDao")
    private ISchoolTodoDao schoolTodoDao;
    
    @Autowired
    @Qualifier("keyDao")
    private IKeyDao keyDao;
    
    @Override
    public List<SchoolVo> getAllSupportedSchools(boolean withUserNum) {
        List<SchoolVo> voList = new ArrayList<>();
        
        List<SchoolBean> list = schoolDao.getAll();
        if (list == null) {
            return new ArrayList<>();
        }
        
        for (SchoolBean bean : list) {
            voList.add(new SchoolVo(bean));
        }
        
        if (withUserNum) {
            Map<String, Long> userNumMap = keyDao.countSchoolUser();
            for (SchoolVo vo : voList) {
                Long num = userNumMap.get(vo.getCode());
                if (num != null) {
                    vo.setUserNum(num.intValue());
                }
            }
        }
        
        return voList;
    }

    @Override
    public List<SchoolTodoVo> getAllTodoSchools() {
        List<SchoolTodoVo> voList = new ArrayList<>();
        
        List<SchoolTodoBean> list = schoolTodoDao.getAll();
        if (list == null) {
            return new ArrayList<>();
        }
        
        for (SchoolTodoBean bean : list) {
            voList.add(new SchoolTodoVo(bean));
        }
        
        return voList;
    }
}