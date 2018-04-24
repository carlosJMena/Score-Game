package com.backend.core.contentItems;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable{
    /**
     * UserId
     */
    private Integer userId;
    /**
     * SessionKey
     */
    private String sessionKey;
    /**
     * createdTime
     */
    private Date createdTime;

    public Session(Integer userId, String sessionKey, Date createdTime) {
        this.userId = userId;
        this.sessionKey = sessionKey;
        this.createdTime = createdTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
