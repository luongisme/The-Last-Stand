package Helper.PathFinding;

import java.util.List;

/**
 * Controller quản lý pathfinding cho từng enemy
 * Mỗi enemy có một EnemyPathController riêng
 */
public class EnemyPathController {

    private Route route;
    private AStarPathfinder pathfinder;
    private PathGrid pathGrid;
    private int tileSize;

    // Current waypoint index trong route
    private int currentWaypointIndex = 0;

    // Current path (danh sách nodes từ A* algorithm)
    private List<Node> currentPath;
    private int currentPathNodeIndex = 0;

    // Trạng thái
    private boolean pathBlocked = false;
    private boolean reachedDestination = false;

    /**
     * Constructor
     * @param route Route mà enemy sẽ đi theo
     * @param pathfinder A* pathfinder instance
     * @param pathGrid Grid để kiểm tra walkable
     * @param tileSize Kích thước mỗi tile (pixels)
     */
    public EnemyPathController(Route route, AStarPathfinder pathfinder,
                               PathGrid pathGrid, int tileSize) {
        this.route = route;
        this.pathfinder = pathfinder;
        this.pathGrid = pathGrid;
        this.tileSize = tileSize;

        // Bắt đầu từ waypoint đầu tiên (spawn point)
        this.currentWaypointIndex = 0;

        // Tính đường đến waypoint tiếp theo
        calculatePathToNextWaypoint();
    }

    /**
     * Tính đường đi từ vị trí hiện tại đến waypoint tiếp theo
     */
    private void calculatePathToNextWaypoint() {
        if (currentWaypointIndex >= route.getWayPoints().size() - 1) {
            // Đã đến waypoint cuối cùng (base)
            reachedDestination = true;
            return;
        }

        WayPoint from = route.getWayPointAt(currentWaypointIndex);
        WayPoint to = route.getWayPointAt(currentWaypointIndex + 1);

        if (from == null || to == null) {
            pathBlocked = true;
            return;
        }

        currentPath = pathfinder.findPath(
            from.getGridX(), from.getGridY(),
            to.getGridX(), to.getGridY()
        );

        if (currentPath == null || currentPath.isEmpty()) {
            pathBlocked = true;
            System.err.println("Path blocked from [" + from.getGridX() + "," + from.getGridY() +
                             "] to [" + to.getGridX() + "," + to.getGridY() + "]");
        } else {
            pathBlocked = false;
            currentPathNodeIndex = 0;
        }
    }

    /**
     * Lấy vị trí target tiếp theo (pixel coordinates)
     * @return float[] {x, y} hoặc null nếu không có target
     */
    public float[] getNextTargetPosition() {
        if (reachedDestination || pathBlocked || currentPath == null) {
            return null;
        }

        if (currentPathNodeIndex >= currentPath.size()) {
            return null;
        }

        Node targetNode = currentPath.get(currentPathNodeIndex);
        float pixelX = pathGrid.toPixelCenterX(targetNode.x);
        float pixelY = pathGrid.toPixelCenterY(targetNode.y);

        return new float[]{pixelX, pixelY};
    }

    /**
     * Chuyển sang node tiếp theo trong path
     */
    public void advanceToNextNode() {
        if (currentPath == null) return;

        currentPathNodeIndex++;

        // Đã hết path hiện tại?
        if (currentPathNodeIndex >= currentPath.size()) {
            // Chuyển sang waypoint tiếp theo
            currentWaypointIndex++;
            calculatePathToNextWaypoint();
        }
    }

    /**
     * Recalculate path từ vị trí grid hiện tại
     * Được gọi khi có tower mới đặt xuống
     */
    public void recalculatePath(int currentGridX, int currentGridY) {
        if (reachedDestination) return;

        // Tìm waypoint tiếp theo
        int nextWaypointIndex = currentWaypointIndex + 1;
        if (nextWaypointIndex >= route.getWayPoints().size()) {
            // Đã gần đến base, tính đường trực tiếp
            WayPoint base = route.getBasePoint();
            if (base != null) {
                currentPath = pathfinder.findPath(
                    currentGridX, currentGridY,
                    base.getGridX(), base.getGridY()
                );
                currentPathNodeIndex = 0;
                pathBlocked = (currentPath == null || currentPath.isEmpty());
            }
            return;
        }

        WayPoint nextWaypoint = route.getWayPointAt(nextWaypointIndex);
        if (nextWaypoint == null) {
            pathBlocked = true;
            return;
        }

        currentPath = pathfinder.findPath(
            currentGridX, currentGridY,
            nextWaypoint.getGridX(), nextWaypoint.getGridY()
        );

        if (currentPath == null || currentPath.isEmpty()) {
            pathBlocked = true;
        } else {
            pathBlocked = false;
            currentPathNodeIndex = 0;
        }
    }

    /**
     * Kiểm tra đã đến đích cuối cùng (base) chưa
     */
    public boolean hasReachedDestination() {
        return reachedDestination;
    }

    /**
     * Kiểm tra đường có bị chặn không
     */
    public boolean isPathBlocked() {
        return pathBlocked;
    }

    /**
     * Lấy route hiện tại
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Lấy waypoint index hiện tại
     */
    public int getCurrentWaypointIndex() {
        return currentWaypointIndex;
    }

    /**
     * Lấy path hiện tại (danh sách nodes)
     */
    public List<Node> getCurrentPath() {
        return currentPath;
    }

    /**
     * Debug: In thông tin path
     */
    public void printPathInfo() {
        System.out.println("=== EnemyPathController Info ===");
        System.out.println("Route: " + route.getRouteName());
        System.out.println("Current Waypoint Index: " + currentWaypointIndex);
        System.out.println("Path Blocked: " + pathBlocked);
        System.out.println("Reached Destination: " + reachedDestination);
        if (currentPath != null) {
            System.out.println("Current Path Nodes: " + currentPath.size());
            System.out.println("Current Path Node Index: " + currentPathNodeIndex);
        }
    }
}

