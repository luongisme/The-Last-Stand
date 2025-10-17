package Scene;

import java.awt.Graphics;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;

public class Playing extends GameScene implements Render, SceneMethod {

    public Playing(Game game){
        super(game);

    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'render'");
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

    
}
