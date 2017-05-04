package com.by_syk.schttableserver.service;

public interface IBugService {
    boolean addBugReport(String schoolCode, String stuNo,
            Long courseDate, Integer courseOrder, String desc);
}
