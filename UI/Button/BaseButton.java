package UI.Button;

import javafx.scene.canvas.GraphicsContext;

public abstract class BaseButton {
    protected double x, y, width, height;
    protected String text;
    protected boolean mouseOver = false;
    protected boolean mousePressed = false;

    public BaseButton(double x, double y, double width, double height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public abstract void render(GraphicsContext gc);

    public void update(int mouseX, int mouseY) {
        mouseOver = contains(mouseX, mouseY);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return contains(mouseX, mouseY);
    }
    
    private boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= x + width && 
               pointY >= y && pointY <= y + height;
    }

    public void setMousePressed(boolean pressed) {
        this.mousePressed = pressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
