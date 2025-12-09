package Logic.Effects;

import Entities.Enemies.Enemy;

public class SlowEffect extends StatusEffect {
    private float slowPercent; // 0.3 = 30%

    public SlowEffect(float duration, float slowPercent) {
        super(duration, "SLOW");
        this.slowPercent = slowPercent;
    }

    @Override
    public void onStart(Enemy enemy) {
        // Tác động ngay khi dính hiệu ứng
        enemy.addSlow(slowPercent);
    }

    @Override
    public void onTick(float dt, Enemy enemy) {
        // Slow không cần làm gì mỗi tick (vì speed đã giảm rồi)
    }

    @Override
    public void onEnd(Enemy enemy) {
        // Hoàn trả lại tốc độ khi hết hiệu ứng
        enemy.removeSlow(slowPercent);
    }
}