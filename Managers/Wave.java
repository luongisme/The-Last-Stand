package Managers; // 1. Add Package

import java.util.ArrayList;

public class Wave {
    private ArrayList<Integer> enemyList; // 2. standard naming convention

    public Wave(ArrayList<Integer> enemyList){
        this.enemyList = enemyList; // 3. Fix assignment
    }

    public ArrayList<Integer> getEnemyList(){
        return enemyList;
    }
}