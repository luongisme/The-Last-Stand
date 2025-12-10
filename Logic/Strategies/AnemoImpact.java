package Logic.Strategies;

import Entities.Enemies.Enemy;
import Managers.EnemyManager;
import java.util.List;

public class AnemoImpact implements ImpactStrategy {
    private float radius; // Biến lưu trữ bán kính

    // Constructor nhận bán kính từ bên ngoài (Clean & Flexible)
    public AnemoImpact(float radius) {
        this.radius = radius;
    }

    @Override
    public void onHit(Enemy target, float x, float y, int damage, EnemyManager em) {
        // 1. Sát thương mục tiêu chính (Single Target)
        // Mục tiêu bị trúng đạn trực tiếp sẽ chịu 100% damage gốc
        if (target != null) {
            target.hurt(damage);
        }

        // Lấy danh sách quái nằm trong vùng nổ (Hitbox tròn)
        List<Enemy> neighbors = em.getEnemiesInRange(x, y, radius);

        for (Enemy e : neighbors) {
            // Có thể bỏ qua mục tiêu chính nếu không muốn nó bị dính thêm dame lan (tùy design)
            if (e == target) continue;

            // Tính khoảng cách từ Tâm Nổ (x,y) -> Tâm Quái (e.centerX, e.centerY)
            float dx = e.getCenterX() - x;
            float dy = e.getCenterY() - y;

            // Dùng Math.sqrt để lấy khoảng cách thực tế (Distance)
            float dist = (float) Math.sqrt(dx*dx + dy*dy);

            // A. Công thức Sát thương giảm dần (Linear Falloff)
            // Nếu dist = 0 (ngay tâm) -> damageFactor = 1 (100% dame)
            // Nếu dist = radius (rìa) -> damageFactor = 0 (0% dame)
            float damageFactor = 1.0f - (dist / radius);

            // Đảm bảo damage không âm
            if (damageFactor < 0) damageFactor = 0;

            float nerfFactor = 0.5f;

            int aoeDamage = (int) (damage * damageFactor * nerfFactor);

            if (aoeDamage > 0) {
                e.hurt(aoeDamage);
            }
        }
    }
}