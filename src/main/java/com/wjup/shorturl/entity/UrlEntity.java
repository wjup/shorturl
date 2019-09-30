package com.wjup.shorturl.entity;

import java.io.Serializable;

/**
 * Create by wjup on 2019/9/29 11:33
 */
public class UrlEntity implements Serializable {

    private String uuid;
    private String shortUrlId;
    private String longUrl;
    private String createTime;
    private String lastView;
    private String viewPwd;

    public String getViewPwd() {
        return viewPwd;
    }

    public void setViewPwd(String viewPwd) {
        this.viewPwd = viewPwd;
    }

    public String getShortUrlId() {
        return shortUrlId;
    }

    public void setShortUrlId(String shortUrlId) {
        this.shortUrlId = shortUrlId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getLastView() {
        return lastView;
    }

    public void setLastView(String lastView) {
        this.lastView = lastView;
    }
}
