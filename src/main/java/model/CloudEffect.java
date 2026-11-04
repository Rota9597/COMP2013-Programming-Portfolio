package model;

import engine.CameraSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.ResourceManager;

/**
 * Visual-only particle effect used when performing a double-jump.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class CloudEffect extends GameObject {

    // Cloud effect remains visible for 0.3s.
    private static final double DURATION = 0.3;
    private double lifeTime = 0;

    private static final String CLOUD_IMAGE_PATH = "Sprites/cloud.png";
    private static final int CLOUD_WIDTH = 30;
    private static final int CLOUD_HEIGHT = 10;
    private static Image cloudSprite;

    public CloudEffect(int centerX, int feetY) {
        // Initial placement centered horizontally at (centerX)
        super(centerX - CLOUD_WIDTH / 2, feetY - CLOUD_HEIGHT, CLOUD_WIDTH, CLOUD_HEIGHT);

        // Load sprite
        if (cloudSprite == null) {
            cloudSprite = ResourceManager.loadImage(CLOUD_IMAGE_PATH);
        }

        this.currentFrames = new Image[]{cloudSprite};
        this.frameDuration = DURATION;

        // Purely visual — no gravity, no physical interaction
        this.setAffectedByGravity(false);

        int verticalOffset = 10;
        // Lift the effect slightly for visual appeal
        this.setY(feetY - this.height + verticalOffset);
    }

    @Override
    public boolean isAffectedByGravity() { return false; }

    @Override
    public void update(double dt) {
        lifeTime += dt;
        if (lifeTime >= DURATION) {
            markForRemoval();
        }
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
    public int getWidth() { return CLOUD_WIDTH; }
    @Override
    public int getHeight() { return CLOUD_HEIGHT; }
}
