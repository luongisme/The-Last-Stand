package Managers.Tower;

import Button.TowerButton;
import Constant.TowerConstant;
import Entities.Towers.Tower;
import Helper.Notification;
import Managers.Tower.TowerManager;
import Scenes.Playing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TowerMenuManager {
    private Playing playing;
    private TowerManager towerManager;
    private Notification notification;

    private final int SELL_ID = -1;
    private final int UPGRADE_ID = -2;
    // private Image[] sellIcon, upgradeIcon;
    private Image sellIcon, upgradeIcon;

    private boolean isBuildMenuOpen = false;
    private TowerButton[] buildButtons;
    private int selectedTileX, selectedTileY;

    private boolean isUpgradeMenuOpen = false;
    private TowerButton[] upgradeButtons;
    private Tower selectedTower;

    private static final int BUILD_BUTTON_SIZE = 28;
    private static final int UPGRADE_BUTTON_SIZE = 24;
    private static final int TILE_SIZE = 16;
    // distance from the middle button to the selected tile
    private static final int MARGIN_VERTICAL = 20;
    // distance between buttons
    private static final int GAP = 8;
    private static final Font MENU_FONT = Font.font("Arial", FontWeight.BOLD, 10);

    public TowerMenuManager(Playing playing, TowerManager towerManager, Notification notification) {
        this.playing = playing;
        this.towerManager = towerManager;
        this.notification = notification;
        loadIcons();
    }

    private void loadIcons() {
        this.sellIcon = towerManager.getSellIcon();
        this.upgradeIcon = towerManager.getUpgradeIcon();
    }

    public void update() {
        if (isBuildMenuOpen) {
            for (TowerButton b : buildButtons) {
                b.update();
            }
        } else if (isUpgradeMenuOpen) {
            for (TowerButton b : upgradeButtons) {
                b.update();
            }
        }
    }

    public void draw(GraphicsContext gc) {
        if (isBuildMenuOpen) {
            drawBuildMenu(gc);
        } else if (isUpgradeMenuOpen) {
            drawUpgradeMenu(gc);
        }
    }

    private void drawBuildMenu(GraphicsContext gc) {
        for (TowerButton b : buildButtons) {
            b.render(gc);

            TowerConstant type = towerManager.getTowerConstantById(b.getId());
            if (type != null) {
                String costText = String.valueOf(type.getCost(1));

                drawCenteredText(gc, b, costText, Color.YELLOW);
            }
        }
    }

    private void drawUpgradeMenu(GraphicsContext gc) {
        for (TowerButton b : upgradeButtons) {
            b.render(gc);

            String textToDraw = "";

            if (b.getId() == SELL_ID) {
                int refund = (int)(selectedTower.getTotalBuildCost() * 0.5); // refund 1/2
                textToDraw = String.valueOf(refund);
            } else if (b.getId() == UPGRADE_ID) {
                int cost = selectedTower.getNextUpgradeCost();
                textToDraw = String.valueOf(cost);
            }

            drawCenteredText(gc, b, textToDraw, Color.YELLOW);
        }
    }

    private void drawCenteredText(GraphicsContext gc, TowerButton b, String text, Color color) {
        gc.setFont(MENU_FONT);
        gc.setFill(color);

        // create a virtual Text object to measure the size
        Text textNode = new javafx.scene.text.Text(text);
        textNode.setFont(MENU_FONT);
        double textWidth = textNode.getLayoutBounds().getWidth();

        // calculate the center coordinates
        double textX = b.getX() + (b.getWidth() / 2) - (textWidth / 2);
        double textY = b.getY() + b.getHeight() + 10;

        gc.fillText(text, textX, textY);
    }

    public void openBuildMenu(int clickedPixelX, int clickedPixelY) {
        isBuildMenuOpen = true;
        this.selectedTileX = clickedPixelX;
        this.selectedTileY = clickedPixelY;

        buildButtons = new TowerButton[3];

        double centerX = clickedPixelX + (TILE_SIZE - BUILD_BUTTON_SIZE) / 2.0;
        double bottomY = clickedPixelY - BUILD_BUTTON_SIZE - MARGIN_VERTICAL;

        // Mid button
        buildButtons[1] = new TowerButton(towerManager.getTowerIcon(1), centerX, bottomY, BUILD_BUTTON_SIZE, BUILD_BUTTON_SIZE, 1);

        double sideY = bottomY + (BUILD_BUTTON_SIZE / 2.0);

        // Left button
        double leftX = centerX - BUILD_BUTTON_SIZE - GAP;
        buildButtons[0] = new TowerButton(towerManager.getTowerIcon(0), leftX, sideY, BUILD_BUTTON_SIZE, BUILD_BUTTON_SIZE, 0);

        // Right button
        double rightX = centerX + BUILD_BUTTON_SIZE + GAP;
        buildButtons[2] = new TowerButton(towerManager.getTowerIcon(2), rightX, sideY, BUILD_BUTTON_SIZE, BUILD_BUTTON_SIZE, 2);
    }

    public void openUpgradeMenu(Tower clickedTower) {
        isUpgradeMenuOpen = true;
        isBuildMenuOpen = false;
        this.selectedTower = clickedTower;

        int level = clickedTower.getLevel();
        int towerX = clickedTower.getX();
        int towerY = clickedTower.getY();

        double extraY = (level > 2) ? 30 : (5 * level);
        double centerX = towerX - 4;
        double topY = towerY - (80 + extraY); // level 1: -85, level 2: -90, level 3: - 110

        // if tower in building process, allow/show upgrade only
        if (selectedTower.isUnderConstruction()) {
            if (selectedTower.isMaxLevel()) {
                isUpgradeMenuOpen = false;
                selectedTower = null;
                return;
            }
            upgradeButtons = new TowerButton[1];
            upgradeButtons[0] = new TowerButton(upgradeIcon, centerX, topY, UPGRADE_BUTTON_SIZE, UPGRADE_BUTTON_SIZE, UPGRADE_ID);
            return;
        }

        // if tower reach max level, allow/show sell only
        if (selectedTower.isMaxLevel()) {
            upgradeButtons = new TowerButton[1];
            upgradeButtons[0] = new TowerButton(sellIcon, centerX, topY, UPGRADE_BUTTON_SIZE, UPGRADE_BUTTON_SIZE, SELL_ID);
            return;
        }

        // default
        upgradeButtons = new TowerButton[2];
        double sideY = towerY - (50 + extraY);

        double sellX = towerX - UPGRADE_BUTTON_SIZE - 20;
        upgradeButtons[0] = new TowerButton(sellIcon, sellX, sideY, UPGRADE_BUTTON_SIZE, UPGRADE_BUTTON_SIZE, SELL_ID);

        double upgradeX = towerX + 36;
        upgradeButtons[1] = new TowerButton(upgradeIcon, upgradeX, sideY, UPGRADE_BUTTON_SIZE, UPGRADE_BUTTON_SIZE, UPGRADE_ID);
    }

    public void handleBuildMenuClick(int x, int y) {
        if (!isBuildMenuOpen) return;

        for (TowerButton b : buildButtons) {
            if (b.isMouseOver(x, y)) {
                TowerConstant towerToBuild = towerManager.getTowerConstantById(b.getId());
                if (towerToBuild != null) {
                    towerManager.addTower(towerToBuild, selectedTileX, selectedTileY);
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
        if (selectedTower.isUnderConstruction()) {return;}

        int refund = (int)(selectedTower.getTotalBuildCost() * 0.5);  // refund 1/2
        playing.getPlayer().addMoney(refund);

        towerManager.startSellTower(selectedTower);
        selectedTower = null;
        isUpgradeMenuOpen = false;
    }

    private void upgradeSelectedTower() {
        if (selectedTower.isMaxLevel()) {
            return;
        }

        int upgradeCost = selectedTower.getNextUpgradeCost();

        if (playing.getPlayer().getMoney() >= upgradeCost) {
            playing.getPlayer().spendMoney(upgradeCost);

            selectedTower.upgradeTower(upgradeCost);
            selectedTower = null;
            isUpgradeMenuOpen = false;
        } else {
            notification.show("Not enough gold to upgrade!", 2000);
        }
    }

    public void handleMouseMoved(int x, int y) {
        if (isBuildMenuOpen) {
            for (TowerButton b : buildButtons) {
                b.update(x, y);
            }
        } else if (isUpgradeMenuOpen) {
            for (TowerButton b : upgradeButtons) {
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

    public boolean isBuildMenuOpen() { return isBuildMenuOpen; }

    public boolean isUpgradeMenuOpen() { return isUpgradeMenuOpen; }

    public void closeUpgradeMenu() { isUpgradeMenuOpen = false; }

    public void closeAllMenus() {
        isBuildMenuOpen = false;
        isUpgradeMenuOpen = false;
    }

}
