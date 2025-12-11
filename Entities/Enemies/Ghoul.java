package Entities.Enemies;

import Constant.EntityConstant;

public class Ghoul extends Enemy {
    public Ghoul(float x, float y) {
        super(x, y, EntityConstant.GHOUL.getId(),300);
        setRewardGold(25);
    }
}
