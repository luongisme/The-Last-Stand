package Entities.Towers;

import Constant.TowerConstant;

import Managers.EnemyManager;
import Managers.ProjectileManager;

import Entities.Enemies.Enemy;
import Entities.Projectiles.Projectile;
import Entities.Projectiles.PoisonProjectile;
import Entities.AnimationEffects.ImpactProfile;

import Logic.Strategies.ImpactStrategy;
import Logic.Strategies.ToxoImpact;

public class Poison extends Tower{
    public Poison(int x, int y, int id, EnemyManager em, ProjectileManager pm){
        super(x, y, id, TowerConstant.POISON, em, pm);
    }

    @Override
    public ImpactStrategy getImpactStrategy() {
        // Poison dùng đạn Toxo: Gây độc theo thời gian
        return new ToxoImpact(getPoisonDuration(), getDamagePerTick());
    }

    @Override
    protected void setupFrameCounts() {
        this.idleFrames = 8;
        this.attackFrames = 29;
        this.shootFrame = 25;
    }

    public float getPoisonDuration() {
        // Level 1: 3s, Level 2: 4s, Level 3: 5s
        return 3.0f + (getLevel() - 1) * 1.0f;
    }

    public int getDamagePerTick() {
        // Level 1: 25 damage/s, Level 2: 30 damage/s, Level 3: 35 damage/s
        return 20 + (getLevel() * 5);
    }

    @Override
    public ImpactProfile getImpactProfile() {
        // Poison dùng size ảnh gốc (-1, -1), không lệch, tốc độ 5
        int animSpeed = 7 - (getLevel() - 1); // Lv1: 7, Lv2: 6, Lv3: 5
        return new ImpactProfile(animSpeed);
    }

    @Override
    protected Projectile createSpecificProjectile(float x, float y, float speed, int maxFrames, int lvl, Enemy target) {
        return new PoisonProjectile(x, y, speed, damage,
                                    towerType.ordinal(), lvl, maxFrames, target,
                                    getImpactStrategy(), enemyManager, getPoisonDuration(), getImpactProfile());
    }
}
