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
            // Dame sốc lớn
            target.hurt(damage * 2);
            // Gắn hiệu ứng Choáng
            target.applyStatus(new StunEffect(duration)); // Choáng 2s (Boss sẽ tự giảm còn 0.6s)
        }
    }
}
