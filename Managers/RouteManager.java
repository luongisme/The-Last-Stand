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
                    System.err.println("Waypoint not walkable: " + wp + " in route " + route.getRouteName());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Alias for validateRouteOnGrid (for compatibility)
     */
    public boolean validateRoutesOnGrid(PathGrid grid) {
        return validateRouteOnGrid(grid);
    }

    //ROUTE FOR MAP 1
    public void initializeDefaultRoutesForMap1() {
        clearRoutes();

        // ═══════════════════════════════════════════
        // ROUTE 1: North Lane (Đường phía trên)
        // ═══════════════════════════════════════════
        createRoute("North Lane")
                .addSpawn(1, 35)
                .addCheckPoint(35, 35)
                .addCheckPoint(31,27)
                .addCheckPoint(31, 14)
                .addCheckPoint(51,8)
                .addCheckPoint(51, 28)
                .addCheckPoint(75, 34)
                .addBase(56, 45);

        // ═══════════════════════════════════════════
        // ROUTE 2: Middle Lane (Đường giữa)
        // ═══════════════════════════════════════════
        createRoute("Middle Lane")
                .addSpawn(1, 37)
                .addCheckPoint(37, 37)
                .addCheckPoint(33, 17)
                .addCheckPoint(46, 10)
                .addCheckPoint(49, 28)
                .addCheckPoint(70, 31)
                .addCheckPoint(70,38)
                .addCheckPoint(55,40)
                .addBase(54, 45);

        // ═══════════════════════════════════════════
        // ROUTE 3: South Lane (Đường phía dưới)
        // ═══════════════════════════════════════════
        createRoute("South Lane")
                .addSpawn(1, 39)
                .addCheckPoint(37, 39)
                .addCheckPoint(45,13)
                .addBase(52, 45);

    }

    // ROUTE FOR MAP 2
    public void initializeDefaultRoutesForMap2() {
        clearRoutes();

        // ═══════════════════════════════════════════
        // ROUTE 1: Left North Lane (Đường trái trên) DONE
        // ═══════════════════════════════════════════
        createRoute("Left North Lane")
                .addSpawn(50, 1)
                .addCheckPoint(50,6)
                .addCheckPoint(29,6)
                .addCheckPoint(19,15)
                .addCheckPoint(19,23)
                .addCheckPoint(22,27)
                .addCheckPoint(26,31)
                .addCheckPoint(30,35)
                .addCheckPoint(34, 39)
                .addBase(34, 45);

        // ═══════════════════════════════════════════
        // ROUTE 2: Middle Lane (Đường trái dưới) DONE
        // ═══════════════════════════════════════════
        createRoute("Left South Lane")
                .addSpawn(52, 1)
                .addBase(36, 45);

        // ═══════════════════════════════════════════
        // ROUTE 3: South Lane (Đường Phải Trên)
        // ═══════════════════════════════════════════
        createRoute("North Right Lane")
                .addSpawn(56, 1)
                .addCheckPoint(88,24)
                .addBase(38, 45);

        // ═══════════════════════════════════════════
        // ROUTE 4: South Lane (Đường Phải Dưới)
        // ═══════════════════════════════════════════
        createRoute("South Right Lane")
                .addSpawn(54, 1)
                .addCheckPoint(54,8)
                .addCheckPoint(73,10)
                .addCheckPoint(79,16)
                .addCheckPoint(83,24)
                .addCheckPoint(79,31)
                .addCheckPoint(69,37)
                .addCheckPoint(39,37)
                .addBase(39, 45);

    }

    // ROUTE FOR MAP 3
    public void initializeDefaultRoutesForMap3() {
        clearRoutes();

        // ═══════════════════════════════════════════
        // ROUTE 1: North Lane (Đường phía trên)
        // ═══════════════════════════════════════════
        createRoute("North Lane")
                .addSpawn(1, 35)
                .addBase(56, 45);

        // ═══════════════════════════════════════════
        // ROUTE 2: Middle Lane (Đường giữa)
        // ═══════════════════════════════════════════
        createRoute("Middle Lane")
                .addSpawn(1, 37)
                .addBase(54, 45);

        // ═══════════════════════════════════════════
        // ROUTE 3: South Lane (Đường phía dưới)
        // ═══════════════════════════════════════════
        createRoute("South Lane")
                .addSpawn(1, 39)
                .addBase(52, 45);

    }


}
