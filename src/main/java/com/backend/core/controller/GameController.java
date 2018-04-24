package com.backend.core.controller;

import com.backend.core.contentItems.Session;
import com.backend.core.exceptions.SessionException;
import com.backend.core.service.ScoreService;
import com.backend.core.service.SessionService;

import java.util.Map;

import static com.backend.server.KingHttpHandler.USER_ID_PARAMETER;

public class GameController {
    /**
     * Singleton instance
     */
    public static GameController instance;
    /**
     * Instance for the ScoreManager,
     * where all the score data are stored.
     */
    private final ScoreService scoreService;
    /**
     * Instance for the SessionManager,
     * where all the session data are stored.
     */
    private final SessionService sessionService;


    public GameController() {
        scoreService = new ScoreService();
        sessionService = new SessionService();
    }


    /**
     * Obtain the instance for the singleton
     *
     * @return the instance initialized
     */
    public static GameController getInstance() {
        if (instance == null) {
            synchronized (GameController.class) {
                if (instance == null) {
                    instance = new GameController();
                }
            }
        }
        return instance;
    }

    /**
     * Login Request
     *
     * @param loginParams
     * @return sessionKey
     */
    public String login(Map<String, String> loginParams) {
        Session session = sessionService.createNewSession(Integer.parseInt(loginParams.get(USER_ID_PARAMETER)));
        return session.getSessionKey();
    }

    /**
     * Score Service Request
     *
     * @param sessionKey sessionKey for a valid and active Session
     * @param levelId    level to insert the score
     * @throws SessionException throw this if the session in invalid
     */
    public void score(int levelId,int score, String sessionKey) throws SessionException {
        if (!sessionService.isSessionValid(sessionKey)) {
            System.out.println("Session not valid");
            throw new SessionException("Session not valid");
        }
        Session session = sessionService.getSession(sessionKey);
        scoreService.saveScore(levelId,session.getUserId(),score);
    }

    public synchronized String getHighScoreList(Integer levelId) {
        String list = scoreService.getUserScoresByLevel(levelId)!=null?scoreService.getUserScoresByLevel(levelId).toString().replace("[", "").replace("]", "").replace(", ", ","):"";
        return list;
    }
}
