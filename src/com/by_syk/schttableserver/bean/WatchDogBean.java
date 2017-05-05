package com.by_syk.schttableserver.bean;

public class WatchDogBean {
    private int port;
    
    private long time;
    
    public WatchDogBean(int port, long time) {
        setPort(port);
        setTime(time);
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public int getPort() {
        return port;
    }
    
    public long getTime() {
        return time;
    }
}
