package Scene;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class Menu extends GameScene implements Render, SceneMethod {
	private Image menuImage;

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

            // Draw the animated GIF (ImageIcon handles the animation automatically)
            g.drawImage(menuImage, 0, 0, width, height, game.getContentPane());

        } else {
            System.err.println("Menu image is null, cannot render");
        }
	}

	private void loadMenuImage() {
		try {
			File imageFile = new File("resource/assets/assets/Menu_bg.gif");
			if (imageFile.exists()) {
				// Use ImageIcon to preserve GIF animation
				ImageIcon menuImageIcon = new ImageIcon(imageFile.getAbsolutePath());
				menuImage = menuImageIcon.getImage();
				System.out.println("Successfully loaded animated menu background from file path");
			} else {
				System.err.println("Menu background file not found: " + imageFile.getAbsolutePath());
			}
		} catch (Exception ex) {
			System.err.println("Failed to load menu image: " + ex.getMessage());
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
