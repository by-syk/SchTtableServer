package com.by_syk.schttableserver.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.by_syk.schttableserver.bean.KeyBean;
import com.by_syk.schttableserver.bean.ResResBean;
import com.by_syk.schttableserver.bean.StatusBean;
import com.by_syk.schttableserver.service.ICommonService;
import com.by_syk.schttableserver.service.IKeyService;
import com.by_syk.schttableserver.service.ISchoolService;
import com.by_syk.schttableserver.service.IStatusService;
import com.by_syk.schttableserver.util.DateUtil;
import com.by_syk.schttableserver.util.net.NetEncryptUtil;
import com.by_syk.schttableserver.vo.CourseVo;
import com.by_syk.schttableserver.vo.KeyVo;
import com.by_syk.schttableserver.vo.SchoolTodoVo;
import com.by_syk.schttableserver.vo.SchoolVo;
import com.by_syk.schttableserver.vo.StatusVo;
import com.by_syk.schttableserver.vo.TermVo;

@Controller
@RequestMapping("/common")
public class CommonController {
    @Autowired
    @Qualifier("commonService")
    private ICommonService commonService;
    
    @Autowired
    @Qualifier("schoolService")
    private ISchoolService schoolService;
    
    @Autowired
    @Qualifier("keyService")
    private IKeyService keyService;
    
    @Autowired
    @Qualifier("statusService")
    private IStatusService statusService;
    
    // Spring线程池
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    
    /**
     * 登录
     * （异步）
     * 
     * @param enStuNo 加密学号
     * @param enPwd 加密密码
     * @return
     */
    @RequestMapping(value = "/{schoolcode}/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<StatusVo> signIn(@PathVariable("schoolcode") String schoolCode,
            @RequestParam(value = "stuno") String enStuNo,
            @RequestParam(value = "pwd") String enPwd) {
        System.out.println("/common/" + schoolCode + "/signin");
        
        String stuNo = NetEncryptUtil.decrypt(enStuNo);
        
        StatusBean statusBean = new StatusBean();
        KeyBean keyBean = keyService.getUserInfo(schoolCode, stuNo, enPwd);
        if (keyBean == null) {
            keyBean = new KeyBean();
            keyBean.setSchoolCode(schoolCode);
            keyBean.setStuNo(stuNo);
            keyBean.setPassword(enPwd);
            
            statusBean.setCode(StatusBean.CODE_WAIT);
            statusService.updateStatus(statusBean);
            
            // 线程抓取数据
            fetchData(keyBean, statusBean.copy());
        } else {
            if (!keyBean.isEvil()) {
                statusBean.setCode(StatusBean.CODE_SUCCESS);
            } else {
                statusBean.setCode(StatusBean.CODE_ERR_EVIL_USER);
            }
        }
        
        return new ResResBean.Builder<StatusVo>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(new StatusVo(statusBean))
                .build();
    }
    
    /**
     * 根据学号密码取到用户信息
     * 
     * @param enStuNo 加密学号
     * @param password 加密密码
     * @return
     */
    @RequestMapping(value = "/{schoolcode}/userinfo", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<KeyVo> getUserInfo(@PathVariable("schoolcode") String schoolCode,
            @RequestParam(value = "stuno") String enStuNo,
            @RequestParam(value = "pwd") String enPwd) {
        System.out.println("/common/" + schoolCode + "/userinfo");
        
        String stuNo = NetEncryptUtil.decrypt(enStuNo);
        
        KeyBean keyBean = keyService.getUserInfo(schoolCode, stuNo, enPwd);
        
        return new ResResBean.Builder<KeyVo>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(new KeyVo(keyBean))
                .build();
    }
    
    /**
     * 请求刷新指定用户所有数据
     * （异步）
     * 
     * @param userKey 用户认证KEY
     * @return
     */
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<StatusVo> refresh(@RequestParam(value = "userkey") String userKey) {
        System.out.println("/common/refresh " + userKey);
        
        StatusBean statusBean = new StatusBean();
        
        KeyBean keyBean = keyService.getUserInfo(userKey);
        if (keyBean != null) {
            if (!keyBean.isEvil()) {
                statusBean.setCode(StatusBean.CODE_WAIT);
                statusService.updateStatus(statusBean);

                fetchData(keyBean, statusBean.copy());
            } else {
                statusBean.setCode(StatusBean.CODE_ERR_EVIL_USER);
            }
        } else {
            statusBean.setCode(StatusBean.CODE_ERR_USER);
        }
        
        return new ResResBean.Builder<StatusVo>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(new StatusVo(statusBean))
                .build();
    }
    
    /**
     * 获取指定日课程数据
     * 
     * @param userKey 用户认证KEY
     * @param dateMillis 日期（毫秒形式，精确到日即可）
     * @return
     */
    @RequestMapping(value = "/daycourses", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<List<CourseVo>> getDayCourses(@RequestParam(value = "userkey") String userKey,
            @RequestParam(value = "date") Long dateMillis) {
        System.out.println("/common/daycourses " + DateUtil.getDateStr(dateMillis, "yyyy-MM-dd"));
        
        return new ResResBean.Builder<List<CourseVo>>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(commonService.getDayCourses(userKey, dateMillis))
                .build();
    }
    
    /**
     * 获取课表网页
     * 
     * @param userKey 用户认证KEY
     * @param forceReresh 是否刷新服务器的缓存
     * @return
     */
//    @RequestMapping("/coursepage")
//    @ResponseBody
//    public String getCoursePage(@RequestParam(value = "userkey") String userKey,
//            @RequestParam(value = "forcerefresh", required = false) Integer forceReresh) {
//        System.out.println("/common/coursepage");
//        
//        boolean reqForceRefresh = forceReresh != null && forceReresh == 1;
//        
//        String coursePage = null;
//        StatusBean statusBean = new StatusBean();
//        KeyBean keyBean = keyService.getUserInfo(userKey);
//        if (keyBean != null) {
//            if (!keyBean.isEvil()) {
//                // TODO 风险：可能引起超时
//                coursePage = commonService.getCoursePage(keyBean, reqForceRefresh, statusBean);
//            } else {
//                statusBean.setCode(StatusBean.CODE_ERR_EVIL_USER);
//            }
//        } else {
//            statusBean.setCode(StatusBean.CODE_ERR_USER);
//        }
//        if (statusBean.getCode() != StatusBean.CODE_SUCCESS) {
//            coursePage = statusBean.getCode() + " " + (new StatusVo(statusBean)).getDesc();
//        }
//        
//        return coursePage;
//    }
    
    @RequestMapping(value = "/coursepage", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getCoursePage(@RequestParam(value = "userkey") String userKey) throws IOException {
        System.out.println("/common/coursepage");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "coursepage.zip");
        
        KeyBean keyBean = keyService.getUserInfo(userKey);
        if (keyBean != null && !keyBean.isEvil()) {
            byte[] bytes = commonService.getCoursePage(keyBean);
            return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
        }
        
        return null;
    }
    
    /**
     * 获取学期信息
     * 
     * @param userKey
     * @return
     */
    @RequestMapping(value = "/term", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<TermVo> getTermInfo(@RequestParam(value = "userkey") String userKey) {
        System.out.println("/common/term " + userKey);
        
        return new ResResBean.Builder<TermVo>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(commonService.getTermInfo(userKey))
                .build();
    }
    
    /**
     * 获取支持的学校
     * 
     * @return
     */
    @RequestMapping("/schools")
    @ResponseBody
    public ResResBean<List<SchoolVo>> getSupportedSchools() {
        System.out.println("/common/schools");
        
        return new ResResBean.Builder<List<SchoolVo>>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(schoolService.getAllSupportedSchools())
                .build();
    }
    
    /**
     * 获取正在申请中的学校
     * 
     * @param request
     * @return
     */
    @RequestMapping("/schoolstodo")
    @ResponseBody
    public ResResBean<List<SchoolTodoVo>> getTodoSchools() {
        System.out.println("/common/schoolstodo");
        
        return new ResResBean.Builder<List<SchoolTodoVo>>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(schoolService.getAllTodoSchools())
                .build();
    }
    
//    /**
//     * 检查请求时间段的数据是否存在
//     * 
//     * @param userKey 用户认证KEY
//     * @param startDateMillis 开始日期（毫秒形式）
//     * @param days 连续天数
//     * @return
//     */
//    @RequestMapping("/dataok")
//    @ResponseBody
//    public JSONObject isDataOk(@RequestParam(value = "userkey", required = true) String userKey,
//            @RequestParam(value = "startdate", required = true) Long startDateMillis,
//            @RequestParam(value = "days", required = false) Integer days) {
//        System.out.println("/common/dataok " + DateUtil.getDateStr(startDateMillis, "yyyy-MM-dd") + " " + days);
//        
//        boolean ok = commonService.isDataOk(userKey, startDateMillis, days);
//        
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("status", ok ? 0 : 1);
//        return jsonObject;
//    }
    
    /**
     * 获取指定流程的最新状态
     * 
     * @param statusId 流程ID
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<StatusVo> getStatus(@RequestParam(value = "id") String statusId) {
        System.out.println("/common/status");
        
        return new ResResBean.Builder<StatusVo>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(statusService.getStatus(statusId))
                .build();
    }
    
    private void fetchData(final KeyBean KEY_BEAN, final StatusBean STATUS_BEAN) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                commonService.refresh(KEY_BEAN, STATUS_BEAN);
                statusService.updateStatus(STATUS_BEAN);
            }
        });
        threadPoolTaskExecutor.execute(thread);
    }
}
