package Constant;

public enum TowerConstant {

    CANNON(
        new int[]{50, 75, 110},
        new int[]{150, 160, 170},
        new int[]{60, 55, 50},
        new int[]{60, 80, 120},
        true,
        1,
        "Deals Wide Area Damage",
        // Weapon Offsets (X Offset, Y Offset from the center of the tile)
        new int[][]{ {8, 44}, {9, 55}, {17, 70} },
        // Weapon Sizes (Width, Height)
        new int[][]{ {32, 30}, {34, 32}, {50, 44} }
    ),
    POISON(
        new int[]{40, 50, 60},
        new int[]{200, 210, 220},
        new int[]{40, 38, 35},
        new int[]{40, 60, 80},
        true,
        2,
        "Deals Poison Damage that Slows and Persists Over Time",
        // Weapon Offsets (X Offset, Y Offset from the center of the tile)
        new int[][]{ {29, 75}, {30, 79}, {31, 84} },
        // Weapon Sizes (Width, Height)
        new int[][]{ {74, 74}, {76, 76}, {78, 78} }
    ),
    FROST(
        new int[]{50, 65, 80},
        new int[]{180, 190, 200},
        new int[]{50, 48, 45},
        new int[]{50, 70, 100},
        true,
        3,
        "Deals Freeze Damage that Immobilizes Enemies",
        // Weapon Offsets (X Offset, Y Offset from the center of the tile)
        new int[][]{ {8, 56}, {15, 67}, {16, 76} },
        // Weapon Sizes (Width, Height)
        new int[][]{ {32, 48}, {46, 54}, {48, 52} }
    );

    private final int[] damages, ranges, cooldowns, costs;
    private final int effectType;
    private final boolean hasEffect;
    private final String effectDescription;

    private final int[][] weaponOffsets; // [Level][x, y]
    private final int[][] weaponSizes; // [Level][w, h]

    TowerConstant(int[] damages, int[] ranges, int[] cooldowns, int[] costs, boolean hasEffect, int effecType, String description, int[][] weaponOffsets, int[][] weaponSizes) {
        this.damages = damages;
        this.ranges = ranges;
        this.cooldowns = cooldowns;
        this.costs = costs;
        this.hasEffect = hasEffect;
        this.effectType = effecType;
        this.effectDescription = description;
        this.weaponOffsets = weaponOffsets;
        this.weaponSizes = weaponSizes;
    }

    public int getDamage(int level) {
        if (level < 1 || level > damages.length) return 0;
        return damages[level-1];
    }

    public int getRange(int level) {
        if (level < 1 || level > ranges.length) return 0;
        return ranges[level-1];
    }

    public int getCooldown(int level) {
        if (level < 1 || level > cooldowns.length) return 0;
        return cooldowns[level-1];
    }

    public int getCost(int level) {
        if (level < 1 || level > costs.length) return 0;
        return costs[level-1];
    }

    public int getMaxLevel() {
        return costs.length;
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

    public int getWeaponOffsetX(int level) {
        if (level < 0 || level >= weaponOffsets.length) return 0;
        return weaponOffsets[level][0];
    }

    public int getWeaponOffsetY(int level) {
        if (level < 0 || level >= weaponOffsets.length) return 0;
        return weaponOffsets[level][1];
    }

    public int getWeaponWidth(int level) {
        if (level < 0 || level >= weaponSizes.length) return 0;
        return weaponSizes[level][0];
    }

    public int getWeaponHeight(int level) {
        if (level < 0 || level >= weaponSizes.length) return 0;
        return weaponSizes[level][1];
    }
}