package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class GameScreen extends JPanel implements MouseListener, MouseMotionListener {
    private Game game;
    private Dimension size; // set size panel
    private static final int sizeWidth = 1504;
    private static final int sizeHeight = 736;


    public GameScreen(Game game) {
        this.game = game;
        setPanelSize();
        initInput();
    }

    private void initInput(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void setPanelSize() {
        size = new Dimension(sizeWidth,sizeHeight); 
        setPreferredSize(size);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Render based on GameState
        switch (GameState.gameState) {
            case MENU:
                game.getMenu().render(g);
                break;
            case PLAYING:
                // Render playing scene
                game.getPlaying().render(g);
                break;
            case SETTINGS:
                // Render settings scene
                renderBlack(g);
                break;
            case GAME_OVER:
                // Render game over scene
                renderBlack(g);
                break;
            default:
                renderBlack(g);
                break;
        }
    }
    
    private void renderBlack(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, sizeWidth, sizeHeight);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseClicked(e.getX(), e.getY());
            case PLAYING -> game.getPlaying().mouseClicked(e.getX(), e.getY());
            case SETTINGS -> game.getSettings().mouseClicked(e.getX(), e.getY());
            case GAME_OVER -> game.getGameOver().mouseClicked(e.getX(), e.getY());
            default -> {}
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mousePressed(e.getX(), e.getY());
            case PLAYING -> game.getPlaying().mousePressed(e.getX(), e.getY());
            case SETTINGS -> game.getSettings().mousePressed(e.getX(), e.getY());
            case GAME_OVER -> game.getGameOver().mousePressed(e.getX(), e.getY());
            default -> {}
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseReleased(e.getX(), e.getY());
            case PLAYING -> game.getPlaying().mouseReleased(e.getX(), e.getY());
            case SETTINGS -> game.getSettings().mouseReleased(e.getX(), e.getY());
            case GAME_OVER -> game.getGameOver().mouseReleased(e.getX(), e.getY());
            default -> {}
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseDragged(e.getX(), e.getY());
            case PLAYING -> game.getPlaying().mouseDragged(e.getX(), e.getY());
            case SETTINGS -> game.getSettings().mouseDragged(e.getX(), e.getY());
            case GAME_OVER -> game.getGameOver().mouseDragged(e.getX(), e.getY());
            default -> {}
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.gameState) {
            case MENU -> game.getMenu().mouseMoved(e.getX(), e.getY());
            case PLAYING -> game.getPlaying().mouseMoved(e.getX(), e.getY());
            case SETTINGS -> game.getSettings().mouseMoved(e.getX(), e.getY());
            case GAME_OVER -> game.getGameOver().mouseMoved(e.getX(), e.getY());
            default -> {}
        }
    }
}

