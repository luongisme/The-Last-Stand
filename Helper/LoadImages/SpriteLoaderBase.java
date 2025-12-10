package Helper.LoadImages;

import Constant.TowerConstant;
import Helper.AssetLoader;
import javafx.scene.image.Image;

public abstract class SpriteLoaderBase {
    protected final int noTowerTypes = TowerConstant.NO_TOWER_TYPES;
    protected final int maxLevelOfTower = TowerConstant.MAX_LEVEL;

    // helper method for loading sprites/animations
    protected Image[] loadSprite(String[] fileNames, String subPath) {
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

    protected Image[][] loadBaseImages(Image[] spriteSheet, int baseWidth, int baseHeight, int maxLevel) {
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

    protected Image[][] loadLevelAnimations(Image[] levelSprites, int[] frameCounts, int[] frameSizes, int rowIndex) {
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
            int y = rowIndex * currentFrameSize;
            for (int frame = 0; frame < currentFrameCount; frame++) {
                int x = frame * currentFrameSize;
                animations[level][frame] = AssetLoader.getSubImage(levelSprites[level], x, y, currentFrameSize, currentFrameSize);
            }
        }
        return animations;
    }

    // simple horizontal image strip
    protected Image[] loadAnimationStrip(Image spriteSheet, int frameCount, int frameWidth, int frameHeight, int y) {
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
    protected Image[] loadAnimationStrip(Image spriteSheet, int frameCount, int frameWidth, int frameHeight) {
        return loadAnimationStrip(spriteSheet, frameCount, frameWidth, frameHeight, 0);
    }

    // special for frost icon
    protected Image[] loadGridWithColOrder(Image spriteSheet, int frameCount, int frameWidth, int frameHeight, int[] colOrder) {
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

    protected Image[] createDummy(int frameCount, int width, int height) {
        Image[] dummy = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            dummy[i] = AssetLoader.createDummyImage(width, height);
        }
        return dummy;
    }

    // helper method for getter
    protected Image getSafe(Image[][] array, int row, int col) {
        // CHECK: array input not null and out of bound
        if (array == null || row < 0 || row >= array.length || col < 0 || col >= array[row].length) {
            System.out.println("Error: " + array.toString() + " at row: " + row + " at col: " + col);
            return AssetLoader.createDummyImage(32, 32);
        }
        return array[row][col];
    }

    // special for icon animations
    protected Image[] getSafeArray(Image[][] array, int index, int dummyWidth, int dummyHeight) {
        if (array == null || index < 0 || index >= array.length || array[index] == null) {
            return createDummy(1, dummyWidth, dummyHeight);
        }
        return array[index];
    }
}
