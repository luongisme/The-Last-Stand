package UI.Button;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TowerButton extends BaseButton {
    private int id;
    private BufferedImage img;

    public TowerButton(BufferedImage img, int x, int y, int width, int height, int id) {
        super(x, y, width, height,"");
        this.id = id;
        this.img = img;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, x, y, width,height,null);
        drawBorder(g);
    }

    private void drawBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        if (mousePressed) {
            g.setColor(Color.BLACK);
            g.drawRect(x + 1, y + 1, width - 2, height - 2);
        } else if (mouseOver) {
            g.setColor(Color.WHITE);
            g.drawRect(x, y, width - 1, height - 1);
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