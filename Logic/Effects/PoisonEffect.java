package Logic.Effects;

import Entities.Enemies.Enemy;

public class PoisonEffect extends StatusEffect {
    private int damagePerTick;
    private float tickRate = 1.0f;
    private float timeSinceLastTick = 0;

    public PoisonEffect(float duration, int damagePerTick) {
        super(duration, "POISON");
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onStart(Enemy enemy) {
        // Có thể thêm logic giảm giáp hoặc đổi màu tại đây nếu muốn
    }

    @Override
    public void onTick(float dt, Enemy enemy) {
        // Độc gây sát thương mỗi chu kỳ (Logic nằm ở đây là chuẩn nhất)
        timeSinceLastTick += dt;
        if (timeSinceLastTick >= tickRate) {
            timeSinceLastTick = 0;
            enemy.hurt(damagePerTick);
        }
    }

    @Override
    public void onEnd(Enemy enemy) {
        // Dọn dẹp nếu cần
    }
}