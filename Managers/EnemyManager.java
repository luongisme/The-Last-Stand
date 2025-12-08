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

// Pathfinding imports
import Helper.PathFinding.*;

// Debug imports
import Helper.Debug.WaypointDebugRenderer;

import java.util.ArrayList;
import java.util.Iterator;

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
    private int spawnCounter = 0;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        directionX = 0;
        directionY = 0;

        enemyImgs = new Image[enemyTypes][DIRECTIONS][FRAMES];
        loadEnemyImgs();

        initializePathfinding();

        spawnEnemyOnRoute(EntityConstant.GOBLIN_BOSS, routeManager.getRouteByName("South Lane"));
        spawnEnemyOnRoute(EntityConstant.SKELETON,routeManager.getRouteByName("North Lane"));
        spawnEnemyOnRoute(EntityConstant.GOBLIN,routeManager.getRouteByName("Middle Lane"));

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


    public void spawnEnemyWithPath(EntityConstant type) {
        Route route = routeManager.getRouteForEnemy(spawnCounter);
        spawnCounter++;
        spawnEnemyOnRoute(type, route);
    }

    public void update(float dt){
        for (Enemy e : enemies) {
            e.update(dt);

            if(e.getIsAlive()){
                // Get enemy's CENTER position for more accurate tile checking
                int pixelX = (int)e.getX();
                int pixelY = (int)e.getY();
                int centerX = pixelX + 16;  // Enemy bounds 32x32, center at +16
                int centerY = pixelY + 16;

                // Convert CENTER to grid coordinates (more accurate)
                int gridX = centerX / 16;
                int gridY = centerY / 16;

                // Get tile at enemy's CENTER position
                int tileType = getTileTypeAt(centerX, centerY);

                System.out.println(
                "[EnemyManager] Enemy type=" + e.getEnemyType()
                        + " pixel=(" + pixelX + "," + pixelY + ")"
                        + " center=(" + centerX + "," + centerY + ")"
                        + " grid=(" + gridX + "," + gridY + ")"
                        + " tileType=" + tileType
                );
            }
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
        // Draw debug waypoints (behind enemies)
        if (DEBUG_MODE) {
            debugRenderer.render(gc);
        }

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
            // Center sprite on enemy position
            // Enemy bounds are 32x32, so we need to offset the sprite to center it
            float offsetX = (32 - e.getFrameW()) / 2f;
            float offsetY = (32 - e.getFrameH()) / 2f;

            // Draw sprite centered on enemy's actual position (no additional offset)
            gc.drawImage(img, e.getX() + offsetX, e.getY() + offsetY-8);
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
     * Toggle waypoint debug rendering (F3 key)
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
}
