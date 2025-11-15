package Helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class loadImg {
    public static BufferedImage load(String path) {
        BufferedImage img = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("File not found: " + file.getAbsolutePath());
                return null;
            }
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

}
