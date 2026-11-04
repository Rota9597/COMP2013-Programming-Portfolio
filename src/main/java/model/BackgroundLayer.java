package model;

import javafx.scene.image.Image;

/**
 * Represents a single parallax background layer.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class BackgroundLayer {

    private final Image image;
    private final double speedFactor;

    public BackgroundLayer(Image image, double speedFactor) {
        this.image = image;
        this.speedFactor = speedFactor;
    }

    public Image getImage() {
        return image;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }
}
