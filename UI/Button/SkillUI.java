package Button;

import Helper.LoadImages.LoadImageSkill;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SkillUI {
    private static final int ICON_SIZE = 42;

    // Selection visual effects
    private static final Color SELECTION_BORDER_COLOR = Color.rgb(255, 215, 0); // Gold color
    private static final Color DARK_OVERLAY_COLOR = Color.rgb(0, 0, 0, 0.3); // Semi-transparent black
    private static final int SELECTION_BORDER_WIDTH = 3;

    private static final String[] SKILL_NAMES = {
        "DarkGhost", "SandStone", "ThunderBolt", "WaterStrike"
    };

    // Icon positions (relative to skill UI position)
    private static final int[][] ICON_POSITIONS = {
        {29, 82},   // DarkGhost (top-left)
        {29, 140},  // SandStone (bottom-left)
        {75, 140},  // ThunderBolt (bottom-right)
        {75, 82}    // WaterStrike (top-right)
    };

    protected int x, y, width, height;
    protected boolean mouseOver = false;
    protected boolean mousePressed = false;

    private Image skillUIBackground;
    private Image[] skillIcons;

    // Track which skill is currently selected (-1 means none)
    private int selectedSkillIndex = -1;
    private int hoveredSkillIndex = -1;

    // Sprite information
    private final int spriteX, spriteY, spriteW, spriteH;

    public SkillUI(int x, int y, int width, int height,
                   int spriteX, int spriteY, int spriteW, int spriteH) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
        this.spriteW = spriteW;
        this.spriteH = spriteH;

        this.skillIcons = new Image[SKILL_NAMES.length];

        loadImages();
    }

    private void loadImages() {

        LoadImageSkill.loadSkillUI();
        skillUIBackground = LoadImageSkill.getSkillSprite(spriteX, spriteY, spriteW, spriteH);


        for (int i = 0; i < SKILL_NAMES.length; i++) {
            skillIcons[i] = LoadImageSkill.getImageSkillIcon(SKILL_NAMES[i]);
        }
    }


    public void render(GraphicsContext gc) {
        if (skillUIBackground != null) {
            gc.drawImage(skillUIBackground, x, y, width, height);
        }

        renderSkillIcons(gc);
    }


    private void renderSkillIcons(GraphicsContext gc) {
        for (int i = 0; i < skillIcons.length; i++) {
            if (skillIcons[i] != null) {
                int iconX = x + ICON_POSITIONS[i][0];
                int iconY = y + ICON_POSITIONS[i][1];

                // Draw the skill icon
                gc.drawImage(skillIcons[i], iconX, iconY, ICON_SIZE, ICON_SIZE);

                // Draw dark overlay if this icon is selected
                if (i == selectedSkillIndex) {
                    gc.setFill(DARK_OVERLAY_COLOR);
                    gc.fillRect(iconX, iconY, ICON_SIZE, ICON_SIZE);

                    // Draw selection border
                    gc.setStroke(SELECTION_BORDER_COLOR);
                    gc.setLineWidth(SELECTION_BORDER_WIDTH);
                    gc.strokeRect(iconX - 1, iconY - 1, ICON_SIZE + 2, ICON_SIZE + 2);
                }

                // Draw hover effect (lighter border)
                if (i == hoveredSkillIndex && i != selectedSkillIndex) {
                    gc.setStroke(Color.rgb(255, 255, 255, 0.5));
                    gc.setLineWidth(2);
                    gc.strokeRect(iconX, iconY, ICON_SIZE, ICON_SIZE);
                }
            }
        }
    }



    public int handleClick(int mouseX, int mouseY) {
        int clickedSkill = getSkillAtPosition(mouseX, mouseY);
        if (clickedSkill != -1) {
            // Toggle selection: if already selected, deselect it
            if (selectedSkillIndex == clickedSkill) {
                selectedSkillIndex = -1;
            } else {
                selectedSkillIndex = clickedSkill;
            }
        }
        return clickedSkill;
    }


    public void updateHover(int mouseX, int mouseY) {
        hoveredSkillIndex = getSkillAtPosition(mouseX, mouseY);
    }


    public void clearHover() {
        hoveredSkillIndex = -1;
    }


    private int getSkillAtPosition(int mouseX, int mouseY) {
        for (int i = 0; i < ICON_POSITIONS.length; i++) {
            int iconX = x + ICON_POSITIONS[i][0];
            int iconY = y + ICON_POSITIONS[i][1];

            // Check if mouse is within icon bounds
            if (mouseX >= iconX && mouseX <= iconX + ICON_SIZE &&
                mouseY >= iconY && mouseY <= iconY + ICON_SIZE) {
                return i;
            }
        }
        return -1;
    }

    public int getSelectedSkillIndex() {
        return selectedSkillIndex;
    }


    public String getSelectedSkillName() {
        if (selectedSkillIndex >= 0 && selectedSkillIndex < SKILL_NAMES.length) {
            return SKILL_NAMES[selectedSkillIndex];
        }
        return null;
    }


    public void deselectSkill() {
        selectedSkillIndex = -1;
    }


    public void selectSkill(int index) {
        if (index >= 0 && index < SKILL_NAMES.length) {
            selectedSkillIndex = index;
        }
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isMouseOver() { return mouseOver; }
    public boolean isMousePressed() { return mousePressed; }

    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
}
