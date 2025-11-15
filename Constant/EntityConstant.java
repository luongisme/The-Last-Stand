package Constant;

import Entities.Enemies.*;

public enum EntityConstant {
    GOBLIN(0, "goblin.png"),
    SKELETON(1, "skeleton.png"),
    GOBLIN_BOSS(2, "goblin_boss.png");

    private final int id;
    private final String spriteName;

    EntityConstant(int id, String spriteName) {
        this.id = id;
        this.spriteName = spriteName;
    }

    public int getId() { return id; }
    public String getSpriteName() { return spriteName; }

    public Enemy createEnemy(float x, float y) {
        switch (this) {
            case GOBLIN: return new Goblin(x, y);
            case SKELETON: return new Skeleton(x, y);
            case GOBLIN_BOSS: return new GoblinBoss(x, y);
        }
        return null;
    }
}

