package Button;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TowerButton extends BaseButton {
    private int id;
    private Image img;

    public TowerButton(Image img, double x, double y, double width, double height, int id) {
        super(x, y, width, height, "");
        this.id = id;
        this.img = img;
    }

    @Override
    public void render(GraphicsContext gc) {
        // Draw tower image
        if (img != null) {
            gc.drawImage(img, x, y, width, height);
        }
        drawBorder(gc);
    }

    private void drawBorder(GraphicsContext gc) {
        // Draw outer border (always black)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
        
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
}
