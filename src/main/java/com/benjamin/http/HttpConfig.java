package com.benjamin.http;

public class HttpConfig {
    private int DEFAULT_TIMEOUT = 20;
    private String baseUrl;
    private int timeOut = DEFAULT_TIMEOUT;
    private HttpResultConfig resultConfig;
    private boolean allowProxy = true;
    private boolean openLogger = true;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public boolean isOpenLogger() {
        return openLogger;
    }

    public void setOpenLogger(boolean openLogger) {
        this.openLogger = openLogger;
    }

    public boolean isAllowProxy() {
        return allowProxy;
    }

    public void setAllowProxy(boolean allowProxy) {
        this.allowProxy = allowProxy;
    }

    public HttpResultConfig getResultConfig() {
        return resultConfig;
    }

    public void setResultConfig(HttpResultConfig resultConfig) {
        this.resultConfig = resultConfig;
    }
}