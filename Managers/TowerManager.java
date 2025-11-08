package Managers;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import Constant.TowerConstant;
import Entities.Tower.Tower;
import Scene.Playing;
import UI.Button.TowerButton;

public class TowerManager {
    private Playing playing;
    private BufferedImage [] towerImagesLevel1, towerImagesLevel2, towerImagesLevel3, towerIcons;
    private ArrayList<Tower> towers = new ArrayList<>(); // Use ArrayList to manage towers
    private int towerAmount = 0; // Initialize tower amount
    private TowerConstant[] towerTypes = TowerConstant.values();

    private boolean isBuildMenuOpen = false;
    private TowerButton[] buildButtons;
    private int selectedTileX, selectedTileY;

    private boolean isUpgradeMenuOpen = false;
    private TowerButton[] upgradeButtons;
    private Tower selectedTower;
    private BufferedImage sellIcon, upgradeIcon;
    private final int SELL_ID = -1;
    private final int UPGRADE_ID = -2;

    private String notification = null;
    private long notificationEndTime = 0;

    public TowerManager(Playing playing) {
        this.playing = playing;
        loadTowerImages();
    }

    private void loadTowerImages() {
        // image for tower
        BufferedImage atlas = loadImage("towers.png");
        
        towerImagesLevel1 = new BufferedImage[3]; 
        towerImagesLevel2 = new BufferedImage[3];
        towerImagesLevel3 = new BufferedImage[3];
        
        // image for level 1
        towerImagesLevel1[0] = atlas.getSubimage(4, 8*13-1, 8*2+4, 8*3+1); // CANNON - Done
        towerImagesLevel1[1] = atlas.getSubimage(0, 8*7, 8*4, 8*4); // POISON - Done
        towerImagesLevel1[2] = atlas.getSubimage(2, 8*1+2, 8*3-2, 8*4); // FROST - Done

        // image for level 2
        towerImagesLevel2[0] = atlas.getSubimage(8*6+1, 8*12+1, 8*3, 8*4-1); // CANNON - Done
        towerImagesLevel2[1] = atlas.getSubimage(8*6, 8*6, 8*4, 8*4); // POISON - Done
        towerImagesLevel2[2] = atlas.getSubimage(8*6+1, 2, 8*3,8*4); // FROST

        // image for level 3
        towerImagesLevel3[0] = atlas.getSubimage(8*12-1, 8*10+3, 8*4-4, 8*6-3); // CANNON - Done
        towerImagesLevel3[1] = atlas.getSubimage(8*12+1, 8*4+4, 8*3, 8*6); // POISON - Done
        towerImagesLevel3[2] = atlas.getSubimage(8*12+2, 0, 8*3-2, 8*4); // FROST

        // image for tower icon
        BufferedImage icons = loadImage("towericons.gif");

        towerIcons = new BufferedImage[3]; 
        
        towerIcons[0] = icons.getSubimage(15*48+3,5*48+3,40,40); // CANNON
        towerIcons[1] = icons.getSubimage(13*48,0*48,48,48); // POISON
        towerIcons[2] = icons.getSubimage(9*48+6,5*48+5,40,40); // FROST

        // image for sell and upgrade icon
        // sellIcon = loadImage("sell_icon.png");
        // upgradeIcon = loadImage("upgrade_icon.png");
    }

    private BufferedImage loadImage(String fileName) {
        // Try classpath first (preferred when resource folder is on classpath)
        String[] cpRoots = { "/assets/assets/", "/assets/" };
        for (String root : cpRoots) {
            String cpPath = root + fileName;
            try (InputStream is = TileManager.class.getResourceAsStream(cpPath)) {
                if (is != null) {
                    return ImageIO.read(is);
                }
            } catch (IOException e) {
                System.err.println("Failed to read classpath resource: " + cpPath);
                e.printStackTrace();
            }
        }

        // Fallback to file system
        String[] fsRoots = {
            "resource/assets/assets/",
            "resource/assets/"
        };
        for (String root : fsRoots) {
            File f = new File(root + fileName);
            if (f.exists()) {
                try {
                    return ImageIO.read(f);
                } catch (IOException e) {
                    System.err.println("Failed to read file: " + f.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }

        System.err.println("Image not found: " + fileName);
        return null;
    }

    public void update() {
        for (Tower t : towers) {
            t.update();
            // attackEnemyIfClose(t);
        }
    }

    public void draw(Graphics g) {
        for (Tower t : towers) {
            int towerType = t.getTowerType().ordinal();
            int level = t.getLevel();

            switch (towerType) { 
                case 0: // CANNON
                    if (level == 1)
                        g.drawImage(towerImagesLevel1[0], t.getX()-10, t.getY()-27, 40, 50, null);
                    else if (level == 2)
                        g.drawImage(towerImagesLevel2[0], t.getX()-13, t.getY()-33, 44, 60, null);
                    else if (level == 3)
                        g.drawImage(towerImagesLevel3[0], t.getX()-18, t.getY()-38, 54, 67, null);
                    break;
                case 1: // POISON
                    if (level == 1)
                        g.drawImage(towerImagesLevel1[1], t.getX()-11, t.getY()-22, 48, 58, null);
                    else if (level == 2)
                        g.drawImage(towerImagesLevel2[1], t.getX()-13, t.getY()-32, 52, 58, null);
                    else if (level == 3)
                        g.drawImage(towerImagesLevel3[1], t.getX()-12, t.getY()-39, 40, 74, null);
                    break;
                case 2: // FROST
                    if (level == 1)
                        g.drawImage(towerImagesLevel1[2], t.getX()-13, t.getY()-24, 42, 64, null);
                    else if (level == 2)
                        g.drawImage(towerImagesLevel2[2], t.getX()-14, t.getY()-32, 45, 60, null);
                    else if (level == 3)
                        g.drawImage(towerImagesLevel3[2], t.getX()-16, t.getY()-36, 50, 64, null);
                    break;
            }
        }

        if (isBuildMenuOpen) {
            drawBuildMenu(g);
        } else if (isUpgradeMenuOpen) {
            drawUpgradeMenu(g);
        }

        drawNotification(g);
    }
    
    private void drawBuildMenu(Graphics g) {
        for (TowerButton b : buildButtons) {
            b.render(g);

            g.drawImage(getTowerIconsAt(b.getId()), b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);
            
            // draw price of tower
            TowerConstant type = getTowerConstantById(b.getId());
            if (type != null) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString("" + type.getCost(1), b.getX() + 3, b.getY() + b.getHeight() + 10);
            }
        }
    }

    private void drawUpgradeMenu(Graphics g) {
        for (TowerButton b : upgradeButtons) {
            b.render(g);

            String text = "";
            BufferedImage icon = null;
            int cost = 0;

            if (b.getId() == SELL_ID) {
                icon = sellIcon;
                text = "SELL";
                cost = (int)(selectedTower.getTotalBuildCost() * 0.75); // refund 3/4 of the construction cost 
            } else if (b.getId() == UPGRADE_ID) {
                icon = upgradeIcon;
                text = "UPG";
                cost = selectedTower.getNextUpgradeCost();
            }

            if (icon != null) {
                g.drawImage(icon, b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);
            } else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString(text, b.getX(), b.getY() + 14);
            }
            
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("" + cost, b.getX() + 3, b.getY() + b.getHeight() + 10);
        }
    }

    private void drawNotification(Graphics g) {
        if (System.currentTimeMillis() < notificationEndTime && notification != null) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            FontMetrics fm = g.getFontMetrics();
            
            int msgWidth = fm.stringWidth(notification);
            int x = (1504 - msgWidth) / 2; 
            int y = 350;
            
            g.drawString(notification, x-50, y);
        } else {
            notification = null; // delete when time out
        }
    }
    
    private void showNotification(String text) {
        this.notification = text;
        this.notificationEndTime = System.currentTimeMillis() + 2000; // display in 2 sec
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
            showNotification("Not enough gold!");
        }
    }

    public void openBuildMenu(int clickedPixelX, int clickedPixelY) {
        isBuildMenuOpen = true;
        this.selectedTileX = clickedPixelX;
        this.selectedTileY = clickedPixelY;
        
        buildButtons = new TowerButton[3];
        int buttonSize = 20;
        
        int b1_X = clickedPixelX - 23;
        int b1_Y = clickedPixelY - 20;
        buildButtons[0] = new TowerButton(getTowerIconsAt(0), b1_X, b1_Y, buttonSize, buttonSize, 0);

        int b2_X = clickedPixelX;
        int b2_Y = clickedPixelY - 40;
        buildButtons[1] = new TowerButton(getTowerIconsAt(1), b2_X, b2_Y, buttonSize, buttonSize, 1); 

        int b3_X = clickedPixelX + 23;
        int b3_Y = clickedPixelY - 20;
        buildButtons[2] = new TowerButton(getTowerIconsAt(2), b3_X, b3_Y, buttonSize, buttonSize, 2);
    }

    public void openUpgradeMenu(Tower clickedTower) {
        isUpgradeMenuOpen = true;
        isBuildMenuOpen = false;
        this.selectedTower = clickedTower;
        
        upgradeButtons = new TowerButton[2];
        int buttonSize = 20 ;
        
        int towerX = clickedTower.getX();
        int towerY = clickedTower.getY();

        int sellX = towerX - buttonSize - 20;
        int sellY = towerY - 50;
        upgradeButtons[0] = new TowerButton(sellIcon, sellX, sellY, buttonSize, buttonSize, SELL_ID);
        
        int upgradeX = towerX + 36;
        int upgradeY = towerY - 50;
        upgradeButtons[1] = new TowerButton(upgradeIcon, upgradeX, upgradeY, buttonSize, buttonSize, UPGRADE_ID);
    }

    public void handleBuildMenuClick(int x, int y) {
        if (!isBuildMenuOpen) return;
        
        for (TowerButton b : buildButtons) {
            if (b.getBounds().contains(x, y)) {
                TowerConstant towerToBuild = getTowerConstantById(b.getId());
                if (towerToBuild != null) {
                    addTower(towerToBuild, selectedTileX, selectedTileY); 
                }
                break;
            }
        }
        isBuildMenuOpen = false;
    }

    public void handleUpgradeMenuClick(int x, int y) {
        if (!isUpgradeMenuOpen) return;

        for (TowerButton b : upgradeButtons) {
            if (b.getBounds().contains(x, y)) {
                if (b.getId() == SELL_ID) {
                    sellSelectedTower();
                } else if (b.getId() == UPGRADE_ID) {
                    upgradeSelectedTower();
                }
                break;
            }
        }
        isUpgradeMenuOpen = false;
    }

    private void sellSelectedTower() {
        int refund = (int)(selectedTower.getTotalBuildCost() * 0.75); // refund 3/4
        playing.getPlayer().addMoney(refund);
        towers.remove(selectedTower);
        selectedTower = null;
    }

    private void upgradeSelectedTower() {
        if (selectedTower.isMaxLevel()) {
            showNotification("Max Level Reached!");
            return;
        }
        
        int upgradeCost = selectedTower.getNextUpgradeCost();
        
        if (playing.getPlayer().getMoney() >= upgradeCost) {
            playing.getPlayer().spendMoney(upgradeCost);

            selectedTower.upgradeTower(upgradeCost);
            showNotification("Tower Upgraded!");
        } else {
            showNotification("Not enough gold for upgrade!");
        }
    }

    public void handleMouseMoved(int x, int y) {
        if (isBuildMenuOpen) {
            for (TowerButton b : buildButtons) {
                b.update(x, y); 
            }
        }
    }

    public void handleMousePressed(int x, int y) {
        if (isBuildMenuOpen) {
            for (TowerButton b : buildButtons) {
                if (b.isMouseOver(x,y)) 
                    b.setMousePressed(true);
            }
        } else if (isUpgradeMenuOpen) {
            for (TowerButton b : upgradeButtons) {
                if (b.isMouseOver(x,y)) 
                    b.setMousePressed(true);
            }
        }
    }

    public void handleMouseReleased(int x, int y) {
        if (isBuildMenuOpen) {
            for (TowerButton b : buildButtons) {
                b.setMousePressed(false);
            }
        } else if (isUpgradeMenuOpen) {
            for (TowerButton b : upgradeButtons) 
                b.setMousePressed(false);
        }
    }

    public boolean isBuildMenuOpen() {
        return isBuildMenuOpen;
    }

    public boolean isUpgradeMenuOpen() {
        return isUpgradeMenuOpen;
    }

    public BufferedImage[] getTowerImage(int level) {
        switch (level) {
            case 1:
                return towerImagesLevel1;
            case 2:
                return towerImagesLevel2;
            case 3:
                return towerImagesLevel3;
            default:
                return null;
        }
    }

    public BufferedImage getTowerImageAt(int level, int index) {
        switch (level) {
            case 1:
                return towerImagesLevel1[index];
            case 2:
                return towerImagesLevel2[index];
            case 3:
                return towerImagesLevel3[index];
            default:
                return null;
        }
    }

    public BufferedImage[] getTowerIcons() {
        return towerIcons;
    }

    public BufferedImage getTowerIconsAt(int index) {
        return towerIcons[index];
    }

    public Tower getTowerAt(int x, int y) {
        for (Tower t : towers) {
            if (t.getX() == x && t.getY() == y) {
                return t;
            }
        }
        return null;
    }

    public TowerConstant getTowerConstantById(int id) {
        if (id >= 0 && id < towerTypes.length) {
            return towerTypes[id];
        }
        return null; // or return default
    }
}