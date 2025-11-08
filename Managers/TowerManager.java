package Managers;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import Constant.TowerConstant;
import Entities.Tower.Tower;
import Scene.Playing;
import UI.Button.TowerButton;

public class TowerManager {
    private Playing playing;
    private Image[] towerImagesLevel1, towerImagesLevel2, towerImagesLevel3, towerIcons;
    private ArrayList<Tower> towers = new ArrayList<>(); // Use ArrayList to manage towers
    private int towerAmount = 0; // Initialize tower amount
    private TowerConstant[] towerTypes = TowerConstant.values();

    private boolean isBuildMenuOpen = false;
    private TowerButton[] buildButtons;
    private int selectedTileX, selectedTileY;

    private boolean isUpgradeMenuOpen = false;
    private TowerButton[] upgradeButtons;
    private Tower selectedTower;
    private Image sellIcon, upgradeIcon;
    private final int SELL_ID = -1;
    private final int UPGRADE_ID = -2;

    private String notification = null;
    private long notificationEndTime = 0;

    public TowerManager(Playing playing) {
        this.playing = playing;
        loadTowerImages();
    }

    private void loadTowerImages() {
        // Load tower atlas
        Image atlas = loadImage("towers.png");
        
        if (atlas == null) {
            System.err.println("⚠ CRITICAL: Không thể load towers.png! Tạo dummy images...");
            createDummyImages();
            return;
        }
        
        towerImagesLevel1 = new Image[3]; 
        towerImagesLevel2 = new Image[3];
        towerImagesLevel3 = new Image[3];
        
        // Extract sprites for level 1
        towerImagesLevel1[0] = getSubImage(atlas, 4, 8*13-1, 8*2+4, 8*3+1); // CANNON - Done
        towerImagesLevel1[1] = getSubImage(atlas, 0, 8*7, 8*4, 8*4); // POISON - Done
        towerImagesLevel1[2] = getSubImage(atlas, 2, 8*1+2, 8*3-2, 8*4); // FROST - Done

        // Extract sprites for level 2
        towerImagesLevel2[0] = getSubImage(atlas, 8*6+1, 8*12+1, 8*3, 8*4-1); // CANNON - Done
        towerImagesLevel2[1] = getSubImage(atlas, 8*6, 8*6, 8*4, 8*4); // POISON - Done
        towerImagesLevel2[2] = getSubImage(atlas, 8*6+1, 2, 8*3, 8*4); // FROST

        // Extract sprites for level 3
        towerImagesLevel3[0] = getSubImage(atlas, 8*12-1, 8*10+3, 8*4-4, 8*6-3); // CANNON - Done
        towerImagesLevel3[1] = getSubImage(atlas, 8*12+1, 8*4+4, 8*3, 8*6); // POISON - Done
        towerImagesLevel3[2] = getSubImage(atlas, 8*12+2, 0, 8*3-2, 8*4); // FROST

        // Load tower icons
        Image icons = loadImage("towericons.gif");

        towerIcons = new Image[3]; 
        
        if (icons != null && icons.getPixelReader() != null) {
            System.out.println("✓ Icons image has valid PixelReader");
            towerIcons[0] = getSubImage(icons, 15*48+3, 5*48+3, 40, 40); // CANNON
            towerIcons[1] = getSubImage(icons, 13*48, 0*48, 48, 48); // POISON
            towerIcons[2] = getSubImage(icons, 9*48+6, 5*48+5, 40, 40); // FROST
        } else {
            if (icons == null) {
                System.err.println("⚠ Không thể load towericons.gif!");
            } else {
                System.err.println("⚠ towericons.gif loaded nhưng PixelReader = null (có thể là animated GIF)");
                System.err.println("  Width: " + icons.getWidth() + ", Height: " + icons.getHeight());
            }
            System.err.println("→ Tạo dummy icons...");
            towerIcons[0] = createDummyImage(40, 40);
            towerIcons[1] = createDummyImage(48, 48);
            towerIcons[2] = createDummyImage(40, 40);
        }

        // image for sell and upgrade icon
        // sellIcon = loadImage("sell_icon.png");
        // upgradeIcon = loadImage("upgrade_icon.png");
    }

    private void createDummyImages() {
        towerImagesLevel1 = new Image[3];
        towerImagesLevel2 = new Image[3];
        towerImagesLevel3 = new Image[3];
        towerIcons = new Image[3];
        
        for (int i = 0; i < 3; i++) {
            towerImagesLevel1[i] = createDummyImage(32, 32);
            towerImagesLevel2[i] = createDummyImage(32, 32);
            towerImagesLevel3[i] = createDummyImage(32, 32);
            towerIcons[i] = createDummyImage(40, 40);
        }
    }

    private Image createDummyImage(int width, int height) {
        WritableImage img = new WritableImage(width, height);
        javafx.scene.image.PixelWriter pw = img.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Create a simple pattern
                if ((x + y) % 2 == 0) {
                    pw.setColor(x, y, javafx.scene.paint.Color.GRAY);
                } else {
                    pw.setColor(x, y, javafx.scene.paint.Color.DARKGRAY);
                }
            }
        }
        return img;
    }

    /**
     * Extract a sub-image from a larger image using PixelReader
     */
    private Image getSubImage(Image source, int x, int y, int width, int height) {
        if (source == null) {
            System.err.println("⚠ getSubImage: source image is null!");
            return createDummyImage(width, height);
        }
        
        PixelReader reader = source.getPixelReader();
        if (reader == null) {
            System.err.println("⚠ getSubImage: PixelReader is null!");
            return createDummyImage(width, height);
        }
        
        WritableImage subImage = new WritableImage(reader, x, y, width, height);
        return subImage;
    }

    private Image loadImage(String fileName) {
        System.out.println("→ Attempting to load: " + fileName);
        
        // Try classpath first (preferred when resource folder is on classpath)
        String[] cpRoots = { "/assets/assets/", "/assets/" };
        for (String root : cpRoots) {
            String cpPath = root + fileName;
            try (InputStream is = TowerManager.class.getResourceAsStream(cpPath)) {
                if (is != null) {
                    System.out.println("✓ Loaded from classpath: " + cpPath);
                    Image img = new Image(is);
                    if (img.isError()) {
                        System.err.println("⚠ Image has error after loading: " + img.getException().getMessage());
                        continue;
                    }
                    return img;
                }
            } catch (IOException e) {
                System.err.println("Failed to read classpath resource: " + cpPath);
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error creating Image from: " + cpPath);
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
                try (FileInputStream fis = new FileInputStream(f)) {
                    System.out.println("✓ Loaded from file: " + f.getAbsolutePath());
                    Image img = new Image(fis);
                    if (img.isError()) {
                        System.err.println("⚠ Image has error after loading: " + img.getException().getMessage());
                        continue;
                    }
                    return img;
                } catch (IOException e) {
                    System.err.println("Failed to read file: " + f.getAbsolutePath());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Error creating Image from file: " + f.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }

        System.err.println("⚠ Image not found: " + fileName);
        return null;
    }

    public void update() {
        for (Tower t : towers) {
            t.update();
            // attackEnemyIfClose(t);
        }
    }

    public void draw(GraphicsContext gc) {
        for (Tower t : towers) {
            int towerType = t.getTowerType().ordinal();
            int level = t.getLevel();

            switch (towerType) { 
                case 0: // CANNON
                    if (level == 1)
                        gc.drawImage(towerImagesLevel1[0], t.getX()-10, t.getY()-27, 40, 50);
                    else if (level == 2)
                        gc.drawImage(towerImagesLevel2[0], t.getX()-13, t.getY()-33, 44, 60);
                    else if (level == 3)
                        gc.drawImage(towerImagesLevel3[0], t.getX()-18, t.getY()-38, 54, 67);
                    break;
                case 1: // POISON
                    if (level == 1)
                        gc.drawImage(towerImagesLevel1[1], t.getX()-11, t.getY()-22, 48, 58);
                    else if (level == 2)
                        gc.drawImage(towerImagesLevel2[1], t.getX()-13, t.getY()-32, 52, 58);
                    else if (level == 3)
                        gc.drawImage(towerImagesLevel3[1], t.getX()-12, t.getY()-39, 40, 74);
                    break;
                case 2: // FROST
                    if (level == 1)
                        gc.drawImage(towerImagesLevel1[2], t.getX()-13, t.getY()-24, 42, 64);
                    else if (level == 2)
                        gc.drawImage(towerImagesLevel2[2], t.getX()-14, t.getY()-32, 45, 60);
                    else if (level == 3)
                        gc.drawImage(towerImagesLevel3[2], t.getX()-16, t.getY()-36, 50, 64);
                    break;
            }
        }

        if (isBuildMenuOpen) {
            drawBuildMenu(gc);
        } else if (isUpgradeMenuOpen) {
            drawUpgradeMenu(gc);
        }

        drawNotification(gc);
    }
    
    private void drawBuildMenu(GraphicsContext gc) {
        for (TowerButton b : buildButtons) {
            b.render(gc);

            gc.drawImage(getTowerIconsAt(b.getId()), b.getX(), b.getY(), b.getWidth(), b.getHeight());
            
            // draw price of tower
            TowerConstant type = getTowerConstantById(b.getId());
            if (type != null) {
                gc.setFill(Color.YELLOW);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                gc.fillText("" + type.getCost(1), b.getX() + 3, b.getY() + b.getHeight() + 10);
            }
        }
    }

    private void drawUpgradeMenu(GraphicsContext gc) {
        for (TowerButton b : upgradeButtons) {
            b.render(gc);

            String text = "";
            Image icon = null;
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
                gc.drawImage(icon, b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } else {
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                gc.fillText(text, b.getX(), b.getY() + 14);
            }
            
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.fillText("" + cost, b.getX() + 3, b.getY() + b.getHeight() + 10);
        }
    }

    private void drawNotification(GraphicsContext gc) {
        if (System.currentTimeMillis() < notificationEndTime && notification != null) {
            gc.setFill(Color.YELLOW);
            Font font = Font.font("Arial", FontWeight.BOLD, 32);
            gc.setFont(font);
            
            // Measure text width using Text node
            Text textNode = new Text(notification);
            textNode.setFont(font);
            
            int msgWidth = (int) textNode.getLayoutBounds().getWidth();
            int x = (1504 - msgWidth) / 2; 
            int y = 350;
            
            gc.fillText(notification, x-50, y);
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
        double buttonSize = 20;
        
        double b1_X = clickedPixelX - 23;
        double b1_Y = clickedPixelY - 20;
        buildButtons[0] = new TowerButton(getTowerIconsAt(0), b1_X, b1_Y, buttonSize, buttonSize, 0);

        double b2_X = clickedPixelX;
        double b2_Y = clickedPixelY - 40;
        buildButtons[1] = new TowerButton(getTowerIconsAt(1), b2_X, b2_Y, buttonSize, buttonSize, 1); 

        double b3_X = clickedPixelX + 23;
        double b3_Y = clickedPixelY - 20;
        buildButtons[2] = new TowerButton(getTowerIconsAt(2), b3_X, b3_Y, buttonSize, buttonSize, 2);
    }

    public void openUpgradeMenu(Tower clickedTower) {
        isUpgradeMenuOpen = true;
        isBuildMenuOpen = false;
        this.selectedTower = clickedTower;
        
        upgradeButtons = new TowerButton[2];
        double buttonSize = 20;
        
        int towerX = clickedTower.getX();
        int towerY = clickedTower.getY();

        double sellX = towerX - buttonSize - 20;
        double sellY = towerY - 50;
        upgradeButtons[0] = new TowerButton(sellIcon, sellX, sellY, buttonSize, buttonSize, SELL_ID);
        
        double upgradeX = towerX + 36;
        double upgradeY = towerY - 50;
        upgradeButtons[1] = new TowerButton(upgradeIcon, upgradeX, upgradeY, buttonSize, buttonSize, UPGRADE_ID);
    }

    public void handleBuildMenuClick(int x, int y) {
        if (!isBuildMenuOpen) return;
        
        for (TowerButton b : buildButtons) {
            if (b.isMouseOver(x, y)) {
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
            if (b.isMouseOver(x, y)) {
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

    public Image[] getTowerImage(int level) {
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

    public Image getTowerImageAt(int level, int index) {
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

    public Image[] getTowerIcons() {
        return towerIcons;
    }

    public Image getTowerIconsAt(int index) {
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