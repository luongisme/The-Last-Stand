package Helper.Debug;

import Helper.PathFinding.Route;
import Helper.PathFinding.WayPoint;
import Managers.RouteManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;


public class WaypointDebugRenderer {
    private boolean enabled = false;
    private boolean showCoordinates = true;
    private boolean showConnections = true;

    private static final int TILE_SIZE = 16;

    // Màu cho từng route
    private static final Color[] ROUTE_COLORS = {
        Color.RED,      // North Lane
        Color.BLUE,     // Middle Lane
        Color.GREEN     // South Lane
    };

    public void render(GraphicsContext gc) {
        if (!enabled) {
            return;
        }

        System.out.println("[WaypointDebugRenderer] Rendering waypoints..."); // DEBUG

        RouteManager routeManager = RouteManager.getInstance();
        List<Route> routes = routeManager.getAllRoutes();

        System.out.println("[WaypointDebugRenderer] Routes count: " + routes.size()); // DEBUG

        if (routes.isEmpty()) {
            System.out.println("[WaypointDebugRenderer] No routes to render!"); // DEBUG
            return;
        }

        // Vẽ tile highlights cho tất cả routes
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            Color routeColor = ROUTE_COLORS[i % ROUTE_COLORS.length];
            renderRouteOnTiles(gc, route, routeColor, i);
        }

        // Vẽ debug panel
        renderDebugPanel(gc, routes.size());
        System.out.println("[WaypointDebugRenderer] Render complete!"); // DEBUG
    }

    /**
     * Vẽ highlight cho các tiles trong route
     */
    private void renderRouteOnTiles(GraphicsContext gc, Route route, Color baseColor, int routeIndex) {
        List<WayPoint> waypoints = route.getWayPoints();
        if (waypoints.isEmpty()) return;

        // Vẽ đường nối trước (nếu bật)
        if (showConnections) {
            drawConnections(gc, waypoints, baseColor);
        }

        // Vẽ tile highlights
        for (int i = 0; i < waypoints.size(); i++) {
            WayPoint wp = waypoints.get(i);
            highlightTile(gc, wp, i, waypoints.size(), baseColor);
        }
    }

    /**
     * Highlight một tile với màu và style phù hợp
     */
    private void highlightTile(GraphicsContext gc, WayPoint wp, int index, int total, Color baseColor) {
        int tileX = wp.getGridX() * TILE_SIZE;
        int tileY = wp.getGridY() * TILE_SIZE;

        // Chọn màu và độ trong suốt dựa vào loại waypoint
        Color fillColor;
        Color strokeColor;
        double lineWidth;

        if (index == 0) {
            // Spawn point - Màu vàng nổi bật
            fillColor = Color.rgb(255, 255, 0, 0.5);
            strokeColor = Color.YELLOW;
            lineWidth = 3;
        } else if (index == total - 1) {
            // Base - Màu đỏ đậm
            fillColor = Color.rgb(255, 0, 0, 0.6);
            strokeColor = Color.RED;
            lineWidth = 3;
        } else {
            // Checkpoint - Màu theo route
            int r = (int)(baseColor.getRed() * 255);
            int g = (int)(baseColor.getGreen() * 255);
            int b = (int)(baseColor.getBlue() * 255);
            fillColor = Color.rgb(r, g, b, 0.4);
            strokeColor = baseColor;
            lineWidth = 2;
        }

        // Tô màu tile
        gc.setFill(fillColor);
        gc.fillRect(tileX, tileY, TILE_SIZE, TILE_SIZE);

        // Vẽ viền tile
        gc.setStroke(strokeColor);
        gc.setLineWidth(lineWidth);
        gc.strokeRect(tileX + 0.5, tileY + 0.5, TILE_SIZE - 1, TILE_SIZE - 1);

        // Vẽ số thứ tự ở giữa tile
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        gc.setLineWidth(2);

        String label;
        if (index == 0) {
            label = "S";  // Spawn
        } else if (index == total - 1) {
            label = "B";  // Base
        } else {
            label = String.valueOf(index);  // Checkpoint number
        }

        // Vẽ text ở giữa tile
        double textX = tileX + TILE_SIZE / 2.0 - 3;
        double textY = tileY + TILE_SIZE / 2.0 + 4;
        gc.strokeText(label, textX, textY);
        gc.fillText(label, textX, textY);

        // Vẽ grid coordinates (nếu bật)
        if (showCoordinates) {
            String coords = "(" + wp.getGridX() + "," + wp.getGridY() + ")";
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 9));
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2.5);

            // Vẽ coordinates bên cạnh tile
            double coordX = tileX + TILE_SIZE + 2;
            double coordY = tileY + TILE_SIZE / 2.0 + 3;
            gc.strokeText(coords, coordX, coordY);
            gc.fillText(coords, coordX, coordY);
        }
    }

    /**
     * Vẽ đường nối giữa các waypoints
     */
    private void drawConnections(GraphicsContext gc, List<WayPoint> waypoints, Color color) {
        if (waypoints.size() < 2) return;

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.setGlobalAlpha(0.6);

        for (int i = 0; i < waypoints.size() - 1; i++) {
            WayPoint current = waypoints.get(i);
            WayPoint next = waypoints.get(i + 1);

            // Vẽ line từ center của tile này đến tile kế tiếp
            double x1 = current.getGridX() * TILE_SIZE + TILE_SIZE / 2.0;
            double y1 = current.getGridY() * TILE_SIZE + TILE_SIZE / 2.0;
            double x2 = next.getGridX() * TILE_SIZE + TILE_SIZE / 2.0;
            double y2 = next.getGridY() * TILE_SIZE + TILE_SIZE / 2.0;

            gc.strokeLine(x1, y1, x2, y2);

            // Vẽ mũi tên nhỏ ở giữa
            drawArrow(gc, x1, y1, x2, y2);
        }

        gc.setGlobalAlpha(1.0);
    }

    /**
     * Vẽ mũi tên chỉ hướng
     */
    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        double midX = (x1 + x2) / 2;
        double midY = (y1 + y2) / 2;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        double arrowSize = 6;
        double angle1 = angle + Math.PI * 0.75;
        double angle2 = angle - Math.PI * 0.75;

        double ax1 = midX + arrowSize * Math.cos(angle1);
        double ay1 = midY + arrowSize * Math.sin(angle1);
        double ax2 = midX + arrowSize * Math.cos(angle2);
        double ay2 = midY + arrowSize * Math.sin(angle2);

        gc.strokeLine(midX, midY, ax1, ay1);
        gc.strokeLine(midX, midY, ax2, ay2);
    }

    /**
     * Vẽ debug info panel
     */
    private void renderDebugPanel(GraphicsContext gc, int routeCount) {
        // Background
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(10, 10, 200, 100);

        // Border
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);
        gc.strokeRect(10, 10, 200, 100);

        // Title
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.fillText("WAYPOINT DEBUG", 20, 28);

        // Info
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        gc.fillText("Press F3 to toggle", 20, 45);
        gc.fillText("Routes: " + routeCount, 20, 62);

        // Legend
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        // Spawn
        gc.setFill(Color.rgb(255, 255, 0, 0.5));
        gc.fillRect(20, 72, 12, 12);
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);
        gc.strokeRect(20, 72, 12, 12);
        gc.setFill(Color.WHITE);
        gc.fillText("S = Spawn", 37, 82);

        // Base
        gc.setFill(Color.rgb(255, 0, 0, 0.6));
        gc.fillRect(20, 90, 12, 12);
        gc.setStroke(Color.RED);
        gc.strokeRect(20, 90, 12, 12);
        gc.setFill(Color.WHITE);
        gc.fillText("B = Base", 37, 100);

        // Checkpoint
        gc.setFill(Color.rgb(0, 0, 255, 0.4));
        gc.fillRect(115, 72, 12, 12);
        gc.setStroke(Color.BLUE);
        gc.strokeRect(115, 72, 12, 12);
        gc.setFill(Color.WHITE);
        gc.fillText("# = Check", 132, 82);

        // Colors
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(115, 92, 127, 92);
        gc.setFill(Color.WHITE);
        gc.fillText("Route Color", 132, 95);
    }

    // Toggle methods
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println("Waypoint Debug: " + (enabled ? "ENABLED" : "DISABLED"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setShowCoordinates(boolean show) {
        this.showCoordinates = show;
    }

    public void setShowConnections(boolean show) {
        this.showConnections = show;
    }
}

