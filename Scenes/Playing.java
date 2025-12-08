package Scenes;


import Button.SkillUI;
import Constant.TileConstant;
import Entities.Tower.Tower;
import Helper.LoadImages.LoadImageSkill;
import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Main.GameState;
import Managers.EnemyManager;
import Managers.TileManager;
import Managers.Tower.TowerManager;
import Managers.WaveManager;
import Map.LevelBuild;
import Map.Tile;
import Player.Player;
import Player.Skill.SkillAnimation;
import Sound.MusicManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Playing extends GameScene implements Render, SceneMethod {
    private static final int GRID_SIZE = 16;

	private final int[][] baseLvl;
	private final int[][] objectLvl;
	private int[][] lvl;
	private final TileManager tileManager;
    private final TowerManager towerManager;
    private WaveManager waveManager;
    private final Player player;
    private SkillUI skillUI;
    private final EnemyManager enemyManager;
    private final List<SkillAnimation> activeSkillAnimations = new ArrayList<>();
    private long lastUpdateTime = System.nanoTime();
    private int levelIndex = 0;

	private int mouseX, mouseY;

    // Skip wave button
    private final int bX = 150;
    private final int bY = 30;
    private final int bW = 100;
    private final int bH = 30;

    private int tick = 0;
    private int animationIndex = 0;

    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		baseLvl = LevelBuild.getFirstMapData();
        objectLvl = LevelBuild.getFirstObjectMapData();
        lvl = LevelBuild.getLevelData(levelIndex);

        initializeSkillUI();

        enemyManager= new EnemyManager(this);
		towerManager = new TowerManager(this);
        waveManager = new WaveManager(this);
        player = new Player(5000, 100); // for example
        loadLevel(levelIndex);
    }

    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex > 2) {
            System.out.println("GAME COMPLETED!");
            levelIndex = 2; // Loop back to start or go to Menu
        }
        loadLevel(levelIndex);
    }

    public void reset() {
        levelIndex = 0;
        player.setMoney(5000);
        player.setHealth(100);
        waveManager.reset();
        enemyManager.reset();
        loadLevel(levelIndex);
    }

    private void loadLevel(int index) {
        System.out.println("Loading Level Index: " + index);
        lvl = LevelBuild.getLevelData(index);
        enemyManager.reset();
        waveManager.reset();
    }

    private void initializeSkillUI() {

        skillUI = new SkillUI(
            1360, 50, (int)(100*1.5), 110*2,
            0, 0, 80, 95);

    }

    public void update() {
        long now = System.nanoTime();
        float dt = (now - lastUpdateTime) / 1_000_000f;
        lastUpdateTime = now;

        updateTick();
        towerManager.update();
        enemyManager.update(dt);

        // CHECK LOSE
        if (player.getHealth() <= 0) {
            game.getGameOver().setLose();
            GameState.SetGameState(GameState.GAME_OVER);
            MusicManager.getInstance().stopAll();
            return;
        }

        // CHECK WIN
        if (!waveManager.isThereMoreWaves() && waveManager.isWaveSpawningFinished()) {
            boolean isFinalLevel = (levelIndex == 2);
            game.getGameOver().setWin(isFinalLevel);
            GameState.SetGameState(GameState.GAME_OVER);
            MusicManager.getInstance().stopAll();
        }

        // Update skill animations
        if (!activeSkillAnimations.isEmpty()) {
            System.out.println("Updating " + activeSkillAnimations.size() + " active animations (dt=" + dt + ")");
        }

        Iterator<SkillAnimation> it = activeSkillAnimations.iterator();
        while (it.hasNext()) {
            SkillAnimation anim = it.next();
            anim.update(dt / 1000.0); // Convert to seconds
            if (anim.isFinished()) {
                it.remove();
                System.out.println("Animation finished and removed");
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
        renderSkillUI(gc);
        towerManager.draw(gc);
        drawPlayerStats(gc);
        drawWaveInfo(gc);
        enemyManager.draw(gc);
        drawSkipButton(gc);

        for (SkillAnimation anim : activeSkillAnimations) {
            anim.render(gc);
        }

        if (!towerManager.getMenu().isBuildMenuOpen() && !towerManager.getMenu().isUpgradeMenuOpen()) {
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

    private void drawWaveInfo(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Wave: " + waveManager.getWaveIndex() + " / " + waveManager.getTotalWaves(), 10, 90);

        if (waveManager.isWaveTimerStarted()) {
            float timeLeft = waveManager.getTimeLeft();
            gc.fillText("Next Wave in: " + String.format("%.1f", timeLeft), 10, 120);
        }
    }

    private void drawSkipButton(GraphicsContext gc) {
        boolean canSkip = waveManager.isThereMoreWaves() && waveManager.isWaveSpawningFinished();
        if (canSkip) {
            // background
            gc.setFill(Color.FORESTGREEN);
            gc.fillRect(bX, bY, bW, bH);
            // border
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeRect(bX, bY, bW, bH);
            // text
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gc.fillText("SKIP WAVE", bX + 5, bY + 20);
        }
    }

    @Override
	public void mouseClicked(int x, int y) {
        // Check if clicking skip wave button
        boolean canSkip = waveManager.isThereMoreWaves() && waveManager.isWaveSpawningFinished();
        if (canSkip && x >= bX && x <= bX + bW && y >= bY && y <= bY + bH) {
            waveManager.skipWave();
            return;
        }

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
        if (towerManager.getMenu().isUpgradeMenuOpen()) {
            towerManager.getMenu().handleUpgradeMenuClick(x, y);
            return;
        }

        if (towerManager.getMenu().isBuildMenuOpen()) {
            towerManager.getMenu().handleBuildMenuClick(x, y);
            return;
        }

		int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;
        int clickedPixelX = tileX * GRID_SIZE;
        int clickedPixelY = tileY * GRID_SIZE;

        Tower clickedTower = towerManager.getTowerAt(clickedPixelX, clickedPixelY);
        if (clickedTower != null) {
            towerManager.getMenu().openUpgradeMenu(clickedTower);
            return;
        }

        if (isTilePlaceable(x, y) && !towerManager.isOccupied(clickedPixelX, clickedPixelY)) {
            towerManager.getMenu().openBuildMenu(clickedPixelX, clickedPixelY);
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

        towerManager.getMenu().handleMouseMoved(x, y);
	}

	@Override
	public void mousePressed(int x, int y) {

		towerManager.getMenu().handleMousePressed(x, y);
	}

	@Override
	public void mouseReleased(int x, int y) {
		towerManager.getMenu().handleMouseReleased(x, y);
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
        for (int y=0; y < baseLvl.length; y++){
            for (int x=0; x < baseLvl[y].length; x++){
                int id= baseLvl[y][x];
                Tile t = tileManager.getTile(id);
                if (t == null) continue;


                if (t.hasAnimation()) {
                     gc.drawImage(t.getSprite(animationIndex), x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                } else {
                     gc.drawImage(t.getSprite(), x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                }
            }
        }


        for (int y = 0; y < objectLvl.length; y++){
            for (int x = 0; x < objectLvl[y].length; x++){
                int id = objectLvl[y][x]; // Lấy ID của Object Tile

                // Chỉ vẽ nếu ô đó KHÔNG phải là EMPTY_TILE (-1)
                if (id != LevelBuild.emptyTile){
                    Tile t = tileManager.getTile(id);
                    if (t == null) continue;

                    double drawX = x * GRID_SIZE;
                    double drawY = y * GRID_SIZE;
                    Image sprite = t.getSprite();

                    if (sprite == null) continue;

                    if (id == TileConstant.BOSS.getId() || id == TileConstant.OCTOPUS.getId()) {
                        // Tính toán tâm của ô Tile (16x16)
                        double centerX = x * GRID_SIZE + (GRID_SIZE / 2.0);
                        double centerY = y * GRID_SIZE + (GRID_SIZE / 2.0);

                        // Tính toán vị trí vẽ để tâm Boss trùng tâm Tile
                        double bossX = centerX - (sprite.getWidth() / 2.0);
                        double bossY = centerY - (sprite.getHeight() / 2.0);

                        // Vẽ Boss kích thước thật
                        gc.drawImage(sprite, bossX, bossY, sprite.getWidth(), sprite.getHeight());
                    }

                    // Nếu là CÂY, điều chỉnh vị trí Y để nó nằm đúng
                    else if (id == TileConstant.TREE.getId() && sprite != null && sprite.getHeight() > GRID_SIZE) {
                        double yOffset = sprite.getHeight() - GRID_SIZE;
                        drawY -= yOffset; // Đẩy sprite lên trên

                        // Vẽ cây với kích thước thật (sẽ to hơn 16x16)
                        gc.drawImage(sprite, drawX, drawY, sprite.getWidth(), sprite.getHeight());

                    } else if (sprite != null) {
                        // Vẽ các vật thể 16x16 khác
                        gc.drawImage(sprite, drawX, drawY, GRID_SIZE, GRID_SIZE);
                    }
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
    
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

	private boolean isTilePlaceable(int x, int y) {
        int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;

        if (tileY < 0 || tileY >= baseLvl.length || tileX < 0 || tileX >= baseLvl[0].length) {
            return false;
        }

        // Kiểm tra Layer Vật thể (Object Layer)
        int objectId = objectLvl[tileY][tileX];
        if (objectId != LevelBuild.emptyTile && !tileManager.getTile(objectId).canPlaceTower()) {
            return false;
        }

        // Kiểm tra Layer Nền (Base Layer)
        int baseId = baseLvl[tileY][tileX];
        Tile t = tileManager.getTile(baseId);
        if (t == null) return false;
        
        return t.canPlaceTower(); 
    }

    public int getTileTypeAt(int x, int y) {
        int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;

        if (tileY < 0 || tileY >= baseLvl.length || tileX < 0 || tileX >= baseLvl[0].length) {
            return -1;
        }

        return baseLvl[tileY][tileX];
    }

    public boolean isTileWalkable(int x, int y) {
        int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;

        if (tileY < 0 || tileY >= baseLvl.length || tileX < 0 || tileX >= baseLvl[0].length) {
            return false;
        }

        int id = baseLvl[tileY][tileX];
        Tile t = tileManager.getTile(id);
        if (t == null) return false;

        return t.isWalkable();
    }
}