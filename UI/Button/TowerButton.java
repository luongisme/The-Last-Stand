package Button;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class TowerButton extends BaseButton {
    private final int id;
    private final Image[] images;

    private int animIndex = 0;
    private int animTick = 0;
    private int animSpeed = 20;

    // for animations
    public TowerButton(Image[] images, double x, double y, double width, double height, int id) {
        super(x, y, width, height, "");
        this.id = id;
        this.images = images;
    }

    // for 1 image
    public TowerButton(Image img, double x, double y, double width, double height, int id) {
        super(x, y, width, height, "");
        this.id = id;
        this.images = new Image[1];
        this.images[0] = img;
    }

    public void update() {
        // If there is only 1 frame (still image), no calculation is needed.
        if (images == null || images.length <= 1) return;

        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= images.length) {
                animIndex = 0;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Draw the current frame based on animIndex
        if (images != null && images.length > 0) {
            // Ensure safe indexing
            if (animIndex >= images.length) animIndex = 0;
            gc.drawImage(images[animIndex], x, y, width, height);
        }
        drawBorder(gc);
    }

    private void drawBorder(GraphicsContext gc) {
//        // Draw outer border (always black)
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(1);
//        gc.strokeRect(x, y, width, height);

        // Draw additional border based on state
        if (mousePressed) {
            // Inner black border when pressed
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x + 1, y + 1, width - 2, height - 2);
        } else if (mouseOver) {
            // White border when mouse over
            gc.setStroke(Color.WHITE);
            gc.strokeRect(x, y, width - 1, height - 1);
        }
    }

    public void resetMouse() {
        this.mouseOver = false;
        this.mousePressed = false;
    }

    public int getId() {
        return id;
    }

    public void setAnimSpeed(int speed) {
        this.animSpeed = speed;
    }
}
