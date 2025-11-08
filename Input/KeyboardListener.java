package Input;

import javafx.scene.input.KeyEvent;
import Main.GameState;

public class KeyboardListener {
    
    /**
     * Handle key pressed events
     */
    public static void handleKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case M:
                GameState.SetGameState(GameState.MENU);
                System.out.println("Switched to MENU state");
                break;
            case P:
                GameState.SetGameState(GameState.PLAYING);
                System.out.println("Switched to PLAYING state");
                break;
            case S:
                GameState.SetGameState(GameState.SETTINGS);
                System.out.println("Switched to SETTINGS state");
                break;
            case ESCAPE:
                GameState.SetGameState(GameState.MENU);
                System.out.println("ESC - Switched to MENU state");
                break;
            case SPACE:
                // Can be used for pause/resume
                System.out.println("SPACE pressed");
                break;
            default:
                break;
        }
    }

    /**
     * Handle key released events
     */
    public static void handleKeyReleased(KeyEvent e) {
        // Handle key release logic here if needed
    }

    /**
     * Handle key typed events
     */
    public static void handleKeyTyped(KeyEvent e) {
        // Handle key typed logic here if needed
    }
}
