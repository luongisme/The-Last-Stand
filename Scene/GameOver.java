package Scene;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameOver extends GameScene implements Render, SceneMethod {
	private VBox mainPanel;
	private Button playAgainButton;
	private Button menuButton;
	private Button exitButton;
	private Label gameOverLabel;

	// Colors
	private static final Color DARK_RED_COLOR = Color.rgb(139, 0, 0);
	private static final Color PARCHMENT_COLOR = Color.rgb(245, 235, 215);
	private static final Color DARK_BROWN_COLOR = Color.rgb(101, 67, 33);

	public GameOver(Game game) {
		super(game);
		initializeComponents();
	}

	private void initializeComponents() {
		mainPanel = new VBox(25);
		mainPanel.setAlignment(Pos.CENTER);
		mainPanel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, null)));

		gameOverLabel = new Label("GAME OVER");
		gameOverLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 72));
		gameOverLabel.setTextFill(DARK_RED_COLOR);
		gameOverLabel.setEffect(new DropShadow(3, Color.BLACK));
		gameOverLabel.setTextAlignment(TextAlignment.CENTER);

		playAgainButton = createMedievalButton("Play Again");
		menuButton = createMedievalButton("Main Menu");
		exitButton = createMedievalButton("Exit Game");

		mainPanel.getChildren().addAll(gameOverLabel, playAgainButton, menuButton, exitButton);

		// Button actions (replace with actual game logic)
		playAgainButton.setOnAction(e -> {
			System.out.println("Action: Play Again");
			// TODO: Add logic to restart the game
		});
		menuButton.setOnAction(e -> {
			System.out.println("Action: Main Menu");
			// TODO: Add logic to return to main menu
		});
		exitButton.setOnAction(e -> {
			System.out.println("Action: Exit Game");
			System.exit(0);
		});
	}

	private Button createMedievalButton(String text) {
		Button button = new Button(text);
		button.setFont(Font.font("Monospaced", FontWeight.BOLD, 36));
		button.setTextFill(DARK_BROWN_COLOR);
		button.setBackground(new Background(new BackgroundFill(PARCHMENT_COLOR, new CornerRadii(10), null)));
		button.setPrefSize(350, 80);
		button.setStyle("-fx-cursor: hand; -fx-border-color: #654321; -fx-border-width: 3; -fx-background-radius: 10;");
		button.setOnMouseEntered(e -> button.setBackground(new Background(new BackgroundFill(PARCHMENT_COLOR.brighter(), new CornerRadii(10), null))));
		button.setOnMouseExited(e -> button.setBackground(new Background(new BackgroundFill(PARCHMENT_COLOR, new CornerRadii(10), null))));
		return button;
	}

	public VBox getMainPanel() {
		return mainPanel;
	}

	// Implement SceneMethod interface methods
	@Override
	public void mouseClicked(int x, int y) {}
	@Override
	public void mouseMoved(int x, int y) {}
	@Override
	public void mousePressed(int x, int y) {}
	@Override
	public void mouseReleased(int x, int y) {}
	@Override
	public void mouseDragged(int x, int y) {}

	// Render method for JavaFX (if needed)
	@Override
	public void render(GraphicsContext gc) {
		// If you want to draw custom graphics, implement here
	}
}
