package Entities.Tower;

import Constant.TowerConstant;

public class Tower {

    private int x, y, id, cooldownTick, damage, effectType, range, cooldown;
    private String effectDescription;
    private TowerConstant towerType;
    private int level;
    private int totalBuildCost;

    public Tower(int x, int y, int id, TowerConstant towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        this.level = 1; // always start in level 1
        this.totalBuildCost = towerType.getCost(this.level);
        
        updateStatsToLevel();
    }

    private void updateStatsToLevel() {
        this.damage = towerType.getDamage(this.level);
        this.range = towerType.getRange(this.level);
        this.cooldown = towerType.getCooldown(this.level);
        
        this.effectType = towerType.getEffectType();
        this.effectDescription = towerType.getEffectDescription();
    }

    public void upgradeTower(int upgradeCost) {
        if (level >= towerType.getMaxLevel()) return;
        this.level++;
        this.totalBuildCost += upgradeCost;
        updateStatsToLevel();
    }

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

    public void update(){
        cooldownTick++;
    }
}