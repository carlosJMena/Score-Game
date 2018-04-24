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
    public void testSaveScore() throws Exception {
        int levelId = 1;
        int userId = 34;
        int score = 25;

        TreeMap<Integer,Integer> userScore = new TreeMap<Integer,Integer>();
        userScore.put(userId,score);
        TreeMap<Integer,Integer> userScore1 =  scoreService.getUserScoresByLevel(levelId);
        Assert.assertEquals(userScore1,userScore);
    }

}
