package model;

import engine.CameraSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.ResourceManager;

/**
 * Represents a projectile fired by the player.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Bullet extends GameObject {

    private final int damage = 1;  // Default damage is 1.
    private final Image bulletSprite;

    private int distanceTravelled = 0;
    // Fixed dimensions of the bullet sprite.
    private static final int BULLET_WIDTH = 8;
    private static final int BULLET_HEIGHT = 5;
    private static final int MAX_DISTANCE = 600;  // Maximum distance the bullet can travel

    public Bullet(int x, int y, int speed) {
        super(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        this.speed = speed;
        bulletSprite = ResourceManager.loadImage("Sprites/Player/shoot/bullet.png");

        this.currentFrames = new Image[]{bulletSprite};

        this.setAffectedByGravity(false);
    }

    @Override
    public void update(double dt) {
        x += speed;
        distanceTravelled += Math.abs(speed);

        if (distanceTravelled >= MAX_DISTANCE) {
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

    public int getDamage() { return damage;}
    public void markForRemoval() { super.markForRemoval();}
}
