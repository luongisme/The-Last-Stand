package Entities.Enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import Logic.Effects.StatusEffect;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // To avoid ConcurrentModification error when looping

public abstract class Enemy {
    // CORE STATS
    protected float x, y;
    protected int enemyType;
    protected int maxHealth;
    protected int health;
    protected int damage;
    protected float speed; // speedX, speedY;
    protected float tenacity; // 0.0 -> 1.0 (Effect resistance)
    protected float baseSpeed; // Tốc độ gốc (không đổi)
    protected int stunCount = 0; // Đếm số lượng effect làm choáng đang tác dụng
    protected float slowFactor = 0f;

    // VISUAL / HITBOX
    protected int frameW = 32, frameH = 32;
    protected Rectangle2D bounds;
    protected int lastDir = 0;

    // STATES
    protected boolean isStunned = false;
    protected List<StatusEffect> statusEffects = new CopyOnWriteArrayList<>();

    // ANIMATION
    protected int animationIndex = 0;
    protected float animationTimer = 0f; // accumulated time in ms
    protected float animationSpeed = 0.1f; // ms per frame (default)
    protected int maxAnimationFrames = 3;

    // Directions
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;

    // Patrol logic
    private float patrolTimer = 0;
    private float patrolInterval = 2000;
    private int patrolStep = 0;

    private final int[] patrolDirections = {
        RIGHT, DOWN, LEFT, UP
    };

    public Enemy(float x, float y, int enemyType, int maxHealth, float speed, float tenacity) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.baseSpeed = speed;
        this.speed = speed;
        this.tenacity = tenacity;
        this.bounds = new Rectangle2D(x, y, 32, 32);

//        updateBounds();
    }
    
    public void setSpriteSize(int w, int h) {
        this.frameW = w;
        this.frameH = h;
//        updateBounds();
    }

//    // Update the JavaFX hitbox
//    public void updateBounds() { this.bounds = new Rectangle2D(x, y, 32, 32); }

    // ==================== Animation System ====================
    public void update(float dt) {
        // 1. Xử lý hiệu ứng (Độc vẫn rút máu kể cả khi choáng)
        updateStatusEffects(dt);

        // 2. Logic di chuyển (Chỉ chạy khi không choáng và còn sống)
        if (stunCount <= 0 && health > 0) {
            move(dt);
            this.bounds = new Rectangle2D(x, y, frameW, frameH);
        }

        if (stunCount <= 0 && health > 0) {
            move(dt);
            this.bounds = new Rectangle2D(x, y, frameW, frameH);
        }

        // 3. Animation
        if (stunCount <= 0) {
            updateAnimation(dt);
        }
    }

    private void updateAnimation(float dt) {
        animationTimer += dt;
        if (animationTimer >= animationSpeed) {
            animationTimer = 0; // or -= animationSpeed
            animationIndex++;
            if (animationIndex >= maxAnimationFrames) {
                animationIndex = 0;
            }
        }
    }

    private void updateStatusEffects(float dt) {
        for (StatusEffect effect : statusEffects) {
            effect.update(dt, this);
            if (!effect.isActive()) {
                statusEffects.remove(effect);
            }
        }
    }

    public abstract void move(float dt);

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

    // Hàm nội bộ để cập nhật speed thực tế
    private void recalculateSpeed() {
        this.speed = this.baseSpeed * (1.0f - this.slowFactor);
        if (this.speed < 0) this.speed = 0;
    }

    // Getter cho EnemyManager dùng để vẽ
    public boolean isStunned() { return stunCount > 0; }

    // ==================== Getters ====================
    public float getX() { return x; }
    public float getY() { return y; }
    public float getCenterX() { return x + frameW / 2.0f; }
    public float getCenterY() { return y + frameH / 2.0f; }
    public int getEnemyHealth() { return health; }
    public int getMaxHealth(){return maxHealth;}
    public float getSpeed() { return speed; }
    public int getEnemyType() { return enemyType; }
    public int getAnimationIndex() { return animationIndex; }
    public Rectangle2D getBounds() { return bounds; }
    public int getEnemyDamage() { return damage; }
    public int getLastDir() { return lastDir; }

    public List<StatusEffect> getStatusEffects() { return statusEffects;}
//    public int getFrameW() { return frameW; }
//    public int getFrameH() { return frameH; }
//    public int getEnemyId() { return enemyID; }

    // ==================== Setters ====================
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setStunned(boolean stunned) { this.isStunned = stunned; }
    public void setLastDir(int lastDir) { this.lastDir = lastDir; }
//    public void setEnemyID(int enemyID) { this.enemyID = enemyID; }

    // ==================== Attack Logic ====================
    public boolean canAttack() {
        return true;
    }
}
