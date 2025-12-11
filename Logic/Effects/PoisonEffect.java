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
    }

    @Override
    public void onTick(float dt, Enemy enemy) {
        timeSinceLastTick += dt;
        if (timeSinceLastTick >= tickRate) {
            timeSinceLastTick = 0;
            enemy.hurt(damagePerTick);
        }
    }

    @Override
    public void onEnd(Enemy enemy) {
    }
}