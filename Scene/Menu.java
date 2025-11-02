package Scene;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Main.GameState;
import UI.Button.MenuButton;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Menu extends GameScene implements Render, SceneMethod {
	private Image menuImage;
	private List<MenuButton> buttons = new ArrayList<>();
	private MenuButton playButton;

	public Menu(Game game) {
		super(game);
		loadMenuImage();
		initButtons();
	}

	private void initButtons() {
		
		playButton = new MenuButton(240, 350,180, 60, "Play");
		buttons.add(playButton);
		
		
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

        // Render all buttons
        for (MenuButton button : buttons) {
            button.render(g);
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
		// Check if play button was clicked
		if (playButton.isMouseOver(x, y)) {
			System.out.println("Play button clicked!");
			GameState.SetGameState(GameState.PLAYING);
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		// Update button hover states
		for (MenuButton button : buttons) {
			button.update(x, y);
		}
	}

	@Override
	public void mousePressed(int x, int y) {
		// Set button pressed state
		for (MenuButton button : buttons) {
			if (button.isMouseOver(x, y)) {
				button.setMousePressed(true);
			}
		}
	}

	@Override
	public void mouseReleased(int x, int y) {
		// Reset button pressed state
		for (MenuButton button : buttons) {
			button.setMousePressed(false);
		}
	}

	@Override
	public void mouseDragged(int x, int y) {
		// Implement mouseDragged method
	}

}
