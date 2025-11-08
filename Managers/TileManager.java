package Managers;

import Constant.TileConstant;
import Map.Tile;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class TileManager {
    public Tile DIRT, SPAWN, ROAD, GRASS, SAND, WATER, WOOD, HOME, WALL, STONE, CURB, AVAILABLEDIRT;

    public ArrayList<Tile> tiles = new ArrayList<>();
    private Image atlas; // The tileset sprite sheet
    private final int TILE_SIZE = 16; 

    public TileManager() {
        loadAtlas();
        createTiles();
    }
    
    private void loadAtlas() {
        atlas = loadImage("grass.png"); // Load your tileset image
        if (atlas == null) {
            System.err.println("Failed to load tileset atlas!");
        }
    }
    
    private Image getSprite(int xCord, int yCord) {
        if (atlas == null) return null;
        
        // Calculate pixel coordinates
        int x = xCord * TILE_SIZE;
        int y = yCord * TILE_SIZE;
        
        // Extract sub-image using JavaFX PixelReader
        PixelReader reader = atlas.getPixelReader();
        WritableImage subImage = new WritableImage(reader, x, y, TILE_SIZE, TILE_SIZE);
        
        return subImage;
    }

    private Image loadImage(String fileName) {
        // Try classpath first (preferred when resource folder is on classpath)
        String[] cpRoots = { "/assets/assets/tileset/", "/assets/tileset/" };
        for (String root : cpRoots) {
            String cpPath = root + fileName;
            try (InputStream is = TileManager.class.getResourceAsStream(cpPath)) {
                if (is != null) {
                    return new Image(is);
                }
            } catch (Exception e) {
                System.err.println("Failed to read classpath resource: " + cpPath);
                e.printStackTrace();
            }
        }

        // Fallback to file system
        String[] fsRoots = {
            "resource/assets/assets/tileset/",
            "resource/assets/tileset/"
        };
        for (String root : fsRoots) {
            File f = new File(root + fileName);
            if (f.exists()) {
                try {
                    return new Image(new FileInputStream(f));
                } catch (Exception e) {
                    System.err.println("Failed to read file: " + f.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }

        System.err.println("Image not found: " + fileName);
        return null;
    }

    private void createTiles() {
        
        tiles.add(DIRT = new Tile(getSprite(5, 8), TileConstant.DIRT));
        tiles.add(SPAWN = new Tile(getSprite(1, 0), TileConstant.SPAWN));
        tiles.add(ROAD = new Tile(getSprite(2, 0), TileConstant.ROAD));
        tiles.add(GRASS = new Tile(getSprite(8, 2), TileConstant.GRASS));
        tiles.add(SAND = new Tile(getSprite(4, 0), TileConstant.SAND));
        tiles.add(WATER = new Tile(getSprite(5, 0), TileConstant.WATER));
        tiles.add(WOOD = new Tile(getSprite(6, 0), TileConstant.WOOD));
        tiles.add(HOME = new Tile(getSprite(7, 0), TileConstant.HOME));
        tiles.add(WALL = new Tile(getSprite(8, 0), TileConstant.WALL));
        tiles.add(STONE = new Tile(getSprite(9, 0), TileConstant.STONE));
        tiles.add(CURB = new Tile(getSprite(1,3),TileConstant.CURB));
        tiles.add(AVAILABLEDIRT = new Tile(getSprite(14,5), TileConstant.AVAILABLEDIRT));
        
        // **animation for WATER?**
        // BufferedImage[] waterFrames = loadWaterAnimation();
        // Tile animatedWater = new Tile(waterFrames, TileConstant.WATER);
        // tiles.set(TileConstant.WATER.getId(), animatedWater);
    }


    public boolean checkSpriteAnimation(int spriteID){
        if (spriteID < 0 || spriteID >= tiles.size()) return false;
        Tile t = tiles.get(spriteID);
        return t != null && t.hasAnimation();
    }
    public Tile getTile(int id) {
        if (id < 0 || id >= tiles.size()) return null;
        return tiles.get(id);
    }

    public Image getSprite(int id){
        return tiles.get(id).getSprite();
    }
}
