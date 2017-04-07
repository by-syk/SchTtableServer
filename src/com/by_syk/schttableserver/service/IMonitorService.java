package com.by_syk.schttableserver.service;

public interface IMonitorService {
    boolean addLog(String userKey, String ip,
            String brand, String model, Integer sdk,
            String appVerName, Integer appVerCode);
}
