package Managers;

import java.util.ArrayList;
import java.util.Arrays; // 1. Missing Import
import Scene.Playing;

public class WaveManager {

    private Playing playing;
    private ArrayList<Wave> waves = new ArrayList<>();
    
    // 2. Switch from Ticks (int) to Time (float)
    private float timeSinceLastSpawn = 0;
    private float spawnTimeLimit = 1000f; // Spawn every 1 second (1000ms)

    private int enemyIndex = 0;
    private int waveIndex = 0;
    
    public WaveManager(Playing playing){
        this.playing = playing;
        createWaves();
    }

    public void update(float dt){
        if(timeSinceLastSpawn < spawnTimeLimit){
            timeSinceLastSpawn += dt;
        }
    }

    public void reset() {
        waves.clear(); // Clear old level waves
        createWaves(); // Generate new waves
        
        // Reset counters
        enemyIndex = 0;
        waveIndex = 0;
        timeSinceLastSpawn = 0;
        
        System.out.println("Wave Manager Reset!");
    }

    public void createWaves(){
        int currentLevel = playing.getLevelIndex();

        if (currentLevel == 0) {
            // Level 1: 10 Enemies (Type 0)
            waves.add(new Wave(new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0))));
        } 
        else if (currentLevel == 1) {
            // Level 2: Harder! (Type 0 and Type 1)
            waves.add(new Wave(new ArrayList<>(Arrays.asList(0,0,1,0,0,1,1,1))));
        }
        else {
            // Level 3: Bosses?
            waves.add(new Wave(new ArrayList<>(Arrays.asList(1,1,2,2,2))));
        }
    }

    public int getNextEnemy(){
        timeSinceLastSpawn = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    public boolean isTimeForNewEnemy(){
        return timeSinceLastSpawn >= spawnTimeLimit;
    }
    
    public boolean isThereMoreEnemiesInWave(){
        return enemyIndex < waves.get(waveIndex).getEnemyList().size();
    }

    public ArrayList<Wave> getWaves(){ return waves; }
}