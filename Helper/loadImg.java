package Helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class loadImg {
    public static BufferedImage getSpriteAtlas() {
        BufferedImage img = null;
        try {
            String path = "resource/assets/sprites/Multi_Slime_Spritesheet.png";
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("File not found at: " + file.getAbsolutePath());
                throw new RuntimeException("Sprite atlas not found at: " + path);
            }
            img = ImageIO.read(file);
            if (img == null) {
                throw new RuntimeException("Failed to load sprite atlas");
            }
        } catch (IOException e) {
            System.err.println("Error loading sprite atlas: " + e.getMessage());
            e.printStackTrace();
        }
        return img;
    }
}
