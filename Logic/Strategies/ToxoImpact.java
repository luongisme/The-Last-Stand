package Logic.Strategies;

import Entities.AnimationEffects.ToxoEffect;
import Entities.Enemies.Enemy;
import Logic.Effects.PoisonEffect;
import Managers.EnemyManager;

public class ToxoImpact implements ImpactStrategy {
    private float duration;
    private int damagePerTick;

    public ToxoImpact(float duration, int damagePerTick) {
        this.duration = duration;
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onHit(Enemy target, float x, float y, int damage, EnemyManager em) {
        if (target != null) {
            // Dame vật lý nhỏ
            target.hurt(damage / 2);
            // Gắn hiệu ứng độc: 5 dame mỗi giây trong 5 giây (tùy chỉ số)
            target.applyStatus(new PoisonEffect(duration, damagePerTick));
        }
    }
}
