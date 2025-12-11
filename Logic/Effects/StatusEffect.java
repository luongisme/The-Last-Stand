package Logic.Effects;

import Entities.Enemies.Enemy;
import javafx.scene.canvas.GraphicsContext;

public abstract class StatusEffect {
    protected float duration;
    protected boolean active = true;
    protected String id;

    public StatusEffect(float duration, String id) {
        this.duration = duration;
        this.id = id;
    }

    public void applyResistance(float tenacity) {
        this.duration = this.duration * (1.0f - tenacity);
    }

    public void update(float dt, Enemy enemy) {
        duration -= dt;
        if (duration <= 0) {
            active = false;
            onEnd(enemy);
        } else {
            onTick(dt, enemy);
        }
    }

    public abstract void onStart(Enemy enemy);
    public abstract void onTick(float dt, Enemy enemy);
    public abstract void onEnd(Enemy enemy);
    public void draw(GraphicsContext gc, float x, float y, float width, float height) {}

    public boolean isActive() { return active; }
    public String getId() { return id; }
}