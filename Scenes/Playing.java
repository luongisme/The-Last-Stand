package Scenes;

import Button.SkillUI;
import Constant.TileConstant;
import Entities.Towers.Tower;
import Helper.LoadImages.LoadImageSkill;
import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Main.GameState;
import Managers.EnemyManager;
import Managers.ProjectileManager;
import Managers.TileManager;
import Managers.Tower.TowerManager;
import Managers.WaveManager;
import Map.LevelBuild;
import Map.Tile;
import Player.Player;
import Player.Skill.Skill;
import Player.Skill.AreaEffectSkill;
import Player.Skill.WaterSplash;
import Player.Skill.SandStone;
import Player.Skill.ThunderBolt;
import Player.Skill.WaterStrike;
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

    private static final double SKILL_ANIMATION_WIDTH = 128;
    private static final double SKILL_ANIMATION_HEIGHT = 128;

	private int[][] baseLvl;      // Removed final to allow level changes
	private int[][] objectLvl;    // Removed final to allow level changes
	private int[][] lvl;
	private final TileManager tileManager;
    private final TowerManager towerManager;
    private WaveManager waveManager;
    private final Player player;
    private SkillUI skillUI;
    private final EnemyManager enemyManager;
    private final List<Skill> activeSkills = new ArrayList<>();
    private long lastUpdateTime = System.nanoTime();
    private int levelIndex = 0;
    private ProjectileManager projectileManager;

	private int mouseX, mouseY;

    // Skip wave button
    private final int bX = 150;
    private final int bY = 30;
    private final int bW = 100;
    private final int bH = 30;
    private int rawMouseX, rawMouseY;

    private int tick = 0;
    private int animationIndex = 0;

    // Skill instances for damage and radius info
    private final AreaEffectSkill[] skills = {
        new WaterSplash(),   // DarkGhost - index 0
        new SandStone(),     // index 1
        new ThunderBolt(),   // index 2
        new WaterStrike()    // index 3
    };

    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		baseLvl = LevelBuild.getFirstMapData();
        objectLvl = LevelBuild.getFirstObjectMapData();
        lvl = LevelBuild.getLevelData(levelIndex);

        initializeSkillUI();

        enemyManager= new EnemyManager(this);
		towerManager = new TowerManager(this);
        projectileManager = new ProjectileManager(this);
        player = new Player(200, 3);
        waveManager = new WaveManager(this);
        loadLevel(levelIndex);
    }

    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex > 2) {
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
        lvl = LevelBuild.getLevelData(index);
        baseLvl = LevelBuild.getLevelData(index);
        objectLvl = LevelBuild.getObjectMapData(index);
        enemyManager.reset();
        waveManager.reset();
        towerManager.reset();
        projectileManager.reset();
    }

    private void initializeSkillUI() {
        skillUI = new SkillUI(
            1360, 50, (int)(100*1.5), 110*2,
            0, 0, 80, 95);

        // Set skills reference so UI can display cooldown and cost
        skillUI.setSkills(skills);
    }

    public void update() {
        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
        lastUpdateTime = now;

        updateTick();
        projectileManager.update();
        towerManager.update();
        enemyManager.update((float)(dt * 1000)); // Convert to milliseconds for enemyManager

        // Update skill cooldowns
        for (AreaEffectSkill skill : skills) {
            skill.updateCoolDown(dt);
        }

        // CHECK LOSE
        if (player.getHealth() <= 0) {
            game.getGameOver().setLose();
            GameState.SetGameState(GameState.GAME_OVER);
            MusicManager.getInstance().stopAll();
            return;
        }

        // CHECK WIN - Show GAME_OVER screen when level is complete
        if (!waveManager.isThereMoreWaves() && waveManager.isWaveSpawningFinished() && enemyManager.getEnemies().isEmpty()) {
            boolean isFinalLevel = (levelIndex == 2);
            game.getGameOver().setWin(isFinalLevel);
            GameState.SetGameState(GameState.GAME_OVER);
            MusicManager.getInstance().stopAll();
        }

        // Update skill animations
        if (!activeSkills.isEmpty()) {
            System.out.println("Updating " + activeSkills.size() + " active animations (dt=" + dt + "s)");
        }

        Iterator<Skill> it = activeSkills.iterator();
        while (it.hasNext()) {
            Skill anim = it.next();
            anim.update(dt); // dt is already in seconds

            // Deal damage to enemies if not already dealt AND delay has passed
            if (!anim.hasDealtDamage() && anim.isReadyToDealDamage()) {
                dealSkillDamageToEnemies(anim);
                anim.setHasDealtDamage(true);
            }

            if (anim.isFinished()) {
                it.remove();
                System.out.println("Animation finished and removed");
            }
        }
    }

    private void dealSkillDamageToEnemies(Skill skill) {
        double skillCenterX = skill.getCenterX();
        double skillCenterY = skill.getCenterY();
        double skillRadius = skill.getRadius();
        int skillDamage = skill.getDamage();
        int skillType = skill.getSkillType();


        for (Entities.Enemies.Enemy enemy : enemyManager.getEnemies()) {
            if (!enemy.getIsAlive()) continue;

            // Calculate distance between skill center and enemy center
            double enemyCenterX = enemy.getX() + enemy.getFrameW() / 2.0;
            double enemyCenterY = enemy.getY() + enemy.getFrameH() / 2.0;

            double distance = Math.sqrt(
                Math.pow(skillCenterX - enemyCenterX, 2) +
                Math.pow(skillCenterY - enemyCenterY, 2)
            );

            // If enemy is within skill radius, deal damage
            if (distance <= skillRadius) {
                int currentHealth = enemy.getEnemyHealth();
                int newHealth = currentHealth - skillDamage;
                enemy.setEnemyHealth(newHealth);
                enemy.setHit(true);

                // Apply stun effect if ThunderBolt (skillType = 2)
                if (skillType == 2) {
                    Logic.Effects.StunEffect stunEffect = new Logic.Effects.StunEffect(2.0f); // 2 seconds stun
                    enemy.applyStatus(stunEffect);
                    System.out.println("Applied stun effect to enemy!");
                }

                if (newHealth <= 0) {
                    enemy.setAlive(false);
                    player.addMoney(enemy.getRewardGold());
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
        towerManager.draw(gc);

        enemyManager.draw(gc);
        projectileManager.draw(gc);
        renderSkillUI(gc);
        drawPlayerStats(gc);
        drawWaveInfo(gc);
        enemyManager.draw(gc);
        drawSkipButton(gc);

        for (Skill anim : activeSkills) {
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
        if (towerManager.getTowerAtPixel(rawMouseX, rawMouseY) != null) {
            return;
        }

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


    private void drawPlayerStats(GraphicsContext gc) {
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gc.setLineWidth(3);

        // Level display - ADDED
        String levelText = "Level: " + (levelIndex + 1);
        gc.setStroke(Color.BLACK);
        gc.strokeText(levelText, 10, 90);
        gc.setFill(Color.CYAN);
        gc.fillText(levelText, 10, 90);

        // Money display
        String moneyText = "Money: $" + player.getMoney();
        gc.setStroke(Color.BLACK);
        gc.strokeText(moneyText, 10, 30);
        gc.setFill(Color.GOLD);
        gc.fillText(moneyText, 10, 30);

        // Health display
        String healthText = "Health: " + player.getHealth();
        gc.setStroke(Color.BLACK);
        gc.strokeText(healthText, 10, 60);
        gc.setFill(Color.LIMEGREEN);
        gc.fillText(healthText, 10, 60);
    }

    private void drawWaveInfo(GraphicsContext gc) {
        // Draw wave number
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        String waveText = "Wave: " + waveManager.getWaveIndex() + " / " + waveManager.getTotalWaves();
        gc.strokeText(waveText, 10, 90);
        gc.fillText(waveText, 10, 90);

        if (waveManager.isWaveTimerStarted()) {
            float timeLeft = waveManager.getTimeLeft();
            String timerText = "Next Wave in: " + String.format("%.1f", timeLeft) + "s";

            gc.setFill(Color.YELLOW);
            gc.strokeText(timerText, 10, 120);
            gc.fillText(timerText, 10, 120);
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
        if (handleSkillUIClick(x, y)) return; // Don't process map clicks when clicking skill UI

        // Check if a skill is selected and we're clicking on the map to cast it
        if (handleSkillCasting(x, y)) return;

        // Normal tower placement logic
        if (handleTowerMenuClick(x, y)) return;

		handleMapClick(x, y);
	}

    private boolean handleSkillUIClick(int x, int y) {
        // Check if clicking skip wave button
        boolean canSkip = waveManager.isThereMoreWaves() && waveManager.isWaveSpawningFinished();
        if (canSkip && x >= bX && x <= bX + bW && y >= bY && y <= bY + bH) {
            waveManager.skipWave();
            return true;
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
            return true;
        }
        return false;
    }

    private boolean handleSkillCasting(int x, int y) {
        int selectedSkill = skillUI.getSelectedSkillIndex();
        if (selectedSkill != -1) {
            // A skill is selected, cast it at the clicked position
            System.out.println("Casting skill " + selectedSkill + " at (" + x + ", " + y + ")");
            castSkill(selectedSkill, x, y);

            // Deselect the skill after casting
            skillUI.deselectSkill();
            System.out.println("Skill cast complete, deselected");
            return true;
        }
        return false;
    }

    private boolean handleTowerMenuClick(int x, int y) {
        if (towerManager.getMenu().isUpgradeMenuOpen()) {
            towerManager.getMenu().handleUpgradeMenuClick(x, y);
            return true;
        }

        if (towerManager.getMenu().isBuildMenuOpen()) {
            towerManager.getMenu().handleBuildMenuClick(x, y);
            return true;
        }
        return false;
    }

    private void handleMapClick(int x, int y) {
        Tower clickedTower = towerManager.getTowerAtPixel(x, y);
        if (clickedTower != null) {
            towerManager.getMenu().openUpgradeMenu(clickedTower);
            return;
        }

        int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;
        int clickedPixelX = tileX * GRID_SIZE;
        int clickedPixelY = tileY * GRID_SIZE;

        if (isTilePlaceable(x, y) && !towerManager.isOccupied(clickedPixelX, clickedPixelY)) {
            towerManager.getMenu().openBuildMenu(clickedPixelX, clickedPixelY);
        }
    }

    private void castSkill(int skillIndex, int x, int y) {
        // Get skill info for damage and radius
        AreaEffectSkill skillInfo = skills[skillIndex];

        // Check if skill is on cooldown
        if (!skillInfo.isOffCooldown()) {
            System.out.println("Skill " + skillInfo.getName() + " is on cooldown! " +
                String.format("%.1f", skillInfo.getCurrentCooldown()) + "s remaining");
            return;
        }

        // Check if player has enough money
        int skillCost = skillInfo.getCost();
        if (player.getMoney() < skillCost) {
            System.out.println("Not enough money! Need " + skillCost + " but have " + player.getMoney());
            return;
        }

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

        double w = SKILL_ANIMATION_WIDTH;
        double h = SKILL_ANIMATION_HEIGHT;
        double frameDuration = 0.08; // 100ms per frame

        double radius = skillInfo.getRadius();
        int damage = skillInfo.getDamage();

        Skill anim = new Skill(
                frames,
                frameDuration,
                x - w/2.0,
                y - h/2.0,
                w, h,
                radius,
                damage,
                skillIndex  // Pass skill type so we know which skill was cast
        );

        activeSkills.add(anim);

        // Deduct money and start cooldown
        player.spendMoney(skillCost);
        skillInfo.useSkill();


    }


    @Override
	public void mouseMoved(int x, int y) {
        this.rawMouseX = x;
        this.rawMouseY = y;

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

                    } else if (id == TileConstant.DOOR.getId()) {
                        // Vẽ cửa với kích thước 3x3 tiles
                        gc.drawImage(sprite, drawX, drawY, GRID_SIZE * 3, GRID_SIZE * 3);

                    } else if (id == TileConstant.WALL.getId()
                            || id == TileConstant.WALL1.getId()
                            || id == TileConstant.WALLLAST1.getId()
                            || id == TileConstant.WALLLAST2.getId()) {
                        // Vẽ tường với chiều cao 3 tiles
                        gc.drawImage(sprite, drawX, drawY, GRID_SIZE, GRID_SIZE * 3);

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

    public int[][] getLvlData() { return lvl;}

    public AreaEffectSkill[] getSkills() { return skills; }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }

    public EnemyManager getEnemyManager() {
        return  enemyManager;
    }

    // Trong Playing.java
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }

    public int getRawMouseX() { return rawMouseX; }
    public int getRawMouseY() { return rawMouseY; }
}