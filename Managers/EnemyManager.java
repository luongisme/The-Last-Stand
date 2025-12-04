package Managers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

import Constant.EntityConstant;
import Entities.Enemies.Enemy;
import Helper.loadImg;
import Scene.Playing;

public class EnemyManager {

    private Playing playing;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private float directionX, directionY;

    private Image[][][] enemyImgs; // [type][direction][frame]
    private final int DIRECTIONS = 4;
    private final int FRAMES = 3;
    private final int enemyTypes = EntityConstant.values().length;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        directionX = 0;
        directionY = 0;

        enemyImgs = new Image[enemyTypes][DIRECTIONS][FRAMES];
        loadEnemyImgs();
    }

    public void reset(){
        enemies.clear();
    }

    public void update(float dt){

        playing.getWaveManager().update(dt);

        if(isTimeForNewEnemy()){
            spawnEnemy();
        }

        for (Enemy e : enemies) {
            e.update(dt);
        }

        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();

        for (Enemy e : enemies) {
            e.update(dt);
            
            if (e.getX() >= 1604) { 
                enemiesToRemove.add(e);
            }
        }

        // REMOVE THEM SAFELY
        for (Enemy e : enemiesToRemove) {
            enemies.remove(e);
            
        }
        if (enemies.isEmpty()) {
            
            //Are there more enemies to spawn in CURRENT wave?
            if (!playing.getWaveManager().isThereMoreEnemiesInWave()) {
                
                //Are there MORE WAVES in this level?
                if (playing.getWaveManager().isThereMoreWaves()) {
                    // Start the 5 second timer (if not already started)
                    playing.getWaveManager().startWaveTimer();
                } 
                //No more waves? Then the LEVEL IS DONE.
                else {
                    //Make sure we don't trigger this if the wave timer is currently ticking down
                    if (!playing.getWaveManager().isWaveTimerStarted()) {
                        playing.loadNextLevel();
                    }
                }
            }
        }
    }

    private boolean isWaveFinished() {
        if (!enemies.isEmpty()) {
            return false;
        }
        
        if (playing.getWaveManager().isThereMoreEnemiesInWave()) {
            return false;
        }
        
        return true; 
    }


    private void spawnEnemy(){
        int enemyId = playing.getWaveManager().getNextEnemy();
        
        // Check bounds to prevent crash
        if(enemyId < EntityConstant.values().length) {
            EntityConstant enemyType = EntityConstant.values()[enemyId];
            
            float startX = 0;
            float startY = 20*16; // Example: Row 10 * GridSize
            
            addEnemy(startX, startY, enemyType);
        }
    }

    public boolean isTimeForNewEnemy(){
        if(playing.getWaveManager().isTimeForNewEnemy()){
            if(playing.getWaveManager().isThereMoreEnemiesInWave()){
                return true;
            }
        }
        return false;
    }

    /** ───────────────────────────────────────────────
     *  Load & slice sprite sheets using PixelReader
     *  Each PNG = 4 rows (directions) × 3 frames
     *  ───────────────────────────────────────────────
     */
    private void loadEnemyImgs() {
        for (EntityConstant type : EntityConstant.values()) {
            int id = type.getId();
            int fw = type.getFrameW();
            int fh = type.getFrameH();

            String path = "resource/sprites/" + type.getSpriteName();
            Image atlas = loadImg.load(path);

            if (atlas == null) {
                System.err.println("Failed to load: " + path);
                continue;
            }

            PixelReader reader = atlas.getPixelReader();
            if (reader == null) {
                System.err.println("PixelReader null: " + path);
                continue;
            }

            for (int dir = 0; dir < DIRECTIONS; dir++) {
                for (int frame = 0; frame < FRAMES; frame++) {

                    int sx = frame * fw;
                    int sy = dir * fh;

                    enemyImgs[id][dir][frame] =
                            new WritableImage(reader, sx, sy, fw, fh);
                }
            }
        }
    }

    public void addEnemy(float x, float y, EntityConstant type) {
        Enemy enemy = type.createEnemy(x, y);
        if (enemy != null) {
            enemies.add(enemy);
        } else {
            System.err.println("Failed to create enemy type: " + type);
        }
    }

    public void draw(GraphicsContext gc){
        for (Enemy e : enemies) {
            drawEnemy(e, gc);
            e.drawHealthBar(gc);
        }
    }

    private void drawEnemy(Enemy e, GraphicsContext gc) {
        int type = e.getEnemyType();
        int frame = e.getAnimationIndex();
        int dir = e.getLastDir();

        // Prevent array out of bounds
        if (type < 0 || type >= enemyImgs.length) type = 0;
        if (dir < 0 || dir >= DIRECTIONS) dir = 0;
        if (frame < 0 || frame >= FRAMES) frame = 0;

        Image img = enemyImgs[type][dir][frame];

        if (img != null) {
            gc.drawImage(img, e.getX(), e.getY());
        }
    }

    public float getDirectionX() { return directionX; }
    public float getDirectionY() { return directionY; }
    public void setDirectionX(float directionX) { this.directionX = directionX; }
    public void setDirectionY(float directionY) { this.directionY = directionY; }
}
