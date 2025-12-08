package Managers;

import java.util.ArrayList;
import java.util.Arrays; // 1. Missing Import
import Scene.Playing;

public class WaveManager {

    private Playing playing;
    private ArrayList<Wave> waves = new ArrayList<>();

    private float timeSinceLastSpawn = 0;
    private float spawnTimeLimit = 1000f; // Spawn every 1 second (1000ms)
    private float timeSinceLastWave = 0;
    private float waveTimerLimit = 5000f; // 5 seconds waiting between waves
    private boolean waveStartTimerActive = false;

    private float currentSpawnTick = 0;
    private float currentSpawnLimit = 0;

    private int enemyIndex = 0;
    private int waveIndex = 0;
    
    public WaveManager(Playing playing){
        this.playing = playing;
        createWaves();
    }

public void update(float dt){
        // STATE 1: Waiting for next wave
        if (waveStartTimerActive) {
            timeSinceLastWave += dt;
            if (timeSinceLastWave >= waveTimerLimit) {
                startNextWave();
            }
        } 
        // STATE 2: Spawning enemies
        else {
            currentSpawnTick += dt;
            if (currentSpawnTick >= currentSpawnLimit) {
                // Logic is handled in getNextEnemy
            }
        }
    }

    public int getNextEnemy(){
        currentSpawnTick = 0; // Reset timer
        // Get enemy ID
        int id = waves.get(waveIndex).getEnemyList().get(enemyIndex);
        // Get the time to wait for the NEXT enemy
        currentSpawnLimit = waves.get(waveIndex).getTimeList().get(enemyIndex);
        enemyIndex++;
        return id;
    }

    public void reset() {
        waves.clear();
        createWaves();
        enemyIndex = 0;
        waveIndex = 0;
        
        // Reset timers
        currentSpawnTick = 0;
        currentSpawnLimit = 0; // Start immediately
        
        timeSinceLastWave = 0;
        waveStartTimerActive = false;
    }

    private void startNextWave() {
        waveStartTimerActive = false;
        timeSinceLastWave = 0;
        waveIndex++;
        enemyIndex = 0;
        System.out.println("Wave " + (waveIndex + 1) + " started!");
    }

    public void startWaveTimer() {
        if (!waveStartTimerActive) {
            waveStartTimerActive = true;
            timeSinceLastWave = 0; // Reset timer
            System.out.println("Wave Finished! Timer started...");
        }
    }

    public boolean isWaveSpawningFinished() {
        return enemyIndex >= waves.get(waveIndex).getEnemyList().size();
    }

    public void skipWave() {
        if (isThereMoreWaves()) {
                // Force the timer to stop and reset
                waveStartTimerActive = false;
                timeSinceLastWave = 0;
                startNextWave();
                // add money reward
                // playing.getPlayer().addMoney(100); 
        }
    }

    public void createWaves(){
        int currentLevel = playing.getLevelIndex();
        
        // ─── LEVEL 1 ─────────────────────────────────────────
        if (currentLevel == 0) {
            Wave w1 = new Wave();
            Wave w2 = new Wave();
            Wave w3 = new Wave();
            Wave w4 = new Wave();
            Wave w5 = new Wave();
            Wave w6 = new Wave();

            //wave 1
            w1.addEnemyGroup(0, 4, 1f);
            w1.addGap(3.0f);
            w1.addEnemyGroup(1, 3, 0.5f);
            waves.add(w1);

            //wave 2
            w2.addEnemyGroup(0, 4, 0.5f);
            w2.addEnemyGroup(0, 2, 0.75f);
            w2.addGap(2.0f);
            w2.addEnemyGroup(1, 3, 1f);
            w2.addEnemyGroup(0, 3, 1f);
            w2.addGap(3.0f);
            w2.addEnemyGroup(0, 4, 0.75f);
            w2.addEnemyGroup(2, 2, 1.2f);
            waves.add(w2);
            // wave 3
            w3.addEnemyGroup(0, 3, 0.75f);
            w3.addEnemyGroup(1, 2, 1.25f);
            w3.addGap(3.0f);
            w3.addEnemyGroup(0, 5, 0.5f);
            w3.addEnemyGroup(1, 5, 1f);
            w3.addGap(3.0f);
            w3.addEnemyGroup(0, 2, 0.5f);
            w3.addEnemyGroup(1, 3, 1f);
            w3.addEnemyGroup(2, 3, 1.2f);
            waves.add(w3);
            //wave 4
            w4.addEnemyGroup(0, 4, 0.75f);
            w4.addEnemyGroup(1, 3, 0.75f);
            w4.addEnemyGroup(2, 3, 1f);
            w4.addGap(3.0f);
            w4.addEnemyGroup(1, 5, 0.5f);
            w4.addEnemyGroup(0, 5, 0.75f);
            w4.addEnemyGroup(2, 4, 0.85f);
            w4.addGap(5.0f);
            w4.addEnemyGroup(1, 3, 0.5f);
            w4.addEnemyGroup(2, 3, 0.75f);
            w4.addEnemyGroup(1, 2, 0.5f);
            w4.addEnemyGroup(2, 2, 0.75f);
            waves.add(w4);
            //wave 5
            w5.addEnemyGroup(0, 3, 0.5f);
            w5.addEnemyGroup(1, 2, 0.5f);
            w5.addEnemyGroup(0, 1, 0.5f);
            w5.addEnemyGroup(1, 3, 0.5f);
            w5.addGap(2f);
            w5.addEnemyGroup(1, 5, 0.5f);
            w5.addEnemyGroup(0, 3, 0.5f);
            w5.addEnemyGroup(2, 3, 0.5f);
            w5.addGap(2f);
            w5.addEnemyGroup(1, 3, 1f);
            w5.addEnemyGroup(2, 1, 0.75f);
            w5.addEnemyGroup(1, 2, 1f);
            w5.addEnemyGroup(2, 3, 0.75f);
            waves.add(w5);
            //wave 6
            w6.addEnemyGroup(0, 5, 0.75f);
            w6.addEnemyGroup(1, 6, 1f);
            w6.addEnemyGroup(2, 3, 1.5f);
            w6.addGap(2.5f);
            w6.addEnemyGroup(1, 5, 1f);
            w6.addEnemyGroup(2, 2, 1.5f);
            w6.addGap(5.0f);
            w6.addEnemyGroup(0, 6, 0.75f);
            w6.addEnemyGroup(1, 6, 1f);
            w6.addEnemyGroup(2, 7, 1.25f);
            waves.add(w6); 
        }
        
        // ─── LEVEL 2 ─────────────────────────────────────────
        else if (currentLevel == 1) {
            Wave w1 = new Wave();
            Wave w2 = new Wave();
            Wave w3 = new Wave();
            Wave w4 = new Wave();
            Wave w5 = new Wave();
            Wave w6 = new Wave();

            //wave 1
            w1.addEnemyGroup(0, 4, 1f);
            w1.addGap(3.0f);
            w1.addEnemyGroup(1, 3, 0.5f);
            waves.add(w1);

            //wave 2
            w2.addEnemyGroup(0, 4, 0.5f);
            w2.addEnemyGroup(0, 2, 0.75f);
            w2.addGap(2.0f);
            w2.addEnemyGroup(1, 3, 1f);
            w2.addEnemyGroup(0, 3, 1f);
            w2.addGap(3.0f);
            w2.addEnemyGroup(0, 4, 0.75f);
            w2.addEnemyGroup(2, 2, 1.2f);
            waves.add(w2);
            // wave 3
            w3.addEnemyGroup(0, 3, 0.75f);
            w3.addEnemyGroup(1, 2, 1.25f);
            w3.addGap(3.0f);
            w3.addEnemyGroup(0, 5, 0.5f);
            w3.addEnemyGroup(1, 5, 1f);
            w3.addGap(3.0f);
            w3.addEnemyGroup(0, 2, 0.5f);
            w3.addEnemyGroup(1, 3, 1f);
            w3.addEnemyGroup(2, 3, 1.2f);
            waves.add(w3);
            //wave 4
            w4.addEnemyGroup(0, 4, 0.75f);
            w4.addEnemyGroup(1, 3, 0.75f);
            w4.addEnemyGroup(2, 3, 1f);
            w4.addGap(3.0f);
            w4.addEnemyGroup(1, 5, 0.5f);
            w4.addEnemyGroup(0, 5, 0.75f);
            w4.addEnemyGroup(2, 4, 0.85f);
            w4.addGap(5.0f);
            w4.addEnemyGroup(1, 3, 0.5f);
            w4.addEnemyGroup(2, 3, 0.75f);
            w4.addEnemyGroup(1, 2, 0.5f);
            w4.addEnemyGroup(2, 2, 0.75f);
            waves.add(w4);
            //wave 5
            w5.addEnemyGroup(0, 3, 0.5f);
            w5.addEnemyGroup(1, 2, 0.5f);
            w5.addEnemyGroup(0, 1, 0.5f);
            w5.addEnemyGroup(1, 3, 0.5f);
            w5.addGap(2f);
            w5.addEnemyGroup(1, 5, 0.5f);
            w5.addEnemyGroup(0, 3, 0.5f);
            w5.addEnemyGroup(2, 3, 0.5f);
            w5.addGap(2f);
            w5.addEnemyGroup(1, 3, 1f);
            w5.addEnemyGroup(2, 1, 0.75f);
            w5.addEnemyGroup(1, 2, 1f);
            w5.addEnemyGroup(2, 3, 0.75f);
            waves.add(w5);
            //wave 6
            w6.addEnemyGroup(0, 5, 0.75f);
            w6.addEnemyGroup(1, 6, 1f);
            w6.addEnemyGroup(2, 3, 1.5f);
            w6.addGap(2.5f);
            w6.addEnemyGroup(1, 5, 1f);
            w6.addEnemyGroup(2, 2, 1.5f);
            w6.addGap(5.0f);
            w6.addEnemyGroup(0, 6, 0.75f);
            w6.addEnemyGroup(1, 6, 1f);
            w6.addEnemyGroup(2, 7, 1.25f);
            waves.add(w6);
        }

        // ─── LEVEL 3 ─────────────────────────────────────────
        else if (currentLevel == 2) {
            Wave w1 = new Wave();
            Wave w2 = new Wave();
            Wave w3 = new Wave();
            Wave w4 = new Wave();
            Wave w5 = new Wave();
            Wave w6 = new Wave();

            //wave 1
            w1.addEnemyGroup(0, 4, 1f);
            w1.addGap(3.0f);
            w1.addEnemyGroup(1, 3, 0.5f);
            waves.add(w1);

            //wave 2
            w2.addEnemyGroup(0, 4, 0.5f);
            w2.addEnemyGroup(0, 2, 0.75f);
            w2.addGap(2.0f);
            w2.addEnemyGroup(1, 3, 1f);
            w2.addEnemyGroup(0, 3, 1f);
            w2.addGap(3.0f);
            w2.addEnemyGroup(0, 4, 0.75f);
            w2.addEnemyGroup(2, 2, 1.2f);
            waves.add(w2);
            // wave 3
            w3.addEnemyGroup(0, 3, 0.75f);
            w3.addEnemyGroup(1, 2, 1.25f);
            w3.addGap(3.0f);
            w3.addEnemyGroup(0, 5, 0.5f);
            w3.addEnemyGroup(1, 5, 1f);
            w3.addGap(3.0f);
            w3.addEnemyGroup(0, 2, 0.5f);
            w3.addEnemyGroup(1, 3, 1f);
            w3.addEnemyGroup(2, 3, 1.2f);
            waves.add(w3);
            //wave 4
            w4.addEnemyGroup(0, 4, 0.75f);
            w4.addEnemyGroup(1, 3, 0.75f);
            w4.addEnemyGroup(2, 3, 1f);
            w4.addGap(3.0f);
            w4.addEnemyGroup(1, 5, 0.5f);
            w4.addEnemyGroup(0, 5, 0.75f);
            w4.addEnemyGroup(2, 4, 0.85f);
            w4.addGap(5.0f);
            w4.addEnemyGroup(1, 3, 0.5f);
            w4.addEnemyGroup(2, 3, 0.75f);
            w4.addEnemyGroup(1, 2, 0.5f);
            w4.addEnemyGroup(2, 2, 0.75f);
            waves.add(w4);
            //wave 5
            w5.addEnemyGroup(0, 3, 0.5f);
            w5.addEnemyGroup(1, 2, 0.5f);
            w5.addEnemyGroup(0, 1, 0.5f);
            w5.addEnemyGroup(1, 3, 0.5f);
            w5.addGap(2f);
            w5.addEnemyGroup(1, 5, 0.5f);
            w5.addEnemyGroup(0, 3, 0.5f);
            w5.addEnemyGroup(2, 3, 0.5f);
            w5.addGap(2f);
            w5.addEnemyGroup(1, 3, 1f);
            w5.addEnemyGroup(2, 1, 0.75f);
            w5.addEnemyGroup(1, 2, 1f);
            w5.addEnemyGroup(2, 3, 0.75f);
            waves.add(w5);
            //wave 6
            w6.addEnemyGroup(0, 5, 0.75f);
            w6.addEnemyGroup(1, 6, 1f);
            w6.addEnemyGroup(2, 3, 1.5f);
            w6.addGap(2.5f);
            w6.addEnemyGroup(1, 5, 1f);
            w6.addEnemyGroup(2, 2, 1.5f);
            w6.addGap(5.0f);
            w6.addEnemyGroup(0, 6, 0.75f);
            w6.addEnemyGroup(1, 6, 1f);
            w6.addEnemyGroup(2, 7, 1.25f);
            waves.add(w6);
        }
    }

    public boolean isTimeForNewEnemy(){
        return !waveStartTimerActive && currentSpawnTick >= currentSpawnLimit;
    }
    
    public boolean isThereMoreEnemiesInWave(){
        return enemyIndex < waves.get(waveIndex).getEnemyList().size();
    }

    public boolean isThereMoreWaves() {
        return waveIndex + 1 < waves.size();
    }
    
    public boolean isWaveTimerStarted() {
        return waveStartTimerActive;
    }

    public float getTimeLeft() { return (waveTimerLimit - timeSinceLastWave) / 1000f;}

    public int getWaveIndex() {
        return waveIndex + 1;
    }

    public int getTotalWaves() {
        return waves.size();
    }

    public ArrayList<Wave> getWaves(){ return waves; }
}