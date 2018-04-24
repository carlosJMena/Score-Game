package com.backend.core.service;

import com.backend.core.contentItems.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class SessionService {
    /**
     * Max Time Alive
     */
    public static final int ALIVE_TIME = 10;

    private final Map<String, Session> sessionActives;
    /**
     * Instance of SessionManager
     */
    public SessionService() {
        sessionActives = new HashMap<>();
    }
    /**
     * Method used to create a new Session for the selected userId
     *
     * @param userId user to create the new Session
     * @return the Session created for the user selected
     */
    public synchronized Session createNewSession(final Integer userId) {
        final Date now = new Date();
        final String newSessionKey = UUID.randomUUID().toString().replace("-", "");
        final Session session = new Session(userId, newSessionKey, now);
        sessionActives.put(newSessionKey, session);
        return session;
    }

    /**
     * Method used to validate if an sessionKey is associated
     * with a Valid and Active Session in the Server
     *
     * @param sessionKey key for the Session to validate
     * @return a true if the sessionKey has a valid Session associated
     */
    public synchronized boolean isSessionValid(final String sessionKey) {
        Session sessionActive = sessionActives.get(sessionKey);
        Date now = new Date();
        if (sessionActive != null) {
            if (now.getTime() - sessionActive.getCreatedTime().getTime() > TimeUnit.MINUTES.toMillis(ALIVE_TIME)) {
                sessionActives.remove(sessionKey);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public Session getSession(final String sessionkey){
        return sessionActives.get(sessionkey);
    }

}
