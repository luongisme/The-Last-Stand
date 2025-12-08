package Entities.Enemies;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Pathfinding imports
import Helper.PathFinding.EnemyPathController;

public abstract class Enemy {

    private float x, y;
    private int enemyID;
    private int maxHealth;
    private int health;
    private int damage;
    private float speedX, speedY;
    private int enemyType;

    private boolean isHit;
    private boolean isAlive;

    protected int frameW = 32;
    protected int frameH = 32;

    private int barWidth;
    private final int barLength = 4;

    // Directions
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    private int lastDir = RIGHT;

    private EnemyPathController pathController;
    private float targetX, targetY;
    private boolean usePathfinding = false;
    private boolean reachedBase = false;

    // Ngưỡng để xác định đã đến target node (pixels)
    private static final float ARRIVAL_THRESHOLD = 4.0f;

    // Tốc độ di chuyển (pixels per second)
    // 60 pixels/second = 1 pixel/frame at 60 FPS
    private float moveSpeed = 60f;

    // JavaFX hitbox
    private Rectangle2D bounds;

    // Animation (time-based)
    protected int animationIndex = 0;
    protected float animationTimer = 0f; // accumulated time in seconds
    protected float animationSpeed = 0.2f; // 0.3s = 300ms per frame (~3 fps)
    protected int maxAnimationFrames = 3;

    public Enemy(float x, float y, int enemyType) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.isHit=false;
        this.isAlive=true;
        this.maxHealth = 50;
        this.health = maxHealth;
        this.bounds = new Rectangle2D(x, y, 32, 32);

        updateBounds();
    }

    public int getFrameW() { return frameW; }
    public int getFrameH() { return frameH; }

    // ==================== Getters ====================
    public float getX() { return x; }
    public float getY() { return y; }
    public int getEnemyId() { return enemyID; }
    public int getEnemyHealth() { return health; }
    public int getMaxHealth(){return maxHealth;}
    public int getEnemyDamage() { return damage; }
    public boolean getIsAlive() {return isAlive;}
    public float getEnemySpeedX() { return speedX; }
    public float getEnemySpeedY() { return speedY; }
    public Rectangle2D getBounds() { return bounds; }
    public int getEnemyType() { return enemyType; }
    public int getLastDir() { return lastDir; }
    public int getAnimationIndex() { return animationIndex; }
    public boolean isHit() {return isHit;}



    // ==================== Setters ====================
    public void setEnemyID(int enemyID) { this.enemyID = enemyID; }
    public void setEnemyHealth(int health) { this.health = health; }
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
    public void setEnemyDamage(int damage) { this.damage = damage; }
    public void setEnemySpeedX(float speedX) { this.speedX = speedX; }
    public void setAlive(boolean alive) {isAlive = alive;}
    public void setHit(boolean hit) {isHit = hit;}
    public void setEnemySpeedY(float speedY) { this.speedY = speedY; }
    public void setEnemyType(int enemyType) { this.enemyType = enemyType; }
    public void setLastDir(int lastDir) { this.lastDir = lastDir; }

    // Update the JavaFX hitbox
    public void updateBounds() {
        this.bounds = new Rectangle2D(x, y, 32, 32);
    }

    // ==================== Health Bar Drawing ====================
    public void drawHealthBar(GraphicsContext gc) {
        float maxHealth = getMaxHealth();
        float currentHealth = this.health;

        if (currentHealth <= 0) return;

        int barX = (int) x;
        int barY = (int) y - 6;

        // Full health bar width
        int fullWidth = 50;

        // Scale width according to HP
        double hpRatio = currentHealth / maxHealth;
        double hpWidth = fullWidth * hpRatio;

        // Pick color
        if (hpRatio > 0.6) {
            gc.setFill(Color.GREEN);
        } 
        else if (hpRatio > 0.3) {
            gc.setFill(Color.YELLOW);
        } 
        else if (hpRatio > 0.15) {
            gc.setFill(Color.ORANGE);
        } 
        else {
            gc.setFill(Color.RED);
        }

        // hp bar outline
        gc.setStroke(Color.BLACK);
        gc.strokeRect(barX - 5, barY, fullWidth, 5);

        // hp bar
        gc.fillRect(barX - 5, barY, hpWidth, 5);
    }

    // ==================== Animation System ====================
    public void update(float dt) {
        // Convert dt from milliseconds to seconds for animation system
        float dtSeconds = dt / 1000.0f;
        updateAnimation(dtSeconds);

        // Use original dt (milliseconds) for movement which expects it
        updateMove(dt / 1000.0f);  // Also convert to seconds for consistent physics
    }

    private void updateAnimation(float dt) {
        animationTimer += dt;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0;
            animationIndex++;
            if (animationIndex >= maxAnimationFrames) {
                animationIndex = 0;
            }
        }
    }

    private void updateMove(float dt) {
        // Sử dụng pathfinding nếu có
        if (usePathfinding && pathController != null) {
            updatePathfindingMove(dt);
        } else {
            // Fallback: di chuyển theo hướng hiện tại
            updateSimpleMove(dt);
        }
    }

    /**
     * Di chuyển theo pathfinding
     */
    private void updatePathfindingMove(float dt) {
        if (pathController == null || reachedBase) {
            return;
        }

        // Kiểm tra đã đến đích cuối cùng chưa
        if (pathController.hasReachedDestination()) {
            reachedBase = true;
            onReachedBase();
            return;
        }

        // Kiểm tra đường có bị chặn không
        if (pathController.isPathBlocked()) {
            return;
        }

        // Lấy target position
        float[] nextPos = pathController.getNextTargetPosition();
        if (nextPos == null) {
            return;
        }

        targetX = nextPos[0];
        targetY = nextPos[1];

        // Tính center của enemy (vì targetX/Y là center của tile)
        float enemyCenterX = this.x + 16;  // Enemy bounds là 32x32, center ở +16
        float enemyCenterY = this.y + 16;

        // Tính khoảng cách từ center enemy đến target center
        float dx = targetX - enemyCenterX;
        float dy = targetY - enemyCenterY;
        float distanceToTarget = (float) Math.sqrt(dx * dx + dy * dy);

        // Đã đến target node?
        if (distanceToTarget <= ARRIVAL_THRESHOLD) {
            // Chuyển sang node tiếp theo
            pathController.advanceToNextNode();
            return;
        }

        // Di chuyển về phía target
        float moveDistance = moveSpeed * dt;

        // Normalize direction
        float dirX = dx / distanceToTarget;
        float dirY = dy / distanceToTarget;

        // Apply movement
        this.x += dirX * moveDistance;
        this.y += dirY * moveDistance;

        // Cập nhật hướng animation
        updateAnimationDirection(dirX, dirY);

        updateBounds();
    }

    /**
     * Di chuyển đơn giản (không dùng pathfinding)
     */
    private void updateSimpleMove(float dt) {
        float distance = moveSpeed * dt;

        float dx = 0, dy = 0;
        switch (lastDir) {
            case RIGHT -> dx = distance;
            case LEFT  -> dx = -distance;
            case UP    -> dy = -distance;
            case DOWN  -> dy = distance;
        }
        
        // Apply movement
        this.x += dx;
        this.y += dy;
        updateBounds();
    }

    /**
     * Cập nhật hướng nhìn cho animation
     */
    private void updateAnimationDirection(float dirX, float dirY) {
        if (Math.abs(dirX) > Math.abs(dirY)) {
            lastDir = dirX > 0 ? RIGHT : LEFT;
        } else {
            lastDir = dirY > 0 ? DOWN : UP;
        }
    }

    /**
     * Được gọi khi enemy đến base
     */
    protected void onReachedBase() {
        System.out.println("Enemy reached base!");
    }


    public void setPathController(EnemyPathController controller) {
        this.pathController = controller;
        this.usePathfinding = true;
        this.reachedBase = false;
    }

    public void onTowerPlaced(int tileSize) {
        if (pathController != null && usePathfinding) {
            int currentGridX = (int) (this.x / tileSize);
            int currentGridY = (int) (this.y / tileSize);
            pathController.recalculatePath(currentGridX, currentGridY);
        }
    }


    public boolean hasReachedBase() {
        return reachedBase;
    }


    public boolean isUsingPathfinding() {
        return usePathfinding;
    }


    public void setMoveSpeed(float speed) {
        this.moveSpeed = speed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public EnemyPathController getPathController() {
        return pathController;
    }

    // ==================== Attack Logic ====================
    public boolean canAttack() {
        return true;
    }

    // ==================== Take Damage Logic ====================

    //handle damage taken and death
    public void takeDamage(int damage){
        this.isHit=true;
        this.health-=damage;
        if(this.health<=0) {
            this.isAlive = false;
            this.health = 0;
        }
    }

    public void setSpriteSize(int w, int h) {
        this.frameW = w;
        this.frameH = h;
        updateBounds();
    }


}
