package Scenes;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Main.GameState;
import Button.MenuButton;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Menu extends GameScene implements Render, SceneMethod {
	private static final Logger logger = Logger.getLogger(Menu.class.getName());
	private Image menuImage;
	private final List<MenuButton> buttons = new ArrayList<>();
	private MenuButton playButton;

	public Menu(Game game) {
		super(game);
		loadMenuImage();
		initButtons();
	}

	private void initButtons() {
		playButton = new MenuButton(240, 350, 180, 60, "Play");
		buttons.add(playButton);
	}

	@Override
	public void render(GraphicsContext gc) {
		if (menuImage == null) {
            loadMenuImage();
        }
		
        if (menuImage != null) {
            // Scale the menu image to fit the canvas size
            int width = 1504;
            int height = 736;
            
            gc.drawImage(menuImage, 0, 0, width, height);
        } else {
            logger.warning("Menu image is null, cannot render");
        }

        // Render all buttons
        for (MenuButton button : buttons) {
            button.render(gc);
        }
	}

	private void loadMenuImage() {
		try {
			File imageFile = new File("resource/assets/assets/Menu_bg.gif");
			if (imageFile.exists()) {
				// Load GIF image using JavaFX Image
				menuImage = new Image(new FileInputStream(imageFile));
				logger.info("Successfully loaded menu background from file path");
			} else {
				logger.severe("Menu background file not found: " + imageFile.getAbsolutePath());
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to load menu image", ex);
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		// Check if play button was clicked
		if (playButton.isMouseOver(x, y)) {
			logger.info("Play button clicked!");
			GameState.SetGameState(GameState.PLAYING);
			game.onEnterPlaying();

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
