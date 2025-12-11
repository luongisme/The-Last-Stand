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

        // ANEMO
        Image[] anemoProjectileSprite = loadSprite(new String[]
                        {"Anemo_Level_01_Projectile.png",
                        "Anemo_Level_02_Projectile.png",
                        "Anemo_Level_03_Projectile.png"},
                "Towers/Types/Anemo/Projectile"
        );
        int[] anemoProjectile_FrameSize = {130,130,200};
        int[] anemoProjectile_FrameCount = {5,5,5};
        projectileFrameCounts[0] = anemoProjectile_FrameCount;
        projectiles[0] = loadLevelAnimations(anemoProjectileSprite, anemoProjectile_FrameCount, anemoProjectile_FrameSize, 0);

        // TOXO
        Image[] toxoProjectileSprite = loadSprite(new String[]
                        {"Toxo_Level_01_Projectile.png",
                        "Toxo_Level_02_Projectile.png",
                        "Toxo_Level_03_Projectile.png"},
                "Towers/Types/Toxo/Projectile"
        );

        int[] toxoProjectile_FrameSize = {32,64,96};
        int[] toxoProjectile_FrameCount = {12,12,12};
        projectileFrameCounts[1] = toxoProjectile_FrameCount;
        projectiles[1] = loadLevelAnimations(toxoProjectileSprite, toxoProjectile_FrameCount, toxoProjectile_FrameSize, 0);

        // ELECTRO
        Image[] electroProjectileSprite = loadSprite(new String[]
                        {"Electro_Level_01_Projectile.png",
                        "Electro_Level_02_Projectile.png",
                        "Electro_Level_03_Projectile.png"},
                "Towers/Types/Electro/Projectile"
        );

        int[] electroProjectile_FrameSize = {32,32,32};
        int[] electroProjectile_FrameCount = {5,5,5};
        projectileFrameCounts[2] = electroProjectile_FrameCount;
        projectiles[2] = loadLevelAnimations(electroProjectileSprite, electroProjectile_FrameCount, electroProjectile_FrameSize, 0);
    }

    private void loadImpacts() {
        impacts = new Image[noTowerTypes][maxLevelOfTower][];
        impactFrameCounts = new int[noTowerTypes][maxLevelOfTower];

        // ANEMO
        Image[] anemoImpactSprite = loadSprite(new String[]
                        {"Anemo_Level_01_Impact.png",
                        "Anemo_Level_02_Impact.png",
                        "Anemo_Level_03_Impact.png"},
                "Towers/Types/Anemo/Impact"
        );

        int[] anemoImpact_FrameSize = {256,320,320};
        int[] anemoImpact_FrameCount = {7,12,17};
        impactFrameCounts[0] = anemoImpact_FrameCount;
        impacts[0] = loadLevelAnimations(anemoImpactSprite, anemoImpact_FrameCount, anemoImpact_FrameSize, 0);

        // TOXO
        Image[] toxoImpactSprite = loadSprite(new String[]
                        {"Toxo_Level_01_Impact.png",
                        "Toxo_Level_02_Impact.png",
                        "Toxo_Level_03_Impact.png"},
                "Towers/Types/Toxo/Impact"
        );

        int[] toxoImpact_FrameSize = {64,64,96};
        int[] toxoImpact_FrameCount = {10,12,12};
        impactFrameCounts[1] = toxoImpact_FrameCount;
        impacts[1] = loadLevelAnimations(toxoImpactSprite, toxoImpact_FrameCount, toxoImpact_FrameSize, 0);


        // ELECTRO
        Image[] electroImpactSprite = loadSprite(new String[]
                        {"Electro_Level_01_Impact.png",
                        "Electro_Level_02_Impact.png",
                        "Electro_Level_03_Impact.png"},
                "Towers/Types/Electro/Impact"
        );

        int[] electroImpact_FrameSize = {64,64,64};
        int[] electroImpact_FrameCount = {5,6,7};
        impactFrameCounts[2] = electroImpact_FrameCount;
        impacts[2] = loadLevelAnimations(electroImpactSprite, electroImpact_FrameCount, electroImpact_FrameSize, 0);
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
