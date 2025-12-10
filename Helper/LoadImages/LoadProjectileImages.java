package Helper.LoadImages;

import javafx.scene.image.Image;

public class LoadProjectileImages extends SpriteLoaderBase {
    // [TowerTypes[Levels][Frames]
    private Image[][][] projectiles, impacts;
    private int[][] projectileFrameCounts;
    private int[][] impactFrameCounts;

    public LoadProjectileImages() {
        loadProjectiles();
        loadImpacts();
    }

    private void loadProjectiles() {
        projectiles = new Image[noTowerTypes][maxLevelOfTower][];
        projectileFrameCounts = new int[noTowerTypes][maxLevelOfTower];

        // CANNON
        Image[] cannonProjectileSprite = loadSprite(new String[]
                        {"Cannon_Level_01_Projectile.png",
                        "Cannon_Level_02_Projectile.png",
                        "Cannon_Level_03_Projectile.png"},
                "Towers/Types/Cannon/Projectile"
        );
        int[] cannonProjectile_FrameSize = {130,130,200};
        int[] cannonProjectile_FrameCount = {5,5,5};
        projectileFrameCounts[0] = cannonProjectile_FrameCount;
        projectiles[0] = loadLevelAnimations(cannonProjectileSprite, cannonProjectile_FrameCount, cannonProjectile_FrameSize, 0);

        // POISON
        Image[] poisonProjectileSprite = loadSprite(new String[]
                        {"Poison_Level_01_Projectile.png",
                        "Poison_Level_02_Projectile.png",
                        "Poison_Level_03_Projectile.png"},
                "Towers/Types/Poison/Projectile"
        );

        int[] poisonProjectile_FrameSize = {32,64,96};
        int[] poisonProjectile_FrameCount = {12,12,12};
        projectileFrameCounts[1] = poisonProjectile_FrameCount;
        projectiles[1] = loadLevelAnimations(poisonProjectileSprite, poisonProjectile_FrameCount, poisonProjectile_FrameSize, 0);

        // FROST
        Image[] frostProjectileSprite = loadSprite(new String[]
                        {"Frost_Level_01_Projectile.png",
                        "Frost_Level_02_Projectile.png",
                        "Frost_Level_03_Projectile.png"},
                "Towers/Types/Frost/Projectile"
        );

        int[] frostProjectile_FrameSize = {32,32,32};
        int[] frostProjectile_FrameCount = {5,5,5};
        projectileFrameCounts[2] = frostProjectile_FrameCount;
        projectiles[2] = loadLevelAnimations(frostProjectileSprite, frostProjectile_FrameCount, frostProjectile_FrameSize, 0);
    }

    private void loadImpacts() {
        impacts = new Image[noTowerTypes][maxLevelOfTower][];
        impactFrameCounts = new int[noTowerTypes][maxLevelOfTower];

        // CANNON
        Image[] cannonImpactSprite = loadSprite(new String[]
                        {"Cannon_Level_01_Impact.png",
                        "Cannon_Level_02_Impact.png",
                        "Cannon_Level_03_Impact.png"},
                "Towers/Types/Cannon/Impact"
        );

        int[] cannonImpact_FrameSize = {256,320,320};
        int[] cannonImpact_FrameCount = {7,12,17};
        impactFrameCounts[0] = cannonImpact_FrameCount;
        impacts[0] = loadLevelAnimations(cannonImpactSprite, cannonImpact_FrameCount, cannonImpact_FrameSize, 0);

        // POISON
        Image[] poisonImpactSprite = loadSprite(new String[]
                        {"Poison_Level_01_Impact.png",
                        "Poison_Level_02_Impact.png",
                        "Poison_Level_03_Impact.png"},
                "Towers/Types/Poison/Impact"
        );

        int[] poisonImpact_FrameSize = {64,64,96};
        int[] poisonImpact_FrameCount = {10,12,12};
        impactFrameCounts[1] = poisonImpact_FrameCount;
        impacts[1] = loadLevelAnimations(poisonImpactSprite, poisonImpact_FrameCount, poisonImpact_FrameSize, 0);


        // FROST
        Image[] frostImpactSprite = loadSprite(new String[]
                        {"Frost_Level_01_Impact.png",
                        "Frost_Level_02_Impact.png",
                        "Frost_Level_03_Impact.png"},
                "Towers/Types/Frost/Impact"
        );

        int[] frostImpact_FrameSize = {64,64,64};
        int[] frostImpact_FrameCount = {5,6,7};
        impactFrameCounts[2] = frostImpact_FrameCount;
        impacts[2] = loadLevelAnimations(frostImpactSprite, frostImpact_FrameCount, frostImpact_FrameSize, 0);
    }

    public Image getProjectile(int type, int level, int animIndex) {
        return getSafe(projectiles[type], level, animIndex);
    }

    public Image getImpact(int type, int level, int animIndex) {
        return getSafe(impacts[type], level, animIndex);
    }

    public int getImpactFrameCount(int type, int level) {
        if (type >= 0 && type < impactFrameCounts.length && level >= 0 && level < impactFrameCounts[type].length) {
            return impactFrameCounts[type][level];
        }
        return 1; // Default fallback
    }

    public int getProjectileFrameCount(int type, int level) {
        if (type >= 0 && type < projectileFrameCounts.length && level >= 0 && level < projectileFrameCounts[type].length) {
            return projectileFrameCounts[type][level];
        }
        return 1; // Default fallback
    }
}
