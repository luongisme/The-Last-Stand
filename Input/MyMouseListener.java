package Input;

import Main.Game;
import Main.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// Chưa có game GameState nên tạm thời để commnent

public class MyMouseListener implements MouseListener, MouseMotionListener{

    private Game game;
    private GameState gameState;

    public MyMouseListener(Game game){
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            // switch(GameStates.gameState){
            //     case MENU:
            //         game.getMenu().mouseDragged(e.getX(), e.getY());
            //         break;
            //     case PLAYING:
            //         game.getPlaying().mouseDragged(e.getX(), e.getY());
            //         break;
            //     case SETTINGS:
            //         game.getSettings().mouseDragged(e.getX(), e.getY());
            //         break;
            //     default:
            //         break;

            // }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // switch(GameStates.gameState){
        //     case MENU:
        //         game.getMenu().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case PLAYING:
        //         game.getPlaying().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case SETTINGS:
        //         game.getSettings().mouseDragged(e.getX(), e.getY());
        //         break;
        //     default:
        //         break;

        // }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // switch(GameStates.gameState){
        //     case MENU:
        //         game.getMenu().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case PLAYING:
        //         game.getPlaying().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case SETTINGS:
        //         game.getSettings().mouseDragged(e.getX(), e.getY());
        //         break;
        //     default:
        //         break;

        // }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // switch(GameStates.gameState){
        //     case MENU:
        //         game.getMenu().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case PLAYING:
        //         game.getPlaying().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case SETTINGS:
        //         game.getSettings().mouseDragged(e.getX(), e.getY());
        //         break;
        //     default:
        //         break;
        // }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // switch(GameStates.gameState){
        //     case MENU:
        //         game.getMenu().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case PLAYING:
        //         game.getPlaying().mouseDragged(e.getX(), e.getY());
        //         break;
        //     case SETTINGS:
        //         game.getSettings().mouseDragged(e.getX(), e.getY());
        //         break;
        //     default:
        //         break;

        // }
    }
    
}  