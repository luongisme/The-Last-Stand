package Managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Constant.EntityConstant;
import Entities.Enemies.Enemy;
import Helper.loadImg;
import Scene.Playing;
public class EnemyManager {

    private Playing playing;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float directionX, directionY;
    private BufferedImage[][][] enemyImgs; // [type][direction][frame] array to hold enemy images
    private final int DIRECTIONS = 4;
    private final int FRAMES = 3;
    private final int enemyTypes = EntityConstant.values().length;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        enemies = new ArrayList<>();
        directionX = 0;
        directionY = 0;

        
        enemyImgs = new BufferedImage[enemyTypes][DIRECTIONS][FRAMES];
        loadEnemyImgs();
        // addEnemy(2*16, 37*16, EntityConstant.SLIME);
        addEnemy(4*16, 37*16, EntityConstant.SKELETON);
        addEnemy(6*16, 37*16, EntityConstant.GOBLIN);
        addEnemy(8*16, 37*16, EntityConstant.GOBLIN_BOSS);
    }

    public void update(float dt){
        for (Enemy e : enemies) {
            e.update(dt);
        }
    }

    private void loadEnemyImgs() {

        int frameW = 32;
        int frameH = 32;
        int frames = enemyImgs[0].length;

        for (EntityConstant type : EntityConstant.values()) {
            int id = type.getId();

            // Load the specific PNG for this enemy
            String path = "resource/assets/sprites/" + type.getSpriteName();
            BufferedImage atlas = loadImg.load(path);

            if (atlas == null) {
                System.err.println("Failed to load: " + path);
                continue;
            }

            // Slice the PNG into frames horizontally
            for (int dir = 0; dir < DIRECTIONS; dir++) {
                for (int frame = 0; frame < FRAMES; frame++) {
                    int x = frame * frameW;
                    int y = dir * frameH;

                    enemyImgs[id][dir][frame] = atlas.getSubimage(x, y, frameW, frameH);
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

    public void draw(Graphics g){
        for(Enemy e : enemies){
            drawEnemy(e, g);
            e.drawHealthBar(g);
        }
    }

    public void drawEnemy(Enemy e, Graphics g){
        int type = e.getEnemyType();  // 0,1,2 → matches enum id
        int frame = e.getAnimationIndex();
        int dir = e.getLastDir();

        // Clamp values to avoid out-of-bounds
        if (type < 0 || type >= enemyImgs.length) type = 0;
        if (frame < 0 || frame >= enemyImgs[type][dir].length) frame = 0;

        BufferedImage img = enemyImgs[type][dir][frame];

        if (img != null) {
            g.drawImage(img, (int) e.getX(), (int) e.getY(), null);
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
