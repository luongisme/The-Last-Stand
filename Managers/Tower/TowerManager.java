package Managers.Tower;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

import Constant.TowerConstant;
import Entities.Tower.Tower;
import Scene.Playing;
import Managers.UI.NotificationManager;
import Managers.UI.TowerMenuManager;

public class TowerManager {
    private Playing playing;
    private NotificationManager notification;
    private TowerMenuManager menu;
    private TowerSpriteManager sprite;

    private ArrayList<Tower> towers = new ArrayList<>();
    private int towerAmount = 0;
    private TowerConstant[] towerTypes = TowerConstant.values();

    public TowerManager(Playing playing) {
        this.playing = playing;
        this.sprite = new TowerSpriteManager();
        this.notification = new NotificationManager();
        this.menu = new TowerMenuManager(playing, this,this.notification);
    }

    public void update() {
        for (int i = 0; i < towers.size(); i++) {
            Tower t = towers.get(i);
            t.update();

            // delete tower after destroy effect run complete
            if (t.isFinishedCollapsing()) {
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

            if (t.isSelling()) {
                Image smoke = sprite.getConstructionCompleted(t.getCompletedIndex());
                if (smoke != null) {
                    gc.drawImage(smoke, t.getX() - 51, t.getY() - 115, 118, 184);
                }
                continue;
            }

            if (t.isUnderConstruction()) {
                // drawing image "Scaffolding" / "Construction site"
                Image constructionImg = sprite.getConstruction(t.getLevel(), t.getConstructionIndex());

                if (constructionImg != null) {
                    gc.drawImage(constructionImg, t.getX() - 48, t.getY() - 108, 110, 170);
                }
                continue;
            }

            Image base = sprite.getTowerBase(towerType, level);
            if (base != null) {
                gc.drawImage(base, t.getX() - 11, t.getY() - 58, 38, 76);
            }

            boolean isAttacking = false;
            t.setAttacking(isAttacking);
            int animIndex = t.getAnimationIndex();
            Image weapon = sprite.getTowerWeapon(towerType, level, animIndex, isAttacking);

            if (weapon != null) {
                int offsetX = typeEnum.getWeaponOffsetX(level);
                int offsetY = typeEnum.getWeaponOffsetY(level);
                int width = typeEnum.getWeaponWidth(level);
                int height = typeEnum.getWeaponHeight(level);

                gc.drawImage(weapon,
                        t.getX() - offsetX, // X - Offset X
                        t.getY() - offsetY, // Y - Offset Y
                        width,
                        height
                );
            }

            // just done - draw completed effect
            if (t.isPlayCompletedEffect()) {
                Image dust = sprite.getConstructionCompleted(t.getCompletedIndex());
                if (dust != null) {
                    gc.drawImage(dust, t.getX() - 51, t.getY() - 115, 118, 184);
                }
                menu.closeUpgradeMenu();
            }
        }
        menu.draw(gc);
        notification.draw(gc);
    }
     
    public void addTower(TowerConstant towerConstant, int xPos, int yPos) {
        // 1. take cost of tower
        int cost = towerConstant.getCost(1);
        // 2. check player money
        if (playing.getPlayer().getMoney() >= cost) {
            // 3. pay
            playing.getPlayer().spendMoney(cost);
            towers.add(new Tower(xPos, yPos, towerAmount++, towerConstant));
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
        for (Tower t : towers) {
            // to avoid player try to build a new tower when destroy incompleted
            if (t.getX() == x && t.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public Tower getTowerAt(int x, int y) {
        for (Tower t : towers) {
            if (t.isSelling()) {
                continue; // cannot click when destroying
            }
            if (t.isUnderConstruction() && t.isMaxLevel()) {
                continue; // cannot click when in the building process to max level
            }
            if (t.getX() == x && t.getY() == y) {
                return t;
            }
        }
        return null;
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

    public Image getTowerIcon(int type) {
        return sprite.getTowerIcon(type);
    }

    public Image getSellIcon() {
        return sprite.getSellIcon();
    }

    public Image getUpgradeIcon() {
        return sprite.getUpgradeIcon();
    }

    public TowerConstant getTowerConstantById(int id) {
        if (id >= 0 && id < towerTypes.length) {
            return towerTypes[id];
        }
        return null; // or return default
    }

    public TowerMenuManager getMenu() {
        return menu;
    }
}