package com.backend.core.service;

import java.util.*;

public class ScoreService {
    /**
     * Max size by Levels.
     */
    private static final int MAX_USER_SCORES = 15;

    private final HashMap<Integer,TreeMap<Integer,Integer>> highestScoreList;


    /**
     * Instance of SessionService
     */
    public ScoreService() {
        highestScoreList = new HashMap<Integer,TreeMap<Integer,Integer>>();
    }

    public synchronized void saveScore(Integer levelId, Integer userId, Integer score) {
        TreeMap<Integer,Integer> levelUsersScores =  getUserScoresByLevel(levelId);
        handleTheSave(levelUsersScores, userId , score, levelId);
        SortedSet<Map.Entry<Integer,Integer>> sortedMap = entriesSortedByValues(getUserScoresByLevel(levelId));

        if(this.highestScoreList.get(levelId).size()>MAX_USER_SCORES){
            removeUserScores(this.highestScoreList.get(levelId));
        }
    }


    public TreeMap<Integer,Integer> getUserScoresByLevel(Integer levelId){
        return highestScoreList.get(levelId);
    }

    private void removeUserScores(TreeMap<Integer, Integer> levelUsersScores) {
        // remove the first key since it's the lowest score
        // The size of levelUsersScores always will be 15
        levelUsersScores.remove(levelUsersScores.firstKey());
    }

    private void handleTheSave(TreeMap<Integer, Integer> levelUsersScores, Integer userId, Integer score, Integer levelId) {
        if(levelUsersScores!=null){
            for(Map.Entry<Integer,Integer> userScore : levelUsersScores.entrySet()) {
                if(userId.equals(userScore.getKey())){
                    // If the user is already in there and the new score is bigger, let's update the userScore
                    if(userScore.getValue()<score){
                        levelUsersScores.put(userId,score);
                        return;
                    }
                    // If the user is already in there but the new score is lower, let's ignore it
                    return;
                }
            }
            // If the user is not there, let's create the userScore
            levelUsersScores.put(userId,score);
            return;
        }
        // If the level does not exist, let's create it and set the userScore
        TreeMap<Integer, Integer> levelUsersScoresNew = new TreeMap<Integer, Integer>();
        levelUsersScoresNew.put(userId,score);
        highestScoreList.put(levelId,levelUsersScoresNew);
    }

    //Method for sorting the TreeMap based on values
    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
