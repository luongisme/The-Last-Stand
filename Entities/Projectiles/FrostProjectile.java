package Entities.Projectiles;

import Entities.AnimationEffects.ElectroEffect;
import Entities.AnimationEffects.ImpactEffect;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;

public class FrostProjectile extends Projectile {
    private float slowDuration;

    public FrostProjectile(float x, float y, float speed, int damage,
                           int projectileType, int towerLevel, int maxFrames,
                           Enemy target, ImpactStrategy strategy, EnemyManager em,
                           float slowDuration, ImpactProfile profile) { // <--- Nhận thêm tham số này

        // Gọi constructor của lớp cha
        super(x, y, speed, damage, projectileType, towerLevel, maxFrames, target, strategy, em, profile);
        this.slowDuration = slowDuration;
    }

    public float getSlowDuration() { return slowDuration; }

    public ImpactEffect createImpactEffect() {
//        return new ElectroEffect(this.x, this.y, projectileType, maxFrames, towerLevel, impactProfile);
        return null;
    }
}