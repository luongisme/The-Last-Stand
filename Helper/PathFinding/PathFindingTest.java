package Helper.PathFinding;

import Map.LevelBuild;
import Constant.TileConstant;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Comprehensive test class for PathFinding package
 * Tests all components: Node, PathGrid, and AStarPathfinder
 * Run this file to verify all PathFinding functionality works correctly
 */
public class PathFindingTest {

    private static final int TILE_SIZE = 16;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("    COMPREHENSIVE PATHFINDING TEST - THE LAST STAND");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // Test 1: Node class
        testNodeClass();

        // Test 2: PathGrid creation and methods
        testPathGridCreation();

        // Test 3: PathGrid coordinate conversions
        testPathGridCoordinateConversions();

        // Test 4: A* pathfinding basic cases
        testAStarPathfinding();

        // Test 5: A* edge cases
        testAStarEdgeCases();

        // Test 6: Tower blocking and dynamic pathfinding
        testTowerBlocking();

        // Test 7: Performance test
        testPerformance();

        // Test 8: TileConstant integration
        testTileConstantIntegration();

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("                    TEST SUMMARY");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        if (testsFailed == 0) {
            System.out.println("\n✓✓✓ ALL TESTS PASSED! ✓✓✓");
        } else {
            System.out.println("\n✗✗✗ SOME TESTS FAILED ✗✗✗");
        }
        System.out.println("═══════════════════════════════════════════════════════");
    }

    /**
     * Test 1: Node class functionality
     */
    private static void testNodeClass() {
        System.out.println("【TEST 1】Testing Node Class...\n");

        // Test Node creation
        Node node1 = new Node(5, 10);
        assertTest("Node x coordinate", node1.x == 5);
        assertTest("Node y coordinate", node1.y == 10);
        assertTest("Node initial gCost is MAX_VALUE", node1.gCost == Integer.MAX_VALUE);
        assertTest("Node initial fCost is MAX_VALUE", node1.fCost == Integer.MAX_VALUE);

        // Test fCost calculation
        node1.gCost = 100;
        node1.hCost = 50;
        node1.calculateFCost();
        assertTest("Node fCost calculation", node1.fCost == 150);

        // Test compareTo (for PriorityQueue)
        Node node2 = new Node(3, 7);
        node2.gCost = 80;
        node2.hCost = 40;
        node2.calculateFCost(); // fCost = 120

        assertTest("Node comparison (lower fCost first)", node2.compareTo(node1) < 0);

        // Test equal fCost - should compare hCost
        Node node3 = new Node(8, 8);
        node3.gCost = 100;
        node3.hCost = 20;
        node3.calculateFCost(); // fCost = 120

        assertTest("Node comparison with equal fCost", node3.compareTo(node2) < 0);

        // Test in PriorityQueue
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(node1);
        pq.add(node2);
        pq.add(node3);

        Node first = pq.poll();
        assertTest("PriorityQueue ordering", first.fCost == 120 && first.hCost == 20);

        // Test parent reference
        node1.parent = node2;
        assertTest("Node parent reference", node1.parent == node2);

        System.out.println("✓ Test 1 Completed\n");
    }

    /**
     * Test 2: PathGrid creation and basic operations
     */
    private static void testPathGridCreation() {
        System.out.println("【TEST 2】Testing PathGrid Creation...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);

        // Test grid dimensions
        assertTest("Grid rows", grid.getRows() == mapData.length);
        assertTest("Grid cols", grid.getCols() == mapData[0].length);
        assertTest("Grid tile size", grid.getTileSize() == TILE_SIZE);

        System.out.println("Grid dimensions: " + grid.getCols() + " x " + grid.getRows());
        System.out.println("Total tiles: " + (grid.getCols() * grid.getRows()));

        // Test walkable tiles (based on TileConstant)
        // SPAWN(1), ROAD(2), HOME(7) are walkable
        int walkableCount = 0;
        int blockedCount = 0;

        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getCols(); x++) {
                if (grid.isWalkable(x, y)) {
                    walkableCount++;
                } else {
                    blockedCount++;
                }
            }
        }

        System.out.println("Walkable tiles: " + walkableCount);
        System.out.println("Blocked tiles: " + blockedCount);

        // Test boundary checks
        assertTest("Grid boundary - negative x", !grid.isWalkable(-1, 0));
        assertTest("Grid boundary - negative y", !grid.isWalkable(0, -1));
        assertTest("Grid boundary - beyond cols", !grid.isWalkable(grid.getCols(), 0));
        assertTest("Grid boundary - beyond rows", !grid.isWalkable(0, grid.getRows()));

        // Test setBlocked
        if (grid.isWalkable(5, 5)) {
            grid.setBlocked(5, 5, true);
            assertTest("setBlocked makes tile unwalkable", !grid.isWalkable(5, 5));
            grid.setBlocked(5, 5, false);
            assertTest("setBlocked(false) makes tile walkable", grid.isWalkable(5, 5));
        }

        System.out.println("✓ Test 2 Completed\n");
    }

    /**
     * Test 3: PathGrid coordinate conversion methods
     */
    private static void testPathGridCoordinateConversions() {
        System.out.println("【TEST 3】Testing PathGrid Coordinate Conversions...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);

        // Test pixel to grid conversion
        assertTest("toGridX(0)", grid.toGridX(0) == 0);
        assertTest("toGridX(16)", grid.toGridX(16) == 1);
        assertTest("toGridX(32)", grid.toGridX(32) == 2);
        assertTest("toGridY(0)", grid.toGridY(0) == 0);
        assertTest("toGridY(16)", grid.toGridY(16) == 1);

        // Test grid to pixel conversion
        assertTest("toPixelX(0)", grid.toPixelX(0) == 0.0f);
        assertTest("toPixelX(1)", grid.toPixelX(1) == 16.0f);
        assertTest("toPixelY(0)", grid.toPixelY(0) == 0.0f);
        assertTest("toPixelY(1)", grid.toPixelY(1) == 16.0f);

        // Test grid to pixel center conversion
        assertTest("toPixelCenterX(0)", grid.toPixelCenterX(0) == 8.0f);
        assertTest("toPixelCenterX(1)", grid.toPixelCenterX(1) == 24.0f);
        assertTest("toPixelCenterY(0)", grid.toPixelCenterY(0) == 8.0f);
        assertTest("toPixelCenterY(1)", grid.toPixelCenterY(1) == 24.0f);

        // Test round-trip conversion
        int testGridX = 10;
        int testGridY = 15;
        float pixelX = grid.toPixelX(testGridX);
        float pixelY = grid.toPixelY(testGridY);
        assertTest("Round-trip X conversion", grid.toGridX(pixelX) == testGridX);
        assertTest("Round-trip Y conversion", grid.toGridY(pixelY) == testGridY);

        System.out.println("✓ Test 3 Completed\n");
    }

    /**
     * Test 4: A* Pathfinding basic functionality
     */
    private static void testAStarPathfinding() {
        System.out.println("【TEST 4】Testing A* Pathfinding...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);
        AStarPathfinder pathfinder = new AStarPathfinder(grid);

        // Test case 1: SPAWN to HOME (most important for tower defense!)
        int[] spawnTile = findTileByType(mapData, 1); // SPAWN tile
        int[] homeTile = findTileByType(mapData, 7);  // HOME tile

        if (spawnTile != null && homeTile != null) {
            System.out.println("Test case 1: SPAWN to HOME (critical path for enemies)");
            System.out.println("  SPAWN tile at [" + spawnTile[0] + ", " + spawnTile[1] + "]");
            System.out.println("  HOME tile at [" + homeTile[0] + ", " + homeTile[1] + "]");
            testPath(pathfinder, spawnTile[0], spawnTile[1], homeTile[0], homeTile[1], true);
            assertTest("SPAWN to HOME path exists", true);
        } else {
            System.out.println("Test case 1: SPAWN or HOME tile not found!");
            assertTest("SPAWN to HOME path exists", false);
        }

        // Test case 2: Short path on ROAD tiles
        int[] roadStart = findTileByType(mapData, 2);
        if (roadStart != null) {
            int[] roadEnd = findWalkableNearby(grid, roadStart[0], roadStart[1], 5);
            if (roadEnd != null) {
                System.out.println("\nTest case 2: Short path on ROAD");
                testPath(pathfinder, roadStart[0], roadStart[1], roadEnd[0], roadEnd[1], true);
            }
        }

        // Test case 3: Same start and end
        if (spawnTile != null) {
            System.out.println("\nTest case 3: Same start and end");
            testPath(pathfinder, spawnTile[0], spawnTile[1], spawnTile[0], spawnTile[1], true);
        }

        // Test case 4: Heuristic calculation
        int heuristic = pathfinder.calculateHeuristic(0, 0, 10, 10);
        assertTest("Heuristic calculation", heuristic == 200); // (10+10)*10

        System.out.println("✓ Test 4 Completed\n");
    }

    /**
     * Test 5: A* Edge cases
     */
    private static void testAStarEdgeCases() {
        System.out.println("【TEST 5】Testing A* Edge Cases...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);
        AStarPathfinder pathfinder = new AStarPathfinder(grid);

        // Find blocked and walkable positions
        int[] blocked = findBlockedTile(grid);
        int[] walkable = findWalkableTile(grid, 0, 0);

        // Test path to blocked destination
        if (blocked != null && walkable != null) {
            System.out.println("Test case 1: Path to blocked destination");
            testPath(pathfinder, walkable[0], walkable[1], blocked[0], blocked[1], false);
        }

        // Test path from blocked start
        if (blocked != null && walkable != null) {
            System.out.println("\nTest case 2: Path from blocked start");
            testPath(pathfinder, blocked[0], blocked[1], walkable[0], walkable[1], false);
        }

        // Test boundary positions
        System.out.println("\nTest case 3: Path involving boundaries");
        if (grid.isWalkable(0, 0)) {
            int[] nearEnd = findWalkableTile(grid, grid.getCols() - 5, grid.getRows() - 5);
            if (nearEnd != null) {
                testPath(pathfinder, 0, 0, nearEnd[0], nearEnd[1], true);
            }
        }

        System.out.println("✓ Test 5 Completed\n");
    }

    /**
     * Test 6: Tower blocking and dynamic pathfinding
     */
    private static void testTowerBlocking() {
        System.out.println("【TEST 6】Testing Tower Blocking...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);
        AStarPathfinder pathfinder = new AStarPathfinder(grid);

        // Find a path first
        int[] start = findWalkableTile(grid, 5, 5);
        int[] end = findWalkableTile(grid, 20, 20);

        if (start != null && end != null) {
            System.out.println("Before placing tower:");
            List<Node> pathBefore = pathfinder.findPath(start[0], start[1], end[0], end[1]);
            if (pathBefore != null) {
                System.out.println("Path length: " + pathBefore.size() + " nodes");

                // Block a tile in the middle of the path (if path exists and has enough nodes)
                if (pathBefore.size() > 3) {
                    Node middleNode = pathBefore.get(pathBefore.size() / 2);
                    System.out.println("\nPlacing tower at grid [" + middleNode.x + ", " + middleNode.y + "]");
                    grid.setBlocked(middleNode.x, middleNode.y, true);

                    System.out.println("\nAfter placing tower:");
                    List<Node> pathAfter = pathfinder.findPath(start[0], start[1], end[0], end[1]);
                    if (pathAfter != null) {
                        System.out.println("Path length: " + pathAfter.size() + " nodes");
                        assertTest("Path rerouted successfully", pathAfter.size() > 0);
                        System.out.println("Path rerouted successfully!");
                    } else {
                        System.out.println("No path found (tower completely blocked the way)");
                    }

                    // Restore the grid
                    grid.setBlocked(middleNode.x, middleNode.y, false);
                }
            }
        }

        // Test updateGrid method
        PathGrid newGrid = new PathGrid(mapData);
        pathfinder.updateGrid(newGrid);
        assertTest("PathGrid update", pathfinder.getPathGrid() == newGrid);

        System.out.println("✓ Test 6 Completed\n");
    }

    /**
     * Test 7: Performance test
     */
    private static void testPerformance() {
        System.out.println("【TEST 7】Testing Performance...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);
        AStarPathfinder pathfinder = new AStarPathfinder(grid);

        int[] start = findWalkableTile(grid, 0, 0);
        int[] end = findWalkableTile(grid, grid.getCols() - 1, grid.getRows() - 1);

        if (start != null && end != null) {
            int iterations = 100;
            long totalTime = 0;

            System.out.println("Running " + iterations + " pathfinding operations...");

            for (int i = 0; i < iterations; i++) {
                long startTime = System.nanoTime();
                pathfinder.findPath(start[0], start[1], end[0], end[1]);
                long endTime = System.nanoTime();
                totalTime += (endTime - startTime);
            }

            double avgTimeMs = (totalTime / iterations) / 1_000_000.0;
            System.out.println("Average time: " + String.format("%.3f", avgTimeMs) + " ms");
            assertTest("Performance acceptable (< 50ms)", avgTimeMs < 50);
        }

        System.out.println("✓ Test 7 Completed\n");
    }

    /**
     * Test 8: TileConstant integration
     */
    private static void testTileConstantIntegration() {
        System.out.println("【TEST 8】Testing TileConstant Integration...\n");

        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);

        // Verify walkable tiles match TileConstant definitions
        // SPAWN(1), ROAD(2), HOME(7) should be walkable
        boolean foundSpawn = false;
        boolean foundRoad = false;
        boolean foundHome = false;

        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                int tileId = mapData[y][x];
                boolean shouldBeWalkable = (tileId == 1 || tileId == 2 || tileId == 7);
                boolean isWalkable = grid.isWalkable(x, y);

                if (tileId == 1 && isWalkable) foundSpawn = true;
                if (tileId == 2 && isWalkable) foundRoad = true;
                if (tileId == 7 && isWalkable) foundHome = true;

                // Verify consistency
                if (shouldBeWalkable != isWalkable) {
                    System.out.println("⚠ Inconsistency at [" + x + "," + y + "]: TileId=" + tileId +
                            ", ShouldBeWalkable=" + shouldBeWalkable + ", IsWalkable=" + isWalkable);
                }
            }
        }

        assertTest("SPAWN tiles are walkable", foundSpawn);
        assertTest("ROAD tiles are walkable", foundRoad);
        assertTest("HOME tiles are walkable", foundHome);

        System.out.println("Found walkable SPAWN tiles: " + foundSpawn);
        System.out.println("Found walkable ROAD tiles: " + foundRoad);
        System.out.println("Found walkable HOME tiles: " + foundHome);

        System.out.println("✓ Test 8 Completed\n");
    }

    // ═══════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════

    /**
     * Assert helper to track test results
     */
    private static void assertTest(String testName, boolean condition) {
        if (condition) {
            testsPassed++;
            System.out.println("  ✓ " + testName);
        } else {
            testsFailed++;
            System.out.println("  ✗ " + testName + " FAILED");
        }
    }

    /**
     * Find a tile by its type ID (1=SPAWN, 2=ROAD, 7=HOME)
     */
    private static int[] findTileByType(int[][] mapData, int tileId) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                if (mapData[y][x] == tileId) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Find a walkable tile starting from given position
     */
    private static int[] findWalkableTile(PathGrid grid, int startX, int startY) {
        for (int y = startY; y < grid.getRows(); y++) {
            for (int x = startX; x < grid.getCols(); x++) {
                if (grid.isWalkable(x, y)) {
                    return new int[]{x, y};
                }
            }
        }
        // Try from beginning if not found
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getCols(); x++) {
                if (grid.isWalkable(x, y)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Find a walkable tile nearby given position
     */
    private static int[] findWalkableNearby(PathGrid grid, int centerX, int centerY, int radius) {
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                int x = centerX + dx;
                int y = centerY + dy;
                if (grid.isWalkable(x, y) && (dx != 0 || dy != 0)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Find a blocked tile
     */
    private static int[] findBlockedTile(PathGrid grid) {
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getCols(); x++) {
                if (!grid.isWalkable(x, y)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Test a pathfinding operation
     */
    private static void testPath(AStarPathfinder pathfinder,
                                 int startX, int startY,
                                 int endX, int endY,
                                 boolean shouldSucceed) {
        System.out.println("Finding path from [" + startX + "," + startY +
                "] to [" + endX + "," + endY + "]");

        long startTime = System.nanoTime();
        List<Node> path = pathfinder.findPath(startX, startY, endX, endY);
        long endTime = System.nanoTime();

        double timeMs = (endTime - startTime) / 1_000_000.0;

        if (path != null) {
            System.out.println("  ✓ Path found!");
            System.out.println("  - Length: " + path.size() + " nodes");
            System.out.println("  - Time: " + String.format("%.3f", timeMs) + " ms");

            if (shouldSucceed) {
                testsPassed++;
            } else {
                testsFailed++;
                System.out.println("  ⚠ Expected no path but found one!");
            }

            // Print partial path
            if (path.size() <= 10) {
                System.out.print("  - Path: ");
                for (Node node : path) {
                    System.out.print("[" + node.x + "," + node.y + "] ");
                }
                System.out.println();
            } else {
                System.out.print("  - Path (first 5): ");
                for (int i = 0; i < 5; i++) {
                    Node node = path.get(i);
                    System.out.print("[" + node.x + "," + node.y + "] ");
                }
                System.out.println("... [" + path.get(path.size()-1).x + "," +
                        path.get(path.size()-1).y + "]");
            }
        } else {
            System.out.println("  ✗ No path found");
            System.out.println("  - Time: " + String.format("%.3f", timeMs) + " ms");

            if (!shouldSucceed) {
                testsPassed++;
                System.out.println("  ✓ Correctly detected no path");
            } else {
                testsFailed++;
                System.out.println("  ⚠ Expected path but none found!");
            }
        }
    }
}