package Helper;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Notification {
    private String notification = null;
    private long notificationEndTime = 0;

    public void update() {
        if (System.currentTimeMillis() > notificationEndTime) {
            notification = null; // delete when time out
        }
    }

    public void show(String text, long durationMs) {
        this.notification = text;
        this.notificationEndTime = System.currentTimeMillis() + durationMs;
    }

    public void draw(GraphicsContext gc) {
        if (notification == null) return;

        gc.setFill(Color.YELLOW);
        Font font = Font.font("Arial", FontWeight.BOLD, 32);
        gc.setFont(font);

        // Measure text width using Text node
        Text textNode = new Text(notification);
        textNode.setFont(font);

        int msgWidth = (int) textNode.getLayoutBounds().getWidth();
        int x = (1504 - msgWidth) / 2;
        int y = 350;

        gc.fillText(notification, x-50, y);
    }
}
