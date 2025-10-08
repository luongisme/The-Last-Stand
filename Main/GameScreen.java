package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
//import Input.MouseListener;

public class GameScreen extends JPanel {
    private Game game;
    private Dimension size; // set size panel
    // private MouseListener mouseListener; // mouse handling
    private int sizeWidth = 1504;
    private int sizeHeight = 736;

    // ===== Constructor =====
    public GameScreen(Game game) {
        this.game = game;
        setPanelSize();
        // initInput();
    }

    // private void initInput(){}

    private void setPanelSize() {
        size = new Dimension(sizeWidth,sizeHeight); // create size of panel
        setPreferredSize(size);
        System.out.println("GameScreen: Panel size set to " + size.width + "x" + size.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0,sizeWidth,sizeHeight);
        // g.getRender()
    }

}

