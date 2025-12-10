package Constant;

public enum TileConstant {
    // define types of Tile with id, file name of the picture, walkable, and can the tower be placed?
    DIRT(0, "dirt.png", false, false),
    SPAWN(1, "spawn.png", true, false),
    ROAD(2, "road.png", true, false),
    GRASS(3, "grass.png", false, false),
    SAND(4, "sand.png", false, false),
    WATER(5, "water.png", false, false),
    WOOD(6, "wood.png", true, false),
    HOME(7, "home.png", true, false),
    WALL(8, "wall.png", false, false),
    STONE(9, "stone.png", false, false),
    CURB(10,"curb.png",false, false),
    AVAILABLEDIRT(11,"Adirt.png",false, true),
TREE (12,"Tree.png",false,false),
    TRUNK ( 13, "Trunk.png", false,false),
    ROCK( 14, "Decostone.png", false,false),
    BOSS(20, "skeleton_boss.png", false,false),
    OCTOPUS(21, "octopus.png", false,false),
    WALLLAST1 (15, "Walllast1.png",false, false),
    WALLLAST2 (16, "Walllast2.png",false, false),
    WALL1(17,"wall1.png",false, false),
    DOOR(18,"Door.png",false,false);

    private final int id;
    private final String spriteName;
    private final boolean walkable;
    private final boolean canPlaceTower;

    TileConstant(int id, String spriteName, boolean walkable, boolean canPlaceTower) {
        this.id = id;
        this.spriteName = spriteName;
        this.walkable = walkable;
        this.canPlaceTower = canPlaceTower;
    }

    public int getId() {
        return id;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean canPlaceTower() {
        return canPlaceTower;
    }
}