package Managers;


import Constant.EntityConstant;
import Entities.Enemies.Enemy;
import Helper.LoadImages.loadImg;
import Helper.MathUtil;
import Logic.Effects.StatusEffect;
import Scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {

    private Playing playing;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private float directionX, directionY;

    private Image[][][] enemyImgs; // [type][direction][frame]
    private final int DIRECTIONS = 4;
    private final int FRAMES = 3;
    private final int enemyTypes = EntityConstant.values().length;

    private ArrayList<Enemy>[][] enemyGrid;
    private final int GRID_CELL_SIZE = 64;
    private int gridRows, gridCols;

    public EnemyManager(Playing playing) {
        this.playing = playing;

        enemyImgs = new Image[enemyTypes][DIRECTIONS][FRAMES];
        loadEnemyImgs();

        initEnemyGrid();
        // Test enemies
        addEnemy(33 * 16, 10 * 16, EntityConstant.SKELETON);
        addEnemy(38 * 16, 10 * 16, EntityConstant.GOBLIN);
        addEnemy(45 * 16, 10 * 16, EntityConstant.GOBLIN_BOSS);

    }

    private void initEnemyGrid() {
        int lvlH = playing.getLvlData().length * 16;
        int lvlW = playing.getLvlData()[0].length * 16;

        gridRows = (lvlH / GRID_CELL_SIZE) + 1;
        gridCols = (lvlW / GRID_CELL_SIZE) + 1;

        enemyGrid = new ArrayList[gridRows][gridCols];
        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                enemyGrid[y][x] = new ArrayList<>();
            }
        }
    }

    public void update(float dt){
        clearGrid();

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);

            // Xóa quái đã chết
            if (e.getEnemyHealth() <= 0) {
                enemies.remove(i);
                i--;
                continue;
            }

            e.update(dt);
            registerToGrid(e);
        }
    }

    private void clearGrid() {
        for (int y = 0; y < gridRows; y++) {
            for (int x = 0; x < gridCols; x++) {
                enemyGrid[y][x].clear();
            }
        }
    }

    private void registerToGrid(Enemy e) {
        int col = (int) (e.getX() / GRID_CELL_SIZE);
        int row = (int) (e.getY() / GRID_CELL_SIZE);

        if (col >= 0 && col < gridCols && row >= 0 && row < gridRows) {
            enemyGrid[row][col].add(e);
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

    public List<Enemy> getEnemiesInRange(float x, float y, float radius) {
        List<Enemy> result = new ArrayList<>();

        // Chỉ quét các ô Grid nằm trong phạm vi bán kính
        int startCol = (int) ((x - radius) / GRID_CELL_SIZE);
        int endCol = (int) ((x + radius) / GRID_CELL_SIZE);
        int startRow = (int) ((y - radius) / GRID_CELL_SIZE);
        int endRow = (int) ((y + radius) / GRID_CELL_SIZE);

        // Kẹp biên để không lỗi ArrayOutOfBounds
        startCol = Math.max(0, startCol);
        endCol = Math.min(gridCols - 1, endCol);
        startRow = Math.max(0, startRow);
        endRow = Math.min(gridRows - 1, endRow);

        // Duyệt các ô lưới tiềm năng
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                for (Enemy e : enemyGrid[row][col]) {
                    // Kiểm tra khoảng cách chính xác (Pythagoras)
                    if (MathUtil.getDistance(x, y, e.getCenterX(), e.getCenterY()) <= radius) {
                        result.add(e);
                    }
                }
            }
        }
        return result;
    }

    public List<Enemy> getAllEnemies() {
        return enemies;
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
            drawHealthBar(e, gc);
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

            drawStatusEffects(e, gc, img.getWidth(), img.getHeight());
        }
    }

    private void drawStatusEffects(Enemy e, GraphicsContext gc, double w, double h) {
        for (StatusEffect effect : e.getStatusEffects()) {
            effect.draw(gc, e.getX(), e.getY(), (float)w, (float)h);
        }
    }

    // ==================== Health Bar Drawing ====================
    public void drawHealthBar(Enemy e, GraphicsContext gc) {
        float maxHealth = e.getMaxHealth();
        float currentHealth = e.getEnemyHealth();

        if (currentHealth <= 0 || currentHealth >= maxHealth) return; // Không vẽ nếu chết hoặc đầy máu (tùy chọn)

        // Cấu hình thanh máu
        int barWidth = 32; // Chiều rộng bằng với Sprite quái (32px)
        int barHeight = 4;
        int barX = (int) e.getX();
        int barY = (int) e.getY() - 6; // Vẽ trên đầu quái một chút

        float hpRatio = currentHealth / maxHealth;

        // 1. Vẽ nền (Đỏ - Phần máu đã mất)
        gc.setFill(Color.RED);
        gc.fillRect(barX, barY, barWidth, barHeight);

        // 2. Vẽ máu hiện tại (Xanh lá - thay đổi màu theo mức máu)
        if (hpRatio > 0.6) {
            gc.setFill(Color.GREEN);
        } else if (hpRatio > 0.3) {
            gc.setFill(Color.YELLOW);
        } else {
            gc.setFill(Color.ORANGE); // Hoặc Red tùy ý
        }

        gc.fillRect(barX, barY, barWidth * hpRatio, barHeight);

        // 3. Vẽ viền đen (Cho nổi bật)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(barX, barY, barWidth, barHeight);
    }

    public float getDirectionX() { return directionX; }
    public float getDirectionY() { return directionY; }
    public void setDirectionX(float directionX) { this.directionX = directionX; }
    public void setDirectionY(float directionY) { this.directionY = directionY; }
}
