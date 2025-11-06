package Constant;

public enum TowerConstant {

    CANNON(50,150,60,60,true,1,"Deals Wide Area Damage"),
    POISON(40,200,40,40,true,2,"Deals Poison Damage that Slows and Persists Over Time"),
    FROST(50,180,50,50,true,3,"Deals Freeze Damage that Immobilizes Enemies");

    private final int damage, range, cooldown, cost, effectType;
    private final boolean hasEffect;
    private final String effectDescription;

    TowerConstant(int damage, int range, int cooldown, int cost, boolean hasEffect, int effecType, String description) {
        this.damage = damage;
        this.range = range;
        this.cooldown = cooldown;
        this.cost = cost;
        this.hasEffect = hasEffect;
        this.effectType = effecType;
        this.effectDescription = description;
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

    public int getCost() {
        return cost;
    }

    public boolean hasEffect() {
        return this.hasEffect;
    }

    public int getEffectType() {
        return effectType;
    }

    public String getEffectDescription() {
        return effectDescription;
    }
}