package Button;

import Helper.LoadImages.LoadImageSkill;
import Player.Skill.AreaEffectSkill;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SkillUI {
    private static final int ICON_SIZE = 42;

    // Selection visual effects
    private static final Color SELECTION_BORDER_COLOR = Color.rgb(255, 215, 0); // Gold color
    private static final Color DARK_OVERLAY_COLOR = Color.rgb(0, 0, 0, 0.3); // Semi-transparent black
    private static final int SELECTION_BORDER_WIDTH = 3;

    private static final String[] SKILL_NAMES = {
        "WaterSplash", "SandStone", "ThunderBolt", "WaterStrike"
    };

    // Icon positions (relative to skill UI position)
    private static final int[][] ICON_POSITIONS = {
        {29, 82},   // WaterSplash (top-left)
        {29, 140},  // SandStone (bottom-left)
        {75, 140},  // ThunderBolt (bottom-right)
        {75, 82}    // WaterStrike (top-right)
    };

    protected int x, y, width, height;
    protected boolean mouseOver = false;
    protected boolean mousePressed = false;

    private Image skillUIBackground;
    private final Image[] skillIcons;
    private AreaEffectSkill[] skills; // Reference to skills for cooldown/cost info

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

    public void setSkills(AreaEffectSkill[] skills) {
        this.skills = skills;
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

                // Draw cooldown overlay if skill is on cooldown
                if (skills != null && i < skills.length) {
                    AreaEffectSkill skill = skills[i];

                    if (!skill.isOffCooldown()) {
                        // Semi-transparent dark overlay
                        gc.setFill(Color.rgb(0, 0, 0, 0.6));
                        gc.fillRect(iconX, iconY, ICON_SIZE, ICON_SIZE);

                        // Cooldown timer text
                        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                        gc.setFill(Color.WHITE);
                        String cooldownText = String.format("%.1f", skill.getCurrentCooldown());
                        gc.fillText(cooldownText, iconX + 8, iconY + 25);
                    }

                    // Draw cost at bottom of icon
                    gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                    gc.setFill(Color.GOLD);
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(2);
                    String costText = "$" + skill.getCost();
                    gc.strokeText(costText, iconX + 4, iconY + ICON_SIZE - 2);
                    gc.fillText(costText, iconX + 4, iconY + ICON_SIZE - 2);
                }

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
