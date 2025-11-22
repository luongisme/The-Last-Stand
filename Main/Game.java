package Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Input.KeyboardListener;
import Scenes.GameOver;
import Scenes.Menu;
import Scenes.Playing;
import Scenes.Settings;

import Sound.MusicManager;
import Sound.MusicSetting;
import javafx.scene.layout.StackPane;


public class Game extends Application {
    private GameScreen gameScreen;
    private GameLoop gameLoop;

    private Menu menu;
    private Playing playing;
    private Settings settings;
    private GameOver gameOver;

    private MusicManager musicManager;
    private MusicSetting musicSetting;

    
    private Stage primaryStage;
    
    private final int FPS = 120;
    private final int UPS = 60;
    
    private static final int WINDOW_WIDTH = 1504;  // 94 tiles * 16
    private static final int WINDOW_HEIGHT = 736;  // 46 tiles * 16

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        initClasses();

        musicManager = MusicManager.getInstance();
        musicManager.playMenuMusic(); // phát nhạc menu khi mở game

        
        // Wrap GameScreen (Canvas) in a Pane
        StackPane root = new StackPane();
        root.getChildren().add(gameScreen);
        
        // Create JavaFX Scene
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Setup keyboard listeners
        scene.setOnKeyPressed(KeyboardListener::handleKeyPressed);
        scene.setOnKeyReleased(KeyboardListener::handleKeyReleased);
        scene.setOnKeyTyped(KeyboardListener::handleKeyTyped);
        
        primaryStage.setTitle("The Last Stand - Tower Defense");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        musicSetting = new MusicSetting(primaryStage, root);

        
        // Request focus for keyboard events
        gameScreen.requestFocus();
        
        startGameLoop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startGameLoop() {
        gameLoop = new GameLoop(this, gameScreen, FPS, UPS);
        gameLoop.start();
    }

    public void updateGame() {
        // Update game logic here
    }
    
    private void initClasses() {
        gameScreen = new GameScreen(this);
        menu = new Menu(this);
        playing = new Playing(this);
        settings = new Settings(this);
        gameOver = new GameOver(this);
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
    public void onEnterPlaying() {
        musicManager.playGameplayMusic();
    }

    public void onEnterMenu() {
        musicManager.playMenuMusic();
    }

}


