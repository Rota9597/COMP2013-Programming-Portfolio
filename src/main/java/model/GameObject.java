package model;

import javafx.scene.image.Image;

/**
 * Abstract base class for all interactive game objects.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public abstract class GameObject extends Entity {

    protected Image[] currentFrames;    // Current active sprite frame sequence
    protected int frameIndex = 0;       // Current index within currentFrames animation array.
    protected double frameTimer = 0;    // Accumulated time since last frame change.
    protected double frameDuration = 0.1; // How long each frame should display (seconds).

    protected int speed;                // Horizontal movement speed
    protected double velocity;          // Vertical movement speed
    protected boolean toRemove = false; // If this object should be removed
    protected boolean affectedByGravity;// If this object should affect by gravity

    public GameObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Updates the object for the current frame.
     *
     * @param dt time elapsed since the previous frame (seconds)
     */
    public abstract void update(double dt);

    public int getSpeed() { return speed; }
    public double getVelocity() { return velocity; }

    public void setSpeed(int v) { this.speed = v; }
    public void setVelocity(double v) { this.velocity = v; }

    public void markForRemoval() { toRemove = true;}
    public boolean shouldRemove() { return toRemove;}

    public void setAffectedByGravity(boolean value) { affectedByGravity = value;}
    public boolean isAffectedByGravity() { return affectedByGravity;}

    public void updateAnimation(double dt) {

        if (currentFrames == null || currentFrames.length <= 1)
            return;

        frameTimer += dt;

        if (frameTimer >= frameDuration) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % currentFrames.length;
        }
    }

    /**
     * Renders this object to the screen.
     */
    public abstract void render(javafx.scene.canvas.GraphicsContext gc, engine.CameraSystem camera);

    public Image getCurrentFrame() {
        if (currentFrames == null || currentFrames.length == 0) return null;
        if (frameIndex >= currentFrames.length) {
            frameIndex = 0;
        }
        return currentFrames[frameIndex];
    }

    public void setCurrentFrames(Image[] frames) {
        this.currentFrames = frames;
        this.frameIndex = 0;
    }
}
