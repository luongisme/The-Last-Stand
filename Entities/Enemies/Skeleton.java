package Entities.Enemies;

import Constant.EntityConstant;

public class Skeleton extends Enemy {
    public Skeleton(float x, float y) {
        super(x, y, EntityConstant.SKELETON.getId(),400);
        setRewardGold(30);
    }
}