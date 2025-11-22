package Button;

import Helper.LoadImages.LoadImageSkill;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SkillUI {
    private static final int ICON_SIZE = 42;

    private static final String[] SKILL_NAMES = {
        "DarkGhost", "SandStone", "ThunderBolt", "WaterStrike"
    };

    protected int x, y, width, height;
    protected boolean mouseOver = false;
    protected boolean mousePressed = false;

    private Image skillUIBackground;
    private Image[] skillIcons;

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

        if(skillIcons[0] != null){
            gc.drawImage(skillIcons[0], x + 29, y + 82, ICON_SIZE,ICON_SIZE );
        }
        if (skillIcons[1] != null){
            gc.drawImage(skillIcons[1], x + 29, y + 140, ICON_SIZE,ICON_SIZE );
        }
        if (skillIcons[2]!= null){
            gc.drawImage(skillIcons[2], x + 75, y + 140, ICON_SIZE,ICON_SIZE );
        }
        if (skillIcons[3]!= null){
            gc.drawImage(skillIcons[3], x + 75, y + 82, ICON_SIZE,ICON_SIZE );
        }
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isMouseOver() { return mouseOver; }
    public boolean isMousePressed() { return mousePressed; }

    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }
}
