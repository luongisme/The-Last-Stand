package Entities.Projectiles;

import Entities.AnimationEffects.ImpactEffect;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Managers.EnemyManager;
import Logic.Strategies.ImpactStrategy;

public class Projectile {
    protected float x, y;
    protected float speed;
    protected int damage;
    protected Enemy target;
    protected boolean active = true;

    protected int projectileType;
    protected int towerLevel;
    protected float rotation;

    protected int animIndex = 0;
    protected int animTick = 0;
    protected int animSpeed = 6; // Tốc độ chuyển frame (càng nhỏ càng nhanh)
    protected int maxFrames; // Số frame tối đa của loại đạn này

    protected int impactAnimSpeed;

    // Strategy tiêm vào khi tạo đạn
    private ImpactStrategy impactStrategy;
    private EnemyManager enemyManager;
    protected ImpactProfile impactProfile;

    public Projectile(float x, float y, float speed, int damage,
                      int projectileType, int towerLevel, int maxFrames, Enemy target,
                      ImpactStrategy strategy, EnemyManager em, ImpactProfile impactProfile) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.projectileType = projectileType;
        this.towerLevel = towerLevel;
        this.target = target;
        this.maxFrames = maxFrames;
        this.impactStrategy = strategy;
        this.enemyManager = em;
        this.impactProfile = impactProfile;
        this.impactAnimSpeed = impactProfile.getAnimSpeed();

        calculateRotation();
    }

    public void update() {
        if (!active) return;

        updateAnimation();

        // Kiểm tra target chết chưa
        if (target == null || target.getEnemyHealth() <= 0) {
            active = false;
            return;
        }

        // 1. Homing Logic (Đạn đuổi chuẩn xác vào tâm)
        float targetX = target.getCenterX();
        float targetY = target.getCenterY();

        float dx = targetX - x;
        float dy = targetY - y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);

        calculateRotation();

        // 2. Check Va chạm (Hitbox)
        // Nếu khoảng cách < tốc độ đạn nghĩa là trong frame này đạn sẽ bay xuyên qua tâm
        if (dist <= speed) {

            this.x = targetX;
            this.y = targetY;
            // Đã trúng đích
            impactStrategy.onHit(target, x, y, damage, enemyManager);
            active = false;
        } else {
            // Di chuyển đạn
            // Normalize vector rồi nhân với speed
            float moveX = (dx / dist) * speed;
            float moveY = (dy / dist) * speed;
            x += moveX;
            y += moveY;

        }
    }

    protected void updateAnimation() {
        if (maxFrames <= 1) return; // Ảnh tĩnh thì không cần tính

        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= maxFrames) {
                animIndex = 0;
            }
        }
    }

    private void calculateRotation() {
        if (target != null) {
            float dx = target.getCenterX() - x;
            float dy = target.getCenterY() - y;
            // Math.atan2 trả về radian, cần đổi sang degree
            // +90 hoặc 0 tùy thuộc vào sprite gốc của bạn hướng lên hay hướng ngang
            this.rotation = (float) Math.toDegrees(Math.atan2(dy, dx));
        }
    }

    public ImpactEffect createImpactEffect() {
        return null; // Mặc định trả về null (như bạn muốn tắt effect)
    }

    public int getImpactAnimSpeed() { return impactAnimSpeed; }

    // Mặc định trả về -1 (Vẽ size gốc)
    public float getDrawSize() { return -1; }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getSpeed() { return speed; }
    public int getDamage() { return damage; }
    public Enemy getTarget() { return target; }
    public EnemyManager getEnemyManager() { return enemyManager; }
    public ImpactStrategy getImpactStrategy() { return impactStrategy; }
    public ImpactProfile getImpactProfile() { return impactProfile; }
    public int getProjectileType() { return projectileType; }
    public int getTowerLevel() { return towerLevel; }
    public int getAnimationIndex() { return animIndex; }
    public float getRotation() { return rotation; }
    public boolean isActive() { return active; }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setTarget(Enemy target) {
        this.target = target;
    }

}