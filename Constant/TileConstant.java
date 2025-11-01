package Constant;

public enum TileConstant {
    // define types of Tile with id, file name of the picture, and can the tower be placed?
    DIRT(0, "dirt.png", true),
    SPAWN(1, "spawn.png", false),
    ROAD(2, "road.png", false),
    GRASS(3, "grass.png", true),
    SAND(4, "sand.png", true),
    WATER(5, "water.png", false),
    WOOD(6, "wood.png", false),
    HOME(7, "home.png", false),
    WALL(8, "wall.png", false),
    STONE(9, "stone.png", true);

    private final int id;
    private final String spriteName;
    private final boolean canPlaceTower;

    TileConstant(int id, String spriteName, boolean canPlaceTower) {
        this.id = id;
        this.spriteName = spriteName;
        this.canPlaceTower = canPlaceTower;
    }

    public int getId() {
        return id;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public boolean canPlaceTower() {
        return canPlaceTower;
    }
}