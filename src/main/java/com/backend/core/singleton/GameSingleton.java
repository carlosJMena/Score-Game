package com.backend.core.singleton;

import com.backend.core.contentItems.Session;
import com.backend.core.exceptions.SessionException;
import com.backend.core.service.ScoreService;
import com.backend.core.service.SessionService;

import java.util.Map;

import static com.backend.server.KingHttpHandler.USER_ID_PARAMETER;

public class GameSingleton {
    /**
     * Singleton instance
     */
    public static GameSingleton instance;
    /**
     * Instance for the ScoreService
     *
     */
    public final ScoreService scoreService;
    /**
     * Instance for the SessionService
     *
     */
    public final SessionService sessionService;



    public GameSingleton() {
        scoreService = new ScoreService();
        sessionService = new SessionService();
    }


    /**
     * Obtain the instance for the singleton
     *
     * @return the instance initialized
     */
    public static GameSingleton getInstance() {
        if (instance == null) {
            synchronized (GameSingleton.class) {
                if (instance == null) {
                    instance = new GameSingleton();
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
     * @param score    score to insert
     * @throws SessionException throw this if the session in not valir
     */
    public void score(int levelId,int score, String sessionKey) throws SessionException {
        if (!sessionService.isSessionValid(sessionKey)) {
            System.out.println("Session not valid");
            throw new SessionException("Session not valid");
        }
        Session session = sessionService.getSession(sessionKey);
        scoreService.saveScore(levelId,session.getUserId(),score);
    }
    /**
     * Get HighScoreList scoreService Request
     *
     * @param levelId    level to get the HighScoreList
     */
    public synchronized String getHighScoreList(Integer levelId) {
        return  scoreService.getUserScoresByLevelAndSorted(levelId)!=null?scoreService.getUserScoresByLevelAndSorted(levelId).toString().replace("[", "").replace("]", "").replace(", ", ","):"";
    }
}
