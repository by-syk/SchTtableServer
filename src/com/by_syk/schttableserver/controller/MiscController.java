package com.by_syk.schttableserver.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.by_syk.schttableserver.bean.ResResBean;
import com.by_syk.schttableserver.bean.WatchDogBean;
import com.by_syk.schttableserver.service.IAppVerService;
import com.by_syk.schttableserver.service.IBugService;
import com.by_syk.schttableserver.service.IMonitorService;
import com.by_syk.schttableserver.util.ExtraUtil;
import com.by_syk.schttableserver.vo.AppVerVo;

@Controller
@RequestMapping("/misc")
public class MiscController {
    @Autowired
    @Qualifier("monitorService")
    private IMonitorService monitorService;
    
    @Autowired
    @Qualifier("appVerService")
    private IAppVerService appVerService;
    
    @Autowired
    @Qualifier("bugService")
    private IBugService bugService;
    
    @RequestMapping("/index")
    public ModelAndView index(HttpServletResponse response) {
//        return new ModelAndView("hello");
        return new ModelAndView("redirect:https://github.com/by-syk/SchTtable");
    }

    /**
     * 获取最新App版本描述信息，同时也用于记录用户基本信息
     * 
     * @param request
     * @param userKey 用户认证KEY
     * @param brand 设备品牌
     * @param model 设备型号
     * @param sdk 系统版本
     * @param appVerName App版本名
     * @param appVerCode App版本号
     * @return
     */
    @RequestMapping("/appver")
    @ResponseBody
    public ResResBean<AppVerVo> checkAppUpdate(HttpServletRequest request,
            @RequestParam(value = "userkey", required = false) String userKey,
            @RequestParam(value = "devicebrand", required = false) String brand,
            @RequestParam(value = "devicemodel", required = false) String model,
            @RequestParam(value = "devicesdk", required = false) Integer sdk,
            @RequestParam(value = "appvername", required = false) String appVerName,
            @RequestParam(value = "appvercode", required = false) Integer appVerCode) {
        System.out.println("GET /misc/appver " + model);
        
        monitorService.addLog(userKey, ExtraUtil.getRemoteHost(request),
                brand, model, sdk, appVerName, appVerCode);
        
//        String dir = request.getServletContext().getRealPath("/");
//        File versionFile = new File(dir + "/WEB-INF/config/appversion.json");
        
//        return ExtraUtil.readFile(versionFile);
        
        return new ResResBean.Builder<AppVerVo>()
                .status(ResResBean.STATUS_SUCCESS)
                .result(appVerService.getLatestApp())
                .build();
    }
    
    /**
     * 反馈课程信息BUG
     * 
     * @param schoolCode 学校代码
     * @param stuNo 学号
     * @param courseDate 课程日期
     * @param courseOrder 课程节次
     * @param desc BUG描述
     * @return
     */
    @RequestMapping(value = "/bugrep", method = RequestMethod.POST)
    @ResponseBody
    public ResResBean<AppVerVo> checkAppUpdate(@RequestParam("schoolCode") String schoolCode,
            @RequestParam("stuNo") String stuNo,
            @RequestParam("courseDate") Long courseDate,
            @RequestParam("courseOrder") Integer courseOrder,
            @RequestParam(value = "desc", required = false) String desc) {
        System.out.println("POST /misc/bugrep " + desc);
        
        boolean ok = bugService.addBugReport(schoolCode, stuNo, courseDate, courseOrder, desc);
        
        return new ResResBean.Builder<AppVerVo>()
                .status(ok ? ResResBean.STATUS_SUCCESS : ResResBean.STATUS_ERROR)
                .build();
    }
    
    @RequestMapping("/error")
    @ResponseBody
    public ResResBean<String> error(@RequestParam(value = "code", required = false) String code) {
        System.out.println("GET /misc/error " + code);
        
        return new ResResBean.Builder<String>()
                .status(ResResBean.STATUS_ERROR)
                .msg(code)
                .build();
    }
    
    /**
     * 获取配置文件内容
     */
    @RequestMapping("/config")
    @ResponseBody
    public String getConfig() {
        System.out.println("GET /misc/config");
        
        String configInfo = ExtraUtil.readFile(this.getClass()
                .getResourceAsStream("/resources/schttable.properties"));
        return configInfo;
    }
    
    /**
     * 看门狗
     * 
     * @return
     */
    @RequestMapping("/watchdog")
    @ResponseBody
    public ResResBean<WatchDogBean> watchDog(HttpServletRequest request) {
        System.out.println("GET /misc/watchdog");
        
        return new ResResBean.Builder<WatchDogBean>()
                .status(ResResBean.STATUS_SUCCESS)
                .msg(ResResBean.MSG_SUCCESS)
                .result(new WatchDogBean(request.getLocalPort(), System.currentTimeMillis()))
                .build();
    }
    
//    @RequestMapping("/calendar")
//    @ResponseBody
//    public JSONObject testCalendar(@RequestParam Long time) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("date", (new Date(time)).toString());
//        return jsonObject;
//    }
  
//    @RequestMapping("testjson")
//    @ResponseBody
//    public SchoolVo testJson() {
//        System.out.println("/misc/testjson");
//        
//        return new SchoolVo();
//    }
//
//    @RequestMapping("testjson2")
//    @ResponseBody
//    public String testJson2(@RequestBody GradeVo vo) {
//        System.out.println("/misc/testjson2");
//        
//        return String.valueOf(vo.getGpa());
//    }
//
//    @RequestMapping("testxml")
//    @ResponseBody
//    public WeChatResTxtMsgBean testXml() {
//        System.out.println("/misc/testxml");
//        
//        return new WeChatResTxtMsgBean();
//    }
//
//    @RequestMapping("testxml2")
//    @ResponseBody
//    public WeChatReqTxtMsgBean testXml2(@RequestBody WeChatReqTxtMsgBean msg) {
//        System.out.println("/misc/testxml2");
//        
//        return msg;
//    }
    
//    @RequestMapping("testencoding")
//    @ResponseBody
//    public String testJson(@RequestParam("text") String text) {
//        System.out.println("/misc/testencoding " + text);
//
//        return text;
//    }
}
