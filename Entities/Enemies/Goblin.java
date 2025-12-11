package Entities.Enemies;

import Constant.EntityConstant;

public class Goblin extends Enemy {
    public Goblin(float x, float y) {
        super(x, y, EntityConstant.GOBLIN.getId(),200);
        setRewardGold(15);
    }
}
