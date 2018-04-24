package com.backend.core.service;

import com.backend.core.contentItems.Session;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class SessionService {
    /**
     * Max time alive
     */
    public static final int ALIVE_TIME = 10;

    public final Map<String, Session> sessionActives;
    /**
     * Inizialize sessionActives
     */
    public SessionService() {
        sessionActives = new HashMap<>();
    }
    /**
     * Method in order to create a new Session
     *
     * @param userId to create the new Session
     * @return the Session created for the user
     */
    public synchronized Session createNewSession(final Integer userId) {
        final Date now = new Date();
        final String newSessionKey = UUID.randomUUID().toString().replace("-", "");
        final Session session = new Session(userId, newSessionKey, now);
        sessionActives.put(newSessionKey, session);
        return session;
    }

    /**
     * Method in order to know if a session is valid by sessionKey
     *
     * @param sessionKey key for the Session to validate
     * @return a true if the session is valid
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

    /**
     * Method in order to get the session by sessionkey
     *
     * @param sessionkey key for the Session to validate
     * @return the Session from sessionActives
     */
    public Session getSession(final String sessionkey){
        return sessionActives.get(sessionkey);
    }

    /**
     * Method used to remove all the invalid session from the singleton.
     */
    public synchronized void removeInvalidSessions() {
        final Date now = new Date();
        for (Session session : new ArrayList<>(sessionActives.values())) {
            if ((now.getTime() - session.getCreatedTime().getTime() > TimeUnit.MINUTES.toMillis(ALIVE_TIME))) {
                sessionActives.remove(session.getSessionKey());
            }
        }
    }

}
