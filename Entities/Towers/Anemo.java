package Entities.Towers;

import Constant.TowerConstant;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Entities.Projectiles.AnemoProjectile;
import Entities.Projectiles.Projectile;
import Logic.Strategies.AnemoImpact;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;
import Managers.ProjectileManager;

public class Anemo extends Tower{
    public Anemo(int x, int y, int id, EnemyManager em, ProjectileManager pm) {
        super(x, y, id, TowerConstant.ANEMO, em, pm);
    }

    @Override
    public ImpactStrategy getImpactStrategy() {
        return new AnemoImpact(getExplosionRadius(), getDuration(), getSlowPercent());
    }

    @Override
    protected void setupFrameCounts() {
        this.idleFrames = 3;
        this.attackFrames = 10;
        this.shootFrame = 8;
    }

    private float getExplosionRadius() {
        // Level 1: 60, Level 2: 80, Level 3: 100
        return 60.0f + (getLevel() - 1) * 20.0f;
    }

    private float getDuration() {
        return 2.5f + (getLevel() * 0.5f);
    }

    private float getSlowPercent() {
        // 30%, 40%, 50%
        return 0.2f + (getLevel() * 0.1f);
    }

    @Override
    public ImpactProfile getImpactProfile() {
        float radius = getExplosionRadius();
        float size = radius * 1.5f;
        int speed = 5; // 6,5,4

        return new ImpactProfile(size, speed);
    }

    @Override
    protected Projectile createSpecificProjectile(float x, float y, float speed, int maxFrames, int lvl, Enemy target) {
        return new AnemoProjectile(x, y, speed, damage,
                                    towerType.ordinal(), lvl, maxFrames, target,
                                    getImpactStrategy(), enemyManager, getImpactProfile());
    }
}
