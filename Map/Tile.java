package Map;

import Constant.TileConstant;
import java.awt.image.BufferedImage;

public class Tile {
    private final TileConstant tileType;
    private final BufferedImage[] sprites;

    // constructor for tile has one picture
    public Tile(BufferedImage sprite, TileConstant tileType) {
        this.sprites = new BufferedImage[1];
        this.sprites[0] = sprite;
        this.tileType = tileType;
    }

    // constructor for tile has many pictures
    public Tile(BufferedImage[] sprites, TileConstant tileType) {
        this.sprites = sprites;
        this.tileType = tileType;
    }

    public BufferedImage getSprite() {
        return sprites[0];
    }
    
    public BufferedImage getSprite(int animationIndex) {
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