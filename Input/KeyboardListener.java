package Input;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        // switch(e.getKeyCode()){
        //     case KeyEvent.VK_M:
        //         GameState.gameState = GameState.GameStates.MENU;
        //         break;
        //      case KeyEvent.VK_SPACE:
        //         GameState.gameState = GameState.GameStates.PLAY;
        //         break;
        //     case KeyEvent.VK_P:
        //         GameState.gameState = GameState.GameStates.SETTINGS;
        //         break;
        //     default:
        //         break;
        // }
        // Chưa có game GameState nên tạm thời để commnent
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
