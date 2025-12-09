package Logic.Effects;

import Entities.Enemies.Enemy;
import javafx.scene.canvas.GraphicsContext;

public abstract class StatusEffect {
    protected float duration;
    protected boolean active = true;

    // ID để tránh stack trùng hiệu ứng (vd: không bị dính 2 lần độc cùng lúc)
    protected String id;

    public StatusEffect(float duration, String id) {
        this.duration = duration;
        this.id = id;
    }

    // Tính toán thời gian thực tế dựa trên chỉ số kháng của quái (Tenacity)
    public void applyResistance(float tenacity) {
        // tenacity = 0.0 (nhận đủ), 0.7 (giảm 70% thời gian)
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

    public abstract void onStart(Enemy enemy); // Khi hiệu ứng bắt đầu
    public abstract void onTick(float dt, Enemy enemy); // Mỗi frame
    public abstract void onEnd(Enemy enemy); // Khi hết hiệu ứng
    public void draw(GraphicsContext gc, float x, float y, float width, float height) {}

    public boolean isActive() { return active; }
    public String getId() { return id; }
}