package resources;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ResourceManager is the central system for loading and caching runtime assets.
 * This keeps resource access fast and memory efficient,
 * and maintains separation between asset IO and gameplay logic.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class ResourceManager {

    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, Image[]> sequenceCache = new HashMap<>();
    private static final Map<String, Font> fontCache = new HashMap<>();

    // Font file location
    private static final String FONT_PATH = "Font/font.ttf";

    // HUD fonts
    public static Font V1_FONT_60;
    public static Font V1_FONT_20;

    /** Pre-loads global frequently used resources. */
    public static void loadResources() {
        loadHUDResources();
        loadBackgroundLayers();
    }

    public static Image loadImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        InputStream is = ResourceManager.class.getResourceAsStream("/" + path);
        if (is == null) {
            System.err.println("[ResourceManager] Missing image: " + path);
            return null;
        }

        Image img = new Image(is);
        imageCache.put(path, img);
        return img;
    }

    public static Image[] loadSequence(String basePath, int count) {

        if (sequenceCache.containsKey(basePath)) {
            return sequenceCache.get(basePath);
        }

        Image[] frames = new Image[count];

        for (int i = 1; i <= count; i++) {
            String filePath = basePath + i + ".png";
            frames[i - 1] = loadImage(filePath);
        }

        sequenceCache.put(basePath, frames);
        return frames;
    }

    public static Font loadFont(String path, double size) {
        String key = path + "#" + size;

        if (fontCache.containsKey(key)) {
            return fontCache.get(key);
        }

        InputStream is = ResourceManager.class.getResourceAsStream("/" + path);
        if (is == null) {
            System.err.println("[ResourceManager] Missing font: " + path);
            return Font.getDefault();
        }

        Font font = Font.loadFont(is, size);
        fontCache.put(key, font);
        return font;
    }


    /** Loads the world background images in order of parallax depth. */
    public static Image[] loadBackgroundLayers() {

        return new Image[]{
                loadImage("Background/1_Background.png"),
                loadImage("Background/2_Background.png"),
                loadImage("Background/3_Background.png"),
                loadImage("Background/4_Background.png"),
                loadImage("Background/5_Background.png"),
                loadImage("Background/6_Background.png")
        };
    }

    public static InputStream loadMap(String path) {
        return ResourceManager.class.getResourceAsStream("/" + path);
    }

    private static void loadHUDResources() {
        try {
            Font baseFont = loadFont(FONT_PATH, 10);

            if (baseFont != null) {
                V1_FONT_60 = Font.font(baseFont.getFamily(), FontWeight.BOLD, 60);
                V1_FONT_20 = Font.font(baseFont.getFamily(), FontWeight.NORMAL, 20);
            } else {
                V1_FONT_60 = Font.font("Monospaced", FontWeight.BOLD, 60);
                V1_FONT_20 = Font.font("Monospaced", FontWeight.NORMAL, 20);
                System.err.println("[ResourceManager] HUD Font failed to load. Using Monospaced fallback.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
