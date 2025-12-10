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
    private final int skillType; // 0=WaterSplash, 1=SandStone, 2=ThunderBolt, 3=WaterStrike

    // Delay before dealing damage (for ThunderBolt)
    private final double damageDelay; // Delay in seconds before damage is dealt
    private double totalElapsed; // Total time elapsed since skill was cast

    public Skill(Image[] frames, double frameDuration,
                 double x, double y, double width, double height,
                 double radius, int damage, int skillType) {

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
        this.skillType = skillType;
        this.hasDealtDamage = false;

        // ThunderBolt (skillType 2) has 1 second delay, others deal damage immediately
        this.damageDelay = (skillType == 2) ? 0.5 : 0.0;
        this.totalElapsed = 0.0;

        this.elapsed = 0;
        this.currentFrame = 0;


    }

    public void update(double dt) {
        if (finished) return;

        totalElapsed += dt;
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

    public int getSkillType() {
        return skillType;
    }

    public boolean hasDealtDamage() {
        return hasDealtDamage;
    }

    public void setHasDealtDamage(boolean hasDealtDamage) {
        this.hasDealtDamage = hasDealtDamage;
    }

    // Check if enough time has passed to deal damage
    public boolean isReadyToDealDamage() {
        return totalElapsed >= damageDelay;
    }
}

