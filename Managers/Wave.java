package Managers;

import java.util.ArrayList;

public class Wave {
    private ArrayList<Integer> enemyList;
    private ArrayList<Float> timeList;



    public Wave(){
        enemyList = new ArrayList<>();
        timeList = new ArrayList<>();
    }

    public void addEnemyGroup(int enemyType, int count, float intervalSeconds) {
        for (int i = 0; i < count; i++) {
            enemyList.add(enemyType);
            timeList.add(intervalSeconds * 1000);
        }
    }

    public void addGap(float seconds) {
        if (!timeList.isEmpty()) {
            int lastIndex = timeList.size() - 1;
            // Overwrite the delay of the last enemy to be longer
            timeList.set(lastIndex, seconds * 1000); 
        }
    }

    public ArrayList<Integer> getEnemyList(){
        return enemyList;
    }

    public ArrayList<Float> getTimeList(){
        return timeList;
    }
}