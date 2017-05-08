package com.by_syk.schttableserver.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.by_syk.schttableserver.bean.CourseBean;
import com.by_syk.schttableserver.bean.KeyBean;
import com.by_syk.schttableserver.bean.SchoolBean;
import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.bean.TimetableBean;
import com.by_syk.schttableserver.config.Config;
import com.by_syk.schttableserver.core.BaseGather;
import com.by_syk.schttableserver.core.BaseSpider;
import com.by_syk.schttableserver.dao.ICourseDao;
import com.by_syk.schttableserver.dao.IKeyDao;
import com.by_syk.schttableserver.dao.ISchoolDao;
import com.by_syk.schttableserver.dao.ITimetableDao;
import com.by_syk.schttableserver.util.DateUtil;
import com.by_syk.schttableserver.vo.CourseVo;
import com.by_syk.schttableserver.vo.TermVo;

@Service("commonService")
public class CommonServiceImpl implements ICommonService {
    @Autowired
    @Qualifier("courseDao")
    private ICourseDao courseDao;
    
    @Autowired
    @Qualifier("timetableDao")
    private ITimetableDao timetableDao;
    
    @Autowired
    @Qualifier("keyDao")
    private IKeyDao keyDao;
    
    @Autowired
    @Qualifier("schoolDao")
    private ISchoolDao schoolDao;
    
    @Autowired
    private Config config;
    
    @Override
    public boolean refresh(KeyBean keyBean, StatusBean statusBean) {
        if (keyBean == null) {
            statusBean.setCode(StatusBean.CODE_ERR_USER);
            return false;
        }
        statusBean.setCode(StatusBean.CODE_SUCCESS);
        
        BaseSpider spider = BaseSpider.chooseSpider(config, keyBean.getSchoolCode(), statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        spider.init(keyBean.getStuNo(), keyBean.getRealPassword(), keyBean.getUserKey(), statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        spider.checkTerm(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        spider.grabUserInfo(keyBean, statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        List<CourseBean> list = spider.grabCourses(statusBean);
        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
            return false;
        }
        
        BaseGather gather = BaseGather.chooseGather(config, keyBean.getSchoolCode(), new StatusBean());
        if (gather != null) {
            gather.saveCoursePage(keyBean.getUserKey(), spider.grabCoursePage(new StatusBean()));
        }
        
        System.out.println("CommonServiceImpl - refresh: writing db...");
        
        keyDao.addOrUpdate(keyBean); // 添加或更新用户信息
        courseDao.delete(keyBean.getUserKey()); // 有则删除旧数据
        try {
//            for (CourseBean courseBean : list) {
//                courseDao.add(courseBean);
//            }
            courseDao.add(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            statusBean.setCode(StatusBean.CODE_ERR_DAO);
        }
        courseDao.delete(keyBean.getUserKey()); // 数据入库失败，清空已入库数据
        
        return false;
    }

//    @Override
//    public String getCoursePage(KeyBean keyBean, boolean forceReresh, StatusBean statusBean) {
//        if (keyBean == null) {
//            statusBean.setCode(StatusBean.CODE_ERR_USER);
//            return null;
//        }
//        statusBean.setCode(StatusBean.CODE_SUCCESS);
//        
//        BaseGather gather = BaseGather.chooseGather(config, keyBean.getSchoolCode(), statusBean);
//        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
//            return null;
//        }
//        if (forceReresh || !gather.isCoursePageSaved(keyBean.getUserKey())) {
//            BaseSpider spider = BaseSpider.chooseSpider(config, keyBean.getSchoolCode(), statusBean);
//            if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
//                return null;
//            }
//            spider.init(keyBean.getStuNo(), keyBean.getRealPassword(), keyBean.getUserKey(), statusBean);
//            if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
//                return null;
//            }
//            String coursePage = spider.grabCoursePage(statusBean);
//            if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
//                return null;
//            }
//            gather.saveCoursePage(keyBean.getUserKey(), coursePage);
//            return gather.pack(coursePage);
//        }
//        return gather.getSavedCoursePageWithPack(keyBean.getUserKey());
//    }
    
    @Override
    public byte[] getCoursePage(KeyBean keyBean) {
        if (keyBean == null) {
            return null;
        }
        
        BaseGather gather = BaseGather.chooseGather(config, keyBean.getSchoolCode(), new StatusBean());
        if (gather != null) {
             return gather.getSavedCoursePagePack(keyBean.getUserKey());
        }
        return null;
    }
    
    @Override
    public List<CourseVo> getDayCourses(String userKey, long startDateMillis) {
        List<CourseVo> voList = new ArrayList<>();
        
        KeyBean keyBean = keyDao.get(userKey);
        if (keyBean == null || keyBean.isEvil()) {
            return null;
        }
        
        List<CourseBean> list = courseDao.getList(userKey, new Date(startDateMillis), 1);
        if (list == null || list.isEmpty()) {
            return voList;
        }
        
        int schoolArea = list.get(0).getSchoolArea(); // 校区序号，一般只有主校区，即0
        int dayCourseNum = -1; // 每天节数
        SchoolBean schoolBean = schoolDao.get(keyBean.getSchoolCode(), schoolArea);
        if (schoolBean != null) {
            dayCourseNum = schoolBean.getDayCourseNum();
        }
        
        List<TimetableBean> timetableList = timetableDao.get(keyBean.getSchoolCode());
        for (CourseBean courseBean : list) {
            if (courseBean.getConflict() != 0) { // 忽略重课
                continue;
            }
            for (int i = 0, len = courseBean.getCourseNum(); i < len; ++i) {
                if (dayCourseNum > 0 && courseBean.getCourseOrder() + i > dayCourseNum) { // 超出，无效
                    continue;
                }
                
                TimetableBean matchTimetableBean = null;
                for (TimetableBean timetableBean : timetableList) {
                    if (courseBean.getCourseOrder() + i == timetableBean.getCourseOrder()
                            && schoolArea == timetableBean.getSchoolArea()) {
                        matchTimetableBean = timetableBean;
                        timetableList.remove(timetableBean);
                        break;
                    }
                }
                
                CourseVo courseVo = new CourseVo();
                courseVo.parse(courseBean, matchTimetableBean, schoolBean, i);
                voList.add(courseVo);
            }
        }
        // 标记有重课
        for (CourseBean courseBean : list) {
            if (courseBean.getConflict() == 0) { // 忽略非重课
                continue;
            }
            for (int i = courseBean.getCourseOrder(), end = courseBean.getCourseOrder() + courseBean.getCourseNum() - 1; i <= end; ++i) {
                voList.get(i - 1).setMerge(true);
            }
        }
        
        return voList;
    }

    @Override
    public TermVo getTermInfo(String userKey) {
        if (userKey == null) {
            return null;
        }
        
        TermVo termVo = new TermVo();
        Date date = courseDao.getTermStartDate(userKey);
        if (date != null) {
            termVo.setDaysFromStart(DateUtil.getDays(date.getTime(), System.currentTimeMillis()));
        }
        
        CourseBean courseBean = courseDao.get(userKey, date, 1);
        if (courseBean != null) {
            termVo.setTerm(courseBean.getTerm());
        }
        
        date = courseDao.getTermEndDate(userKey);
        if (date != null) {
            termVo.setDaysBeforeEnd(DateUtil.getDays(System.currentTimeMillis(), date.getTime()));
        }
        
        return termVo;
    }

//    @Override
//    public boolean isDataOk(String userKey, long startDateMillis, int days) {
//        KeyBean keyBean = keyDao.get(userKey);
//        if (keyBean == null || keyBean.isEvil()) {
//            return false;
//        }
//        
//        int courseNum = courseDao.getCount(userKey, new Date(startDateMillis), days);
//        if (courseNum < 0) {
//            return false;
//        }
//        
//        SchoolBean schoolBean = schoolDao.get(keyBean.getSchoolCode());
//        if (schoolBean == null) {
//            return false;
//        }
//        
//        System.out.println("isDataOk: " + courseNum + " -> " + (schoolBean.getDayCourseNum() * days));
//        return courseNum >= schoolBean.getDayCourseNum() * days;
//    }
}
