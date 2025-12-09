package Managers;

import Constant.EntityConstant;
import Entities.Enemies.Enemy;
import Helper.LoadImages.loadImg;
import Map.LevelBuild;
import Scenes.Playing;

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

import Helper.PathFinding.*;

import Helper.Debug.WaypointDebugRenderer;

import java.util.ArrayList;
import java.util.List;


public class EnemyManager {

    // Debug mode flag - Set to false to disable debug rendering
    private static final boolean DEBUG_MODE = true;
    private final WaypointDebugRenderer debugRenderer = new WaypointDebugRenderer();

    private final Playing playing;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private LevelBuild levelBuild;

    private float directionX, directionY;

    private final Image[][][] enemyImgs; // [type][direction][frame]
    private final int DIRECTIONS = 4;
    private final int FRAMES = 3;
    private final int enemyTypes = EntityConstant.values().length;

    // ═══════════════════════════════════════════
    // PATHFINDING COMPONENTS
    // ═══════════════════════════════════════════
    private PathGrid pathGrid;
    private AStarPathfinder pathfinder;
    private RouteManager routeManager;
    private static final int TILE_SIZE = 16;

    private ArrayList<Enemy>[][] enemyGrid;
    private final int GRID_CELL_SIZE = 64;
    private int gridRows, gridCols;

    public EnemyManager(Playing playing) {
        this.playing = playing;

        enemyImgs = new Image[enemyTypes][DIRECTIONS][FRAMES];
        loadEnemyImgs();

        initEnemyGrid();
        initializePathfinding();

    }


    private void initializePathfinding() {
        // Get map data
        int[][] mapData = LevelBuild.getFirstMapData();

        // PathGrid
        pathGrid = new PathGrid(mapData, TILE_SIZE);

        //  A* pathfinder
        pathfinder = new AStarPathfinder(pathGrid);

        // RouteManager - use singleton instance
        routeManager = RouteManager.getInstance();
        routeManager.initializeDefaultRoutesForMap1();

        // Validate routes
        if (!routeManager.validateRouteOnGrid(pathGrid)) {
            System.err.println("WARNING: Some routes have invalid waypoints!");
        } else {
            System.out.println("Pathfinding initialized successfully!");
        }
    }

    /**
     * Spawn enemy trên Middle Lane để test
     */
    public void spawnEnemyOnMiddleLane(EntityConstant type) {
        Route middleLane = routeManager.getRouteByName("Middle Lane");
        if (middleLane == null) {
            System.err.println("Middle Lane route not found!");
            return;
        }

        spawnEnemyOnRoute(type, middleLane);
    }

    /**
     * Spawn enemy trên route cụ thể
     */
    public void spawnEnemyOnRoute(EntityConstant type, Route route) {
        if (route == null) {
            System.err.println("Cannot spawn enemy: route is null");
            return;
        }

        WayPoint spawnPoint = route.getSpawnPoint();
        if (spawnPoint == null) {
            System.err.println("Cannot spawn enemy: no spawn point in route");
            return;
        }


        float tileCenterX = spawnPoint.getPixelX(TILE_SIZE);
        float tileCenterY = spawnPoint.getPixelY(TILE_SIZE);

        // Enemy bounds là 32x32, nên top-left cần offset -16 để center enemy
        float spawnX = tileCenterX - 16;
        float spawnY = tileCenterY - 16;

        Enemy enemy = type.createEnemy(spawnX, spawnY);

        if (enemy != null) {
            // Tạo path controller
            EnemyPathController pathController = new EnemyPathController(
                route, pathfinder, pathGrid, TILE_SIZE
            );

            // Gán path controller cho enemy
            enemy.setPathController(pathController);

            enemies.add(enemy);

            System.out.println("Spawned " + type.name() + " on " + route.getRouteName() +
                             " at [" + spawnPoint.getGridX() + "," + spawnPoint.getGridY() + "]");
        } else {
            System.err.println("Failed to create enemy: " + type);
        }
    }


    /**
     * Spawn enemy với route ngẫu nhiên (random 1 trong 3 routes với tỉ lệ bằng nhau)
     */
    public void spawnEnemyWithPath(EntityConstant type) {
        Route route = routeManager.getRandomRoute();
        spawnEnemyOnRoute(type, route);
    }

    public void reset(){
        enemies.clear();
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


        playing.getWaveManager().update(dt);

        if(isTimeForNewEnemy()){
            spawnEnemy();
        }


        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();

        for (Enemy e : enemies) {
            e.update(dt);
            registerToGrid(e);
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

            // Use route-based spawning with pathfinding instead of fixed position
            spawnEnemyWithPath(enemyType);
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
        // Draw debug waypoints (behind enemies)
        if (DEBUG_MODE) {
            debugRenderer.render(gc);
        }

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
            // Center sprite on enemy position
            // Enemy bounds are 32x32, so we need to offset the sprite to center it
            float offsetX = (32 - e.getFrameW()) / 2f;
            float offsetY = (32 - e.getFrameH()) / 2f;

            // Draw sprite centered on enemy's actual position (no additional offset)
            gc.drawImage(img, e.getX() + offsetX, e.getY() + offsetY-8);

            drawStatusEffects(e, gc, img.getWidth(), img.getHeight());
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

    /**
     * Toggle waypoint debug rendering (D key)
     */
    public void toggleDebugWaypoints() {
        if (DEBUG_MODE) {
            debugRenderer.toggle();
        }
    }

    public boolean isDebugEnabled() {
        return DEBUG_MODE && debugRenderer.isEnabled();
    }

    public float getDirectionX() { return directionX; }
    public float getDirectionY() { return directionY; }
    public void setDirectionX(float directionX) { this.directionX = directionX; }
    public void setDirectionY(float directionY) { this.directionY = directionY; }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
    private void drawStatusEffects(Enemy e, GraphicsContext gc, double w, double h) {
        for (StatusEffect effect : e.getStatusEffects()) {
            effect.draw(gc, e.getX(), e.getY(), (float)w, (float)h);
        }
    }
}
