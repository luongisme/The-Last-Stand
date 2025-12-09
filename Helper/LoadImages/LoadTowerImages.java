package Helper.LoadImages;

import javafx.scene.image.Image;

public class LoadTowerImages extends SpriteLoaderBase {
    private Image[] towerIconImages, // [Cannon,Poison,Frost]
                    menuIconImages; // [Sell,Upgrade]

    // private Image[] iconsSprite, menuSprite - if using animation


    // [TowerType][LevelIndex/FrameIndex] - animations
    private Image[][] towerBases, towerBuild; // towerIcons, towerMenuIcons, if using animation

    // [TowerTypes[Levels][Frames]
    private Image[][][] towerWeaponDefault, towerWeaponAttack;

    public LoadTowerImages() {
        loadAll();
    }

    private void loadAll() {
        loadBase();
        loadWeapons();
        loadIcons();
        loadMenuIcons();
        loadConstructionCollapse();
    }

    // Load tower sprite and base levels for each type
    private void loadBase() {
        // [Cannon,Poison,Frost]
        Image[] baseSprite = loadSprite(new String[]
                {"Cannon_Bases.png",
                "Poison_Bases.png",
                "Frost_Bases.png"},
        "Towers/Types"
        );
        towerBases = loadBaseImages(baseSprite, 64, 128, 3);
    }

    // Load tower sprites and animations: weapon [default, attack], projectile, impact, icon
    private void loadWeapons() {
        towerWeaponDefault = new Image[noTowerTypes][maxLevelOfTower][];
        towerWeaponAttack = new Image[noTowerTypes][maxLevelOfTower][];

        // CANNON
        Image[] cannonWeaponSprite = loadSprite(new String[]
                {"Cannon_Level_01_Weapon.png",
                "Cannon_Level_02_Weapon.png",
                "Cannon_Level_03_Weapon.png"},
        "Towers/Types/Cannon/Weapon"
        );

        int[] cannonWeapon_FrameSize = {48,48,64};
        int[] cannonWeaponDefault_FrameCount = {3,3,3};
        towerWeaponDefault[0] = loadLevelAnimations(cannonWeaponSprite, cannonWeaponDefault_FrameCount, cannonWeapon_FrameSize,0);

        int[] cannonWeaponAttack_FrameCount = {10,10,10};
        towerWeaponAttack[0] = loadLevelAnimations(cannonWeaponSprite, cannonWeaponAttack_FrameCount, cannonWeapon_FrameSize,0);

        // POISON
        Image[] poisonWeaponSprite = loadSprite(new String[]
                {"Poison_Level_01_Weapon.png",
                "Poison_Level_02_Weapon.png",
                "Poison_Level_03_Weapon.png"},
        "Towers/Types/Poison/Weapon"
        );

        int[] poisonWeapon_FrameSize = {96,96,96};
        int[] poisonWeaponDefault_FrameCount = {8,8,8};
        towerWeaponDefault[1] = loadLevelAnimations(poisonWeaponSprite, poisonWeaponDefault_FrameCount, poisonWeapon_FrameSize,0);

        int[] poisonWeaponAttack_FrameCount = {29,29,29};
        towerWeaponAttack[1] = loadLevelAnimations(poisonWeaponSprite, poisonWeaponAttack_FrameCount, poisonWeapon_FrameSize,1);

        // FROST
        Image[] frostWeaponSprite = loadSprite(new String[]
                {"Frost_Level_01_Weapon.png",
                "Frost_Level_02_Weapon.png",
                "Frost_Level_03_Weapon.png"},
        "Towers/Types/Frost/Weapon"
        );

        int[] frostWeapon_FrameSize = {48,64,64};
        int[] frostWeaponDefault_FrameCount = {10,16,20};
        towerWeaponDefault[2] = loadLevelAnimations(frostWeaponSprite, frostWeaponDefault_FrameCount, frostWeapon_FrameSize,0);

        int[] frostWeaponAttack_FrameCount = {16,17,19};
        towerWeaponAttack[2] = loadLevelAnimations(frostWeaponSprite, frostWeaponAttack_FrameCount, frostWeapon_FrameSize,1);
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
    private void loadConstructionCollapse() {
        // [Construction, Collapse]
        Image[] buildSprite = loadSprite(new String[]
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
        towerBuild[4] = towerBuild[3];
    }

    // GETTER methods - all return only 1 image
    public Image getTowerBase(int type, int level) {
        return getSafe(towerBases, type, level);
    }

    public Image getTowerWeapon(int type, int level, int animIndex, boolean isAttacking) {
        Image[][][] targetArray = isAttacking ? towerWeaponAttack : towerWeaponDefault;
        return getSafe(targetArray[type],level,animIndex);
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
        return getSafe(towerBuild, level, animIndex);
    }

    public Image getConstructionCompleted(int animIndex) {
        return getSafe(towerBuild, 3, animIndex);
    }

    public Image getCollapse(int animIndex) {
        return getSafe(towerBuild, 4, animIndex);
    }
}