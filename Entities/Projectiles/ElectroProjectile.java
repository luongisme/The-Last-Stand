package Entities.Projectiles;

import Entities.AnimationEffects.ImpactEffect;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;

public class ElectroProjectile extends Projectile {
    public ElectroProjectile(float x, float y, float speed, int damage,
                             int projectileType, int towerLevel, int maxFrames, Enemy target,
                             ImpactStrategy strategy, EnemyManager em, ImpactProfile profile) {

        super(x, y, speed, damage, projectileType, towerLevel, maxFrames, target, strategy, em, profile);
    }

    public ImpactEffect createImpactEffect() {
//        return new ElectroEffect(this.x, this.y, projectileType, maxFrames, towerLevel, impactProfile);
        return null;
    }
}