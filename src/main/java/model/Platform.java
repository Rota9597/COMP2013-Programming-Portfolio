package model;

import javafx.scene.image.Image;

/**
 * Represents a static solid surface in the game world.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Platform extends Entity {

    private final Image tileImage;

    public static final int TILE_SIZE = 48;  // Standard visual tile size used for map layout.

    public Platform(int x, int y, int width, int height, Image tile) {
        super(x, y, width, height);
        this.tileImage = tile;
    }

    public Image getTileImage() { return tileImage; }
}
