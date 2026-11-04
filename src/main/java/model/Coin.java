package model;

import engine.CameraSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.ResourceManager;

/**
 * Represents a collectible coin.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Coin extends GameObject {

    private final int value = 5;

    private static final String COIN_IMAGE_PATH = "Sprites/coin.png";
    private static final int COIN_WIDTH = 23;
    private static final int COIN_HEIGHT = 30;
    private static Image coinSprite;

    public Coin(int x, int y) {
        super(x, y, COIN_WIDTH, COIN_HEIGHT); // size placeholder

        if (coinSprite == null) {
            coinSprite = ResourceManager.loadImage(COIN_IMAGE_PATH);
        }
        this.currentFrames = new Image[]{coinSprite};

        this.setAffectedByGravity(false);
    }

    @Override
    public boolean isAffectedByGravity() { return false; }

    public int getValue() {
        return value;
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void render(GraphicsContext gc, CameraSystem camera) {
        double sx = camera.applyX(this.x);
        double sy = this.y;

        Image sprite = getCurrentFrame();

        if (sprite != null) {
            gc.drawImage(sprite, sx, sy, getWidth(), getHeight());
        }
    }

    @Override
    public int getWidth() { return COIN_WIDTH; }
    @Override
    public int getHeight() { return COIN_HEIGHT; }
}
