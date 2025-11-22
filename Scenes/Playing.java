package Scenes;


import Button.SkillUI;
import Helper.LoadImages.LoadImageSkill;
import Player.Skill.SkillAnimation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Managers.TileManager;
import Managers.TowerManager;
import Map.LevelBuild;
import Map.Tile;
import Entities.Tower.Tower;
import Player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Playing extends GameScene implements Render, SceneMethod {
    private static final int GRID_SIZE = 16;

	private int[][] lvl;
	private TileManager tileManager;
    private TowerManager towerManager;
    private Player player;
    private SkillUI skillUI;
    private List<SkillAnimation> activeSkillAnimations = new ArrayList<>();


	private int mouseX, mouseY;


    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		lvl = LevelBuild.getFirstMapData();

        initializeSkillUI();

		towerManager = new TowerManager(this);
        player = new Player(5000, 100); // for example
    }


    private void initializeSkillUI() {

        skillUI = new SkillUI(
            1360, 50, (int)(100*1.5), (int)(110*2),
            0, 0, 80, 95);

    }

    public void update() {
        updateTick();
        towerManager.update();
        double dt = 0.016; // ~60 FPS (16ms per frame)

        if (!activeSkillAnimations.isEmpty()) {
            System.out.println("🔄 Updating " + activeSkillAnimations.size() + " active animations (dt=" + dt + ")");
        }

        Iterator<SkillAnimation> it = activeSkillAnimations.iterator();
        while (it.hasNext()) {
            SkillAnimation anim = it.next();
            anim.update(dt);
            if (anim.isFinished()) {
                it.remove();
                System.out.println("✅ Animation finished and removed");
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
        renderSkillUI(gc);
        towerManager.draw(gc);
        drawPlayerStats(gc);

        for (SkillAnimation anim : activeSkillAnimations) {
            anim.render(gc);
        }

        if (!towerManager.isBuildMenuOpen() && !towerManager.isUpgradeMenuOpen()) {
            drawHighlight(gc);
        }
    }


    private void renderSkillUI(GraphicsContext gc) {
        skillUI.render(gc);
    }

    private void drawHighlight(GraphicsContext gc) {
        if (isTilePlaceable(mouseX, mouseY) && towerManager.getTowerAt(mouseX, mouseY) == null) {
            gc.setStroke(Color.WHITE);
        } else {
            gc.setStroke(Color.RED);
        }
        gc.setLineWidth(2);
        gc.strokeRect(mouseX, mouseY, GRID_SIZE, GRID_SIZE);

        if (towerManager.getTowerAt(mouseX, mouseY) != null) {
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(2);
            gc.strokeRect(mouseX, mouseY, GRID_SIZE, GRID_SIZE);
        }
    }


    private void drawPlayerStats(GraphicsContext gc) { // Demo
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Money: " + player.getMoney(), 10, 30);
        
        gc.setFill(Color.RED);
        gc.fillText("Health: " + player.getHealth(), 10, 60);
    }

    @Override
	public void mouseClicked(int x, int y) {
        // Check if clicking on skill icon to select/deselect
        int clickedSkill = skillUI.handleClick(x, y);
        if (clickedSkill != -1) {
            // Clicked on skill icon area
            String skillName = skillUI.getSelectedSkillName();
            if (skillName != null) {
                System.out.println("Skill selected: " + skillName + " (index: " + clickedSkill + ")");
                System.out.println("Now click on the map to cast the skill!");
            } else {
                System.out.println("Skill deselected");
            }
            return; // Don't process map clicks when clicking skill UI
        }

        // Check if a skill is selected and we're clicking on the map to cast it
        int selectedSkill = skillUI.getSelectedSkillIndex();
        if (selectedSkill != -1) {
            // A skill is selected, cast it at the clicked position
            System.out.println("Casting skill " + selectedSkill + " at (" + x + ", " + y + ")");
            castSkill(selectedSkill, x, y);

            // Deselect the skill after casting
            skillUI.deselectSkill();
            System.out.println("Skill cast complete, deselected");
            return;
        }

        // Normal tower placement logic
        if (towerManager.isUpgradeMenuOpen()) {
            towerManager.handleUpgradeMenuClick(x, y);
            return;
        }

		if (towerManager.isBuildMenuOpen()) {
            towerManager.handleBuildMenuClick(x, y);
            return;
        }

		int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;
        int clickedPixelX = tileX * GRID_SIZE;
        int clickedPixelY = tileY * GRID_SIZE;

        Tower clickedTower = towerManager.getTowerAt(clickedPixelX, clickedPixelY);
        if (clickedTower != null) {
            towerManager.openUpgradeMenu(clickedTower);
            return;
        }

        if (isTilePlaceable(x, y) && towerManager.getTowerAt(clickedPixelX, clickedPixelY) == null) {
            towerManager.openBuildMenu(clickedPixelX, clickedPixelY);
        }
	}

    private void castSkill(int skillIndex, int x, int y) {
        Image[] frames = null;

        switch (skillIndex) {
            case 0: frames = LoadImageSkill.loadDarkGhostAnim(); break;
            case 1: frames = LoadImageSkill.loadSandStoneAnim(); break;
            case 2: frames = LoadImageSkill.loadThunderBoltAnim(); break;
            case 3: frames = LoadImageSkill.loadWaterStrikeAnim(); break;
        }

        if (frames == null) {
            System.out.println(" WARNING: Frames null for skill: " + skillIndex);
            return;
        }

        System.out.println(" Skill " + skillIndex + " loaded with " + frames.length + " frames");

        double w = 96;
        double h = 96;
        double frameDuration = 0.05; // 100ms per frame

        SkillAnimation anim = new SkillAnimation(
                frames,
                frameDuration,
                x - w/2.0,
                y - h/2.0,
                w, h
        );

        activeSkillAnimations.add(anim);
        System.out.println("✅ Animation added at (" + x + ", " + y + ")");
        System.out.println("✅ Total active animations: " + activeSkillAnimations.size());
    }


    @Override
	public void mouseMoved(int x, int y) {
		mouseX = (x / GRID_SIZE) * GRID_SIZE;
        mouseY = (y / GRID_SIZE) * GRID_SIZE;

        // Update hover state for skill UI
        skillUI.updateHover(x, y);

        towerManager.handleMouseMoved(x, y);
	}

	@Override
	public void mousePressed(int x, int y) {

		towerManager.handleMousePressed(x, y);
	}

	@Override
	public void mouseReleased(int x, int y) {
		towerManager.handleMouseReleased(x, y);
	}

	@Override
	public void mouseDragged(int x, int y) {

	}


	public void updateTick(){
        tick++;
        if (tick>=20){
            tick=0;
            animationIndex++;
            if (animationIndex>=10){
                animationIndex=0;
            }
        }
    }

	public void drawLevel(GraphicsContext gc){
        // go through all the tile
        for (int y=0;y<lvl.length;y++){
            for (int x=0;x<lvl[y].length;x++){
                int id=lvl[y][x];
				Tile t = tileManager.getTile(id);
				if (t == null) continue;

				if (t.hasAnimation()) {
                     gc.drawImage(t.getSprite(animationIndex), x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                } else {
                     gc.drawImage(t.getSprite(), x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                }
			}
        }
    }
    
	private boolean checkAnimation(int spriteID){
		return tileManager != null && tileManager.checkSpriteAnimation(spriteID);
    }

	public TileManager getTileManager(){
		return tileManager;
    }

	public Player getPlayer() {
        return player;
    }
    
	private boolean isTilePlaceable(int x, int y) {
        int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;

        if (tileY < 0 || tileY >= lvl.length || tileX < 0 || tileX >= lvl[0].length) {
            return false;
        }

        int id = lvl[tileY][tileX];
        Tile t = tileManager.getTile(id); 
        if (t == null) return false;
        
        return t.canPlaceTower(); 
    }

    
}