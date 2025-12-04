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
    public Tile DIRT, SPAWN, ROAD, GRASS, SAND, WATER, WOOD, HOME, WALL, STONE, CURB, AVAILABLEDIRT, TREE, TRUNK, ROCK;

    public ArrayList<Tile> tiles = new ArrayList<>();
    private Image atlas; // The tileset sprite sheet
    private Image atlas1;
    private Image atlas2;
    private Image atlas3;
    private Image treeImage;
    private Image trunkImage;
    private Image rockImage;
    private final int TILE_SIZE = 16; 

    public TileManager() {
        loadAtlas();
        createTiles();
    }
    
    private void loadAtlas() {
        atlas = loadImage("grass.png");// Load your tileset image
        atlas1 = loadImage("DirtRoad.png");
        atlas2 = loadImage("tile1.png");
        atlas3 = loadImage ( "tile2.png"); 
        treeImage = loadImage("Tree (1).png"); 
        trunkImage = loadImage("Trunk (1).png");
        rockImage = loadImage("Rock (2).png");
        if (atlas == null ) {
            System.err.println("Failed to load tileset atlas!");
        }
        if (atlas1 == null) {
            System.err.println("Failed to load tileset atlas!");
        }
        if (atlas2 == null) {
            System.err.println("Failed to load tileset atlas!");
        }
        if (atlas3 == null) {
            System.err.println("Failed to load tileset atlas!");
        }
        if (treeImage == null) { 
            System.err.println("Failed to load tree image!");
        }
        if (trunkImage == null) { 
            System.err.println("Failed to load trunk image!");
        }
        if (rockImage == null) { 
            System.err.println("Failed to load trunk image!");
        }
    }
    
    private Image getSprite(Image source, int xCord, int yCord) {
        if (source == null) return null;
        
        // Calculate pixel coordinates
        int x = xCord * TILE_SIZE;
        int y = yCord * TILE_SIZE;
        
        // Extract sub-image using JavaFX PixelReader
        PixelReader reader = source.getPixelReader();
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
        
        tiles.add(DIRT = new Tile(getSprite(atlas, 17, 1), TileConstant.DIRT));
        tiles.add(SPAWN = new Tile(getSprite(atlas, 1, 0), TileConstant.SPAWN));
        tiles.add(ROAD = new Tile(getSprite(atlas1, 4, 1), TileConstant.ROAD));
        tiles.add(GRASS = new Tile(getSprite(atlas2, 1, 5), TileConstant.GRASS));
        tiles.add(SAND = new Tile(getSprite(atlas, 4, 0), TileConstant.SAND));
        tiles.add(WATER = new Tile(getSprite(atlas3,3, 15), TileConstant.WATER));
        tiles.add(WOOD = new Tile(getSprite(atlas, 22, 11), TileConstant.WOOD));
        tiles.add(HOME = new Tile(getSprite(atlas, 7, 0), TileConstant.HOME));
        tiles.add(WALL = new Tile(getSprite(atlas, 8, 0), TileConstant.WALL));
        tiles.add(STONE = new Tile(getSprite(atlas2, 1, 5), TileConstant.STONE));
        tiles.add(CURB = new Tile(getSprite(atlas, 1,3),TileConstant.CURB));
        tiles.add(AVAILABLEDIRT = new Tile(getSprite(atlas, 14,5), TileConstant.AVAILABLEDIRT));
        tiles.add(TREE = new Tile(treeImage, TileConstant.TREE));
        tiles.add(TRUNK = new Tile(trunkImage, TileConstant.TRUNK));
        tiles.add(ROCK = new Tile(rockImage, TileConstant.ROCK));
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
