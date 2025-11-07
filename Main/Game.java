package Main;

import javax.swing.JFrame;

import Interfaces.Render;
import Scene.GameOver;
import Scene.Menu;
import Scene.Playing;
import Scene.Settings;
import Main.GameState;

public class Game extends JFrame implements Runnable {
    private GameScreen gameScreen;
    private Thread gameThread;
    
    private Render render;
    private Menu menu;
    private Playing playing;
    private Settings settings;
    private GameOver gameOver;
     
    
    private final int FPS=120;
    private final int UPS=60;

    public Game(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        initClasses();
        add(gameScreen);
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        Game game=new Game();
        game.start();
    }


    private void start(){
        gameThread=new Thread(this){};
        gameThread.start();
    }

    private void updateGame(){
        switch(GameState.gameState){
            case MENU:
                break;
            case PLAYING:
                playing.update();
                break;
            case SETTINGS:
                break;
            // case GAMEOVER:
            //     break;
            default:
                break;
        }
    }

    
    private void initClasses() {
        gameScreen = new GameScreen(this);
        menu = new Menu(this);
        playing = new Playing(this);
        settings = new Settings(this);
        gameOver = new GameOver(this);
    }
     

    /*
    private void updateGame() {
        playing.update();
    }
     */
    @Override
    public void run(){

        double timePerFrame = 1000000000.0/FPS;//16.6667 nano secs = 0.016666 secs per frame
        double timePerUdpdate = 1000000000.0/UPS;
        long lastFrame = System.nanoTime();
        long lastTimeCheck = System.currentTimeMillis();
        long lastUpdate = System.nanoTime();

        int frames = 0;
        int updates = 0;

        long now;

        while(true) {
            now = System.nanoTime();
            //render
            if (now - lastFrame >= timePerFrame) {
                repaint();
                lastFrame = now;
                frames++;
            }
            //update
            if(now - lastUpdate >= timePerUdpdate){
                updateGame();
                lastUpdate = now;
                updates++;
            }

            if(System.currentTimeMillis() - lastTimeCheck >= 1000){
                System.out.println("FPS: " + frames + "| UPS: " + updates);
                frames = 0;
                updates = 0;
                lastTimeCheck = System.currentTimeMillis();
            }
        }
    }

    

    public Menu getMenu(){
        return menu;
    }
    
    public Playing getPlaying(){
        return playing;
    }
    
    public Settings getSettings(){
        return settings;
    }
    
    public GameOver getGameOver() {
        return gameOver;
    }
}
