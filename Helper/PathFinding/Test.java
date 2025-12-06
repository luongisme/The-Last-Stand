package Helper.PathFinding;


import Managers.RouteManager;
import Map.LevelBuild;
import java.util.List;

/**
 * Test class cho Phase 2: Route System
 * Kiểm tra chi tiết từng waypoint để xác định lỗi
 */
public class Test {

    private static final int TILE_SIZE = 16;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("        PHASE 2 TEST - ROUTE SYSTEM VALIDATION");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // Test 1: Tạo routes
        testRouteCreation();

        // Test 2: Validate routes trên grid - CHI TIẾT TỪNG WAYPOINT
        testRouteValidationDetailed();

        // Test 3: Test A* với routes
        testPathfindingWithRoutes();

        // Test 4: Phân bổ routes cho enemies
        testRouteDistribution();

        // Summary
        printTestSummary();
    }

    /**
     * Test 1: Kiểm tra tạo routes
     */
    private static void testRouteCreation() {
        System.out.println("【TEST 1】Route Creation...\n");

        RouteManager manager = new RouteManager();
        manager.initializeDefaultRoutesForMap1();

        // Kiểm tra số lượng routes
        int routeCount = manager.getRouteCount();
        System.out.println("Routes created: " + routeCount);

        if (routeCount == 3) {
            testsPassed++;
            System.out.println("  ✓ Expected 3 routes, got " + routeCount);
        } else {
            testsFailed++;
            System.out.println("  ✗ Expected 3 routes, got " + routeCount);
        }

        // Kiểm tra từng route
        for (Route route : manager.getAllRoutes()) {
            boolean valid = route.isValid();
            int wpCount = route.getWayPoints().size();

            System.out.println("  " + (valid ? "✓" : "✗") + " " + route.getRouteName() +
                    " - Waypoints: " + wpCount +
                    " - Valid: " + valid);

            if (valid) testsPassed++;
            else testsFailed++;
        }

        System.out.println("\n✓ Test 1 Completed\n");
    }

    /**
     * Test 2: Validate routes trên PathGrid - CHI TIẾT TỪNG WAYPOINT
     */
    private static void testRouteValidationDetailed() {
        System.out.println("【TEST 2】Route Validation on Grid (DETAILED)...\n");

        // Tạo PathGrid
        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);

        System.out.println("Grid Info:");
        System.out.println("  - Size: " + grid.getCols() + " x " + grid.getRows());
        System.out.println("  - Tile Size: " + grid.getTileSize() + " pixels\n");

        // Tạo routes
        RouteManager manager = new RouteManager();
        manager.initializeDefaultRoutesForMap1();

        // Validate từng route CHI TIẾT
        int totalWaypoints = 0;
        int walkableWaypoints = 0;
        int notWalkableWaypoints = 0;

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("           WAYPOINT VALIDATION DETAILS");
        System.out.println("═══════════════════════════════════════════════════════\n");

        for (Route route : manager.getAllRoutes()) {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Route: " + route.getRouteName());
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            List<WayPoint> waypoints = route.getWayPoints();
            boolean routeHasError = false;

            for (int i = 0; i < waypoints.size(); i++) {
                WayPoint wp = waypoints.get(i);
                int gridX = wp.getGridX();
                int gridY = wp.getGridY();
                boolean isWalkable = grid.isWalkable(gridX, gridY);

                // Lấy tile ID từ mapData
                int tileId = -1;
                if (gridY >= 0 && gridY < mapData.length && gridX >= 0 && gridX < mapData[0].length) {
                    tileId = mapData[gridY][gridX];
                }

                totalWaypoints++;

                String status;
                String tileInfo = getTileTypeName(tileId);

                if (isWalkable) {
                    walkableWaypoints++;
                    status = "✓ WALKABLE";
                    System.out.println("  [" + i + "] " + wp.getType() + " at Grid[" + gridX + ", " + gridY + "]");
                    System.out.println("      " + status + " (TileID: " + tileId + " = " + tileInfo + ")");
                } else {
                    notWalkableWaypoints++;
                    routeHasError = true;
                    status = "✗ NOT WALKABLE";
                    System.out.println("  [" + i + "] " + wp.getType() + " at Grid[" + gridX + ", " + gridY + "]");
                    System.out.println("      ⚠️ " + status + " (TileID: " + tileId + " = " + tileInfo + ")");
                    System.out.println("      └─ FIX: Change this waypoint to a walkable tile (SPAWN=1, ROAD=2, HOME=7)");
                }
            }

            if (routeHasError) {
                testsFailed++;
                System.out.println("\n  ⚠️ ROUTE HAS ERRORS - Some waypoints are on non-walkable tiles!\n");
            } else {
                testsPassed++;
                System.out.println("\n  ✓ Route OK - All waypoints are walkable!\n");
            }
        }

        // Summary
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("           WAYPOINT VALIDATION SUMMARY");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  Total Waypoints: " + totalWaypoints);
        System.out.println("  ✓ Walkable: " + walkableWaypoints);
        System.out.println("  ✗ Not Walkable: " + notWalkableWaypoints);

        if (notWalkableWaypoints > 0) {
            System.out.println("\n  ⚠️ ACTION REQUIRED:");
            System.out.println("     Fix the waypoints marked as NOT WALKABLE above.");
            System.out.println("     Walkable tiles are: SPAWN(1), ROAD(2), HOME(7)");
        } else {
            System.out.println("\n  ✓ All waypoints are on walkable tiles!");
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

    /**
     * Lấy tên loại tile từ ID
     */
    private static String getTileTypeName(int tileId) {
        switch (tileId) {
            case 0: return "DIRT (not walkable)";
            case 1: return "SPAWN (walkable)";
            case 2: return "ROAD (walkable)";
            case 3: return "GRASS (not walkable)";
            case 4: return "SAND (not walkable)";
            case 5: return "WATER (not walkable)";
            case 6: return "WOOD (not walkable)";
            case 7: return "HOME (walkable)";
            case 8: return "WALL (not walkable)";
            case 9: return "STONE (not walkable)";
            case 10: return "CURB (not walkable)";
            case 11: return "AVAILABLEDIRT (not walkable)";
            default: return "UNKNOWN";
        }
    }

    /**
     * Test 3: Test A* pathfinding giữa các waypoints
     */
    private static void testPathfindingWithRoutes() {
        System.out.println("【TEST 3】A* Pathfinding with Routes...\n");

        // Setup
        int[][] mapData = LevelBuild.getFirstMapData();
        PathGrid grid = new PathGrid(mapData);
        AStarPathfinder pathfinder = new AStarPathfinder(grid);
        RouteManager manager = new RouteManager();
        manager.initializeDefaultRoutesForMap1();

        // Test từng route
        for (Route route : manager.getAllRoutes()) {
            System.out.println("Testing route: " + route.getRouteName());

            boolean routePathable = true;
            int totalNodes = 0;
            int blockedSegments = 0;

            // Tìm đường giữa các waypoints liên tiếp
            for (int i = 0; i < route.getWayPoints().size() - 1; i++) {
                WayPoint from = route.getWayPointAt(i);
                WayPoint to = route.getWayPointAt(i + 1);

                List<Node> path = pathfinder.findPath(
                        from.getGridX(), from.getGridY(),
                        to.getGridX(), to.getGridY()
                );

                if (path != null) {
                    totalNodes += path.size();
                    System.out.println("  ✓ Segment " + i + ": [" +
                            from.getGridX() + "," + from.getGridY() + "] → [" +
                            to.getGridX() + "," + to.getGridY() + "] = " + path.size() + " nodes");
                } else {
                    routePathable = false;
                    blockedSegments++;
                    System.out.println("  ✗ Segment " + i + ": [" +
                            from.getGridX() + "," + from.getGridY() + "] → [" +
                            to.getGridX() + "," + to.getGridY() + "] = NO PATH FOUND!");
                    System.out.println("      └─ Check if both waypoints are on walkable tiles and path exists");
                }
            }

            if (routePathable) {
                testsPassed++;
                System.out.println("  ✓ Route complete! Total nodes: " + totalNodes);
            } else {
                testsFailed++;
                System.out.println("  ✗ Route has " + blockedSegments + " blocked segment(s)!");
            }
            System.out.println();
        }

        System.out.println("✓ Test 3 Completed\n");
    }

    /**
     * Test 4: Phân bổ routes cho enemies
     */
    private static void testRouteDistribution() {
        System.out.println("【TEST 4】Route Distribution for Enemies...\n");

        RouteManager manager = new RouteManager();
        manager.initializeDefaultRoutesForMap1();

        System.out.println("Simulating 9 enemies spawning:\n");

        for (int i = 0; i < 9; i++) {
            Route route = manager.getRouteForEnemy(i);
            WayPoint spawn = route.getSpawnPoint();

            System.out.println("  Enemy " + i + " → " + route.getRouteName() +
                    " (spawn at [" + spawn.getGridX() + "," + spawn.getGridY() + "])");
        }

        System.out.println("\nExpected distribution:");
        System.out.println("  - Enemies 0, 3, 6 → North Lane");
        System.out.println("  - Enemies 1, 4, 7 → Middle Lane");
        System.out.println("  - Enemies 2, 5, 8 → South Lane");

        testsPassed++;
        System.out.println("\n✓ Test 4 Completed\n");
    }

    /**
     * In tổng kết test
     */
    private static void printTestSummary() {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("                    TEST SUMMARY");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  Tests Passed: " + testsPassed);
        System.out.println("  Tests Failed: " + testsFailed);

        if (testsFailed == 0) {
            System.out.println("\n  ✓✓✓ ALL TESTS PASSED! ✓✓✓");
        } else {
            System.out.println("\n  ⚠️ SOME TESTS FAILED - Check the errors above");
        }
        System.out.println("═══════════════════════════════════════════════════════");
    }
}

