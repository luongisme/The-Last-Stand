package Scene;

import Player.Player;
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
import Constant.TileConstant;
import Entities.Tower.Tower;

public class Playing extends GameScene implements Render, SceneMethod {
    private final int GRID_SIZE = 16;

	private int[][] baseLvl;
    private int[][] objectLvl;
	private TileManager tileManager;
    private TowerManager towerManager;
    private Player player;

	private int mouseX, mouseY;

    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		baseLvl = LevelBuild.getThirdMapData();
        objectLvl = LevelBuild.getThirdObjectMapData();
		towerManager = new TowerManager(this);
        player = new Player(5000, 100); // for example
    }

    public void update() {
        updateTick();
        towerManager.update();
    }

    @Override
    public void render(GraphicsContext gc) {
        drawLevel(gc);
		updateTick();

		towerManager.draw(gc);

        drawPlayerStats(gc);

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
    
    private void drawPlayerStats(GraphicsContext gc) { // Demo
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Money: " + player.getMoney(), 10, 30);
        
        gc.setFill(Color.RED);
        gc.fillText("Health: " + player.getHealth(), 10, 60);
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
                    
                    // Nếu là CÂY, điều chỉnh vị trí Y để nó nằm đúng
                    if (id == TileConstant.TREE.getId() && sprite != null && sprite.getHeight() > GRID_SIZE) {
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
    
    private boolean isTilePlaceable(int x, int y) {
        int tileX = x / GRID_SIZE;
        int tileY = y / GRID_SIZE;

        // >>> START ADD/CHANGE: Sửa kiểm tra biên
        if (tileY < 0 || tileY >= baseLvl.length || tileX < 0 || tileX >= baseLvl[0].length) {
            return false;
        }
        // >>> END ADD/CHANGE: Sửa kiểm tra biên

        // >>> START ADD/CHANGE: Kiểm tra Layer Vật thể (Object Layer) <<<
        int objectId = objectLvl[tileY][tileX];
        if (objectId != LevelBuild.emptyTile && !tileManager.getTile(objectId).canPlaceTower()) {
            return false;
        }
        // >>> END ADD/CHANGE: Kiểm tra Layer Vật thể
        
        // Kiểm tra Layer Nền (Base Layer)
        int baseId = baseLvl[tileY][tileX]; // Đổi từ 'id' thành 'baseId'
        Tile t = tileManager.getTile(baseId); 
        if (t == null) return false;
        
        return t.canPlaceTower(); 
    }

}

