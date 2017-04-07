package com.by_syk.schttableserver.service;

import java.util.List;

import com.by_syk.schttableserver.bean.KeyBean;
import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.vo.CourseVo;
import com.by_syk.schttableserver.vo.TermVo;

public interface ICommonService {
    boolean refresh(KeyBean keyBean, StatusBean statusBean);
    
//    String getCoursePage(KeyBean keyBean, boolean forceReresh, StatusBean statusBean);

    byte[] getCoursePage(KeyBean keyBean);

    List<CourseVo> getDayCourses(String userKey, long startDateMillis);
    
    TermVo getTermInfo(String userKey);
    
//    boolean isDataOk(String userKey, long startDateMillis, int days);
}
