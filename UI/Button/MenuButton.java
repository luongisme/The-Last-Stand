package UI.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MenuButton extends BaseButton {

    public MenuButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

       
        g2.setFont(new Font("Monospaced", Font.BOLD, 60));
        var fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textAscent = fm.getAscent();
        int textDescent = fm.getDescent();
        

  
        int textX = textWidth+130;
        int textY = y + (height + textAscent - textDescent) / 2; 

        
        if (mouseOver || mousePressed) {
            textX-=20;
            g2.setFont(new Font("Monospaced", Font.BOLD, 80));
        }

        // Text color: darker yellow when pressed
        if (mousePressed) {
            g2.setColor(new Color(200, 150, 0));
        } else {
            g2.setColor(Color.YELLOW);
        }
        g2.drawString(text, textX, textY);

        g2.dispose();
    }
}
