package Helper;

import Managers.TileManager;
import Map.Tile;

import java.util.LinkedList;
import java.util.Queue;

public class Pathfinding {
    public static final int DIR_NONE=-1;
    public static final int DIR_UP=0;
    public static final int DIR_RIGHT=1;
    public static final int DIR_DOWN=2;
    public static final int DIR_LEFT=3;

    public static int[][] buildDirectionMap(int[][] map, TileManager tileManager, int homeTileId) {
        int rows = map.length;
        int cols = map[0].length;
        int[][] directionMap = new int[rows][cols];

        // Initialize direction map with DIR_NONE
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                directionMap[i][j] = DIR_NONE;
            }
        }

        // Find home position
        int homeRow = -1, homeCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == homeTileId) {
                    homeRow = i;
                    homeCol = j;
                    break;
                }
            }
            if (homeRow != -1) break;
        }

        if (homeRow == -1) {
            System.err.println("Home tile not found in map!");
            return directionMap;
        }

        // BFS from home to all walkable tiles
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.add(new int[]{homeRow, homeCol});
        visited[homeRow][homeCol] = true;

        int[] dRow = {-1, 0, 1, 0}; // UP, RIGHT, DOWN, LEFT
        int[] dCol = {0, 1, 0, -1};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            // Check all 4 directions
            for (int dir = 0; dir < 4; dir++) {
                int newRow = row + dRow[dir];
                int newCol = col + dCol[dir];

                // Check bounds
                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                    continue;
                }

                // Skip if already visited
                if (visited[newRow][newCol]) {
                    continue;
                }

                // Check if tile is walkable
                int tileId = map[newRow][newCol];
                Tile tile = tileManager.getTile(tileId);
                if (tile == null || !tile.isWalkable()) {
                    continue;
                }

                // Mark as visited and set direction (opposite direction to point towards home)
                visited[newRow][newCol] = true;
                directionMap[newRow][newCol] = getOppositeDirection(dir);
                queue.add(new int[]{newRow, newCol});
            }
        }

        return directionMap;
    }

    private static int getOppositeDirection(int dir) {
        switch (dir) {
            case DIR_UP: return DIR_DOWN;
            case DIR_DOWN: return DIR_UP;
            case DIR_LEFT: return DIR_RIGHT;
            case DIR_RIGHT: return DIR_LEFT;
            default: return DIR_NONE;
        }
    }
}
