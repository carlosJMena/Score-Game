package com.backend.core.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.TreeMap;
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
        for(int i=1;i<16;i++){
            for(int j=1;j<15;j++){
                scoreService.saveScore(j,i, ThreadLocalRandom.current().nextInt(1, 1000 + 1));
            }
        }
    }

}
