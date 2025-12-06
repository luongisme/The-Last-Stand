package Entities.Tower;

import Constant.TowerConstant;

public class Tower {
    private int x, y, id, cooldownTick, damage, effectType, range, cooldown;
    private String effectDescription;
    private final TowerConstant towerType;
    private int level;
    private int totalBuildCost;

    // active
    private int animationTick = 0;
    private int animationIndex = 0;
    private final int animationSpeed = 5;
    private int currentTotalFrames;
    private int idleFrames;
    private int attackFrames;
    private boolean isAttacking = false;

    // construction
    private boolean isUnderConstruction = true; // new tower => always build first
    private int constructionTick = 0;
    private int constructionIndex = 0;
    private final int constructionSpeed = 5;
    private final int CONSTRUCTION_FRAMES = 6; // TowerBuild[0,1,2].length
    private int constructionLoopCount = 0;
    private final int TARGET_CONSTRUCTION_LOOPS = 3;

    // build completed effect
    private boolean isPlayCompletedEffect = false;
    private int completedTick = 0;
    private int completedIndex = 0;
    private final int completedSpeed = 6;
    private final int COMPLETED_FRAMES = 6; // TowerBuild[3].length

    // collapse
    private boolean isSelling = false;
    private boolean finishedCollapsing = false;

    public Tower(int x, int y, int id, TowerConstant towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        this.level = 1; // always start in level 1
        this.totalBuildCost = towerType.getCost(this.level);
        
        updateStatsToLevel();

        this.currentTotalFrames = idleFrames;
        this.isUnderConstruction = true;
    }

    private void updateStatsToLevel() {
        this.damage = towerType.getDamage(this.level);
        this.range = towerType.getRange(this.level);
        this.cooldown = towerType.getCooldown(this.level);
        
        this.effectType = towerType.getEffectType();
        this.effectDescription = towerType.getEffectDescription();

        updateFrameCounts();
    }

    private void updateFrameCounts() {
        switch (towerType) {
            case CANNON:
                this.idleFrames = 3;
                this.attackFrames = 10;
                break;
            case POISON:
                this.idleFrames = 8;
                this.attackFrames = 29;
                break;
            case FROST:
                // Frost has a number of frames that change according to level
                // Level 1, 2, 3 -> Index 0, 1, 2
                int[] frostIdle = {10, 16, 20};
                int[] frostAttack = {16, 17, 19};

                int idx = level - 1;
                if (idx < 0) idx = 0;
                if (idx > 2) idx = 2;

                this.idleFrames = frostIdle[idx];
                this.attackFrames = frostAttack[idx];
                break;
        }
        // Update current limit
        this.currentTotalFrames = isAttacking ? attackFrames : idleFrames;
    }

    public void upgradeTower(int upgradeCost) {
        if (level >= towerType.getMaxLevel()) return;
        this.level++;
        this.totalBuildCost += upgradeCost;

        updateStatsToLevel();

        this.animationIndex = 0;
        this.animationTick = 0;

        this.isUnderConstruction = true;
        this.constructionIndex = 0;
        this.constructionTick = 0;

        this.constructionLoopCount = 0;

        this.isPlayCompletedEffect = false;
        this.completedIndex = 0;
        this.completedTick = 0;
    }

    public void update(){
        if (isSelling) {
            completedTick++;
            if (completedTick >= completedSpeed) {
                completedTick = 0;
                completedIndex++;

                if (completedIndex >= COMPLETED_FRAMES) {
                    isSelling = false;
                    finishedCollapsing = true; // can delte
                }
            }
            return;
        }

        if (isUnderConstruction) {
            constructionTick++;
            if (constructionTick >= constructionSpeed) {
                constructionTick = 0;
                constructionIndex++;

                if (constructionIndex >= CONSTRUCTION_FRAMES) {
                    constructionLoopCount++;

                    if (constructionLoopCount >= TARGET_CONSTRUCTION_LOOPS + getLevel()) { // higher level takes longer to build
                        // end build
                        isUnderConstruction = false;
                        constructionIndex = 0;

                        // turn to completed effect
                        isPlayCompletedEffect = true;
                    } else {
                        // next loop
                        constructionIndex = 0;
                    }
                }
            }
            return; // is building - do nothing
        }

        if (isPlayCompletedEffect) {
            completedTick++;
            if (completedTick >= completedSpeed) {
                completedTick = 0;
                completedIndex++;

                if (completedIndex >= COMPLETED_FRAMES) {
                    isPlayCompletedEffect = false; // build completed
                    completedIndex = 0;
                }
            }
        }

        cooldownTick++;
        animationTick++;

        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            // Loop animation
            if (animationIndex >= currentTotalFrames) {
                animationIndex = 0;
            }
        }
    }

    public void startSell() {
        // sell if build completed
        if (isUnderConstruction) return;

        // active selling status
        this.isSelling = true;

        // reset counter (reuse smoke effect variable)
        this.completedIndex = 0;
        this.completedTick = 0;

        this.isPlayCompletedEffect = false;
        this.isAttacking = false;

        this.finishedCollapsing = false;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        if (this.isAttacking != attacking) {
            this.isAttacking = attacking;

            // Toggle frame limit
            this.currentTotalFrames = attacking ? attackFrames : idleFrames;

            // Reset index to 0 to start new animation
            this.animationIndex = 0;
            this.animationTick = 0;
        }
    }

    public boolean isSelling() { return isSelling; }

    public boolean isFinishedCollapsing() { return finishedCollapsing; }

    public boolean isUnderConstruction() { return isUnderConstruction; }

    public int getConstructionIndex() { return constructionIndex; }

    public boolean isPlayCompletedEffect() { return isPlayCompletedEffect; }

    public int getCompletedIndex() { return completedIndex; }

    public int getLevel() {
        return level;
    }
    
    public int getTotalBuildCost() {
        return totalBuildCost;
    }

    public int getNextUpgradeCost() {
        if (level >= towerType.getMaxLevel()) return 0;
        return towerType.getCost(this.level + 1);
    }

    public int getAnimationIndex() { return animationIndex; }

    public boolean isMaxLevel() {
        return level >= towerType.getMaxLevel();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TowerConstant getTowerType() {
        return towerType;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }  

    public int getCooldown() {
        return cooldown;
    }

    public int getEffectType() {
        return effectType;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    public void resetCooldown() {
        cooldownTick = 0;
    }

    public boolean isCoolDownOver() {
        return cooldownTick >= cooldown;
    }
}