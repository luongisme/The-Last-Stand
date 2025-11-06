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
    private BufferedImage [] towerImages, towerIcons;
    private ArrayList<Tower> towers = new ArrayList<>(); // Use ArrayList to manage towers
    private int towerAmount = 0; // Initialize tower amount
    private TowerConstant[] towerTypes = TowerConstant.values();

    private final int GRID_SIZE = 16;
    private boolean isBuildMenuOpen = false;
    private TowerButton[] buildButtons;
    private int selectedTileX, selectedTileY;

    private String notification = null;
    private long notificationEndTime = 0;

    public TowerManager(Playing playing) {
        this.playing = playing;
        loadTowerImages();
    }

    private void loadTowerImages() {
        towerImages = new BufferedImage[3]; 
        BufferedImage atlas = loadImage("towers.png");
        // cắt ảnh cho level 1
        towerImages[2] = atlas.getSubimage(0, 0, 36, 36); // FROST
        towerImages[1] = atlas.getSubimage(0, 56, 36, 36); // POISON
        towerImages[0] = atlas.getSubimage(0, 92, 36, 36); // CANNON

        // cần cắt thêm ảnh cho level 2 và 3

        // towerImages[2] = atlas.getSubimage(48, 0, 36, 36); // FROST
        // towerImages[1] = atlas.getSubimage(48, 56, 36, 36); // POISON
        // towerImages[0] = atlas.getSubimage(48, 92, 36, 36); // CANNON

        towerIcons = new BufferedImage[3]; 
        BufferedImage icons = loadImage("towericons.gif");
            
        towerIcons[0] = icons.getSubimage(15*48+3,5*48+3,40,40); // CANNON
        towerIcons[1] = icons.getSubimage(13*48,0*48,48,48); // POISON
        towerIcons[2] = icons.getSubimage(9*48+6,5*48+5,40,40); // FROST
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
            switch (towerType) { // làm cho từng level, hiện tại đã có level 1, làm thêm vẽ từng cái cho level 2 và 3
                // cần quản lí các level và chuyể đổi hợp lí, cần thêm tính năng xóa tháp đi để xây tháp khác và hoàn một phần tiền
                // phân loại giá tiền cho các loại level khác nhau của tower, chỉnh sửa ở phần towerConstant
                case 0: // CANNON
                    g.drawImage(towerImages[0], t.getX()-16, t.getY()-42, 64, 64, null);
                    break;
                case 1: // POISON
                    g.drawImage(towerImages[1], t.getX()-13, t.getY()-20, 60, 60, null);
                    break;
                case 2: // FROST
                    g.drawImage(towerImages[2], t.getX()-16, t.getY()-36, 64, 64, null);
                    break;
                default:
                    break;
            }
            
        }

        if (isBuildMenuOpen) {
            drawBuildMenu(g);
        }

        drawNotification(g);
    }
    
    private void drawBuildMenu(Graphics g) {
        for (TowerButton b : buildButtons) {
            b.render(g);
            
            // draw price of tower
            TowerConstant type = getTowerConstantById(b.getId());
            if (type != null) {
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                g.drawString("" + type.getCost(), b.getX() + 3, b.getY() + b.getHeight() + 10);
            }
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

    public BufferedImage[] getTowerImages() {
        return towerImages;
    }

    public BufferedImage getTowerImagesAt(int index) {
        return towerImages[index];
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
     
    public void addTower(TowerConstant towerConstant, int xPos, int yPos) {
        // 1. take cost of tower
        int cost = towerConstant.getCost();
        // 2. check player money
        if (playing.getPlayer().getMoney() >= cost) {
            // 3. pay
            playing.getPlayer().spendMoney(cost);
            towers.add(new Tower(xPos, yPos, towerAmount++, towerConstant));
        } else {
            showNotification("Not enough gold!");
        }
    }

    public TowerConstant getTowerConstantById(int id) {
        if (id >= 0 && id < towerTypes.length) {
            return towerTypes[id];
        }
        return null; // or return default
    }

    public boolean isBuildMenuOpen() {
        return isBuildMenuOpen;
    }

    public void openBuildMenu(int clickedPixelX, int clickedPixelY) {
        isBuildMenuOpen = true;
        this.selectedTileX = clickedPixelX;
        this.selectedTileY = clickedPixelY;
        
        buildButtons = new TowerButton[3];
        int buttonSize = GRID_SIZE;
        
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
        }
    }

    public void handleMouseReleased(int x, int y) {
        if (isBuildMenuOpen) {
            for (TowerButton b : buildButtons) {
                b.setMousePressed(false);
            }
        }
    }

    public static void main(String[] args) {
        Tower t = new Tower(0, 0, 0, TowerConstant.POISON);
        System.out.println(t.getTowerType().ordinal());
    }
}