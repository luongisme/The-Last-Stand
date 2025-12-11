package Logic.Strategies;

import Entities.Enemies.Enemy;
import Logic.Effects.StunEffect;
import Managers.EnemyManager;

public class ElectroImpact implements ImpactStrategy {
    private float duration;

    public ElectroImpact(float duration) {
        this.duration = duration;
    }
    @Override
    public void onHit(Enemy target, float x, float y, int damage, EnemyManager em) {
        if (target != null) {
            target.hurt(damage);
            target.applyStatus(new StunEffect(duration));
        }
    }
}
