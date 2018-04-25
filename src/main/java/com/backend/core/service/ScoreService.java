package com.backend.core.service;

import java.util.*;

public class ScoreService {
    /**
     * Max size by Levels.
     */
    public static final int MAX_USER_SCORES = 15;
    /**
     * highestScoreList
     */
    public final HashMap<Integer,SortedMap<Integer,Integer>> highestScoreList;


    /**
     * Inizialize highestScoreList
     */
    public ScoreService() {
        highestScoreList = new HashMap<Integer,SortedMap<Integer,Integer>>();
    }

    public synchronized void saveScore(Integer levelId, Integer userId, Integer score) {
        SortedMap<Integer,Integer> levelUsersScores =  getUserScoresByLevel(levelId);
        handleTheSave(levelUsersScores, userId , score, levelId);
        Set<Map.Entry<Integer,Integer>>  sortedUserScores = entriesSortedByValues(getUserScoresByLevel(levelId));
        if(sortedUserScores.size()>MAX_USER_SCORES){
            removeUserScores(sortedUserScores);
        }
        levelUsersScores =  getUserScoresByLevel(levelId);
        levelUsersScores.clear();
        // set sortedUserScores and the 15 highest into highestScoreList
        for (Iterator<Map.Entry<Integer, Integer>> it = sortedUserScores.iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            levelUsersScores.put(entry.getKey(),entry.getValue());
        }

        System.out.println(this.highestScoreList);
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------");
    }


    /**
     * Method in order to get userScores by levelId
     *
     * @param levelId
     * @return userScores
     */
    public SortedMap<Integer,Integer> getUserScoresByLevel(Integer levelId){
        return highestScoreList.get(levelId);
    }
    /**
     * Method in order to get userScores by levelId and sorted
     *
     * @param levelId
     * @return userScores sorted
     */
    public Set<Map.Entry<Integer,Integer>> getUserScoresByLevelAndSorted(Integer levelId){
        return entriesSortedByValues(getUserScoresByLevel(levelId));
    }

    /**
     * Method in order to know if a session is valid by sessionKey
     *
     * @param sortedUserScores sortedUserScores by level
     */
    public void removeUserScores(Set<Map.Entry<Integer,Integer>> sortedUserScores) {
        // The size of sortedUserScores always will be 15
        for (Iterator<Map.Entry<Integer, Integer>> iterator = sortedUserScores.iterator(); iterator.hasNext();) {
            Map.Entry<Integer, Integer> s =  iterator.next();
            // remove if the iterator is the first key of the sortedUSerScores since that will be the lowest score
            if(s.equals(sortedUserScores.iterator().next())){
                iterator.remove();
                return;
            }
        }
    }
    /**
     * Method in order to know if a session is valid by sessionKey
     *
     * @param levelUsersScores key for the Session to validate
     * @param userId
     * @param score
     * @param levelId
     */
    public void handleTheSave(SortedMap<Integer, Integer> levelUsersScores, Integer userId, Integer score, Integer levelId) {
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
