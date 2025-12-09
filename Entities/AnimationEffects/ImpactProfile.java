package Entities.AnimationEffects;

public class ImpactProfile {
    private float width;
    private float height;
    private float offsetX;
    private float offsetY;
    private int animSpeed;

    // Constructor đầy đủ
    public ImpactProfile(float width, float height, float offsetX, float offsetY, int animSpeed) {
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.animSpeed = animSpeed;
    }

    public ImpactProfile(float width, float height, int animSpeed) {
        this(width, height, 0, 0, animSpeed);
    }

    public ImpactProfile(float size, int animSpeed) {
        this(size, size, 0, 0, animSpeed);
    }

    // Constructor mặc định (nếu muốn dùng size gốc của ảnh)
    public ImpactProfile(int animSpeed) {
        this(-1, -1, 0, 0, animSpeed);
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }
    public int getAnimSpeed() { return animSpeed; }
}