package Button;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuButton extends BaseButton {

    public MenuButton(double x, double y, double width, double height, String text) {
        super(x, y, width, height, text);
    }

    @Override
    public void render(GraphicsContext gc) {
        // Set font - JavaFX font size roughly equivalent to AWT
        Font baseFont = Font.font("Monospaced", FontWeight.BOLD, 60);
        
        // Calculate text dimensions using JavaFX Text node
        Text textNode = new Text(text);
        textNode.setFont(baseFont);
        double textWidth = textNode.getLayoutBounds().getWidth();
        double textHeight = textNode.getLayoutBounds().getHeight();
        
        // Position calculations (keeping same logic as AWT version)
        double textX = textWidth + 130;
        double textY = y + (height + textHeight) / 2;
        
        // Scale up font and adjust position when mouse over or pressed
        if (mouseOver || mousePressed) {
            textX -= 20;
            baseFont = Font.font("Monospaced", FontWeight.BOLD, 80);
            textNode.setFont(baseFont);
        }
        
        gc.setFont(baseFont);
        
        // Text color: darker yellow when pressed
        if (mousePressed) {
            gc.setFill(Color.rgb(200, 150, 0));
        } else {
            gc.setFill(Color.YELLOW);
        }
        
        gc.fillText(text, textX, textY);
    }
}
