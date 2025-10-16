package UI.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MenuButton extends BaseButton {

    public MenuButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    @Override
    public void render(Graphics g) {
        
        if (mousePressed) {
            g.setColor(new Color(255, 255, 255, 80)); 
        } else if (mouseOver) {
            g.setColor(new Color(255, 255, 255, 50)); 
        } else {
            g.setColor(new Color(255, 255, 255, 20)); 
        }
        g.fillRect(x, y, width, height);

        // Button border
        if (mouseOver) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(new Color(255, 255, 255, 150));
        }
        g.drawRect(x, y, width, height);

        // Button text
        g.setColor(Color.WHITE); 
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textHeight = g.getFontMetrics().getHeight();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight / 2) / 2;
        g.drawString(text, textX, textY);
    }
}
