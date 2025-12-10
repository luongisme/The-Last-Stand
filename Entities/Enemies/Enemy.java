package Entities.Enemies;

import Player.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Pathfinding imports
import Helper.PathFinding.EnemyPathController;
import Logic.Effects.StatusEffect;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // To avoid ConcurrentModification error when looping

public abstract class Enemy {
    protected float speed; // speedX, speedY;
    protected float tenacity; // 0.0 -> 1.0 (Effect resistance)
    protected int stunCount = 0; // Đếm số lượng effect làm choáng đang tác dụng
    protected float slowFactor = 0f;

    private float x, y;
    private int enemyID;
    private int maxHealth;
    private int health;
    private int damage;
    private int rewardGold; // Gold reward when killed
    private float speedX, speedY;
    private int enemyType;

    // STATES
    protected boolean isStunned = false;
    protected List<StatusEffect> statusEffects = new CopyOnWriteArrayList<>();
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
    private Player player;
    private float targetX, targetY;
    private boolean usePathfinding = false;
    private boolean reachedBase = false;

    // Ngưỡng để xác định đã đến target node (pixels)
    private static final float ARRIVAL_THRESHOLD = 4.0f;

    private float moveSpeed = 50f;

    private Rectangle2D bounds;

    // Animation (time-based)
    protected int animationIndex = 0;
    protected float animationTimer = 0f;
    protected float animationSpeed = 0.2f;
    protected int maxAnimationFrames = 3;

    public Enemy(float x, float y, int enemyType, int maxHealth) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.isHit = false;
        this.isAlive = true;
        this.maxHealth = maxHealth;
        this.tenacity = 0.0f;
        this.health = maxHealth;
        this.rewardGold = 10;
        this.bounds = new Rectangle2D(x, y, 32, 32);
        this.speed = moveSpeed;

        updateBounds();
    }

    public int getFrameW() { return frameW; }
    public int getFrameH() { return frameH; }

    // Update the JavaFX hitbox
    public void updateBounds() {
        this.bounds = new Rectangle2D(x, y, 32, 32);
    }

    // ==================== Health Bar Drawing ====================
    public void drawHealthBar(GraphicsContext gc) {
        if (health <= 0 || !isAlive) return;

        double barX = x+6;
        double barY = y - 6;

        final double HEALTH_BAR_WIDTH = 30.0;
        final double HEALTH_BAR_HEIGHT = 4.0;
        final double CENTER_OFFSET = 5.0;

        double healthPercent = Math.max(0.0, Math.min(1.0, (double) health / (double) maxHealth));

        double fillWidth = HEALTH_BAR_WIDTH * healthPercent;

        gc.setFill(Color.DARKRED);
        gc.fillRect(barX - CENTER_OFFSET, barY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        if (healthPercent > 0.6) {
            gc.setFill(Color.LIMEGREEN);
        }
        else if (healthPercent > 0.3) {
            gc.setFill(Color.YELLOW);
        }
        else if (healthPercent > 0.15) {
            gc.setFill(Color.ORANGE);
        }
        else {
            gc.setFill(Color.RED);
        }
        gc.fillRect(barX - CENTER_OFFSET, barY, fillWidth, HEALTH_BAR_HEIGHT);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(barX - CENTER_OFFSET, barY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    // ==================== Animation System ====================
    public void update(float dt) {
        float dtSeconds = dt / 1000.0f;
        // 1. Xử lý hiệu ứng (Độc vẫn rút máu kể cả khi choáng)
        updateStatusEffects(dtSeconds);

        // 2. Logic di chuyển (Chỉ chạy khi không choáng và còn sống)
        if (stunCount <= 0 && health > 0) {
            updateMove(dtSeconds);
            this.bounds = new Rectangle2D(x, y, frameW, frameH);
        }

        // 3. Animation
        if (stunCount <= 0) {
            updateAnimation(dtSeconds);
        }


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
        float enemyCenterX = this.x + 16;
        float enemyCenterY = this.y + 16;

        // Tính khoảng cách từ center enemy đến target center
        float dx = targetX - enemyCenterX;
        float dy = targetY - enemyCenterY;
        float distanceToTarget = (float) Math.sqrt(dx * dx + dy * dy);

        // Đã đến target
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


    private void updateStatusEffects(float dt) {
        for (StatusEffect effect : statusEffects) {
            effect.update(dt, this);
            if (!effect.isActive()) {
                statusEffects.remove(effect);
            }
        }
    }


    public void applyStatus(StatusEffect newEffect) {
        // Kiểm tra trùng lặp ID (ví dụ không cho stack 2 effect stun cùng lúc)
        for (StatusEffect e : statusEffects) {
            if (e.getId().equals(newEffect.getId())) {
                // Reset thời gian hiệu ứng cũ hoặc bỏ qua
                return;
            }
        }

        // Tính toán kháng hiệu ứng
        newEffect.applyResistance(this.tenacity);
        newEffect.onStart(this);
        statusEffects.add(newEffect);
    }

    public void hurt(int dmg) {
        this.health -= dmg;
        if (this.health < 0) this.health = 0;
    }

    // Hàm nội bộ để cập nhật speed thực tế
    private void recalculateSpeed() {
        this.speed = this.moveSpeed * (1.0f - this.slowFactor);
        if (this.speed < 0) this.speed = 0;
    }

    // Getter cho EnemyManager dùng để vẽ
    public boolean isStunned() { return stunCount > 0; }

    // ==================== Getters ====================
    public float getX() { return x; }
    public float getY() { return y; }
    public float getCenterX() { return x + frameW / 2.0f; }
    public float getCenterY() { return y + frameH / 2.0f; }
    public int getEnemyId() { return enemyID; }
    public int getEnemyHealth() { return health; }
    public int getMaxHealth(){return maxHealth;}
    public float getSpeed() { return speed; }
    public int getEnemyType() { return enemyType; }
    public int getAnimationIndex() { return animationIndex; }
    public Rectangle2D getBounds() { return bounds; }
    public int getEnemyDamage() { return damage; }
    public int getLastDir() { return lastDir; }
    public boolean getIsAlive() {return isAlive;}
    public boolean isHit() {return isHit;}
    public int getRewardGold() { return rewardGold; }
    public float getEnemySpeedX() { return speedX; }
    public float getEnemySpeedY() { return speedY; }
    public List<StatusEffect> getStatusEffects() { return statusEffects;}

    // ==================== Setters ====================
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setStunned(boolean stunned) { this.isStunned = stunned; }
    public void setLastDir(int lastDir) { this.lastDir = lastDir; }
    public void setEnemyID(int enemyID) { this.enemyID = enemyID; }
    public void setEnemyHealth(int health) { this.health = health; }
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
    public void setEnemyDamage(int damage) { this.damage = damage; }
    public void setRewardGold(int rewardGold) { this.rewardGold = rewardGold; }
    public void setEnemySpeedX(float speedX) { this.speedX = speedX; }
    public void setAlive(boolean alive) {isAlive = alive;}
    public void setHit(boolean hit) {isHit = hit;}
    public void setEnemySpeedY(float speedY) { this.speedY = speedY; }
    public void setEnemyType(int enemyType) { this.enemyType = enemyType; }

    public void onReachedBase(){
        player.takeDamage(1);
    }


    private void updateAnimationDirection(float dirX, float dirY) {
        if (Math.abs(dirX) > Math.abs(dirY)) {
            lastDir = dirX > 0 ? RIGHT : LEFT;
        } else {
            lastDir = dirY > 0 ? DOWN : UP;
        }
    }

    public void setPathController(EnemyPathController controller) {
        this.pathController = controller;
        this.usePathfinding = true;
        this.reachedBase = false;
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
    public void addStun() {
        this.stunCount++;
    }

    // Gọi khi kết thúc Stun
    public void removeStun() {
        this.stunCount--;
        if (this.stunCount < 0) this.stunCount = 0; // Safety check
    }

    // Gọi khi bắt đầu Slow
    public void addSlow(float factor) {
        this.slowFactor += factor;
        // Giới hạn slow tối đa (ví dụ không quá 90%)
        if (this.slowFactor > 0.9f) this.slowFactor = 0.9f;
        recalculateSpeed();
    }

    // Gọi khi kết thúc Slow
    public void removeSlow(float factor) {
        this.slowFactor -= factor;
        if (this.slowFactor < 0) this.slowFactor = 0;
        recalculateSpeed();
    }



}
