package Entities.AnimationEffects;

import Helper.LoadImages.LoadProjectileImages;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class ImpactEffect {
    protected Point2D pos;
    protected int type; // 0: Anemo, 1: Toxo, 2: Electro
    protected int animIndex = 0;
    protected int animTick = 0;
    protected int animSpeed;
    protected boolean active = true;
    protected int maxFrames;
    protected int towerLevel;
    protected ImpactProfile profile;

    public ImpactEffect(float x, float y, int type, int maxFrames, int towerLevel, ImpactProfile profile) {
        this.pos = new Point2D(x, y);
        this.type = type;
        this.maxFrames = maxFrames;
        this.towerLevel = towerLevel;
        this.profile = profile;
        this.animSpeed = profile.getAnimSpeed();
    }

    public void update() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= maxFrames) {
                active = false;
                animIndex = maxFrames - 1;
            }
        }
    }

    public void draw(GraphicsContext gc, LoadProjectileImages loader) {
        Image img = loader.getImpact(type, towerLevel, animIndex);
        if (img != null) {
            // Lấy thông số từ Profile
            double w = (profile.getWidth() == -1) ? img.getWidth() : profile.getWidth();
            double h = (profile.getHeight() == -1) ? img.getHeight() : profile.getHeight();
            double ox = profile.getOffsetX();
            double oy = profile.getOffsetY();

            gc.drawImage(img,
                    pos.getX() + ox - (w / 2),
                    pos.getY() + oy - (h / 2),
                    w, h);
        }
    }

    public boolean isActive() { return active; }
    public Point2D getPos() { return pos; }
    public int getAnimIndex() { return animIndex; }
    public int getType() { return type; }
    public int getTowerLevel() { return towerLevel; }
}