package Helper;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class loadImg {

    public static Image load(String path) {
        File file = new File(path);

        if (!file.exists()) {
            System.err.println("Image not found: " + file.getAbsolutePath());
            return null;
        }

        // JavaFX loads using file:
        return new Image(file.toURI().toString());
    }
}
