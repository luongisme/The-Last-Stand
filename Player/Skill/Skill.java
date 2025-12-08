package Player.Skill;



import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Skill {

    private final Image[] frames;       // toàn bộ frame animation
    private final double frameDuration; // 1 frame chạy bao lâu (giây)
    private double elapsed;       // thời gian tích lũy
    private int currentFrame;
    private boolean finished;

    private final double x;
    private final double y;          // vị trí vẽ
    private final double width;
    private final double height; // scale

    // For damage calculation
    private final double centerX;
    private final double centerY;
    private final double radius;
    private final int damage;
    private boolean hasDealtDamage; // Flag to ensure damage is dealt only once

    public Skill(Image[] frames, double frameDuration,
                 double x, double y, double width, double height,
                 double radius, int damage) {

        if (frames == null || frames.length == 0) {
            this.frames = new Image[0];
            this.finished = true;
        } else {
            this.frames = frames;
            this.finished = false;
        }

        this.frameDuration = frameDuration;
        this.x = x;
        this.y = y-50;
        this.width = width;
        this.height = height;

        // Calculate center position for collision detection
        this.centerX = x + width / 2.0;
        this.centerY = y - 50 + height / 2.0;
        this.radius = radius;
        this.damage = damage;
        this.hasDealtDamage = false;

        this.elapsed = 0;
        this.currentFrame = 0;


    }

    public void update(double dt) {
        if (finished) return;

        elapsed += dt;

        if (elapsed >= frameDuration) {
            elapsed -= frameDuration;
            currentFrame++;
            if (currentFrame >= frames.length) {
                finished = true;
                currentFrame = frames.length - 1;
            }
        }
    }

    public void render(GraphicsContext gc) {

        // Draw current frame
        Image frame = frames[currentFrame];
        if (frame == null) {
            return;
        }

        gc.drawImage(frame, x, y, width, height);
    }

    public boolean isFinished() {
        return finished;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }

    public int getDamage() {
        return damage;
    }

    public boolean hasDealtDamage() {
        return hasDealtDamage;
    }

    public void setHasDealtDamage(boolean hasDealtDamage) {
        this.hasDealtDamage = hasDealtDamage;
    }
}

