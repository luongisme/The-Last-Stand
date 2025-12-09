package Entities.Projectiles;

import Entities.AnimationEffects.ImpactEffect;
import Entities.AnimationEffects.ImpactProfile;
import Entities.AnimationEffects.ToxoEffect;
import Entities.Enemies.Enemy;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;

public class PoisonProjectile extends Projectile {
    private float poisonDuration;

    public PoisonProjectile(float x, float y, float speed, int damage,
                            int projectileType, int towerLevel, int maxFrames,
                            Enemy target, ImpactStrategy strategy, EnemyManager em,
                            float poisonDuration, ImpactProfile profile) {
        super(x, y, speed, damage, projectileType, towerLevel, maxFrames, target, strategy, em, profile);
        this.poisonDuration = poisonDuration;
    }

    public float getPoisonDuration() { return poisonDuration; }

    public ImpactEffect createImpactEffect() {
//        return new ToxoEffect(this.x, this.y, projectileType, maxFrames, towerLevel, impactProfile);
        return null;
    }
}