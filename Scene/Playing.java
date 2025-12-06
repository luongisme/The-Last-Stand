package Scene;

import Player.Player;
import Sound.MusicManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Main.GameState;
import Managers.TileManager;
import Managers.TowerManager;
import Managers.WaveManager;
import Map.LevelBuild;
import Map.Tile;
import Entities.Tower.Tower;
import Managers.EnemyManager;

import Constant.EntityConstant;

public class Playing extends GameScene implements Render, SceneMethod {
    private final int GRID_SIZE = 16;

	private int[][] lvl;
	private TileManager tileManager;
    private TowerManager towerManager;
    private WaveManager waveManager;
    private Player player;
    private EnemyManager enemyManager;
    private long lastUpdateTime = System.nanoTime();
    private boolean levelSwitched = false;
    private int levelIndex = 0;
	private int mouseX, mouseY;

    private int bX = 150; // Adjust these to fit your UI
    private int bY = 30;
    private int bW = 100;
    private int bH = 30;

    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		lvl = LevelBuild.getFirstMapData();

		towerManager = new TowerManager(this);
        enemyManager = new EnemyManager(this);
        waveManager = new WaveManager(this);
        player = new Player(5000, 100); // for example
        loadLevel(levelIndex);
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
        player = new Player(5000, 100); 
        waveManager.reset();
        enemyManager.reset();
        loadLevel(levelIndex);
    }


    private void loadLevel(int index) {
        System.out.println("Loading Level Index: " + index);
        lvl = LevelBuild.getLevelData(index);
        enemyManager.reset();
        //towerManager.reset();
        waveManager.reset(); 
    }

    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
		updateTick();
        towerManager.draw(gc);
        drawInfo(gc);
        enemyManager.draw(gc);
        drawSkipButton(gc);

        if (!towerManager.isBuildMenuOpen() && !towerManager.isUpgradeMenuOpen()) {
            drawHighlight(gc);
        }
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
    
    private void drawInfo(GraphicsContext gc) { 
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Money: " + player.getMoney(), 10, 30);
        
        gc.setFill(Color.RED);
        gc.fillText("Health: " + player.getHealth(), 10, 60);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Wave: " + waveManager.getWaveIndex() + " / " + waveManager.getTotalWaves(), 10, 90);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Next Wave in : ", 10, 120);
        if (waveManager.isWaveTimerStarted()) {
            float timeLeft = waveManager.getTimeLeft();
            gc.fillText("Next Wave in : " + String.format("%.1f", timeLeft), 10, 120);
        }
    }

    @Override
	public void mouseClicked(int x, int y) {
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

        boolean canSkip = waveManager.isThereMoreWaves() && waveManager.isWaveSpawningFinished();
        if (canSkip) {
            if (x >= bX && x <= bX + bW && y >= bY && y <= bY + bH) {
                waveManager.skipWave();
                return;
            }
        }
	}

	@Override
	public void mouseMoved(int x, int y) {
		mouseX = (x / GRID_SIZE) * GRID_SIZE;
        mouseY = (y / GRID_SIZE) * GRID_SIZE;

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
		// Implement mouseDragged method
	}

	public void updateTick(){
        tick++;
        if (tick>=20){
            tick=0;
            animationIndex++;
            if (animationIndex>=4){
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
    
    public WaveManager getWaveManager(){
        return waveManager;
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

    public int getLevelIndex() { return levelIndex; }
}