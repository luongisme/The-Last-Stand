package Helper;

import Entities.Enemies.Enemy;
import Entities.Towers.Tower;

public class MathUtil {
    // Tính khoảng cách giữa 2 điểm (Pythagoras)
    public static float getDistance(float x1, float y1, float x2, float y2) {
        float xDiff = Math.abs(x1 - x2);
        float yDiff = Math.abs(y1 - y2);
        return (float) Math.hypot(xDiff, yDiff);
    }

    // Tính góc quay để nòng súng hướng về địch (Return độ)
    public static float getAngle(Tower t, Enemy e) {
        float dx = e.getX() - t.getX();
        float dy = e.getY() - t.getY();
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }
}