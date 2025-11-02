package Scene;

import java.awt.Graphics;

import Map.LevelBuild;
import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Managers.TileManager;
import Map.Tile;

public class Playing extends GameScene implements Render, SceneMethod {
	private int[][] lvl;
	private TileManager tileManager;

    public Playing(Game game){
        super(game);
	this.tileManager = new TileManager();
	this.lvl = new LevelBuild().getFirstMapData();

    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
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
	}

	@Override
	public void mouseReleased(int x, int y) {
		// Implement mouseReleased method
	}

	@Override
	public void mouseDragged(int x, int y) {
		// Implement mouseDragged method
	}

	public void drawLevel(Graphics g){
        // go through all the tile
        for (int y=0;y<lvl.length;y++){
            for (int x=0;x<lvl[y].length;x++){
                int id=lvl[y][x];
				Tile t = tileManager.getTile(id);
				if (t == null) continue;
				if (checkAnimation(id)) {
					g.drawImage(t.getSprite(animationIndex), x*16, y*16, null);
				} else {
					g.drawImage(t.getSprite(), x*16, y*16, null);
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
    
}
