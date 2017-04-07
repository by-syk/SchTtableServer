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
    
    @Value("${neuq.graphicc}")
    private String neuqGraphicC;
    
    @Value("${neuq.courses}")
    private String neuqCourses;

    @Value("${neuq.term}")
    private int neuqTerm;

    @Value("${neuq.weekorder1startdate}")
    private String neuqWeekOrder1StartDate;

    @Value("${elu.host}")
    private String eluHost;
    
    @Value("${elu.index}")
    private String eluIndex;
    
    @Value("${elu.login}")
    private String eluLogin;
    
    @Value("${elu.userinfo}")
    private String eluUserInfo;

    @Value("${elu.courses}")
    private String eluCourses;

    @Value("${elu.term}")
    private int eluTerm;

    @Value("${elu.weekorder1startdate}")
    private String eluWeekOrder1StartDate;

    @Value("${elu.weeks}")
    private int eluWeeks;

    @Value("${cqu.host}")
    private String cquHost;
    
    @Value("${cqu.index}")
    private String cquIndex;
    
    @Value("${cqu.login}")
    private String cquLogin;
    
    @Value("${cqu.userinfo}")
    private String cquUserInfo;

    @Value("${cqu.courseshead}")
    private String cquCoursesHead;

    @Value("${cqu.courses}")
    private String cquCourses;

    @Value("${cqu.term}")
    private int cquTerm;
    
    @Value("${cqu.weekorder1startdate}")
    private String cquWeekOrder1StartDate;

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

    @Value("${suse.host}")
    private String suseHost;
    
    @Value("${suse.index}")
    private String suseIndex;
    
    @Value("${suse.login}")
    private String suseLogin;
    
    @Value("${suse.graphicc}")
    private String suseGraphicC;
    
    @Value("${suse.home}")
    private String suseHome;
    
    @Value("${suse.term}")
    private int suseTerm;
    
    @Value("${suse.weekorder1startdate}")
    private String suseWeekOrder1StartDate;

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

    @Value("${rzpt.host}")
    private String rzptHost;
    
    @Value("${rzpt.index}")
    private String rzptIndex;
    
    @Value("${rzpt.login}")
    private String rzptLogin;
    
    @Value("${rzpt.userinfo}")
    private String rzptUserInfo;

    @Value("${rzpt.courses}")
    private String rzptCourses;

    @Value("${rzpt.term}")
    private int rzptTerm;
    
    @Value("${rzpt.weekorder1startdate}")
    private String rzptWeekOrder1StartDate;

    @Value("${gzu.host}")
    private String gzuHost;
    
    @Value("${gzu.index}")
    private String gzuIndex;
    
    @Value("${gzu.login}")
    private String gzuLogin;
    
    @Value("${gzu.graphicc}")
    private String gzuGraphicC;
    
    @Value("${gzu.home}")
    private String gzuHome;
    
    @Value("${gzu.term}")
    private int gzuTerm;
    
    @Value("${gzu.weekorder1startdate}")
    private String gzuWeekOrder1StartDate;
    
    @Value("${sdjtu.host}")
    private String sdjtuHost;
    
    @Value("${sdjtu.index}")
    private String sdjtuIndex;
    
    @Value("${sdjtu.login}")
    private String sdjtuLogin;
    
    @Value("${sdjtu.graphicc}")
    private String sdjtuGraphicC;
    
    @Value("${sdjtu.userinfo}")
    private String sdjtuUserInfo;

    @Value("${sdjtu.courses}")
    private String sdjtuCourses;

    @Value("${sdjtu.term}")
    private int sdjtuTerm;

    @Value("${sdjtu.weekorder1startdate}")
    private String sdjtuWeekOrder1StartDate;

    @Value("${sdjtu.daycoursenum}")
    private int sdjtuDayCourseNum;

    @Value("${bbmc.host}")
    private String bbmcHost;
    
    @Value("${bbmc.index}")
    private String bbmcIndex;
    
    @Value("${bbmc.login}")
    private String bbmcLogin;
    
    @Value("${bbmc.graphicc}")
    private String bbmcGraphicC;
    
    @Value("${bbmc.courseshead}")
    private String bbmcCoursesHead;

    @Value("${bbmc.courses}")
    private String bbmcCourses;
    
    @Value("${bbmc.term}")
    private int bbmcTerm;
    
    @Value("${bbmc.weekorder1startdate}")
    private String bbmcWeekOrder1StartDate;

    @Value("${bbmc.daycoursenum}")
    private int bbmcDayCourseNum;

    @Value("${ncu.host}")
    private String ncuHost;
    
    @Value("${ncu.index}")
    private String ncuIndex;
    
    @Value("${ncu.login}")
    private String ncuLogin;
    
    @Value("${ncu.graphicc}")
    private String ncuGraphicC;
    
    @Value("${ncu.userinfo}")
    private String ncuUserInfo;

    @Value("${ncu.courses}")
    private String ncuCourses;

    @Value("${ncu.term}")
    private int ncuTerm;

    @Value("${ncu.weekorder1startdate}")
    private String ncuWeekOrder1StartDate;

    @Value("${aynu.host}")
    private String aynuHost;
    
    @Value("${aynu.index}")
    private String aynuIndex;
    
    @Value("${aynu.login}")
    private String aynuLogin;
    
    @Value("${aynu.graphicc}")
    private String aynuGraphicC;
    
    @Value("${aynu.userinfo}")
    private String aynuUserInfo;

    @Value("${aynu.courseshead}")
    private String aynuCoursesHead;

    @Value("${aynu.courses}")
    private String aynuCourses;
    
    @Value("${aynu.term}")
    private int aynuTerm;

    @Value("${aynu.weekorder1startdate}")
    private String aynuWeekOrder1StartDate;

    @Value("${aynu.daycoursenum}")
    private int aynuDayCourseNum;

    @Value("${ahut.host}")
    private String ahutHost;
    
    @Value("${ahut.index}")
    private String ahutIndex;
    
    @Value("${ahut.login}")
    private String ahutLogin;
    
    @Value("${ahut.graphicc}")
    private String ahutGraphicC;
    
    @Value("${ahut.home}")
    private String ahutHome;
    
    @Value("${ahut.term}")
    private int ahutTerm;

    @Value("${ahut.weekorder1startdate}")
    private String ahutWeekOrder1StartDate;

    @Value("${qau.host}")
    private String qauHost;
    
    @Value("${qau.index}")
    private String qauIndex;
    
    @Value("${qau.login}")
    private String qauLogin;
    
    @Value("${qau.graphicc}")
    private String qauGraphicC;
    
    @Value("${qau.userinfo}")
    private String qauUserInfo;

    @Value("${qau.courses}")
    private String qauCourses;

    @Value("${qau.term}")
    private int qauTerm;

    @Value("${qau.weekorder1startdate}")
    private String qauWeekOrder1StartDate;

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

    public String getNeuqGraphicC() {
        return neuqGraphicC;
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

    public String getEluHost() {
        return eluHost;
    }

    public String getEluIndex() {
        return eluIndex;
    }

    public String getEluLogin() {
        return eluLogin;
    }

    public String getEluUserInfo() {
        return eluUserInfo;
    }

    public String getEluCourses() {
        return eluCourses;
    }

    public int getEluTerm() {
        return eluTerm;
    }

    public String getEluWeekOrder1StartDate() {
        return eluWeekOrder1StartDate;
    }

    public int getEluWeeks() {
        return eluWeeks;
    }

    public String getCquHost() {
        return cquHost;
    }

    public String getCquIndex() {
        return cquIndex;
    }

    public String getCquLogin() {
        return cquLogin;
    }

    public String getCquUserInfo() {
        return cquUserInfo;
    }

    public String getCquCoursesHead() {
        return cquCoursesHead;
    }

    public String getCquCourses() {
        return cquCourses;
    }

    public int getCquTerm() {
        return cquTerm;
    }
    
    public String getCquWeekOrder1StartDate() {
        return cquWeekOrder1StartDate;
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

    public String getSuseHost() {
        return suseHost;
    }

    public String getSuseIndex() {
        return suseIndex;
    }

    public String getSuseLogin() {
        return suseLogin;
    }

    public String getSuseGraphicC() {
        return suseGraphicC;
    }

    public String getSuseHome() {
        return suseHome;
    }

    public int getSuseTerm() {
        return suseTerm;
    }
    
    public String getSuseWeekOrder1StartDate() {
        return suseWeekOrder1StartDate;
    }

    public String getCdutGrade() {
        return cdutGrade;
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

    public String getRzptHost() {
        return rzptHost;
    }

    public String getRzptIndex() {
        return rzptIndex;
    }

    public String getRzptLogin() {
        return rzptLogin;
    }

    public String getRzptUserInfo() {
        return rzptUserInfo;
    }

    public String getRzptCourses() {
        return rzptCourses;
    }

    public int getRzptTerm() {
        return rzptTerm;
    }
    
    public String getRzptWeekOrder1StartDate() {
        return rzptWeekOrder1StartDate;
    }

    public String getGzuHost() {
        return gzuHost;
    }

    public String getGzuIndex() {
        return gzuIndex;
    }

    public String getGzuLogin() {
        return gzuLogin;
    }

    public String getGzuGraphicC() {
        return gzuGraphicC;
    }

    public String getGzuHome() {
        return gzuHome;
    }
    
    public int getGzuTerm() {
        return gzuTerm;
    }

    public String getGzuWeekOrder1StartDate() {
        return gzuWeekOrder1StartDate;
    }

    public String getSdjtuHost() {
        return sdjtuHost;
    }

    public String getSdjtuIndex() {
        return sdjtuIndex;
    }

    public String getSdjtuLogin() {
        return sdjtuLogin;
    }

    public String getSdjtuGraphicC() {
        return sdjtuGraphicC;
    }

    public String getSdjtuUserInfo() {
        return sdjtuUserInfo;
    }

    public String getSdjtuCourses() {
        return sdjtuCourses;
    }

    public int getSdjtuTerm() {
        return sdjtuTerm;
    }

    public String getSdjtuWeekOrder1StartDate() {
        return sdjtuWeekOrder1StartDate;
    }

    public int getSdjtuDayCourseNum() {
        return sdjtuDayCourseNum;
    }

    public String getBbmcHost() {
        return bbmcHost;
    }

    public String getBbmcIndex() {
        return bbmcIndex;
    }

    public String getBbmcLogin() {
        return bbmcLogin;
    }

    public String getBbmcGraphicC() {
        return bbmcGraphicC;
    }

    public String getBbmcCoursesHead() {
        return bbmcCoursesHead;
    }

    public String getBbmcCourses() {
        return bbmcCourses;
    }

    public int getBbmcTerm() {
        return bbmcTerm;
    }
    
    public String getBbmcWeekOrder1StartDate() {
        return bbmcWeekOrder1StartDate;
    }

    public int getBbmcDayCourseNum() {
        return bbmcDayCourseNum;
    }

    public String getNcuHost() {
        return ncuHost;
    }

    public String getNcuIndex() {
        return ncuIndex;
    }

    public String getNcuLogin() {
        return ncuLogin;
    }

    public String getNcuGraphicC() {
        return ncuGraphicC;
    }

    public String getNcuUserInfo() {
        return ncuUserInfo;
    }

    public String getNcuCourses() {
        return ncuCourses;
    }

    public int getNcuTerm() {
        return ncuTerm;
    }
    
    public String getNcuWeekOrder1StartDate() {
        return ncuWeekOrder1StartDate;
    }

    public String getAynuHost() {
        return aynuHost;
    }

    public String getAynuIndex() {
        return aynuIndex;
    }

    public String getAynuLogin() {
        return aynuLogin;
    }

    public String getAynuGraphicC() {
        return aynuGraphicC;
    }

    public String getAynuUserInfo() {
        return aynuUserInfo;
    }

    public String getAynuCoursesHead() {
        return aynuCoursesHead;
    }

    public String getAynuCourses() {
        return aynuCourses;
    }

    public int getAynuTerm() {
        return aynuTerm;
    }
    
    public String getAynuWeekOrder1StartDate() {
        return aynuWeekOrder1StartDate;
    }

    public int getAynuDayCourseNum() {
        return aynuDayCourseNum;
    }

    public String getAhutHost() {
        return ahutHost;
    }

    public String getAhutIndex() {
        return ahutIndex;
    }

    public String getAhutLogin() {
        return ahutLogin;
    }

    public String getAhutGraphicC() {
        return ahutGraphicC;
    }

    public String getAhutHome() {
        return ahutHome;
    }

    public int getAhutTerm() {
        return ahutTerm;
    }
    
    public String getAhutWeekOrder1StartDate() {
        return ahutWeekOrder1StartDate;
    }

    public String getQauHost() {
        return qauHost;
    }

    public String getQauIndex() {
        return qauIndex;
    }

    public String getQauLogin() {
        return qauLogin;
    }

    public String getQauGraphicC() {
        return qauGraphicC;
    }

    public String getQauUserInfo() {
        return qauUserInfo;
    }

    public String getQauCourses() {
        return qauCourses;
    }

    public int getQauTerm() {
        return qauTerm;
    }

    public String getQauWeekOrder1StartDate() {
        return qauWeekOrder1StartDate;
    }
}
