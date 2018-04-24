package com.backend.core.job;

import com.backend.core.service.SessionService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionCleanJob implements Runnable {

    /**
     * Instance for the SessionService,
     *
     */
    private final SessionService sessionService;

    /**
     * New instance of SessionCleanJob
     *
     * @param sessionService
     */
    public SessionCleanJob(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Method in order to start the job for cleaning session
     */
    public void startService() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this, SessionService.ALIVE_TIME, SessionService.ALIVE_TIME, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        sessionService.removeInvalidSessions();
    }
}

