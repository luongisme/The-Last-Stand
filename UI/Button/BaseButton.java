package UI.Button;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class BaseButton {
    protected int x, y, width, height;
    protected String text;
    protected Rectangle bounds;
    protected boolean mouseOver = false;
    protected boolean mousePressed = false;

    public BaseButton(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);
    }

    
    public abstract void render(Graphics g);

    
    public void update(int mouseX, int mouseY) {
        mouseOver = bounds.contains(mouseX, mouseY);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return bounds.contains(mouseX, mouseY);
    }

    public void setMousePressed(boolean pressed) {
        this.mousePressed = pressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    
    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, width, height);
    }
}
