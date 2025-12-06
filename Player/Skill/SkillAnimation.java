package Player.Skill;



import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SkillAnimation {

    private final Image[] frames;       // toàn bộ frame animation
    private final double frameDuration; // 1 frame chạy bao lâu (giây)
    private double elapsed;       // thời gian tích lũy
    private int currentFrame;
    private boolean finished;

    private final double x;
    private final double y;          // vị trí vẽ
    private final double width;
    private final double height; // scale

    public SkillAnimation(Image[] frames, double frameDuration,
                          double x, double y, double width, double height) {

        if (frames == null || frames.length == 0) {
            System.err.println("❌ SkillAnimation: frames is null or empty!");
            this.frames = new Image[0];
            this.finished = true;
        } else {
            System.out.println("🎬 SkillAnimation created with " + frames.length + " frames");
            this.frames = frames;
            this.finished = false;
        }

        this.frameDuration = frameDuration;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.elapsed = 0;
        this.currentFrame = 0;


    }

    public void update(double dt) {
        if (finished) return;

        elapsed += dt;

        if (elapsed >= frameDuration) {
            elapsed -= frameDuration;
            currentFrame++;

            System.out.println("🎬 Frame changed: " + currentFrame + "/" + frames.length + " (elapsed: " + elapsed + "s)");

            if (currentFrame >= frames.length) {
                finished = true;
                currentFrame = frames.length - 1;
                System.out.println("🎬 Animation finished!");
            }
        }
    }

    public void render(GraphicsContext gc) {

        // Draw current frame
        Image frame = frames[currentFrame];
        if (frame == null) {
            System.err.println("❌ Frame " + currentFrame + " is NULL!");
            return;
        }

        gc.drawImage(frame, x, y, width, height);
    }

    public boolean isFinished() {
        return finished;
    }
}

