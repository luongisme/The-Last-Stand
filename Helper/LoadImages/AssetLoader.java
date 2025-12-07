package Helper.LoadImages;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetLoader {
    private static final String CLASSPATH_ROOT = "/assets/assets/";
    private static final String FILESYSTEM_ROOT = "resource/assets/assets/";

    public static Image loadImage(String fileName, String subPath) {
        // Normalize
        String normalizedSubPath = subPath;

        if (normalizedSubPath != null && !normalizedSubPath.isEmpty() && !normalizedSubPath.endsWith("/")) {
            normalizedSubPath += "/";
        }

        if (normalizedSubPath == null) { normalizedSubPath = ""; }

        String fullRelativePath = normalizedSubPath + fileName;

        System.out.println("Attempting to load: " + fullRelativePath);

        // Try classpath first (preferred when resource folder is on classpath)
        String cpPath = CLASSPATH_ROOT + fullRelativePath;
        try (InputStream is = AssetLoader.class.getResourceAsStream(cpPath)) {
            if (is != null) {
                System.out.println("Loaded from classpath: " + cpPath);
                Image img = new Image(is);
                if (img.isError()) {
                    System.err.println("Image has error after loading: " + img.getException().getMessage());
                }
                else {
                return img;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read classpath resource: " + cpPath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error creating Image from: " + cpPath);
            e.printStackTrace();
        }

        // Fallback to file system
        String fsPath = FILESYSTEM_ROOT + fullRelativePath;
        File f = new File(fsPath);
        if (f.exists()) {
            try (FileInputStream fis = new FileInputStream(f)) {
                System.out.println("Loaded from file: " + f.getAbsolutePath());
                Image img = new Image(fis);
                if (img.isError()) {
                    System.err.println("Image has error after loading: " + img.getException().getMessage());
                }
                else {
                    return img;
                }
            } catch (IOException e) {
                System.err.println("Failed to read file: " + f.getAbsolutePath());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error creating Image from file: " + f.getAbsolutePath());
                e.printStackTrace();
            }
        }

        System.err.println("Image not found: " + fullRelativePath);
        return null;
    }

    public static Image getSubImage(Image source, int x, int y, int width, int height) {
        if (source == null) {
            System.err.println("Error getSubImage: source image is null!");
            return createDummyImage(width, height);
        }

        PixelReader reader = source.getPixelReader();
        if (reader == null) {
            System.err.println("Error getSubImage: PixelReader is null (The source image may be corrupted)!");
            return createDummyImage(width, height);
        }

        // Check out of bound
        if (x + width > source.getWidth() || y + height > source.getHeight()) {
            System.err.println("Error getSubImage: The crop coordinates are outside the source image!");
            return createDummyImage(width, height);
        }

        WritableImage subImage = new WritableImage(reader, x, y, width, height);
        return subImage;
    }

    public static Image createDummyImage(int width, int height) {
        // Make sure that width, height > 0
        width = Math.max(1, width);
        height = Math.max(1, height);

        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Create a simple pattern
                if ((x + y) % 2 == 0) {
                    pw.setColor(x, y, Color.GRAY);
                } else {
                    pw.setColor(x, y, Color.DARKGRAY);
                }
            }
        }

        // Draw Error
        pw.setColor(0, 0, Color.RED);
        pw.setColor(width - 1, 0, Color.RED);
        pw.setColor(0, height - 1, Color.RED);
        pw.setColor(width - 1, height - 1, Color.RED);

        return img;
    }
}
