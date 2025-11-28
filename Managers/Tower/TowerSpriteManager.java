package Managers.Tower;

import javafx.scene.image.Image;
import Helper.AssetLoader;

public class TowerSpriteManager {
    private Image[] towerIconImages, // [Cannon,Poison,Frost]
                    menuIconImages; // [Sell,Upgrade]

    private Image[]
            baseSprite, // [Cannon,Poison,Frost]
            buildSprite, // [Construction, Collapse]
            // iconsSprite, menuSprite, if using animation

            // [Level 1 -> Level 3]
            cannonWeaponSprite, cannonProjectileSprite, cannonImpactSprite,
            poisonWeaponSprite, poisonProjectileSprite, poisonImpactSprite,
            frostWeaponSprite, frostProjectileSprite, frostImpactSprite;

    // [TowerType][LevelIndex/FrameIndex] - animations
    private Image[][] towerBases, towerBuild, // towerIcons, towerMenuIcons, if using animation
            cannonWeaponDefault, cannonWeaponAttack, cannonProjectile, cannonImpact,
            poisonWeaponDefault, poisonWeaponAttack, poisonProjectile, poisonImpact,
            frostWeaponDefault, frostWeaponAttack, frostProjectile, frostImpact;

    public TowerSpriteManager() {
        loadAll();
    }

    private void loadAll() {
        loadBase();
        loadWeapons();
        loadProjectiles();
        loadImpacts();
        loadIcons();
        loadMenuIcons();
        loadConsColl();
    }

    // Load tower sprite and base levels for each type
    private void loadBase() {
        baseSprite = loadSprite(new String[]
                {"Cannon_Bases.png","Poison_Bases.png", "Frost_Bases.png"},
        "Towers/Types"
        );
        towerBases = loadBaseImages(baseSprite, 64, 128, 3);
    }

    // Load tower sprites and animations: weapon [default, attack], projectile, impact, icon
    private void loadWeapons() {
        // CANNON
        cannonWeaponSprite = loadSprite(new String[]
                {"Cannon_Level_01_Weapon.png","Cannon_Level_02_Weapon.png","Cannon_Level_03_Weapon.png"},
        "Towers/Types/Cannon/Weapon"
        );

        int[] cannonWeapon_FrameSize = {48,48,64};
        int[] cannonWeaponDefault_FrameCount = {3,3,3};
        cannonWeaponDefault = loadLevelAnimations(cannonWeaponSprite, cannonWeaponDefault_FrameCount, cannonWeapon_FrameSize,false);

        int[] cannonWeaponAttack_FrameCount = {10,10,10};
        cannonWeaponAttack = loadLevelAnimations(cannonWeaponSprite, cannonWeaponAttack_FrameCount, cannonWeapon_FrameSize,false);

        // POISON
        poisonWeaponSprite = loadSprite(new String[]
                {"Poison_Level_01_Weapon.png","Poison_Level_02_Weapon.png","Poison_Level_03_Weapon.png"},
        "Towers/Types/Poison/Weapon"
        );

        int[] poisonWeapon_FrameSize = {96,96,96};
        int[] poisonWeaponDefault_FrameCount = {8,8,8};
        poisonWeaponDefault = loadLevelAnimations(poisonWeaponSprite, poisonWeaponDefault_FrameCount, poisonWeapon_FrameSize,false);

        int[] poisonWeaponAttack_FrameCount = {29,29,29};
        poisonWeaponAttack = loadLevelAnimations(poisonWeaponSprite, poisonWeaponAttack_FrameCount, poisonWeapon_FrameSize,true);

        // FROST
        frostWeaponSprite = loadSprite(new String[]
                {"Frost_Level_01_Weapon.png","Frost_Level_02_Weapon.png","Frost_Level_03_Weapon.png"},
        "Towers/Types/Frost/Weapon"
        );

        int[] frostWeapon_FrameSize = {48,64,64};
        int[] frostWeaponDefault_FrameCount = {10,16,20};
        frostWeaponDefault = loadLevelAnimations(frostWeaponSprite, frostWeaponDefault_FrameCount, frostWeapon_FrameSize,false);

        int[] frostWeaponAttack_FrameCount = {16,17,19};
        frostWeaponAttack = loadLevelAnimations(frostWeaponSprite, frostWeaponAttack_FrameCount, frostWeapon_FrameSize,true);
    }

    private void loadProjectiles() {
        // CANNON
        cannonProjectileSprite = loadSprite(new String[]
                {"Cannon_Level_01_Projectile.png","Cannon_Level_02_Projectile.png","Cannon_Level_03_Projectile.png"},
        "Towers/Types/Cannon/Projectile"
        );
        int[] cannonProjectile_FrameSize = {130,130,200};
        int[] cannonProjectile_FrameCount = {5,5,5};
        cannonProjectile = loadLevelAnimations(cannonProjectileSprite, cannonProjectile_FrameCount, cannonProjectile_FrameSize, false);

        // POISON
        poisonProjectileSprite = loadSprite(new String[]
                {"Poison_Level_01_Projectile.png","Poison_Level_02_Projectile.png","Poison_Level_03_Projectile.png"},
        "Towers/Types/Poison/Projectile"
        );

        int[] poisonProjectile_FrameSize = {32,32,32};
        int[] poisonProjectile_FrameCount = {5,5,5};
        poisonProjectile = loadLevelAnimations(poisonProjectileSprite, poisonProjectile_FrameCount, poisonProjectile_FrameSize, false);

        // FROST
        frostProjectileSprite = loadSprite(new String[]
                {"Frost_Level_01_Projectile.png","Frost_Level_02_Projectile.png","Frost_Level_03_Projectile.png"},
        "Towers/Types/Frost/Projectile"
        );

        int[] frostProjectile_FrameSize = {32,64,96};
        int[] frostProjectile_FrameCount = {12,12,12};
        frostProjectile = loadLevelAnimations(frostProjectileSprite, frostProjectile_FrameCount, frostProjectile_FrameSize, false);
    }

    private void loadImpacts() {
        // CANNON
        cannonImpactSprite = loadSprite(new String[]
                {"Cannon_Level_01_Impact.png","Cannon_Level_02_Impact.png","Cannon_Level_03_Impact.png"},
        "Towers/Types/Cannon/Impact"
        );

        int[] cannonImpact_FrameSize = {256,320,320};
        int[] cannonImpact_FrameCount = {7,12,17};
        cannonImpact = loadLevelAnimations(cannonImpactSprite, cannonImpact_FrameCount, cannonImpact_FrameSize, false);

        // POISON
        poisonImpactSprite = loadSprite(new String[]
                {"Poison_Level_01_Impact.png","Poison_Level_02_Impact.png","Poison_Level_03_Impact.png"},
        "Towers/Types/Poison/Impact"
        );

        int[] poisonImpact_FrameSize = {64,64,96};
        int[] poisonImpact_FrameCount = {10,12,12};
        poisonImpact = loadLevelAnimations(poisonImpactSprite, poisonImpact_FrameCount, poisonImpact_FrameSize, false);


        // FROST
        frostImpactSprite = loadSprite(new String[]
                {"Frost_Level_01_Impact.png","Frost_Level_02_Impact.png","Frost_Level_03_Impact.png"},
        "Towers/Types/Frost/Impact"
        );

        int[] frostImpact_FrameSize = {64,64,64};
        int[] frostImpact_FrameCount = {5,6,7};
        frostImpact = loadLevelAnimations(frostImpactSprite, frostImpact_FrameCount, frostImpact_FrameSize, false);
    }

    private void loadIcons() {
        // load images
        towerIconImages = loadSprite(new String[]
                {"wind.png","darkness.png","thunder.png"},
        "Towers/Icons/Crystals"
        );

        // Load tower icon animations
//        towerIcons = new Image[3][];
        // CANNON
//        int cannon_FrameCount = ?;
//        int cannon_FrameWidth = ?;
//        int cannon_FrameHeight = ?;
//        towerIcons[0] = loadAnimationStrip(iconsSprite,cannon_FrameCount,cannon_FrameWidth,cannon_FrameHeight);
        // POISON
//        int poison_FrameCount = ?;
//        int poison_FrameWidth = ?;
//        int poison_FrameHeight = ?;
//        towerIcons[1] = loadAnimationStrip(iconsSprite,poison_FrameCount,poison_FrameWidth,poison_FrameHeight);
        // FROST
//        int frost_FrameCount = ?;
//        int frost_FrameWidth = ?;
//        int frost_FrameHeight = ?;
//        towerIcons[2] = loadAnimationStrip(iconsSprite,frost_FrameCount,frost_FrameWidth,frost_FrameHeight);
    }

    private void loadMenuIcons() {
        menuIconImages = loadSprite(new String[]
                {"Sell.png", "Upgrade.png"},
        "Towers/Icons"
        );

        // load menu icon animations
//        towerMenuIcons = new Image[2][];

        // SELL
//        int sell_FrameCount = ?;
//        int sell_FrameWidth = ?;
//        int sell_FrameHeight = ?;
//        towerMenuIcons[0] = loadAnimationStrip(menuSprite[0],sell_FrameCount,sell_FrameWidth,sell_FrameHeight);

        // UPGRADE
//        int upgrade_FrameCount = ?;
//        int upgrade_FrameWidth = ?;
//        int upgrade_FrameHeight = ?;
//        towerMenuIcons[1] = loadAnimationStrip(menuSprite[1],upgrade_FrameCount,upgrade_FrameWidth,upgrade_FrameHeight);
    }

    // building and destroy effect
    private void loadConsColl() { // load Construction and Collapse
        buildSprite = loadSprite(new String[]
                {"Construction.png","Collapse.png"},
        "Towers/Construction_Collapse"
        );

        towerBuild = new Image[5][];

        // CONSTRUCTION
        // Level 1
        towerBuild[0] = loadAnimationStrip(buildSprite[0], 6, 192, 256);

        // Level 2
        towerBuild[1] = loadAnimationStrip(buildSprite[0], 6, 192, 256, 2*256);

        // Level 3
        towerBuild[2] = loadAnimationStrip(buildSprite[0], 6, 192, 256, 4*256);

        // Completed Effect
        towerBuild[3] = loadAnimationStrip(buildSprite[0], 6, 192, 256, 5*256);

        // COLLAPSE
//        towerBuild[4] = loadAnimationStrip(buildSprite[1], 13, 256, 192);

        // using completed effect for collapsing
        towerBuild[4] = loadAnimationStrip(buildSprite[0], 6, 192, 256, 5*256);
    }

    // helper method for loading sprites/animations
    private Image[] loadSprite(String[] fileNames, String subPath) {
        if (fileNames == null || fileNames.length == 0) {
            System.err.println("ERROR (loadSprite): FileNames array is null/empty!");
            return new Image[0];
        }

        Image[] sprites = new Image[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            sprites[i] = AssetLoader.loadImage(fileNames[i], subPath);

            if (sprites[i] == null) {
                System.err.println("ERROR (loadSprite): Failed to load '" + fileNames[i] + "'. Now it is NULL!");
            }
        }
        return sprites;
    }

    private Image[][] loadBaseImages(Image[] spriteSheet, int baseWidth, int baseHeight, int maxLevel) {
        if (baseWidth <= 0 || baseHeight <= 0 || maxLevel <= 0) {
            System.err.println("ERROR (loadBases): Invalid parameters (width, height, level)!");
            return new Image[0][];
        }

        if (spriteSheet == null || spriteSheet.length == 0) {
            System.err.println("ERROR (loadBases): Source spriteSheet array is null/empty!");
            return new Image[0][];
        }

        Image[][] bases = new Image[spriteSheet.length][];
        int y = 0;
        for (int type = 0; type < spriteSheet.length; type++) {
            bases[type] = new Image[maxLevel];

            if (spriteSheet[type] == null) {
                System.err.println("ERROR (loadBases): spriteSheet[" + type + "] is null. Creating dummy...");
                bases[type] = createDummy(maxLevel, baseWidth, baseHeight);
                continue;
            }

            for (int level = 0; level < maxLevel; level++) {
                int x = level * baseWidth;
                bases[type][level] = AssetLoader.getSubImage(spriteSheet[type], x, y, baseWidth, baseHeight);
            }
        }
        return bases;
    }

    private Image[][] loadLevelAnimations(Image[] levelSprites, int[] frameCounts, int[] frameSizes, Boolean isSecRow) {
        if (levelSprites == null || levelSprites.length == 0) {
            System.err.println("ERROR (loadLevel): Source image is null/empty! Cannot crop image.");
            return new Image[0][];
        }

        if (levelSprites.length != frameCounts.length || levelSprites.length != frameSizes.length) {
            System.err.println("CONFIGURATION ERROR (loadLevel): Sprite array does not match frameCount/frameSize array!");
            return new Image[levelSprites.length][];
        }

        Image[][] animations = new Image[levelSprites.length][];
        for (int level = 0; level < levelSprites.length; level++) {
            int currentFrameCount = frameCounts[level];
            int currentFrameSize = frameSizes[level];

            if (levelSprites[level] == null) {
                System.err.println("ERROR (loadLevel): Source sprite for level " + (level+1) + " is null. Creating dummy...");
                animations[level] = createDummy(currentFrameCount, currentFrameSize, currentFrameSize);
                continue;
            }

            animations[level] = new Image[currentFrameCount];
            int y = (isSecRow) ? currentFrameSize : 0;
            for (int frame = 0; frame < currentFrameCount; frame++) {
                int x = frame * currentFrameSize;
                animations[level][frame] = AssetLoader.getSubImage(levelSprites[level], x, y, currentFrameSize, currentFrameSize);
            }
        }
        return animations;
    }

    // simple horizontal image strip
    private Image[] loadAnimationStrip(Image spriteSheet, int frameCount, int frameWidth, int frameHeight, int y) {
        if (frameCount <= 0 || frameWidth <= 0 || frameHeight <= 0) {
            System.err.println("ERROR (loadStrip): Invalid frame count, width, or height (<= 0).");
            return new Image[0];
        }

        if (spriteSheet == null) {
            System.err.println("ERROR (loadStrip): Source image is null. Creating dummy...");
            return createDummy(frameCount, frameWidth, frameHeight);
        }

        Image[] animations = new Image[frameCount];
        for (int frame = 0; frame < frameCount; frame++) {
            int x = frame * frameWidth;
            animations[frame] = AssetLoader.getSubImage(spriteSheet, x, y, frameWidth, frameHeight);
        }
        return animations;
    }

    // utility function
    private Image[] loadAnimationStrip(Image spriteSheet, int frameCount, int frameWidth, int frameHeight) {
        return loadAnimationStrip(spriteSheet, frameCount, frameWidth, frameHeight, 0);
    }

    // special for frost icon
    private Image[] loadGridWithColOrder(Image spriteSheet, int frameCount, int frameWidth, int frameHeight, int[] colOrder) {
        if (frameCount <= 0 || frameWidth <= 0 || frameHeight <= 0) {
            System.err.println("ERROR (loadGrid): Invalid frame count, width, or height (<= 0).");
            return new Image[0];
        }

        if (spriteSheet == null) {
            System.err.println("ERROR (loadGrid): Source image is null. Creating dummy...");
            return createDummy(frameCount, frameWidth, frameHeight);
        }

        if (colOrder == null || colOrder.length == 0) {
            System.err.println("ERROR (loadGrid): colOrder array is null/empty!");
            return new Image[0];
        }

        if (frameCount % colOrder.length != 0) {
            System.err.println("WARNING (loadGrid): frameCount (" + frameCount +
                    ") not divisible by colOrder.length (" + colOrder.length + ").");
        }

        Image[] animations = new Image[frameCount];
        int rows = frameCount / colOrder.length;
        int frameIndex = 0;

        for (int colIndex : colOrder) {
            int x = colIndex * frameWidth;
            for (int row = 0; row < rows; row++) {
                int y = frameHeight * (rows - row - 1); // from bottom up
                if (frameIndex >= frameCount) {
                    System.err.println("ERROR (loadGrid): frameIndex exceeds frameCount!");
                    break;
                }
                animations[frameIndex] = AssetLoader.getSubImage(spriteSheet, x, y, frameWidth, frameHeight);
                frameIndex++;
            }
            if (frameIndex >= frameCount) {
                break;
            }
        }
        return animations;
    }

    private Image[] createDummy(int frameCount, int width, int height) {
        Image[] dummy = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            dummy[i] = AssetLoader.createDummyImage(width, height);
        }
        return dummy;
    }

    // helper method for getter
    private Image getSafe(Image[][] array, int row, int col, int dummyWidth, int dummyHeight) {
        // CHECK: array input not null and out of bound
        if (array == null || row < 0 || row >= array.length || col < 0 || col >= array[row].length) {
            return AssetLoader.createDummyImage(dummyWidth, dummyHeight);
        }
        return array[row][col];
    }

    // special for icon animations
//    private Image[] getSafeArray(Image[][] array, int index, int dummyWidth, int dummyHeight) {
//        if (array == null || index < 0 || index >= array.length || array[index] == null) {
//            return createDummy(1, dummyWidth, dummyHeight);
//        }
//        return array[index];
//    }

    // GETTER methods - all return only 1 image
    public Image getTowerBase(int type, int level) {
        return getSafe(towerBases, type, level, 64, 128);
    }

    public Image getTowerWeapon(int type, int level, int animIndex, boolean isAttacking) {
        Image[][] targetArray = null;
        int size = 32;

        if (isAttacking) {
            switch(type) {
                case 0:
                    targetArray = cannonWeaponAttack;
                    size = 48;
                    break;
                case 1:
                    targetArray = poisonWeaponAttack;
                    size = 96;
                    break;
                case 2:
                    targetArray = frostWeaponAttack;
                    size = 64;
                    break;
            }
        } else {
            switch(type) {
                case 0:
                    targetArray = cannonWeaponDefault;
                    size = 48;
                    break;
                case 1:
                    targetArray = poisonWeaponDefault;
                    size = 96;
                    break;
                case 2:
                    targetArray = frostWeaponDefault;
                    size = 48;
                    break;
            }
        }
        return getSafe(targetArray, level, animIndex, size, size);
    }

    public Image getProjectile(int type, int level, int animIndex) {
        Image[][] targetArray = null;
        int size = 32;
        switch(type) {
            case 0:
                targetArray = cannonProjectile;
                size = 64;
                break;
            case 1:
                targetArray = poisonProjectile;
                size = 28;
                break;
            case 2:
                targetArray = frostProjectile;
                size = 30;
                break;
        }
        return getSafe(targetArray, level, animIndex, size, size);
    }

    public Image getImpact(int type, int level, int animIndex) {
        Image[][] targetArray = null;

        int size = 32;
        switch(type) {
            case 0:
                targetArray = cannonImpact;
                size = 64;
                break;
            case 1:
                targetArray = poisonImpact;
                size = 28;
                break;
            case 2:
                targetArray = frostImpact;
                size = 30;
                break;
        }
        return getSafe(targetArray, level, animIndex, size, size);
    }

    public Image getTowerIcon(int type) {
        return towerIconImages[type];
    }

    public Image getSellIcon() {
        return menuIconImages[0];
    }

    public Image getUpgradeIcon() {
        return menuIconImages[1];
    }

    // if using animation
//    public Image[] getSellIcon() {
//        return getSafeArray(towerMenuIcons, 0, 20, 20);
//    }
//
//    public Image[] getUpgradeIcon() {
//        return getSafeArray(towerMenuIcons, 1, 20, 20);
//    }
//
//    public Image[] getTowerIconArray(int type) {
//        return getSafeArray(towerIcons, type, 40, 40);
//    }

    public Image getConstruction(int level, int animIndex) {
        // Level 1 -> Index 0
        return getSafe(towerBuild, level - 1, animIndex, 64, 64);
    }

    public Image getConstructionCompleted(int animIndex) {
        return getSafe(towerBuild, 3, animIndex, 64, 64);
    }

    public Image getCollapse(int animIndex) {
        return getSafe(towerBuild, 4, animIndex, 64, 64);
    }
}