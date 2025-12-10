package Constant;

import Entities.Enemies.*;

public enum EntityConstant {

    GOBLIN(0, "goblin.png", 32, 32),
    SKELETON(1, "skeleton.png", 32, 32),
    GHOUL(2, "ghoul.png", 32, 32),
    GOBLIN_BOSS(3, "goblin_boss.png", 32, 32);

    private final int id;
    private final String spriteName;
    private final int frameW;
    private final int frameH;

    EntityConstant(int id, String spriteName, int fw, int fh) {
        this.id = id;
        this.spriteName = spriteName;
        this.frameW = fw;
        this.frameH = fh;
    }

    public int getId() { return id; }
    public String getSpriteName() { return spriteName; }
    public int getFrameW() { return frameW; }
    public int getFrameH() { return frameH; }

    public Enemy createEnemy(float x, float y) {
        return switch (this) {
            case GOBLIN -> new Goblin(x, y);
            case SKELETON -> new Skeleton(x, y);
            case GOBLIN_BOSS -> new GoblinBoss(x, y);
            case GHOUL -> new Ghoul(x, y);
        };
    }
}

