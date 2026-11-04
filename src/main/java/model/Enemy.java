package model;

import engine.CameraSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import resources.ResourceManager;

/**
 * Represents an enemy character.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Enemy extends Character{

    private final Image[] idleSprites;
    private final Image[] walkingSprites;
    private final Image[] runningSprites;
    private final Image aerialSprites;
    private final Image hurtSprite;
    private static final int ENEMY_HITBOX_WIDTH = 30;
    private static final int ENEMY_HITBOX_HEIGHT = 64;
    // Movement detection range
    private final int IDLE_RANGE = 400;
    private final int RUN_RANGE = 200;
    private final int CHASE_VERTICAL_LIMIT = 100;
    private final int CLOSEST_CHASING_RANGE = 20;

    private final int maxWalkSpeed = 1;
    private final int maxRunSpeed = 3;

    private static final int HURT_DURATION = 300;

    public Enemy(int x, int y) {
        super(x, y, ENEMY_HITBOX_WIDTH, ENEMY_HITBOX_HEIGHT);

        this.health = 2;

        idleSprites = ResourceManager.loadSequence("Sprites/Enemy/Idle/Idle_", 8);
        walkingSprites = ResourceManager.loadSequence("Sprites/Enemy/Walking/Walk_", 8);
        runningSprites = ResourceManager.loadSequence("Sprites/Enemy/Running/Run_", 7);
        aerialSprites = ResourceManager.loadImage("Sprites/Enemy/Running/Run_5.png");
        hurtSprite = ResourceManager.loadImage("Sprites/Enemy/Hurt.png");

        currentFrames = idleSprites;
    }

    @Override
    public void update(double dt) {

        // If dead: remove, and reward player with ammo
        if (isDead()) {
            return;
        }

        // Clears hurt state after invulnerability duration expires
        this.updateHurtStatus(HURT_DURATION);

        // Select correct animation frame sequence and update it.
        selectAnimation();
        updateAnimation(dt);
    }

    private void selectAnimation() {

        double absSpeed = Math.abs(this.speed);

        if (isHurt) {
            currentFrames = new Image[]{hurtSprite};
            return;
        }
        if (!onGround) {
            currentFrames = new Image[]{aerialSprites};
            return;
        }

        if (absSpeed == 0) {
            currentFrames = idleSprites;
            return;
        }

        if (absSpeed > maxWalkSpeed) {
            currentFrames = runningSprites;
            return;
        }

        if (absSpeed > 0) {
            currentFrames = walkingSprites;
            return;
        }

        currentFrames = idleSprites;
    }

    @Override
    public void render(GraphicsContext gc, CameraSystem camera) {

        double sx = camera.applyX(this.x);
        double sy = this.y;

        Image sprite = getCurrentFrame();
        if (sprite == null) {
            return;
        }

        boolean right = this.isFacingRight();

        double spriteW = sprite.getWidth();
        double spriteH = sprite.getHeight();

        // Center horizontally, align bottom to hitbox base
        double offsetX = (ENEMY_HITBOX_WIDTH - spriteW) / 2.0;
        double offsetY = (ENEMY_HITBOX_HEIGHT - spriteH);

        double renderX = sx + offsetX;
        double renderY = sy + offsetY;

        // Draw normally or mirrored
        if (right) {
            gc.drawImage(sprite, renderX, renderY);
        } else {
            gc.drawImage(
                    sprite,
                    renderX + spriteW,
                    renderY,
                    -spriteW,
                    spriteH
            );
        }
    }

    public void takeDamage(int dmg) {
        if (isHurt) return;
        health -= dmg;
        isHurt = true;
        lastHurtTime = System.currentTimeMillis();
        this.setVelocity(-6);  // 击飞效果
    }

    public int getIdleRange() { return IDLE_RANGE; }
    public int getRunRange() { return RUN_RANGE; }
    public int getChaseVerticalLimit() { return CHASE_VERTICAL_LIMIT; }
    public int getClosestChasingRange() { return CLOSEST_CHASING_RANGE; }

    public int getMaxWalkSpeed() { return maxWalkSpeed; }
    public int getMaxRunSpeed() { return maxRunSpeed; }
}
