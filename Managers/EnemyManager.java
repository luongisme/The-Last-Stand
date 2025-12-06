package Managers;

import Constant.EntityConstant;
import Entities.Enemies.Enemy;
import Helper.LoadImages.loadImg;
import Map.LevelBuild;
import Scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

public class EnemyManager {

    private final Playing playing;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private LevelBuild levelBuild;

    private float directionX, directionY;

    private final Image[][][] enemyImgs; // [type][direction][frame]
    private final int DIRECTIONS = 4;
    private final int FRAMES = 3;
    private final int enemyTypes = EntityConstant.values().length;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        directionX = 0;
        directionY = 0;

        enemyImgs = new Image[enemyTypes][DIRECTIONS][FRAMES];
        loadEnemyImgs();

        // Test enemies
        addEnemy(4 * 16, 37 * 16, EntityConstant.SKELETON);
        addEnemy(6 * 16, 37 * 16, EntityConstant.GOBLIN);
        addEnemy(8 * 16, 37 * 16, EntityConstant.GOBLIN_BOSS);
    }

    public void update(float dt){
        for (Enemy e : enemies) {
            if(e.getIsAlive()){

                int centerX = (int)(e.getX() + e.getFrameW() / 2f);
                int centerY = (int)(e.getY() + e.getFrameH() / 2f);

                int tileType = getTileTypeAt(centerX, centerY);

                System.out.println(
                "[EnemyManager] Enemy type=" + e.getEnemyType()
                        + " pos=(" + centerX + "," + centerY + ")"
                        + " tileType=" + tileType
                );


            }
            e.update(dt);
        }
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

            String path = "resource/assets/assets/sprites/" + type.getSpriteName();
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

    private int getTileTypeAt(int x, int y){
        int maxY= LevelBuild.getRowFirst()*16;
        int maxX= LevelBuild.getColFirst()*16;

        if (x<0 || x>=maxX || y<0 || y>=maxY){
            return -1;
        }
        return playing.getTileTypeAt(x,y);
    }

    /**
     * Check if the tile at pixel position (x, y) is walkable
     * Uses Playing.isTileWalkable() to avoid duplicate logic
     */
    public boolean isTileWalkableAt(int x, int y) {
        int maxY = LevelBuild.getRowFirst() * 16;
        int maxX = LevelBuild.getColFirst() * 16;

        if (x < 0 || x >= maxX || y < 0 || y >= maxY) {
            return false;
        }
        return playing.isTileWalkable(x, y);
    }


    public float getDirectionX() { return directionX; }
    public float getDirectionY() { return directionY; }
    public void setDirectionX(float directionX) { this.directionX = directionX; }
    public void setDirectionY(float directionY) { this.directionY = directionY; }
}
