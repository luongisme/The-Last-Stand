package Helper.LoadImages;

import javafx.scene.image.Image;

public class LoadTowerImages extends SpriteLoaderBase {
    private Image[] towerIconImages, // [Anemo,Toxo,Electro]
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
        // [Anemo,Toxo,Electro]
        Image[] baseSprite = loadSprite(new String[]
                {"Anemo_Base.png",
                "Toxo_Base.png",
                "Electro_Base.png"},
        "Towers/Types/Bases"
        );
        towerBases = loadBaseImages(baseSprite, 64, 128, 3);
    }

    // Load tower sprites and animations: weapon [default, attack], projectile, impact, icon
    private void loadWeapons() {
        towerWeaponDefault = new Image[noTowerTypes][maxLevelOfTower][];
        towerWeaponAttack = new Image[noTowerTypes][maxLevelOfTower][];

        // ANEMO
        Image[] anemoWeaponSprite = loadSprite(new String[]
                {"Anemo_Level_01_Weapon.png",
                "Anemo_Level_02_Weapon.png",
                "Anemo_Level_03_Weapon.png"},
        "Towers/Types/Anemo/Weapon"
        );

        int[] anemoWeapon_FrameSize = {48,48,64};
        int[] anemoWeaponDefault_FrameCount = {3,3,3};
        towerWeaponDefault[0] = loadLevelAnimations(anemoWeaponSprite, anemoWeaponDefault_FrameCount, anemoWeapon_FrameSize,0);

        int[] anemoWeaponAttack_FrameCount = {10,10,10};
        towerWeaponAttack[0] = loadLevelAnimations(anemoWeaponSprite, anemoWeaponAttack_FrameCount, anemoWeapon_FrameSize,0);

        // TOXO
        Image[] toxoWeaponSprite = loadSprite(new String[]
                {"Toxo_Level_01_Weapon.png",
                "Toxo_Level_02_Weapon.png",
                "Toxo_Level_03_Weapon.png"},
        "Towers/Types/Toxo/Weapon"
        );

        int[] toxoWeapon_FrameSize = {96,96,96};
        int[] toxoWeaponDefault_FrameCount = {8,8,8};
        towerWeaponDefault[1] = loadLevelAnimations(toxoWeaponSprite, toxoWeaponDefault_FrameCount, toxoWeapon_FrameSize,0);

        int[] toxoWeaponAttack_FrameCount = {29,29,29};
        towerWeaponAttack[1] = loadLevelAnimations(toxoWeaponSprite, toxoWeaponAttack_FrameCount, toxoWeapon_FrameSize,1);

        // ELECTRO
        Image[] electroWeaponSprite = loadSprite(new String[]
                {"Electro_Level_01_Weapon.png",
                "Electro_Level_02_Weapon.png",
                "Electro_Level_03_Weapon.png"},
        "Towers/Types/Electro/Weapon"
        );

        int[] electroWeapon_FrameSize = {48,64,64};
        int[] electroWeaponDefault_FrameCount = {10,16,20};
        towerWeaponDefault[2] = loadLevelAnimations(electroWeaponSprite, electroWeaponDefault_FrameCount, electroWeapon_FrameSize,0);

        int[] electroWeaponAttack_FrameCount = {16,17,19};
        towerWeaponAttack[2] = loadLevelAnimations(electroWeaponSprite, electroWeaponAttack_FrameCount, electroWeapon_FrameSize,1);
    }

    private void loadIcons() {
        // load images
        towerIconImages = loadSprite(new String[]
                {"wind.png","darkness.png","thunder.png"},
        "Towers/Icons/Crystals"
        );
    }

    private void loadMenuIcons() {
        menuIconImages = loadSprite(new String[]
                {"Sell.png", "Upgrade.png"},
        "Towers/Icons"
        );
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