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
        enemy.addSlow(slowPercent);
    }

    @Override
    public void onTick(float dt, Enemy enemy) {
    }

    @Override
    public void onEnd(Enemy enemy) {
        enemy.removeSlow(slowPercent);
    }
}