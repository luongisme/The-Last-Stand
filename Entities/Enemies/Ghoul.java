package Entities.Enemies;

import Constant.EntityConstant;

public class Ghoul extends Enemy {
    public Ghoul(float x, float y) {
        super(x, y, EntityConstant.GHOUL.getId(),200);
        setRewardGold(25);
    }
}
