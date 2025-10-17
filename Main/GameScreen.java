package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

public class GameScreen extends JPanel  {
    private Game game;
    private Dimension size; // set size panel
    private int sizeWidth = 1504;
    private int sizeHeight = 736;


    public GameScreen(Game game) {
        this.game = game;
        setPanelSize();
        initInput();
    }

    private void initInput(){
        //addMouseListener(this);
        //addMouseMotionListener(this);
    }

    private void setPanelSize() {
        size = new Dimension(sizeWidth,sizeHeight); // create size of panel
        setPreferredSize(size);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Render based on GameState
        switch (GameState.gameState) {
            case MENU:
                if (game.getMenu() != null) {
                    game.getMenu().render(g);
                } else {
                    renderBlack(g);
                }
                break;
            case PLAYING:
                // Render playing scene
                renderBlack(g);
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

}

