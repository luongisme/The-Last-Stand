package Sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.InputStream;

public class MusicManager {

    private static MusicManager instance;
    private MediaPlayer menuMusic;
    private MediaPlayer gameplayMusic;
    private MediaPlayer currentPlayer;
    private double volume = 0.5;
    private boolean isMuted = false;

    private MusicManager() {
        loadMusic();
    }

    public static MusicManager getInstance() {
        if (instance == null)
            instance = new MusicManager();
        return instance;
    }

    private void loadMusic() {
        try {
            menuMusic = createMusic("resource/sounds/main_menu.mp3");
            gameplayMusic = createMusic("resource/sounds/gameplay.mp3");

            if (menuMusic != null) {
                menuMusic.setCycleCount(MediaPlayer.INDEFINITE);
                menuMusic.setVolume(volume);
            }
            
            if (gameplayMusic != null) {
                gameplayMusic.setCycleCount(MediaPlayer.INDEFINITE);
                gameplayMusic.setVolume(volume);
            }
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi load nhạc: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private MediaPlayer createMusic(String path) {
        try {
            // Try classpath resource first
            String[] resourcePaths = {
                "/sounds/main_menu.mp3",
                "/sounds/gameplay.mp3",
                "/" + path
            };
            
            for (String resPath : resourcePaths) {
                InputStream is = getClass().getResourceAsStream(resPath);
                if (is != null) {
                    // Found in classpath - but MediaPlayer needs File/URL
                    // So we still need to check file system
                    is.close();
                    break;
                }
            }
            
            // Check file system
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("⚠ Không tìm thấy file nhạc: " + path);
                return null;
            }
            
            Media media = new Media(file.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            
            // Add error handler
            player.setOnError(() -> {
                System.err.println("⚠ Lỗi MediaPlayer: " + player.getError().getMessage());
            });
            
            return player;
        } catch (Exception e) {
            System.err.println("⚠ Lỗi tạo MediaPlayer cho: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public void playMenuMusic() {
        stopAll();
        if (menuMusic != null && !isMuted) {
            menuMusic.play();
            currentPlayer = menuMusic;
        }
    }

    public void playGameplayMusic() {
        stopAll();
        if (gameplayMusic != null && !isMuted) {
            gameplayMusic.play();
            currentPlayer = gameplayMusic;
        }
    }

    public void pause() {
        if (currentPlayer != null) {
            try {
                currentPlayer.pause();
            } catch (Exception e) {
                System.err.println("⚠ Lỗi khi pause: " + e.getMessage());
            }
        }
    }

    public void resume() {
        if (currentPlayer != null && !isMuted) {
            try {
                currentPlayer.play();
            } catch (Exception e) {
                System.err.println("⚠ Lỗi khi resume: " + e.getMessage());
            }
        }
    }

    public void stopAll() {
        try {
            if (menuMusic != null) {
                menuMusic.stop();
            }
            if (gameplayMusic != null) {
                gameplayMusic.stop();
            }
            currentPlayer = null;
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi stop: " + e.getMessage());
        }
    }

    public void setVolume(double value) {
        // Clamp volume between 0.0 and 1.0
        volume = Math.max(0.0, Math.min(1.0, value));
        
        if (menuMusic != null) {
            menuMusic.setVolume(volume);
        }
        if (gameplayMusic != null) {
            gameplayMusic.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
        if (muted) {
            pause();
        } else {
            resume();
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void dispose() {
        stopAll();
        if (menuMusic != null) {
            menuMusic.dispose();
        }
        if (gameplayMusic != null) {
            gameplayMusic.dispose();
        }
    }
}
