package com.backend.core.service;

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
    public void testSaveScoresByOneLevel() throws Exception {
        for(int i=1;i<15;i++){
            scoreService.saveScore(1,i, ThreadLocalRandom.current().nextInt(1, 1000 + 1));
        }
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
