package Main;

import javafx.animation.AnimationTimer;
import java.util.logging.Logger;

public class GameLoop extends AnimationTimer {
    private static final Logger LOGGER = Logger.getLogger(GameLoop.class.getName());

    private final Game game;
    private final GameScreen gameScreen;

    private final int FPS;
    private final int UPS;

    private long lastFrame;
    private long lastUpdate;
    private long lastTimeCheck;
    private int frames;
    private int updates;

    private double timePerFrame;
    private double timePerUpdate;

    public GameLoop(Game game, GameScreen gameScreen, int fps, int ups) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.FPS = fps;
        this.UPS = ups;

        this.timePerFrame = 1000000000.0 / FPS;
        this.timePerUpdate = 1000000000.0 / UPS;

        this.lastFrame = System.nanoTime();
        this.lastUpdate = System.nanoTime();
        this.lastTimeCheck = System.currentTimeMillis();
        this.frames = 0;
        this.updates = 0;
    }

    @Override
    public void handle(long now) {
        // Render
        if (now - lastFrame >= timePerFrame) {
            gameScreen.render();
            lastFrame = now;
            frames++;
        }

        // Update
        if (now - lastUpdate >= timePerUpdate) {
            game.updateGame();
            lastUpdate = now;
            updates++;
        }

        // FPS/UPS counter
        if (System.currentTimeMillis() - lastTimeCheck >= 1000) {
            LOGGER.info("FPS: " + frames + " | UPS: " + updates);
            frames = 0;
            updates = 0;
            lastTimeCheck = System.currentTimeMillis();
        }
    }

    public void reset() {
        this.lastFrame = System.nanoTime();
        this.lastUpdate = System.nanoTime();
        this.lastTimeCheck = System.currentTimeMillis();
        this.frames = 0;
        this.updates = 0;
    }
}

