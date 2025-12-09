package Entities.Enemies;

import Constant.EntityConstant;

public class Goblin extends Enemy {
    public Goblin(float x, float y) {
        super(x, y, EntityConstant.GOBLIN.getId(),
                EntityConstant.GOBLIN.getMaxHealth(),
                EntityConstant.GOBLIN.getSpeed(),
                EntityConstant.GOBLIN.getTenacity()); // <--- Truyền thêm cái này
    }


    @Override
    public void move(float dt) {
        // Giả sử logic đi theo đường (Pathfinding)
        // Khi di chuyển, bạn phải cập nhật hướng:

        float oldX = x;
        float oldY = y;

        // ... Code di chuyển thay đổi x, y ...

        // CẬP NHẬT LAST DIR ĐỂ VẼ ĐÚNG HƯỚNG
        if (x > oldX) {
            this.lastDir = RIGHT;
        } else if (x < oldX) {
            this.lastDir = LEFT;
        } else if (y > oldY) {
            this.lastDir = DOWN;
        } else if (y < oldY) {
            this.lastDir = UP;
        }
    }
}
