package Logic.Strategies;
import Entities.Enemies.Enemy;
import Managers.EnemyManager;

public interface ImpactStrategy {
    void onHit(Enemy target, float x, float y, int damage, EnemyManager enemyManager);
}