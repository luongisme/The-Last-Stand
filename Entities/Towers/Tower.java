package Entities.Towers;

import Constant.TowerConstant;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Entities.Projectiles.Projectile;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;
import Managers.ProjectileManager;
import java.util.Comparator;

public abstract class Tower {
    // Core
    protected int x, y, id, level;
    protected TowerConstant towerType;
    protected TowerState currentState;

    // Managers
    protected EnemyManager enemyManager;
    protected ProjectileManager projectileManager;

    // COMBAT STATS (Active State)
    protected Enemy currentTarget;
    protected int range, damage, cooldown;
    protected float cooldownTick = 0;
    protected boolean isAttacking = false;

    // ANIMATION LIMITS
    protected int idleFrames, attackFrames, shootFrame;
    protected int currentTotalFrames;

    // ADDITIONAL FEATURES
    protected int effectType;
    protected String effectDescription;
    protected int totalBuildCost;

    // ACTIVE STATE
    private int animationTick = 0;
    private int animationIndex = 0;
    private final int ANIMATION_SPEED = 5;

    // CONSTRUCTING STATE
    private int constructionTick = 0;
    private int constructionIndex = 0;
    private int constructionLoopCount = 0;
    private final int CONSTRUCTION_SPEED = 5;
    private final int CONSTRUCTION_FRAMES = 6; // TowerBuild[0,1,2].length
    private final int TARGET_CONSTRUCTION_LOOPS = 3;

    // COMPLETED STATE
    private int completedTick = 0;
    private int completedIndex = 0;
    private final int COMPLETED_SPEED = 6;
    private final int COMPLETED_FRAMES = 6; // TowerBuild[3].length

    // SELL STATE
    private int sellTick = 0;
    private int sellIndex = 0;
    private final int SELL_SPEED = COMPLETED_SPEED;
    private final int SELL_FRAMES = COMPLETED_FRAMES;
    private boolean isFinishedCollapsing = false;

    public Tower(int x, int y, int id, TowerConstant towerType,
                 EnemyManager enemyManager, ProjectileManager projectileManager) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        this.level = 1; // always start in level 1
        this.totalBuildCost = towerType.getCost(this.level);
        // new tower => always build first
        this.currentState = TowerState.CONSTRUCTING;

        this.enemyManager = enemyManager;
        this.projectileManager = projectileManager;

        updateStatsToLevel();
        this.currentTotalFrames = idleFrames;
    }

    private void updateStatsToLevel() {
        this.damage = towerType.getDamage(this.level);
        this.range = towerType.getRange(this.level);
        this.cooldown = towerType.getCooldown(this.level);

        setupFrameCounts();
        this.animationIndex = 0;
        this.animationTick = 0;
        updateCurrentTotalFrames();
    }

    private void updateCurrentTotalFrames() {
        // Update current limit
        this.currentTotalFrames = isAttacking ? attackFrames : idleFrames;
    }

    public void update() {
        switch (currentState) {
            case CONSTRUCTING:
                updateConstructionState();
                break;
            case COMPLETED:
                updateCompletedState();
                break;
            case ACTIVE:
                updateActiveState();
                break;
            case SELLING:
                updateSellingState();
                break;
        }
    }

    private void updateConstructionState() {
        constructionTick++;
        if (constructionTick >= CONSTRUCTION_SPEED) {
            constructionTick = 0;
            constructionIndex++;

            if (constructionIndex >= CONSTRUCTION_FRAMES) {
                constructionLoopCount++;

                // higher level takes longer to build
                if (constructionLoopCount >= TARGET_CONSTRUCTION_LOOPS + getLevel()) {
                    changeState(TowerState.COMPLETED);
                } else {
                    // next loop
                    constructionIndex = 0;
                }
            }
        }
    }

    private void updateSellingState() {
        sellTick++;
        if (sellTick >= SELL_SPEED) {
            sellTick = 0;
            sellIndex++;

            if (sellIndex >= SELL_FRAMES) {
                isFinishedCollapsing = true; // can delete
            }
        }
    }

    private void updateCompletedState() {
        completedTick++;
        if (completedTick >= COMPLETED_SPEED) {
            completedTick = 0;
            completedIndex++;

            if (completedIndex >= COMPLETED_FRAMES) {
                changeState(TowerState.ACTIVE);
            }
        }
    }

    private void updateActiveState() {
        scanEnemy();

        // 1. Luôn hồi chiêu (dù đang bắn hay đang nghỉ)
        if (cooldownTick < cooldown) {
            cooldownTick++;
        }

        // 3. Logic Kích hoạt Tấn công
        // Nếu chưa tấn công VÀ có mục tiêu VÀ đã hồi chiêu xong -> BẮT ĐẦU TẤN CÔNG
        if (!isAttacking && currentTarget != null && isCoolDownOver()) {
            startAttack();
        }

        // 4. Logic Chạy Animation
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

            // A. Nếu đang tấn công (Attack Animation)
            if (isAttacking) {
                // -- THỜI ĐIỂM BẮN ĐẠN --
                if (animationIndex == shootFrame) {
                    // Kiểm tra lại mục tiêu lần cuối trước khi bắn (tránh bắn vào hư không)
                    if (currentTarget != null && currentTarget.getEnemyHealth() > 0) {
                        shoot();
                        resetCooldown(); // Reset cooldown ngay khi đạn bay ra
                    }
                }

                // -- KẾT THÚC ANIMATION TẤN CÔNG --
                if (animationIndex >= attackFrames) {
                    animationIndex = 0;
                    isAttacking = false; // Quay về trạng thái nghỉ (Idle)
                    updateCurrentTotalFrames(); // Cập nhật lại limit frame về Idle
                }
            }
            // B. Nếu đang nghỉ (Idle Animation)
            else {
                if (animationIndex >= idleFrames) {
                    animationIndex = 0; // Loop idle animation
                }
            }
        }
        if (animationIndex >= currentTotalFrames) {
            animationIndex = 0;
        }
    }

    protected void scanEnemy() {
        // Nếu target cũ chết hoặc chạy thoát -> Bỏ target
        if (currentTarget != null) {
            if (currentTarget.getEnemyHealth() <= 0 || !isInRange(currentTarget)) {
                currentTarget = null;
                // Nếu đang tấn công mà mất target -> Có thể ngừng hoặc để animation chạy hết (tùy chọn)
                // Ở đây ta giữ animation chạy hết cho mượt, nhưng lần sau sẽ không bắn
            }
        }

        float centerX = x + 16;
        float centerY = y + 16;

        // Chỉ tìm target mới nếu đang rảnh tay hoặc target cũ đã mất
        // (Hoặc nếu bạn muốn logic "Luôn đổi sang con máu cao nhất ngay lập tức"):
        Enemy bestCandidate = enemyManager.getEnemiesInRange(centerX, centerY, range).stream()
                .filter(e -> e.getEnemyHealth() > 0)
                .max(Comparator.comparingInt(Enemy::getEnemyHealth))
                .orElse(null);

        if (bestCandidate != null) {
            this.currentTarget = bestCandidate;
        }
    }

    protected boolean isInRange(Enemy e) {
        double dist = Math.hypot(e.getCenterX() - (x + 16), e.getCenterY() - (y + 16));
        return dist <= range;
    }

    private void startAttack() {
        isAttacking = true;
        animationIndex = 0;
        animationTick = 0;
        updateCurrentTotalFrames();
    }

    // helper method to reset the variable every time the state changes
    private void changeState(TowerState newState) {
        this.currentState = newState;
        switch (newState) {
            case CONSTRUCTING:
                constructionIndex = 0;
                constructionTick = 0;
                constructionLoopCount = 0;
            case COMPLETED:
                completedIndex = 0;
                completedTick = 0;
                break;
            case ACTIVE:
                animationIndex = 0;
                animationTick = 0;
                break;
            case SELLING:
                sellIndex = 0;
                sellTick = 0;
                break;
            default:
                break;
        }
    }

    public Projectile createProjectile(Enemy target) {
        int lvlIdx = level - 1;

        int offsetX = towerType.getWeaponOffsetX(lvlIdx);
        int offsetY = towerType.getWeaponOffsetY(lvlIdx);
        int weaponW = towerType.getWeaponWidth(lvlIdx);
        int weaponH = towerType.getWeaponHeight(lvlIdx);

        float startX = (this.x - offsetX) + (weaponW / 2.0f);
        float startY = (this.y - offsetY) + (weaponH / 2.0f) - 5;

        float speed = towerType.getProjectileSpeed();

        int maxFrames = projectileManager.getMaxFrames(towerType.ordinal(), lvlIdx);

        return createSpecificProjectile(startX, startY, speed, maxFrames, lvlIdx, target);
    }

    protected abstract Projectile createSpecificProjectile(float x, float y, float speed, int maxFrames, int lvl, Enemy target);

    protected void shoot() {
        Projectile p = createProjectile(currentTarget);
        if (p != null) {
            projectileManager.addProjectile(p);
        }
    }

    public void startSell() {
        // sell if build completed
        if (currentState == TowerState.CONSTRUCTING) return;
        changeState(TowerState.SELLING);

        this.isAttacking = false;
        this.isFinishedCollapsing = false;
    }

    public void upgradeTower(int upgradeCost) {
        if (level >= towerType.getMaxLevel()) return;
        this.level++;
        this.totalBuildCost += upgradeCost;

        updateStatsToLevel();

        changeState(TowerState.CONSTRUCTING);
    }

    public abstract ImpactStrategy getImpactStrategy();
    protected abstract void setupFrameCounts();
    public abstract ImpactProfile getImpactProfile();

    public boolean isAttacking() { return isAttacking; }

    public boolean isFinishedCollapsing() { return isFinishedCollapsing; }

    public boolean isUnderConstruction() { return this.currentState == TowerState.CONSTRUCTING; }

    public boolean isSelling() { return this.currentState == TowerState.SELLING; }

    public boolean isPlayCompletedEffect() { return this.currentState == TowerState.COMPLETED; }

    public TowerState getCurrentState() { return currentState; }

    public int getConstructionIndex() { return constructionIndex; }

    public int getCompletedIndex() { return completedIndex; }

    public int getSellIndex() { return sellIndex; }

    public int getLevel() { return level; }
    
    public int getTotalBuildCost() { return totalBuildCost; }

    public int getNextUpgradeCost() {
        if (level >= towerType.getMaxLevel()) return 0;
        return towerType.getCost(this.level + 1);
    }

    public int getAnimationIndex() { return animationIndex; }

    public boolean isMaxLevel() { return this.level >= towerType.getMaxLevel(); }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public TowerConstant getTowerType() { return towerType; }

    public int getDamage() { return damage; }

    public int getRange() { return range; }

    public int getCooldown() { return cooldown; }

    public int getEffectType() { return effectType; }

    public String getEffectDescription() { return effectDescription; }

    public void resetCooldown() { cooldownTick = 0; }

    public boolean isCoolDownOver() { return cooldownTick >= cooldown; }
}