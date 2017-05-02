package com.by_syk.schttableserver.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置信息
 */
@Component("config")
public class Config {
    @Value("${global.dir.html}")
    private String htmlDir;

    @Value("${global.dir.html.debug}")
    private String debugHtmlDir;

    @Value("${cdut.schoolcode}")
    private String cdutSchoolCode;
    
    @Value("${cdut.host}")
    private String cdutHost;
    
    @Value("${cdut.login}")
    private String cdutLogin;
    
    @Value("${cdut.home}")
    private String cdutHome;
    
    @Value("${cdut.grade}")
    private String cdutGrade;
    
    @Value("${neuq.schoolcode}")
    private String neuqSchoolCode;
    
    @Value("${neuq.host}")
    private String neuqHost;
    
    @Value("${neuq.index}")
    private String neuqIndex;
    
    @Value("${neuq.login}")
    private String neuqLogin;
    
    @Value("${neuq.login1}")
    private String neuqLogin1;
    
    @Value("${neuq.courses}")
    private String neuqCourses;

    @Value("${neuq.term}")
    private int neuqTerm;

    @Value("${neuq.weekorder1startdate}")
    private String neuqWeekOrder1StartDate;

    @Value("${cmc.schoolcode}")
    private String cmcSchoolCode;
    
    @Value("${cmc.host}")
    private String cmcHost;
    
    @Value("${cmc.index}")
    private String cmcIndex;
    
    @Value("${cmc.login}")
    private String cmcLogin;
    
    @Value("${cmc.graphicc}")
    private String cmcGraphicC;
    
    @Value("${cmc.home}")
    private String cmcHome;
    
    @Value("${cmc.term}")
    private int cmcTerm;
    
    @Value("${cmc.weekorder1startdate}")
    private String cmcWeekOrder1StartDate;

    @Value("${cmc.daycoursenum}")
    private int cmcDayCourseNum;

    @Value("${nju.schoolcode}")
    private String njuSchoolCode;
    
    @Value("${nju.host}")
    private String njuHost;
    
    @Value("${nju.index}")
    private String njuIndex;
    
    @Value("${nju.login}")
    private String njuLogin;
    
    @Value("${nju.userinfo}")
    private String njuUserInfo;

    @Value("${nju.courses}")
    private String njuCourses;

    @Value("${nju.term}")
    private int njuTerm;
    
    @Value("${nju.weekorder1startdate}")
    private String njuWeekOrder1StartDate;

    @Value("${nju.weeks}")
    private int njuWeeks;

    @Value("${nju.daycoursenum}")
    private int njuDayCourseNum;

    @Value("${gdou.schoolcode}")
    private String gdouSchoolCode;
    
    @Value("${gdou.host}")
    private String gdouHost;
    
    @Value("${gdou.index}")
    private String gdouIndex;
    
    @Value("${gdou.login}")
    private String gdouLogin;
    
    @Value("${gdou.graphicc}")
    private String gdouGraphicC;
    
    @Value("${gdou.home}")
    private String gdouHome;
    
    @Value("${gdou.term}")
    private int gdouTerm;
    
    @Value("${gdou.weekorder1startdate}")
    private String gdouWeekOrder1StartDate;

    public String getHtmlDir() {
        if ((new File(debugHtmlDir)).exists()) {
            return debugHtmlDir;
        }
        return htmlDir;
    }
    
    public String getCdutSchoolCode() {
        return cdutSchoolCode;
    }
    
    public String getCdutHost() {
        return cdutHost;
    }

    public String getCdutLogin() {
        return cdutLogin;
    }

    public String getCdutHome() {
        return cdutHome;
    }
    
    public String getNeuqSchoolCode() {
        return neuqSchoolCode;
    }

    public String getNeuqHost() {
        return neuqHost;
    }

    public String getNeuqIndex() {
        return neuqIndex;
    }

    public String getNeuqLogin() {
        return neuqLogin;
    }

    public String getNeuqLogin1() {
        return neuqLogin1;
    }

    public String getNeuqCourses() {
        return neuqCourses;
    }

    public int getNeuqTerm() {
        return neuqTerm;
    }

    public String getNeuqWeekOrder1StartDate() {
        return neuqWeekOrder1StartDate;
    }

    public String getCmcSchoolCode() {
        return cmcSchoolCode;
    }
    
    public String getCmcHost() {
        return cmcHost;
    }

    public String getCmcIndex() {
        return cmcIndex;
    }

    public String getCmcLogin() {
        return cmcLogin;
    }

    public String getCmcGraphicC() {
        return cmcGraphicC;
    }

    public String getCmcHome() {
        return cmcHome;
    }

    public int getCmcTerm() {
        return cmcTerm;
    }
    
    public String getCmcWeekOrder1StartDate() {
        return cmcWeekOrder1StartDate;
    }

    public int getCmcDayCourseNum() {
        return cmcDayCourseNum;
    }

    public String getCdutGrade() {
        return cdutGrade;
    }
    
    public String getNjuSchoolCode() {
        return njuSchoolCode;
    }

    public String getNjuHost() {
        return njuHost;
    }

    public String getNjuIndex() {
        return njuIndex;
    }

    public String getNjuLogin() {
        return njuLogin;
    }

    public String getNjuUserInfo() {
        return njuUserInfo;
    }

    public String getNjuCourses() {
        return njuCourses;
    }

    public int getNjuTerm() {
        return njuTerm;
    }
    
    public String getNjuWeekOrder1StartDate() {
        return njuWeekOrder1StartDate;
    }

    public int getNjuWeeks() {
        return njuWeeks;
    }

    public int getNjuDayCourseNum() {
        return njuDayCourseNum;
    }

    public String getGdouSchoolCode() {
        return gdouSchoolCode;
    }
    
    public String getGdouHost() {
        return gdouHost;
    }

    public String getGdouIndex() {
        return gdouIndex;
    }

    public String getGdouLogin() {
        return gdouLogin;
    }

    public String getGdouGraphicC() {
        return gdouGraphicC;
    }

    public String getGdouHome() {
        return gdouHome;
    }

    public int getGdouTerm() {
        return gdouTerm;
    }
    
    public String getGdouWeekOrder1StartDate() {
        return gdouWeekOrder1StartDate;
    }
}
