package Entities.Towers;

import Constant.TowerConstant;
import Entities.AnimationEffects.ImpactProfile;
import Entities.Enemies.Enemy;
import Entities.Projectiles.CannonProjectile;
import Entities.Projectiles.Projectile;
import Logic.Strategies.AnemoImpact;
import Logic.Strategies.ImpactStrategy;
import Managers.EnemyManager;
import Managers.ProjectileManager;

public class Cannon extends Tower{
    public Cannon(int x, int y, int id, EnemyManager em, ProjectileManager pm) {
        super(x, y, id, TowerConstant.CANNON, em, pm);
    }

    @Override
    public ImpactStrategy getImpactStrategy() {
        // Cannon dùng đạn Anemo: Đẩy lùi + Sát thương lan
        return new AnemoImpact(getExplosionRadius());
    }

    @Override
    protected void setupFrameCounts() {
        this.idleFrames = 3;
        this.attackFrames = 10;
        this.shootFrame = 8;
    }

    public float getExplosionRadius() {
        // Level 1: 60, Level 2: 80, Level 3: 100
        return 60.0f + (getLevel() - 1) * 20.0f;
    }

    @Override
    public ImpactProfile getImpactProfile() {
        // Logic tính toán thông số dựa trên Level
        float radius = getExplosionRadius();
        float size = radius * 1.5f; // Hình nổ to gấp đôi bán kính

        // Tốc độ nổ: Level cao nổ nhanh
        int speed = 8 - (getLevel() * 2); // 6,4,2

        // Tạo Profile: Size động
        return new ImpactProfile(size, speed);
    }

    @Override
    protected Projectile createSpecificProjectile(float x, float y, float speed, int maxFrames, int lvl, Enemy target) {
        return new CannonProjectile(x, y, speed, damage,
                                    towerType.ordinal(), lvl, maxFrames, target,
                                    getImpactStrategy(), enemyManager, getExplosionRadius(), getImpactProfile());
    }
}
