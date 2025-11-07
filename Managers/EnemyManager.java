package Managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Entities.Enemy.Enemy;
import Helper.loadImg;
import Scene.Playing;

public class EnemyManager {

    private Playing playing;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float directionX, directionY;
    private BufferedImage[][] enemyImgs; // [type][frame] array to hold enemy images

    public EnemyManager(Playing playing) {
        this.playing = playing;
        enemies = new ArrayList<>();
        directionX = 0;
        directionY = 0;
    enemyImgs = new BufferedImage[5][15]; // Assuming 5 different enemy types and up to 15 frames each
        addEnemy(2*16, 37*16);
        loadEnemyImgs();
    }

    public void update(){
        for(Enemy e : enemies){
            e.move(0.5f, 0);
            e.updateAnimation(); // update animation frame
        }
    }

    private void loadEnemyImgs() {
        BufferedImage atlas = loadImg.getSpriteAtlas();
        if (atlas == null) {
            System.err.println("Failed to load sprite atlas");
            return;
        }

        int frameW = 32;
        int frameH = 32;

        for (int type = 0; type < enemyImgs.length; type++){
            for (int frame = 0; frame < enemyImgs[0].length; frame++){
                int x = frame * frameW;
                int y = type * frameH;
                // ensure subimage is within atlas bounds
                if (x + frameW <= atlas.getWidth() && y + frameH <= atlas.getHeight()) {
                    enemyImgs[type][frame] = atlas.getSubimage(x, 3*32, frameW, frameH);
                } else {
                    // if out of bounds, set null and continue
                    enemyImgs[type][frame] = null;
                }
            }
        }
    }


    public void addEnemy(int x, int y){
        enemies.add(new Enemy(x, y, 1, 100, 10, 2, 2, 1)); // Example enemy
    }

    public void draw(Graphics g){
        for(Enemy e : enemies){
            drawEnemy(e, g);
            e.drawHealthBar(g);
        }
    }

    public void drawEnemy(Enemy e, Graphics g){
        int type = Math.max(0, Math.min(enemyImgs.length - 1, e.getEnemyType()));
        int frame = e.getAnimationIndex();
        // guard frame/index bounds
        if (frame < 0) frame = 0;
        if (frame >= enemyImgs[type].length) frame = 0;

        BufferedImage img = enemyImgs[type][frame];
        if (img != null) {
            g.drawImage(img, (int)e.getX(), (int)e.getY(), null);
        }
    }

    public void isNextTileRoad(float nextX, float nextY){
        // Check if the next tile is road
    }

    public float getDirectionX() {
        return directionX;
    }
    public float getDirectionY() {
        return directionY;
    }
    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }
    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }
}
