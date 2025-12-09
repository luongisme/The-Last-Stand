package Constant;

import Entities.Enemies.*;

public enum EntityConstant {

    GOBLIN(0, "goblin.png", 32, 32, 50, 0.5f, 5, 0.0f),
    SKELETON(1, "skeleton.png", 32, 32, 80, 0.7f, 1, 0.3f),
    GOBLIN_BOSS(2, "goblin_boss.png", 32, 32, 500, 0.3f, 50, 0.7f);

    private final int id;
    private final String spriteName;
    private final int frameW;
    private final int frameH;

    private final int maxHealth;
    private final float speed;
    private final int reward;
    private final float tenacity;

    EntityConstant(int id, String spriteName, int fw, int fh,
                   int maxHealth, float speed, int reward, float tenacity) {
        this.id = id;
        this.spriteName = spriteName;
        this.frameW = fw;
        this.frameH = fh;
        this.maxHealth = maxHealth;
        this.speed = speed;
        this.reward = reward;
        this.tenacity = tenacity;
    }

    public int getId() { return id; }
    public String getSpriteName() { return spriteName; }
    public int getFrameW() { return frameW; }
    public int getFrameH() { return frameH; }

    public int getMaxHealth() { return maxHealth; }
    public float getSpeed() { return speed; }
    public int getReward() { return reward; }

    public float getTenacity() { return tenacity; }

    public Enemy createEnemy(float x, float y) {
        return switch (this) {
            case GOBLIN -> new Goblin(x, y);
            case SKELETON -> new Skeleton(x, y);
            case GOBLIN_BOSS -> new GoblinBoss(x, y);
        };
    }
}

