package Scene;

import static Constant.EntityConstant.GOBLIN;
import static Constant.EntityConstant.GOBLIN_BOSS;
import static Constant.EntityConstant.SKELETON;

import java.awt.Graphics;

import Map.LevelBuild;
import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Managers.EnemyManager;
import Managers.TileManager;
import Map.Tile;


public class Playing extends GameScene implements Render, SceneMethod {
	private int[][] lvl;
	private TileManager tileManager;
	private Tile selectedTile;
	private boolean drawSelect = false;
	private int mouseX, mouseY;
	private EnemyManager enemyManager;

    public Playing(Game game){
        super(game);
		tileManager = new TileManager();
		lvl = new LevelBuild().getFirstMapData();
		enemyManager = new EnemyManager(this);

    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
		updateTick();
		drawSelectedTile(g);
		enemyManager.draw(g);
    }

    @Override
	public void mouseClicked(int x, int y) {
		// Implement mouseClicked method
	}

	@Override
	public void mouseMoved(int x, int y) {
		// Implement mouseMoved method
	}

	@Override
	public void mousePressed(int x, int y) {
		// Implement mousePressed method
		enemyManager.addEnemy(x, y, GOBLIN);
		enemyManager.addEnemy(x-75, y, SKELETON);
		enemyManager.addEnemy(x+75, y, GOBLIN_BOSS);
	}

	@Override
	public void mouseReleased(int x, int y) {
		// Implement mouseReleased method
	}

	@Override
	public void mouseDragged(int x, int y) {
		// Implement mouseDragged method
	}

	private void drawSelectedTile(Graphics g) {
        if(selectedTile != null && drawSelect){
            g.drawImage(selectedTile.getSprite(), mouseX, mouseY, 16, 16, null);
        }
    }

	public void update(){
		float dt = 16f;
		enemyManager.update(dt);
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
					g.drawImage(t.getSprite(), x*16, y*16, null);
				}
            }
        }
    
	private boolean checkAnimation(int spriteID){
		return tileManager != null && tileManager.checkSpriteAnimation(spriteID);
    }
	public TileManager getTileManager(){
		return tileManager;
    }
    
}
