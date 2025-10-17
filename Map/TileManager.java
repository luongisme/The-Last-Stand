package Map;

import Constant.TileConstant;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TileManager {

    public ArrayList<Tile> tiles = new ArrayList<>();

    public TileManager() {
        // reorder the ArrayList so it can be accessed by ID
        for (int i = 0; i < TileConstant.values().length; i++) {
            tiles.add(null);
        }
        createTiles();
    }

    private void createTiles() {
        // iterate through all values ​​in TileType enum
        for (TileConstant type : TileConstant.values()) {
            BufferedImage image = loadImage(type.getSpriteName());
            Tile newTile = new Tile(image, type);
            // put Tile in the correct ID position in ArrayList
            tiles.set(type.getId(), newTile);
        }
        
        // **animation for WATER?**
        // BufferedImage[] waterFrames = loadWaterAnimation(); // need to declare this function
        // Tile animatedWater = new Tile(waterFrames, TileType.WATER);
        // tiles.set(TileType.WATER.getId(), animatedWater);
    }

    private BufferedImage loadImage(String fileName) {
        BufferedImage img = null;
        // path to the folder containing the tile image
        InputStream is = TileManager.class.getResourceAsStream("/tileset/" + fileName);
        if (is == null) {
            System.err.println("Image file could not be found: /tileset/" + fileName);
            return null;
        }
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }

    public Tile getTile(int id) {
        if (id >= 0 && id < tiles.size()) {
            return tiles.get(id);
        }
        return null; // or return a default tile?
    }  
}
