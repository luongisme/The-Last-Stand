package Scene;

import Interfaces.Render;
import Main.Game;
import Main.GameScene;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class GameOver extends GameScene implements Render, SceneMethod {
    private final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 72);
    private final Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 36);
    private final Color DARK_RED_COLOR = new Color(139, 0, 0);
    private final Color PARCHMENT_COLOR = new Color(245, 235, 215);
    private final Color DARK_BROWN_COLOR = new Color(101, 67, 33);
    
    private JPanel mainPanel;
    private JButton playAgainButton;
    private JButton menuButton;
    private JButton exitButton;
    
    public GameOver(Game game) {
        super(game);
        initializeComponents();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        JOutlinedLabel gameOverLabel = new JOutlinedLabel("GAME OVER");
        gameOverLabel.setFont(TITLE_FONT);
        gameOverLabel.setForeground(DARK_RED_COLOR);
        gameOverLabel.setOutlineColor(Color.BLACK);
        gameOverLabel.setOpaque(false);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        playAgainButton = createMedievalButton("Play Again");
        menuButton = createMedievalButton("Main Menu");
        exitButton = createMedievalButton("Exit Game");

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(gameOverLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(playAgainButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(menuButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(exitButton);
        mainPanel.add(Box.createVerticalGlue());
    }

    private static class JOutlinedLabel extends JLabel {
        private Color outlineColor = Color.BLACK;
        private int outlineOffset = 3;

        public JOutlinedLabel(String text) {
            super(text);
        }

        public void setOutlineColor(Color outlineColor) {
            this.outlineColor = outlineColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            String text = getText();
            Font font = getFont();
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int x = (getWidth() - metrics.stringWidth(text)) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

            // Draw outline
            g2d.setColor(outlineColor);
            int offset = outlineOffset;
            g2d.drawString(text, x - offset, y - offset);
            g2d.drawString(text, x + offset, y - offset);
            g2d.drawString(text, x - offset, y + offset);
            g2d.drawString(text, x + offset, y + offset);
            g2d.drawString(text, x - offset, y);
            g2d.drawString(text, x + offset, y);
            g2d.drawString(text, x, y - offset);
            g2d.drawString(text, x, y + offset);

            // Draw main text
            g2d.setColor(getForeground());
            g2d.drawString(text, x, y);

            g2d.dispose();
        }
    }

    private JButton createMedievalButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(DARK_BROWN_COLOR);
        button.setBackground(PARCHMENT_COLOR);
        button.setOpaque(true);
        button.setFocusPainted(false);
        
        Border raisedBevel = BorderFactory.createRaisedBevelBorder();
        Border empty = BorderFactory.createEmptyBorder(10, 30, 10, 30);
        button.setBorder(BorderFactory.createCompoundBorder(raisedBevel, empty));
        
        Dimension buttonSize = new Dimension(350, 80);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PARCHMENT_COLOR.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PARCHMENT_COLOR);
            }
        });
        return button;
    }

    @Override
    public void mouseClicked(int x, int y) {
        // Handle mouse click events for buttons
    }

    @Override
    public void mouseMoved(int x, int y) {
        // Handle mouse move events
    }

    @Override
    public void mousePressed(int x, int y) {
        // Handle mouse press events
    }

    @Override
    public void mouseReleased(int x, int y) {
        // Handle mouse release events
    }

    @Override
    public void mouseDragged(int x, int y) {
        // Handle mouse drag events
    }

    @Override
    public void render(Graphics g) {
        // Draw the game over screen
        mainPanel.paint(g);
    }
}
