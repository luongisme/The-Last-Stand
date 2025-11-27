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


    private static Image getSprite(Image atlas, int xCord, int yCord, int width, int height) {
        if (atlas == null) {
            System.err.println("❌ Atlas is NULL in getSprite!");
            return null;
        }

        int x = xCord;
        int y = yCord;


        try {
            // Extract sub-image using JavaFX PixelReader
            PixelReader reader = atlas.getPixelReader();
            if (reader == null) {
                System.err.println("❌ PixelReader is NULL!");
                return null;
            }

            WritableImage subImage = new WritableImage(reader, x, y, width, height);
            return subImage;
        } catch (Exception e) {
            System.err.println("❌ Exception in getSprite: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Image[] loadSkillAnimationGrid(String fileName,
                                                 int frameCount,
                                                 int framesPerRow,
                                                 int frameWidth,
                                                 int frameHeight) {
        System.out.println("🎬 Loading animation: " + fileName);
        System.out.println("   ├─ frameCount: " + frameCount);
        System.out.println("   ├─ framesPerRow: " + framesPerRow);
        System.out.println("   ├─ frameWidth: " + frameWidth);
        System.out.println("   └─ frameHeight: " + frameHeight);

        Image sheet = loadSkillImage(fileName);
        if (sheet == null) {
            System.err.println("❌ Skill animation sheet not found: " + fileName);
            return null;
        }

        System.out.println("✅ Sheet loaded: " + sheet.getWidth() + "x" + sheet.getHeight());

        Image[] frames = new Image[frameCount];

        for (int i = 0; i < frameCount; i++) {
            int col = i % framesPerRow;      // cột
            int row = i / framesPerRow;      // hàng

            int x = col * frameWidth;
            int y = row * frameHeight;

            frames[i] = getSprite(sheet, x, y, frameWidth, frameHeight);

            if (frames[i] == null) {
                System.err.println("❌ Frame " + i + " is NULL at (" + x + ", " + y + ")");
            } else {
                System.out.println("✅ Frame " + i + " loaded: " + frames[i].getWidth() + "x" + frames[i].getHeight() + " from (" + x + ", " + y + ")");
            }
        }

        System.out.println("✅ Total frames loaded: " + frames.length);
        return frames;
    }

    public static Image[] loadDarkGhostAnim() {
        return loadSkillAnimationGrid(
                "DarkGhost.png", // tên file trong thư mục Skill
                10,              // frameCount
                10,              // framesPerRow
                40,              // frameWidth
                64               // frameHeight
        );
    }

    public static Image[] loadSandStoneAnim() {
        return loadSkillAnimationGrid(
                "SandStone.png",
                12,     // 6x2
                6,      // framesPerRow
                48, 48  // frameWidth, frameHeight
        );
    }

    public static Image[] loadThunderBoltAnim() {
        return loadSkillAnimationGrid(
                "ThunderBolt.png",
                13,     // frameCount
                13,     // framesPerRow (1 hàng)
                64, 64  // frameWidth, frameHeight
        );
    }

    public static Image[] loadWaterStrikeAnim() {
        return loadSkillAnimationGrid(
                "WaterStrike.png",
                16,     // 4x4
                4,      // framesPerRow
                80, 80  // frameWidth, frameHeight
        );
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
