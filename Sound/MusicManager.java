package Sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class MusicManager {

    private static MusicManager instance;
    private MediaPlayer menuMusic;
    private MediaPlayer gameplayMusic;
    private double volume = 0.5;

    private MusicManager() {
        loadMusic();
    }

    public static MusicManager getInstance() {
        if (instance == null)
            instance = new MusicManager();
        return instance;
    }

    private void loadMusic() {
        menuMusic = createMusic("resource/sounds/main_menu.mp3");
        gameplayMusic = createMusic("resource/sounds/gameplay.mp3");

        menuMusic.setCycleCount(MediaPlayer.INDEFINITE);
        gameplayMusic.setCycleCount(MediaPlayer.INDEFINITE);

        setVolume(volume);
    }

    private MediaPlayer createMusic(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("⚠ Không tìm thấy file nhạc: " + path);
            return null;
        }
        Media media = new Media(file.toURI().toString());
        return new MediaPlayer(media);
    }

    public void playMenuMusic() {
        stopAll();
        if (menuMusic != null) menuMusic.play();
    }

    public void playGameplayMusic() {
        stopAll();
        if (gameplayMusic != null) gameplayMusic.play();
    }

    public void stopAll() {
        if (menuMusic != null) menuMusic.stop();
        if (gameplayMusic != null) gameplayMusic.stop();
    }

    public void setVolume(double value) {
        volume = value;
        if (menuMusic != null) menuMusic.setVolume(value);
        if (gameplayMusic != null) gameplayMusic.setVolume(value);
    }

    public double getVolume() {
        return volume;
    }
}
