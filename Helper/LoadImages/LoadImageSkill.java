package Helper.LoadImages;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class LoadImageSkill {
    private static Image skillUIAtlas;


    public static Image getImageSkill(String skillName) {
        String fileName = skillName + ".png";
        return loadSkillImage(fileName);
    }


    public static Image getImageSkillIcon(String skillName) {
        String fileName = skillName + "Icon.png";
        return loadSkillIconImage(fileName);
    }

    public static Image loadSkillUI() {
        skillUIAtlas = loadSkillImage("Mediavel.png");
        return skillUIAtlas;
    }

    public static Image getSkillUIImage() {
        if (skillUIAtlas == null) {
            loadSkillUI();
        }
        return skillUIAtlas;
    }


    public static Image getSkillSprite(int xCord, int yCord, int width, int height) {
        return getSprite(skillUIAtlas, xCord, yCord, width, height);
    }


    private static Image getSprite(Image atlas, int xCord, int yCord,int width,int height ) {
        if (atlas == null) return null;

        int x = xCord ;
        int y = yCord ;

        // Extract sub-image using JavaFX PixelReader
        PixelReader reader = atlas.getPixelReader();
        WritableImage subImage = new WritableImage(reader, x, y, width, height);

        return subImage;
    }


    private static Image loadSkillImage(String fileName) {

        String[] cpRoots = { "/assets/assets/Skill/", "/assets/Skill/", "/Skill/" };
        for (String root : cpRoots) {
            String cpPath = root + fileName;
            try (InputStream is = LoadImageSkill.class.getResourceAsStream(cpPath)) {
                if (is != null) {
                    return new Image(is);
                }
            } catch (Exception e) {
                System.err.println("Failed to read classpath resource: " + cpPath);
                e.printStackTrace();
            }
        }

        // Fallback to file system
        String[] fsRoots = {
            "resource/assets/assets/Skill/",
            "resource/assets/Skill/",
            "assets/Skill/"
        };
        for (String root : fsRoots) {
            File f = new File(root + fileName);
            if (f.exists()) {
                try {
                    return new Image(new FileInputStream(f));
                } catch (Exception e) {
                    System.err.println("Failed to read file: " + f.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }

        System.err.println("Skill image not found: " + fileName);
        return null;
    }


    private static Image loadSkillIconImage(String fileName) {
        // Try classpath first (preferred when resource folder is on classpath)
        String[] cpRoots = { "/assets/assets/Skill/Icon/", "/assets/Skill/Icon/", "/Skill/Icon/" };
        for (String root : cpRoots) {
            String cpPath = root + fileName;
            try (InputStream is = LoadImageSkill.class.getResourceAsStream(cpPath)) {
                if (is != null) {
                    return new Image(is);
                }
            } catch (Exception e) {
                System.err.println("Failed to read classpath resource: " + cpPath);
                e.printStackTrace();
            }
        }

        // Fallback to file system
        String[] fsRoots = {
            "resource/assets/assets/Skill/Icon/",
            "resource/assets/Skill/Icon/",
            "assets/Skill/Icon/"
        };
        for (String root : fsRoots) {
            File f = new File(root + fileName);
            if (f.exists()) {
                try {
                    return new Image(new FileInputStream(f));
                } catch (Exception e) {
                    System.err.println("Failed to read file: " + f.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }

        System.err.println("Skill icon image not found: " + fileName);
        return null;
    }
}
