package Managers.Tower;

import java.util.ArrayList;

import Entities.Towers.TowerState;
import Helper.LoadImages.LoadTowerImages;
import Managers.EnemyManager;
import Managers.ProjectileManager;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import Constant.TowerConstant;
import Entities.Towers.Tower;
import Scenes.Playing;
import Helper.Notification;

public class TowerManager {
    private Playing playing;
    private Notification notification;
    private TowerMenuManager menu;
    private LoadTowerImages sprite;

    private Tower[][] towerMap; // [MapHeight][MapWidth]
    private ArrayList<Tower> towers = new ArrayList<>();

    private int towerAmount = 0;
    private TowerConstant[] towerTypes = TowerConstant.values();

    private final int TILE_SIZE = 16;

    public TowerManager(Playing playing) {
        this.playing = playing;
        this.sprite = new LoadTowerImages();
        this.notification = new Notification();
        this.menu = new TowerMenuManager(playing,this,this.notification);
        int rows = playing.getLvlData().length;
        int cols = playing.getLvlData()[0].length;
        towerMap = new Tower[rows][cols];
    }

    public void update() {
        for (int i = 0; i < towers.size(); i++) {
            Tower t = towers.get(i);
            t.update();

            // delete tower after destroy effect run complete
            if (t.isFinishedCollapsing()) {
                removeTowerFromMap(t);
                towers.remove(i);
                i--;
            }
        }
        notification.update();
        menu.update();
    }

    public void draw(GraphicsContext gc) {
        for (Tower t : towers) {
            int towerType = t.getTowerType().ordinal();
            TowerConstant typeEnum = t.getTowerType();
            int level = t.getLevel() - 1;
            TowerState currentState = t.getCurrentState();

            switch (currentState) {
                case SELLING:
                    drawCollapseEffect(gc, t);
                    break;
                case CONSTRUCTING:
                    drawConstructing(gc, level, t);
                    break;
                case COMPLETED: // just done - draw completed effect
                    drawBase(gc, towerType, level, t);
                    drawWeapon(gc, towerType, level, false, t, typeEnum);
                    drawCompletedEffect(gc, t);
                    menu.closeUpgradeMenu();
                    break;
                case ACTIVE:
                    drawBase(gc, towerType, level, t);
                    drawWeapon(gc, towerType, level, t.isAttacking(), t, typeEnum);
                    break;
            }
            if (isHovering(t)) {
                drawRange(gc, t);
            }
        }
        menu.draw(gc);
        notification.draw(gc);
    }

    private void drawBase(GraphicsContext gc, int towerType, int level, Tower tower) {
        Image base = sprite.getTowerBase(towerType, level);
        if (base != null) {
            gc.drawImage(base, tower.getX() - 11, tower.getY() - 58, 38, 76);
        }
    }

    private void drawWeapon(GraphicsContext gc, int towerType, int level, boolean isAttacking, Tower tower, TowerConstant typeEnum) {
        int animIndex = tower.getAnimationIndex();
        Image weapon = sprite.getTowerWeapon(towerType, level, animIndex, isAttacking);

        if (weapon != null) {
            int offsetX = typeEnum.getWeaponOffsetX(level);
            int offsetY = typeEnum.getWeaponOffsetY(level);
            int width = typeEnum.getWeaponWidth(level);
            int height = typeEnum.getWeaponHeight(level);

            gc.drawImage(weapon,
                    tower.getX() - offsetX, // X - Offset X
                    tower.getY() - offsetY, // Y - Offset Y
                    width,
                    height
            );
        }
    }

    private void drawCompletedEffect(GraphicsContext gc, Tower tower) {
        Image smoke = sprite.getConstructionCompleted(tower.getCompletedIndex());
        if (smoke != null) {
            gc.drawImage(smoke, tower.getX() - 51, tower.getY() - 115, 118, 184);
        }
    }

    private void drawCollapseEffect(GraphicsContext gc, Tower tower) {
        Image smoke = sprite.getCollapse(tower.getSellIndex());
        if (smoke != null) {
            gc.drawImage(smoke, tower.getX() - 51, tower.getY() - 115, 118, 184);
        }
    }

    private void drawConstructing(GraphicsContext gc, int level, Tower tower) {
        // drawing image "Scaffolding" / "Construction site"
        Image constructionImg = sprite.getConstruction(level, tower.getConstructionIndex());

        if (constructionImg != null) {
            gc.drawImage(constructionImg, tower.getX() - 48, tower.getY() - 108, 110, 170);
        }
    }

    private void drawRange(GraphicsContext gc, Tower t) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1); // Hoặc 2 cho dễ nhìn

        // 1. ĐỒNG BỘ TÂM (Center Sync)
        // Trong Tower.java bạn dùng (x + 16, y + 16) để tính khoảng cách bắn.
        // Nên khi vẽ vòng tròn, tâm cũng phải nằm chính xác ở đó.
        float centerX = t.getX() + 16;
        float centerY = t.getY() + 16;

        // 2. SỬA LỖI RADIUS vs DIAMETER
        // t.getRange() trả về BÁN KÍNH (Radius - khoảng cách từ tâm ra rìa).
        // Hàm strokeOval của JavaFX yêu cầu width/height là ĐƯỜNG KÍNH (Diameter).
        float range = t.getRange(); // Đây là bán kính
        float diameter = range * 2; // Đường kính = Bán kính * 2

        // 3. VẼ
        // strokeOval vẽ từ góc trên-trái của hình vuông bao quanh hình tròn
        // Tọa độ vẽ = Tâm - Bán kính
        gc.strokeOval(
                centerX - range, // X
                centerY - range, // Y
                diameter,        // Width
                diameter         // Height
        );
    }

    // Hàm kiểm tra xem chuột có đang đè lên tháp không (Cần biến mouseX, mouseY từ Playing)
    private boolean isHovering(Tower t) {
        // Lấy tọa độ chuột pixel thực tế (không phải grid)
        int mx = playing.getRawMouseX();
        int my = playing.getRawMouseY();

        // Kích thước vùng check dựa trên hàm drawBase:
        // x - 11, y - 58, w = 38, h = 76
        int imgX = t.getX() - 11;
        int imgY = t.getY() - 58;
        int imgW = 38;
        int imgH = 76;

        return mx >= imgX && mx <= imgX + imgW &&
                my >= imgY && my <= imgY + imgH;
    }

    public Tower getTowerAtPixel(int x, int y) {
        // Duyệt ngược từ cuối danh sách để ưu tiên tháp vẽ sau (nằm trên) nếu có chồng lấn
        for (int i = towers.size() - 1; i >= 0; i--) {
            Tower t = towers.get(i);

            if (t.isSelling()) continue;

            // Logic Hitbox giống hệt isHovering
            int imgX = t.getX() - 11;
            int imgY = t.getY() - 58;
            int imgW = 38;
            int imgH = 76;

            if (x >= imgX && x <= imgX + imgW &&
                    y >= imgY && y <= imgY + imgH) {
                return t;
            }
        }
        return null;
    }
     
    public void addTower(TowerConstant towerConstant, int xPos, int yPos) {
        // 1. take cost of tower
        int cost = towerConstant.getCost(1);
        // 2. check player money
        if (playing.getPlayer().getMoney() >= cost) {
            EnemyManager em = playing.getEnemyManager();
            ProjectileManager pm = playing.getProjectileManager();

            Tower newTower = towerConstant.createTower(xPos, yPos, towerAmount++, em, pm);
            if (newTower != null) {
                // 3. pay
                playing.getPlayer().spendMoney(cost);
                towers.add(newTower);

                int gridX = xPos / TILE_SIZE;
                int gridY = yPos / TILE_SIZE;
                if (isValidIndex(gridX, gridY)) {
                    towerMap[gridY][gridX] = newTower;
                }
            }
        } else {
            notification.show("Not enough gold to build!",2000);
        }
    }

    public void startSellTower(Tower t) {
        if (t != null) {
            t.startSell();
        }
    }

    public boolean isOccupied(int x, int y) {
        // to avoid player try to build a new tower when destroy incompleted
        int gridX = x / TILE_SIZE;
        int gridY = y / TILE_SIZE;
        return isValidIndex(gridX, gridY) && towerMap[gridY][gridX] != null;
    }

    public Tower getTowerAt(int x, int y) {
        int gridX = x / TILE_SIZE;
        int gridY = y / TILE_SIZE;

        if (!isValidIndex(gridX, gridY)) return null;

        Tower t = towerMap[gridY][gridX];
        if (t != null) {
            // cannot click when destroying
            if (t.isSelling()) return null;
            // cannot click when in the building process to max level
            if (t.isUnderConstruction() && t.isMaxLevel()) return null;
            return t;
        }
        return null;
    }

    private void removeTowerFromMap(Tower t) {
        int gridX = t.getX() / TILE_SIZE;
        int gridY = t.getY() / TILE_SIZE;
        if (isValidIndex(gridX, gridY)) {
            towerMap[gridY][gridX] = null;
        }
    }

    private boolean isValidIndex(int x, int y) {
        return x >= 0 && x < towerMap[0].length && y >= 0 && y < towerMap.length;
    }

//    public Image[] getTowerIconsAnimation(int type) {
//        return sprite.getTowerIconArray(type);
//    }
//
//    public Image[] getSellIconsAnimation() {
//        return sprite.getSellIcon();
//    }
//
//    public Image[] getUpgradeIconsAnimation() {
//        return sprite.getUpgradeIcon();
//    }

    public Image getTowerIcon(int type) { return sprite.getTowerIcon(type); }

    public Image getSellIcon() { return sprite.getSellIcon(); }

    public Image getUpgradeIcon() { return sprite.getUpgradeIcon(); }

    public TowerConstant getTowerConstantById(int id) {
        if (id >= 0 && id < towerTypes.length) {
            return towerTypes[id];
        }
        return null; // or return default
    }

    public TowerMenuManager getMenu() { return menu; }
}