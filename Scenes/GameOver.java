package Scenes;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import Main.GameState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class GameOver extends GameScene implements Render, SceneMethod {

    private final double WIDTH = 1504;
    private final double HEIGHT = 736;

    private double btnWidth = 220;
    private double btnHeight = 50;

    private double nextX, nextY;
    private double menuX, menuY;

    private Image gameOverBg;
    private boolean isWin = true; 
    private boolean isFinalLevel = false; // Track if this is the final level

    public GameOver(Game game){
        super(game);

        nextX = WIDTH / 2 - btnWidth / 2;
        nextY = HEIGHT / 2 - btnHeight / 2;

        menuX = WIDTH / 2 - btnWidth / 2;
        menuY = HEIGHT / 2 + 50;
        gameOverBg = new Image(getClass().getResource("/sounds/background.jpg").toString());
    }

    public void setWin(boolean isFinalLevel) {
        this.isWin = true;
        this.isFinalLevel = isFinalLevel;
    }

    public void setLose() {
        this.isWin = false;
        this.isFinalLevel = false;
    }

    @Override
	public void render(GraphicsContext gc) {

        gc.drawImage(gameOverBg, 0, 0, WIDTH, HEIGHT);

    	String text;
    	if (isWin) {
    	    text = isFinalLevel ? "YOU WIN!" : "LEVEL COMPLETE!";
    	} else {
    	    text = "YOU LOSE";
    	}

    	Font font = Font.font("Arial", FontWeight.BOLD, 72);
    	gc.setFont(font);
    	gc.setFill(isWin ? Color.LIMEGREEN : Color.RED);

    	Text temp = new Text(text);
    	temp.setFont(font);
    	double textWidth = temp.getLayoutBounds().getWidth();
    	double textX = (WIDTH - textWidth) / 2;
    	double textY = HEIGHT / 3;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(6);   
        gc.strokeText(text, textX, textY);
    	gc.fillText(text, textX, textY);

    	if (isWin && !isFinalLevel) {
    	    // Show NEXT LEVEL button for non-final levels
        	drawButton(gc, "NEXT LEVEL", nextX, nextY);
    	}

    	drawButton(gc, "MAIN MENU", menuX, menuY);
	}

    private void drawButton(GraphicsContext gc, String text, double x, double y){
    	gc.setFill(Color.GRAY);
    	gc.fillRoundRect(x, y, btnWidth, btnHeight, 20, 20);

    	gc.setStroke(Color.WHITE);
    	gc.setLineWidth(2);
    	gc.strokeRoundRect(x, y, btnWidth, btnHeight, 20, 20);

    	Font font = Font.font("Arial", FontWeight.BOLD, 20);
    	gc.setFont(font);
    	gc.setFill(Color.WHITE);

    	Text temp = new Text(text);
    	temp.setFont(font);
    	double textWidth = temp.getLayoutBounds().getWidth();

    	double textX = x + btnWidth / 2 - textWidth / 2;
    	double textY = y + 33;

    	gc.fillText(text, textX, textY);
	}

    @Override
    public void mouseClicked(int x, int y) {
        // NEXT LEVEL (only for non-final levels)
        if (isWin && !isFinalLevel &&
            x >= nextX && x <= nextX + btnWidth &&
            y >= nextY && y <= nextY + btnHeight) {
			game.onEnterPlaying();
            game.getPlaying().loadNextLevel();
            GameState.SetGameState(GameState.PLAYING);
            return;
        }

        // MAIN MENU
        if (x >= menuX && x <= menuX + btnWidth &&
            y >= menuY && y <= menuY + btnHeight) {
            GameState.SetGameState(GameState.MENU);
			game.onEnterMenu();      
        	game.getPlaying().reset(); 
        	return;
        }
    }

    @Override public void mouseMoved(int x, int y) {}
    @Override public void mousePressed(int x, int y) {}
    @Override public void mouseReleased(int x, int y) {}
    @Override public void mouseDragged(int x, int y) {}
}
