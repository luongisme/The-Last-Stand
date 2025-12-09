package Managers;

import Entities.AnimationEffects.*;
import Entities.Projectiles.*;
import Helper.LoadImages.LoadProjectileImages;
import Scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Iterator;

public class ProjectileManager {
    private Playing playing;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<ImpactEffect> effects = new ArrayList<>(); // Class ImpactEffect bạn tự tạo tương tự hiệu ứng xây
    private LoadProjectileImages spriteLoader;

    private int projId = 0;

    public ProjectileManager(Playing playing) {
        this.playing = playing;
        this.spriteLoader = new LoadProjectileImages();
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    private void spawnImpactEffect(Projectile p) {
        // GỌI HÀM CỦA ĐẠN: Đa hình sẽ tự chạy vào CannonProjectile.createImpactEffect()
        ImpactEffect effect = p.createImpactEffect();

        if (effect != null) {
            effects.add(effect);
        }
    }

    public void update() {
        // 1. Update Projectiles
        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile p = it.next();
            p.update(); // Logic bay và va chạm đã nằm trong class Projectile

            if (!p.isActive()) {
                spawnImpactEffect(p);
                it.remove();
            }
        }

        // 2. Update Effects (Nổ)
        Iterator<ImpactEffect> itEffect = effects.iterator();
        while (itEffect.hasNext()) {
            ImpactEffect e = itEffect.next();
            e.update();
            if (!e.isActive()) {
                itEffect.remove();
            }
        }
    }

    public void draw(GraphicsContext gc) {
        // 1. Vẽ Đạn
        for (Projectile p : projectiles) {
            Image img = spriteLoader.getProjectile(p.getProjectileType(), p.getTowerLevel(), p.getAnimationIndex());
            if (img != null) {
                float drawSize = p.getDrawSize();
                if (drawSize == -1) drawSize = (float) img.getWidth();
                drawRotatedImage(gc, img, p.getRotation(), p.getX(), p.getY(), drawSize);
            }
        }

        // 2. Vẽ Hiệu ứng Nổ (Impact) - ĐÃ BỔ SUNG
        for (ImpactEffect e : effects) {
            e.draw(gc, spriteLoader);
        }
    }

    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy, double size) {
        gc.save();
        gc.translate(tlpx, tlpy);
        gc.rotate(angle);
        gc.drawImage(image, -size/2, -size/2, size, size);
        gc.restore();
    }

    public int getMaxFrames(int towerType, int level) {
        return spriteLoader.getProjectileFrameCount(towerType, level);
    }
}