package Entities.Projectiles;

import Entities.AnimationEffects.AnemoEffect;
import Entities.AnimationEffects.ImpactEffect;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;

public class CannonProjectile extends Projectile {

    private float explosionRadius;

    public CannonProjectile(float x, float y, float speed, int damage,
                            int projectileType, int towerLevel, int maxFrames,
                            Enemy target, ImpactStrategy strategy, EnemyManager em,
                            float explosionRadius, ImpactProfile profile) { // <--- Nhận thêm tham số này

        // Gọi constructor của lớp cha
        super(x, y, speed, damage, projectileType, towerLevel, maxFrames, target, strategy, em, profile);

        this.explosionRadius = explosionRadius;
    }

    public float getExplosionRadius() {
        return explosionRadius;
    }

    @Override
    public float getDrawSize() {
        return 32f;
    }

    public ImpactEffect createImpactEffect() {
        // CannonProjectile tự biết tạo AnemoEffect
        return new AnemoEffect(this.x, this.y, projectileType, maxFrames, towerLevel, impactProfile);
    }
}