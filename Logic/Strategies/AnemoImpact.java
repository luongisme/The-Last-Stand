package Logic.Strategies;

import Entities.Enemies.Enemy;
import Logic.Effects.SlowEffect;
import Managers.EnemyManager;
import java.util.List;

public class AnemoImpact implements ImpactStrategy {
    private float radius, duration, slowPercent;

    public AnemoImpact(float radius, float duration, float slowPercent) {
        this.radius = radius;
        this.duration = duration;
        this.slowPercent = slowPercent;
    }

    @Override
    public void onHit(Enemy target, float x, float y, int damage, EnemyManager em) {
        if (target != null) {
            target.hurt(damage);
            target.applyStatus(new SlowEffect(duration, slowPercent));
        }

        List<Enemy> neighbors = em.getEnemiesInRange(x, y, radius);

        for (Enemy e : neighbors) {
            if (e == target) continue;

            float dx = e.getCenterX() - x;
            float dy = e.getCenterY() - y;

            float dist = (float) Math.sqrt(dx*dx + dy*dy);
            float damageFactor = 1.0f - (dist / radius);

            if (damageFactor < 0) damageFactor = 0;

            float nerfFactor = 0.5f;
            int aoeDamage = (int) (damage * damageFactor * nerfFactor);

            if (aoeDamage > 0) {
                e.hurt(aoeDamage);
            }
        }
    }
}