package Main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameScreen extends Canvas {
    private Game game;
    private GraphicsContext gc;
    private static final int sizeWidth = 1504;
    private static final int sizeHeight = 736;

    public GameScreen(Game game) {
        super(sizeWidth, sizeHeight);
        this.game = game;
        this.gc = getGraphicsContext2D();
        initInput();
    }

    private void initInput() {
        // JavaFX mouse events
        setOnMouseClicked(this::handleMouseClicked);
        setOnMousePressed(this::handleMousePressed);
        setOnMouseReleased(this::handleMouseReleased);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseMoved(this::handleMouseMoved);
    }

    public void render() {
        // Clear canvas
        gc.clearRect(0, 0, sizeWidth, sizeHeight);
        
        // Render based on GameState
        switch (GameState.gameState) {
            case MENU:
                game.getMenu().render(gc);
                break;
            case PLAYING:
                game.getPlaying().render(gc);
                break;
            case SETTINGS:
                renderBlack();
                break;
            case GAME_OVER:
                renderBlack();
                break;
            default:
                renderBlack();
                break;
        }
    }
    
    private void renderBlack() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, sizeWidth, sizeHeight);
    }

    private void handleMouseClicked(MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseClicked(x, y);
            case PLAYING -> game.getPlaying().mouseClicked(x, y);
            case SETTINGS -> game.getSettings().mouseClicked(x, y);
            case GAME_OVER -> game.getGameOver().mouseClicked(x, y);
            default -> {}
        }
    }

    private void handleMousePressed(MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mousePressed(x, y);
            case PLAYING -> game.getPlaying().mousePressed(x, y);
            case SETTINGS -> game.getSettings().mousePressed(x, y);
            case GAME_OVER -> game.getGameOver().mousePressed(x, y);
            default -> {}
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseReleased(x, y);
            case PLAYING -> game.getPlaying().mouseReleased(x, y);
            case SETTINGS -> game.getSettings().mouseReleased(x, y);
            case GAME_OVER -> game.getGameOver().mouseReleased(x, y);
            default -> {}
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseDragged(x, y);
            case PLAYING -> game.getPlaying().mouseDragged(x, y);
            case SETTINGS -> game.getSettings().mouseDragged(x, y);
            case GAME_OVER -> game.getGameOver().mouseDragged(x, y);
            default -> {}
        }
    }

    private void handleMouseMoved(MouseEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseMoved(x, y);
            case PLAYING -> game.getPlaying().mouseMoved(x, y);
            case SETTINGS -> game.getSettings().mouseMoved(x, y);
            case GAME_OVER -> game.getGameOver().mouseMoved(x, y);
            default -> {}
        }
    }
}

