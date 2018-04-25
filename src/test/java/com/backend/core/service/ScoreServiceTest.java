package com.backend.core.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;


public class ScoreServiceTest {
    ScoreService scoreService;


    @Before
    public void setUp() throws Exception {
        scoreService = new ScoreService();
    }


    @Test
    public void testSaveScore() throws Exception {
        int levelId = 1;
        int userId = 78;
        int score = 12;
        scoreService.saveScore(levelId,userId,score);
        String userScore = scoreService.getUserScoresByLevel(levelId).toString();
        Assert.assertEquals("User Score", "{78=12}", userScore);
    }
    @Test
    public void testSaveUserScoresInTheSameLevel() throws Exception {
        int levelId = 1;
        for(int i=1;i<3;i++){
                scoreService.saveScore(levelId,i, i);
        }
        String userScore = scoreService.getUserScoresByLevel(levelId).toString();
        Assert.assertEquals("User Scores", "{1=1, 2=2}", userScore);
    }
    @Test
    public void testSaveScoresBySeveralLevelsAndCheckMaxSize() throws Exception {
        for(int i=1;i<20;i++){
            for(int j=1;j<15;j++){
                scoreService.saveScore(j,i, ThreadLocalRandom.current().nextInt(1, 1000 + 1));
            }
        }
    }
    @Test
    public void testHighestUserScoresFromALevel() throws Exception {
        Integer levelId = 2;
        for(int i=1;i<20;i++){
            for(int j=1;j<15;j++){
                scoreService.saveScore(j,i, ThreadLocalRandom.current().nextInt(1, 1000 + 1));
            }
        }
        String result = scoreService.getUserScoresByLevelAndSorted(levelId)!=null?scoreService.getUserScoresByLevelAndSorted(levelId).toString().replace("[", "").replace("]", "").replace(", ", ","):"";

        System.out.println(result);
    }

}
