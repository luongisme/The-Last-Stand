package Entities.Towers;

import Constant.TowerConstant;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Entities.Projectiles.ElectroProjectile;
import Entities.Projectiles.Projectile;
import Logic.Strategies.ElectroImpact;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;
import Managers.ProjectileManager;

public class Electro extends Tower{
    public Electro(int x, int y, int id, EnemyManager em, ProjectileManager pm) {
        super(x, y, id, TowerConstant.ELECTRO, em, pm);
    }

    @Override
    public ImpactStrategy getImpactStrategy() {
        // Gây Stun/Đóng băng
        return new ElectroImpact(getSlowDuration());
    }

    @Override
    protected void setupFrameCounts() {
        // Electro has a number of frames that change according to level
        // Level 1, 2, 3 -> Index 0, 1, 2
        int[] electroIdle = {10, 16, 20};
        int[] electroAttack = {16, 17, 19};

        int idx = getLevel() - 1;
        if (idx < 0) idx = 0;
        if (idx > 2) idx = 2;

        this.idleFrames = electroIdle[idx];
        this.attackFrames = electroAttack[idx];
        this.shootFrame = 9;
    }

    public float getSlowDuration() {
        // Level 1: 1.5s, Level 2: 2.0s, Level 3: 2.5s
        return 1.5f + (getLevel() - 1) * 0.5f;
    }

    @Override
    public ImpactProfile getImpactProfile() {
        int speed = 6 - (getLevel() - 1);
        float size = 72;
        return new ImpactProfile(size, speed);
    }

    @Override
    protected Projectile createSpecificProjectile(float x, float y, float speed, int maxFrames, int lvl, Enemy target) {
        return new ElectroProjectile(x, y, speed, damage,
                                    towerType.ordinal(), lvl, maxFrames, target,
                                    getImpactStrategy(), enemyManager, getImpactProfile());
    }
}
