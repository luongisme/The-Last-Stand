package Managers;

import Helper.PathFinding.PathGrid;
import Helper.PathFinding.Route;
import Helper.PathFinding.WayPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouteManager {
    private final List<Route> routes;
    private final Random random;
    private int nextRouteID;

    private static RouteManager instance;

    public RouteManager() {
        this.routes = new java.util.ArrayList<>();
        this.random = new Random();
        this.nextRouteID = 1;
    }
    public static RouteManager getInstance() {
        if (instance == null) {
            instance = new RouteManager();
        }
        return instance;
    }
    public Route createRoute(String routeName) {
        Route route = new Route(nextRouteID++, routeName);
        routes.add(route);
        return route;
    }
    public void addRoute(Route route) {
        routes.add(route);
    }
    public void removeRoute(int index) {
        if(index >= 0 && index < routes.size()) {
            routes.remove(index);
        }
    }

    public Route getRandomRoute() {
        if (routes.isEmpty()) {
            return null;
        }
        int index = random.nextInt(routes.size());
        return routes.get(index);
    }

    public Route getRouteByName(String routeName) {
        for (Route route : routes) {
            if (route.getRouteName().equals(routeName)) {
                return route;
            }
        }
        return null;
    }

    public Route getRouteForEnemy(int enemyLevel) {
        if (routes.isEmpty()) {
            return null;
        }
        int index = enemyLevel % routes.size();
        return routes.get(index);
    }

    public List<Route> getAllRoutes() {
        return new ArrayList<>(routes);
    }

    public int getRouteCount() {
        return routes.size();
    }

    public void clearRoutes() {
        routes.clear();
        nextRouteID = 1;
    }

    public boolean validateAllRoutes() {
        for (Route route : routes) {
            if(!route.isValid()){
                System.out.println("Invalid route: " + route.getRouteName());
                return false;
            }
        }
        return true;
    }

    public boolean validateRouteOnGrid(PathGrid grid){
        for (Route route : routes) {
            for (WayPoint wp : route.getWayPoints()) {
                if (!grid.isWalkable(wp.getGridX(), wp.getGridY())) {
                    System.err.println("Waypoint not walkable: " + wp + " in route " + route. getRouteName());
                    return false;
                }
            }
        }
        return true;
    }
    public void initializeDefaultRoutesForMap1() {
        clearRoutes();

        // ═══════════════════════════════════════════
        // ROUTE 1: North Lane (Đường phía trên)
        // ═══════════════════════════════════════════
        createRoute("North Lane")
                .addSpawn(0, 35)           // Spawn ở bên trái
                .addCheckPoint(39, 35)
                .addCheckPoint(33, 17)
                .addCheckPoint(51, 12)
                .addCheckPoint(51, 28)
                .addCheckPoint(74, 36)
                .addBase(56, 45);          // Base (điều chỉnh theo vị trí base thực tế)

        // ═══════════════════════════════════════════
        // ROUTE 2: Middle Lane (Đường giữa)
        // ═══════════════════════════════════════════
        createRoute("Middle Lane")
                .addSpawn(0, 37)           // Spawn ở giữa-trái
                .addCheckPoint(39, 37)
                .addCheckPoint(33, 17)
                .addCheckPoint(51, 12)
                .addCheckPoint(51, 28)
                .addCheckPoint(74, 36)     // Checkpoint 2
                .addBase(54, 45);          // Base

        // ═══════════════════════════════════════════
        // ROUTE 3: South Lane (Đường phía dưới)
        // ═══════════════════════════════════════════
        createRoute("South Lane")
                .addSpawn(0, 39)           // Spawn khu vực tile 9 (đất)
                .addCheckPoint(39, 37)
                .addCheckPoint(33, 17)
                .addCheckPoint(51, 12)
                .addCheckPoint(51, 28)
                .addCheckPoint(74, 36)     // Checkpoint 3
                .addBase(52, 45);          // Base

        System.out.println("Initialized " + routes.size() + " routes for Map 1");
        printAllRoutes();
    }

    public void printAllRoutes() {

        for (Route route : routes) {
            System.out.println("\n" + route.getRouteName() + ":");
            System.out.println("  Valid: " + route. isValid());
            System.out.println("  Total distance: " + route.getTotalDistance());
            System.out.println("  Waypoints:");

            for (int i = 0; i < route.getWayPoints().size(); i++) {
                WayPoint wp = route.getWayPointAt(i);
                System.out.println("    " + i + ". " + wp);
            }
        }

        System.out. println("\n═══════════════════════════════════════════\n");
    }
}
