package Scene;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Menu extends GameScene implements Render, SceneMethod {
	private BufferedImage menuImage;




	public Menu(Game game) {
		super(game);
		loadMenuImage();
	}


	@Override
	public void render(Graphics g) {
		if (menuImage == null) {
            loadMenuImage();
        }
        if (menuImage != null) {
            // Scale the menu image to fit the panel size
            int width, height;
			if (g.getClipBounds() != null) {
				width = g.getClipBounds().width;
				height = g.getClipBounds().height;
			} else {
				width = 1504;
				height = 736;
			}
            g.drawImage(menuImage, 0, 0, width, height, null);
        } else {
            System.err.println("Menu image is null, cannot render");
        }
	}

	private void loadMenuImage() {
		try {
			menuImage = ImageIO.read(new java.io.File("resource/assets/assets/Menu_bg.gif"));
			System.out.println("Successfully loaded menu background from file path");
		} catch (IOException ex) {
			System.err.println("Failed to load menu image from file: " + ex.getMessage());
			ex.printStackTrace();
		}
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