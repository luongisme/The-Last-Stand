package Constant;

import Entities.Towers.*;
import Managers.EnemyManager;
import Managers.ProjectileManager;

public enum TowerConstant {

    ANEMO(
        new int[]{50, 75, 100},
        new int[]{180, 190, 200},
        new int[]{60, 55, 50},
        new int[]{60, 80, 120},
        4.0f,
        // Weapon Offsets (X Offset, Y Offset from the center of the tile)
        new int[][]{ {8, 44}, {9, 55}, {17, 70} },
        // Weapon Sizes (Width, Height)
        new int[][]{ {32, 30}, {34, 32}, {50, 44} },
        Anemo::new
    ),
    TOXO(
        new int[]{30, 40, 50},
        new int[]{130, 140, 150},
        new int[]{100,95,90},
        new int[]{40, 60, 80},
        5.5f,
        // Weapon Offsets (X Offset, Y Offset from the center of the tile)
        new int[][]{ {29, 75}, {30, 79}, {31, 84} },
        // Weapon Sizes (Width, Height)
        new int[][]{ {74, 74}, {76, 76}, {78, 78} },
        Toxo::new
    ),
    ELECTRO(
        new int[]{50, 60, 70},
        new int[]{200, 210, 220},
        new int[]{80,75,70},
        new int[]{50, 70, 100},
        7.0f,
        // Weapon Offsets (X Offset, Y Offset from the center of the tile)
        new int[][]{ {8, 56}, {15, 67}, {16, 76} },
        // Weapon Sizes (Width, Height)
        new int[][]{ {32, 48}, {46, 54}, {48, 52} },
        Electro::new
    );

    private final int[] damages, ranges, cooldowns, costs;
    private final int[][] weaponOffsets; // [Level][x, y]
    private final int[][] weaponSizes; // [Level][w, h]

    private final float projectileSpeed;

    public final static int NO_TOWER_TYPES = values().length;
    public final static int MAX_LEVEL = calculateMaxLevel();

    private final TowerFactory factory;

    private static int calculateMaxLevel() {
        int max = 0;
        for (TowerConstant tower : values()) {
            // check how many elements the costs (or damages) array has, that is the number of levels
            int currentLevelCount = tower.costs.length;
            if (currentLevelCount > max) {
                max = currentLevelCount;
            }
        }
        return max;
    }

    TowerConstant(int[] damages, int[] ranges, int[] cooldowns, int[] costs, float projectileSpeed,
                  int[][] weaponOffsets, int[][] weaponSizes, TowerFactory factory) {
        this.damages = damages;
        this.ranges = ranges;
        this.cooldowns = cooldowns;
        this.costs = costs;
        this.weaponOffsets = weaponOffsets;
        this.weaponSizes = weaponSizes;
        this.projectileSpeed = projectileSpeed;
        this.factory = factory;
    }

    @FunctionalInterface
    public interface TowerFactory {
        // Factory phải nhận thêm 2 Manager để truyền cho Tower
        Tower create(int x, int y, int id, EnemyManager em, ProjectileManager pm);
    }

    public Tower createTower(int x, int y, int id, EnemyManager em, ProjectileManager pm) {
        return this.factory.create(x, y, id, em, pm);
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

    public int getMaxLevel() { return this.costs.length; }

    public float getProjectileSpeed() { return this.projectileSpeed; }

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