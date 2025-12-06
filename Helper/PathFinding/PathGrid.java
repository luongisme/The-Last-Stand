package Helper.PathFinding;

import Constant.TileConstant;

public class PathGrid {
    private final int[][] grid;
    private final int rows;
    private final int cols;
    private final int tileSize;
    private static final int DEFAULT_TILE_SIZE = 16;


    public PathGrid(int[][] mapData) {
        this(mapData, DEFAULT_TILE_SIZE);
    }

    public PathGrid(int[][] mapData, int tileSize) {
        this.rows = mapData.length;
        this.cols = mapData[0].length;
        this.grid = new int[rows][cols];
        this.tileSize = tileSize;

        // Loop through the map data to set walkable and blocked tiles
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int tileId = mapData[y][x];
                TileConstant tileType = getTileConstantById(tileId);
                if (tileType != null && tileType.isWalkable()) {
                    grid[y][x] = 0; // walkable
                } else {
                    grid[y][x] = 1; // blocked
                }
            }
        }

    }


    private TileConstant getTileConstantById(int id) {
        for (TileConstant tc : TileConstant.values()) {
            if (tc.getId() == id) {
                return tc;
            }
        }
        return null;
    }

    public boolean isWalkable(int gridX, int gridY) {
        if (gridX < 0 || gridX >= cols || gridY < 0 || gridY >= rows) {
            return false;
        }
        return grid[gridY][gridX] == 0;
    }

    public void setBlocked(int gridX, int gridY, boolean blocked) {
        if (gridX >= 0 && gridX < cols && gridY >= 0 && gridY < rows) {
            grid[gridY][gridX] = blocked ? 1 : 0;
        }
    }


    public int toGridX(float pixelX) {
        return (int) (pixelX / tileSize);
    }

    public int toGridY(float pixelY) {
        return (int) (pixelY / tileSize);
    }

    public float toPixelCenterX(int gridX) {
        return gridX * tileSize + tileSize / 2.0f;
    }

    public float toPixelCenterY(int gridY) {
        return gridY * tileSize + tileSize / 2.0f;
    }

    public float toPixelX(int gridX) {
        return gridX * tileSize;
    }

    public float toPixelY(int gridY) {
        return gridY * tileSize;
    }

    /**
     * Đặt tower tại vị trí grid (block tile)
     */
    public void placeTower(int gridX, int gridY) {
        setBlocked(gridX, gridY, true);
    }

    /**
     * Xóa tower tại vị trí grid (unblock tile)
     */
    public void removeTower(int gridX, int gridY) {
        setBlocked(gridX, gridY, false);
    }


    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTileSize() {
        return tileSize;
    }

    public String getWidth() {
        return Integer.toString(cols * tileSize);
    }
}
