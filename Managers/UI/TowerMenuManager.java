package Managers.UI;

import Constant.TowerConstant;
import Entities.Tower.Tower;
import Scenes.Playing;
import Button.TowerButton;
import Managers.Tower.TowerManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TowerMenuManager {
    private Playing playing;
    private TowerManager towerManager;
    private NotificationManager notification;

    private boolean isBuildMenuOpen = false;
    private TowerButton[] buildButtons;
    private int selectedTileX, selectedTileY;

    private boolean isUpgradeMenuOpen = false;
    private TowerButton[] upgradeButtons;
    private Tower selectedTower;

//    private Image[] sellIcon, upgradeIcon;
    private Image sellIcon, upgradeIcon;
    private final int SELL_ID = -1;
    private final int UPGRADE_ID = -2;

    private final int TILE_SIZE = 16;

    public TowerMenuManager(Playing playing, TowerManager towerManager, NotificationManager notification) {
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

                Font font = Font.font("Arial", FontWeight.BOLD, 10);
                gc.setFill(Color.YELLOW);
                gc.setFont(font);

                // create a virtual Text object to measure the size
                javafx.scene.text.Text textNode = new javafx.scene.text.Text(costText);
                textNode.setFont(font);
                double textWidth = textNode.getLayoutBounds().getWidth();

                // calculate the center X coordinate
                double textX = b.getX() + (b.getWidth() / 2) - (textWidth / 2);
                double textY = b.getY() + b.getHeight() + 10;

                gc.fillText(costText, textX, textY);
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

            Font font = Font.font("Arial", FontWeight.BOLD, 10);
            gc.setFont(font);
            gc.setFill(Color.YELLOW);

            javafx.scene.text.Text textNode = new javafx.scene.text.Text(textToDraw);
            textNode.setFont(font);
            double textWidth = textNode.getLayoutBounds().getWidth();

            double textX = b.getX() + (b.getWidth() / 2) - (textWidth / 2);
            double textY = b.getY() + b.getHeight() + 10;

            gc.fillText(textToDraw, textX, textY);
        }
    }

    public void openBuildMenu(int clickedPixelX, int clickedPixelY) {
        isBuildMenuOpen = true;
        this.selectedTileX = clickedPixelX;
        this.selectedTileY = clickedPixelY;

        buildButtons = new TowerButton[3];

        double buttonSize = 28;
        double horizontalGap = 8; // distance between buttons
        double verticalMargin = 20; // distance from the middle button to the selected tile
        double arcHeight = buttonSize / 2; // button 2 below the middle button

        double centerOffset = (TILE_SIZE - buttonSize) / 2;
        double centerX = clickedPixelX + centerOffset;

        // Mid button
        double b2_X = centerX;
        double b2_Y = clickedPixelY - buttonSize - verticalMargin;
        buildButtons[1] = new TowerButton(towerManager.getTowerIcon(1), b2_X, b2_Y, buttonSize, buttonSize, 1);

        // Left button
        double b1_X = b2_X - buttonSize - horizontalGap;
        double b1_Y = b2_Y + arcHeight;
        buildButtons[0] = new TowerButton(towerManager.getTowerIcon(0), b1_X, b1_Y, buttonSize, buttonSize, 0);

        // Right button
        double b3_X = b2_X + buttonSize + horizontalGap;
        double b3_Y = b2_Y + arcHeight;
        buildButtons[2] = new TowerButton(towerManager.getTowerIcon(2), b3_X, b3_Y, buttonSize, buttonSize, 2);
    }

    public void openUpgradeMenu(Tower clickedTower) {
        isUpgradeMenuOpen = true;
        isBuildMenuOpen = false;
        this.selectedTower = clickedTower;
        int level = clickedTower.getLevel();
        double extraY = (level > 2) ? 30 : (5 * level);

        int towerX = clickedTower.getX();
        int towerY = clickedTower.getY();

        double buttonSize = 24;
        double centerX = towerX - 4;
        double topY = towerY - (80 + extraY); // level 1: -85, level 2: -90, level 3: - 110

        // if tower in building process, allow/show upgrade only
        if (selectedTower.isUnderConstruction()) {
            upgradeButtons = new TowerButton[1];
            upgradeButtons[0] = new TowerButton(upgradeIcon, centerX, topY, buttonSize, buttonSize, UPGRADE_ID);
            return;
        }

        // if tower reach max level, allow/show sell only
        if (selectedTower.isMaxLevel()) {
            upgradeButtons = new TowerButton[1];
            upgradeButtons[0] = new TowerButton(sellIcon, centerX, topY, buttonSize, buttonSize, SELL_ID);
            return;
        }

        // default
        upgradeButtons = new TowerButton[2];

        double sellX = towerX - buttonSize - 20;
        double sellY = towerY - (50 + extraY);
        upgradeButtons[0] = new TowerButton(sellIcon, sellX, sellY, buttonSize, buttonSize, SELL_ID);

        double upgradeX = towerX + 36;
        double upgradeY = towerY - (50 + extraY);
        upgradeButtons[1] = new TowerButton(upgradeIcon, upgradeX, upgradeY, buttonSize, buttonSize, UPGRADE_ID);
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
}
