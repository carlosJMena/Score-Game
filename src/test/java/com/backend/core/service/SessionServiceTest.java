package com.backend.core.service;

import com.backend.core.contentItems.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionServiceTest {

    SessionService sessionService;

    @Before
    public void setUp() throws Exception {
        sessionService = new SessionService();
    }


    @Test
    public void testCreateNewSession() throws Exception {
        Session session = sessionService.createNewSession(550);
        System.out.println("session.getUserId() = " + session.getUserId());
        System.out.println("session.getCreatedTime() = " + session.getCreatedTime());
        System.out.println("session.getSessionKey() = " + session.getSessionKey());
        Assert.assertNotNull("Session Invalid", session.getSessionKey());
    }

    @Test
    public void testIsSessionValid() throws Exception {
        Session session = sessionService.createNewSession(550);
        System.out.println("session.getUserId() = " + session.getUserId());
        System.out.println("session.getCreatedTime() = " + session.getCreatedTime());
        System.out.println("session.getSessionKey() = " + session.getSessionKey());
        String sessionKey = session.getSessionKey();
        boolean valid = sessionService.isSessionValid(sessionKey);
        Assert.assertTrue("Session Invalid", valid);

    }

}
