package Logic.Effects;

import Entities.Enemies.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class StunEffect extends StatusEffect {
    public StunEffect(float duration) {
        super(duration, "STUN");
    }

    @Override
    public void onStart(Enemy enemy) {
        enemy.addStun();
    }

    @Override
    public void onTick(float dt, Enemy enemy) {
    }

    @Override
    public void onEnd(Enemy enemy) {
        enemy.removeStun();
    }

    @Override
    public void draw(GraphicsContext gc, float x, float y, float width, float height) {
        gc.save();
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);

        double centerX = x + width / 2;
        double topY = y - 5;
        double startAngle = (System.currentTimeMillis() / 5) % 360;

        gc.strokeArc(centerX - 8, topY - 10, 16, 16, startAngle, 270, ArcType.OPEN);
        gc.restore();
    }
}