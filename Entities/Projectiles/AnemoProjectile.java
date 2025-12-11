package Entities.Projectiles;

import Entities.AnimationEffects.AnemoEffect;
import Entities.AnimationEffects.ImpactEffect;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;

public class AnemoProjectile extends Projectile {
    public AnemoProjectile(float x, float y, float speed, int damage,
                           int projectileType, int towerLevel, int maxFrames, Enemy target,
                           ImpactStrategy strategy, EnemyManager em, ImpactProfile profile) {

        super(x, y, speed, damage, projectileType, towerLevel, maxFrames, target, strategy, em, profile);
    }

    @Override
    public float getDrawSize() {
        return 32f;
    }

    public ImpactEffect createImpactEffect() {
        return new AnemoEffect(this.x, this.y, projectileType, maxFrames, towerLevel, impactProfile);
    }
}