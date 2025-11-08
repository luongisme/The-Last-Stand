package Scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import Player.Player;
import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Managers.TileManager;
import Managers.TowerManager;
import Map.LevelBuild;
import Map.Tile;
import Entities.Tower.Tower;

public class Playing extends GameScene implements Render, SceneMethod {
    private final int GRID_SIZE = 16;

	private int[][] lvl;
	private TileManager tileManager;
    private TowerManager towerManager;
    private Player player;

	private int mouseX, mouseY;

    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		lvl = new LevelBuild().getFirstMapData();

		towerManager = new TowerManager(this);
        player = new Player(5000, 100); // for example
    }

    public void update() {
        updateTick();
        towerManager.update();
    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
		updateTick();

		towerManager.draw(g);

        drawPlayerStats(g);

        if (!towerManager.isBuildMenuOpen() && !towerManager.isUpgradeMenuOpen()) {
            drawHighlight(g);
        }
    }

    private void drawHighlight(Graphics g) {
        if (isTilePlaceable(mouseX, mouseY) && towerManager.getTowerAt(mouseX, mouseY) == null) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.RED);
        }
        g.drawRect(mouseX, mouseY, GRID_SIZE, GRID_SIZE);

        if (towerManager.getTowerAt(mouseX, mouseY) != null) {
            g.setColor(Color.GREEN);
            g.drawRect(mouseX, mouseY, GRID_SIZE, GRID_SIZE);
        }
    }
    
    private void drawPlayerStats(Graphics g) { // Demo
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Money: " + player.getMoney(), 10, 30);
        
        g.setColor(Color.RED);
        g.drawString("Health: " + player.getHealth(), 10, 60);
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
            if (animationIndex>=10){
                animationIndex=0;
            }
        }
    }

	public void drawLevel(Graphics g){
        // go through all the tile
        for (int y=0;y<lvl.length;y++){
            for (int x=0;x<lvl[y].length;x++){
                int id=lvl[y][x];
				Tile t = tileManager.getTile(id);
				if (t == null) continue;

				if (t.hasAnimation()) {
                     g.drawImage(t.getSprite(animationIndex), x * GRID_SIZE, y * GRID_SIZE, null); //
                } else {
                     g.drawImage(t.getSprite(), x * GRID_SIZE, y * GRID_SIZE, null); //
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