package Input;

import Main.Game;
import Main.GameState;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * JavaFX mouse handler thay thế MouseListener + MouseMotionListener (Swing).
 * Gắn các handler này vào Scene hoặc Canvas.
 */
public class MyMouseHandler {

    private final Game game;

    public MyMouseHandler(Game game) {
        this.game = game;
    }

    /** Gắn toàn bộ mouse handlers vào một Scene */
    public void attachTo(Scene scene) {
        scene.setOnMouseClicked(this::onMouseClicked);
        scene.setOnMousePressed(this::onMousePressed);
        scene.setOnMouseReleased(this::onMouseReleased);
        scene.setOnMouseDragged(this::onMouseDragged);
        scene.setOnMouseMoved(this::onMouseMoved);
    }

    /** Hoặc gắn vào Canvas nếu bạn đang vẽ trên Canvas */
    public void attachTo(Canvas canvas) {
        canvas.setOnMouseClicked(this::onMouseClicked);
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseMoved(this::onMouseMoved);
    }

    // ====== Handlers tương đương với code Swing cũ ======

    private void onMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
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
    }

    private void onMousePressed(MouseEvent e) {
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

    private void onMouseReleased(MouseEvent e) {
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

    private void onMouseDragged(MouseEvent e) {
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

    private void onMouseMoved(MouseEvent e) {
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
