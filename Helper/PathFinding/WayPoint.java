package Helper.PathFinding;


import Constant.WayPointType;

/**
 *point in the grid used for pathfinding and navigation.
 */
public class WayPoint {

    private int gridX;
    private int gridY;
    private WayPointType type;
    private String name;


    public WayPoint(int gridX, int gridY, WayPointType type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;
        this. name = type.toString() + "[" + gridX + "," + gridY + "]";
    }


    public WayPoint(int gridX, int gridY, WayPointType type, String name) {
        this. gridX = gridX;
        this. gridY = gridY;
        this. type = type;
        this.name = name;
    }



    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public WayPointType getType() {
        return type;
    }

    public String getName() {
        return name;
    }


    public float getPixelX(int tileSize) {
        return gridX * tileSize + tileSize / 2.0f;
    }


    public float getPixelY(int tileSize) {
        return gridY * tileSize + tileSize / 2.0f;
    }


    public int distanceTo(WayPoint other) {
        return Math.abs(this.gridX - other.gridX) + Math.abs(this. gridY - other. gridY);
    }

    @Override
    public String toString() {
        return String.format("Waypoint{%s, [%d,%d], %s}", name, gridX, gridY, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WayPoint other = (WayPoint) obj;
        return gridX == other.gridX && gridY == other. gridY;
    }

    @Override
    public int hashCode() {
        return 31 * gridX + gridY;
    }
}
