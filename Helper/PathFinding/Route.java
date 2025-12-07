package Helper.PathFinding;

import Constant.WayPointType;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a route consisting of multiple waypoints for pathfinding and navigation.
 */
public class Route {
    private  String routeName;
    private List<WayPoint> wayPoints;
    private int routeID;

    public Route(int routeID,String routeName){
        this.routeID=routeID;
        this.routeName=routeName;
        this.wayPoints=new ArrayList<>();
    }

    public Route addWayPoint(int gridX, int gridY, WayPointType type){
        wayPoints.add(new WayPoint(gridX,gridY,type));
        return this;
    }

    public Route addWayPoint(int gridX, int gridY,WayPointType type,String name){
        wayPoints.add(new WayPoint(gridX,gridY,type,name));
        return this;
    }

    public Route addSpawn(int gridX, int gridY){
        wayPoints.add(new WayPoint(gridX,gridY, WayPointType.SPAWN,"Spawn["+gridX+","+gridY+"]"));
        return this;
    }

    public Route addCheckPoint(int gridX, int gridY){
        wayPoints.add(new WayPoint(gridX,gridY, WayPointType.CHECKPOINT,"Checkpoint["+gridX+","+gridY+"]"));
        return this;
    }

    public Route addBase(int gridX, int gridY){
        wayPoints.add(new WayPoint(gridX,gridY, WayPointType.BASE,"Base["+gridX+","+gridY+"]"));
        return this;
    }

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }
    public String getRouteName() {
        return routeName;
    }
    public int getRouteID() {
        return routeID;
    }

    public WayPoint getSpawnPoint(){
        for(WayPoint wp : wayPoints){
            if(wp.getType() == WayPointType.SPAWN){
                return wp;
            }
        }
        //take the first waypoint as spawn if no explicit spawn point is defined
        return wayPoints.isEmpty() ? null: wayPoints.get(0);
    }

    public WayPoint getBasePoint(){
        //loop from the end to find the last base point(better for time trials with multiple bases)
        for(int i=wayPoints.size()-1;i>=0;i--){
            if(wayPoints.get(i).getType() == WayPointType.BASE){
                return wayPoints.get(i);
            }

        }
        //fallback to last waypoint
        return wayPoints.isEmpty() ? null: wayPoints.get(wayPoints.size()-1);
    }

    public WayPoint getWayPointAt(int index){
        if(index<0 || index>=wayPoints.size()){
            return null;
        }
        return wayPoints.get(index);
    }


    // Get the next waypoint in the route given the current index
    public WayPoint getNextWaypoint(int currentIndex) {
        int nextIndex = currentIndex + 1;
        if (nextIndex < wayPoints. size()) {
            return wayPoints.get(nextIndex);
        }
        return null;
    }

    //check the route is valid (has at least spawn and base, and at least 2 waypoints)
    public boolean isValid(){
        boolean hasSpawn=false;
        boolean hasBase=false;
        for(WayPoint wayPoint:wayPoints){
            if(wayPoint.getType()== WayPointType.SPAWN){
                hasSpawn=true;
            }
            if(wayPoint.getType()== WayPointType.BASE){
                hasBase=true;
            }
        }
        return wayPoints.size()>=2 && hasSpawn && hasBase;
    }
    public int getTotalDistance() {
        int total = 0;
        for (int i = 0; i < wayPoints. size() - 1; i++) {
            total += wayPoints.get(i).distanceTo(wayPoints.get(i + 1));
        }
        return total;
    }

    //debugging purpose
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Route{"). append(routeName).append(", waypoints=[");
        for (int i = 0; i < wayPoints.size(); i++) {
            if (i > 0) sb.append(" → ");
            WayPoint wp = wayPoints.get(i);
            sb. append("[").append(wp. getGridX()).append(",").append(wp.getGridY()). append("]");
        }
        sb.append("]}");
        return sb.toString();
    }

}
