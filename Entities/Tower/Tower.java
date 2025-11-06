package Entities.Tower;

import Constant.TowerConstant;

public class Tower {

    private int x, y, id, cooldownTick, damage, effectType, range, cooldown;
    private String effectDescription;
    private TowerConstant towerType;

    public Tower(int x, int y, int id, TowerConstant towerType) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.towerType = towerType;
        setDefault();
    }

    private void setDefault() {
        this.damage = towerType.getDamage();
        this.effectType = towerType.getEffectType();
        this.range = towerType.getRange();
        this.cooldown = towerType.getCooldown();
        this.effectDescription = towerType.getEffectDescription();
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

    public float getRange() {
        return range;
    }  

    public float getCooldown() {
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