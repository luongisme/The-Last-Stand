package Map;
import javafx.scene.image.Image;

import Constant.TileConstant;

public class Tile {
    private final TileConstant tileType;
    private final Image[] sprites;

    // constructor for tile has one picture
    public Tile(Image sprite, TileConstant tileType) {
        this.sprites = new Image[1];
        this.sprites[0] = sprite;
        this.tileType = tileType;
    }

    // constructor for tile has many pictures
    public Tile(Image[] sprites, TileConstant tileType) {
        this.sprites = sprites;
        this.tileType = tileType;
    }

    public Image getSprite() {
        return sprites[0];
    }
    
    public Image getSprite(int animationIndex) {
        // make sure animationIndex does not exceed the number of frames
        if (animationIndex >= sprites.length) {
            animationIndex = 0;
        }
        return sprites[animationIndex];
    }

    public boolean hasAnimation() {
        return sprites.length > 1;
    }

    public int getId() {
        return tileType.getId();
    }
    
    public boolean canPlaceTower() {
        return tileType.canPlaceTower();
    }
    
    public TileConstant getTileType() {
        return tileType;
    }
}