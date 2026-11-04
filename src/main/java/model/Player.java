package model;

import engine.CameraSystem;
import engine.ScoreManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import input.InputManager;
import resources.ResourceManager;

/**
 * Represents the player character.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Player extends Character{

    private final Image[] idleSprites;
    private final Image[] runSprites;
    private final Image[] hurtSprites;
    private final Image aerialSprites;
    private final Image shootingSprite;
    private final Image heartImage;
    private final Image ammoBoxImage;
    private static final int PLAYER_HITBOX_WIDTH = 30;
    private static final int PLAYER_HITBOX_HEIGHT = 52;

    private final InputManager input;  // Reference to input manager used by this player instance.
    private final ScoreManager scoreManager;

    private final int MAX_SPEED = 5;
    private boolean prevJumpPressed = false;  // Tracks previous frame's jump key state to detect rising edge
    private static final int INVINCIBLE_DURATION = 1000;
    private int ammo;               // 不再默认 10，让构造函数决定
    private int ammoRewardPerKill;
    private boolean justShot = false;
    private long lastShot = 0;
    private int shootCooldown;
    private static final int NORMAL_SHOOT_COOLDOWN = 500;

    public Player(int x, int y, InputManager input, ScoreManager scoreManager, DifficultyConfig difficultyConfig) {
        super(x, y, PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);
        this.input = input;

        this.health = 3;
        this.onGround = true;

        idleSprites = ResourceManager.loadSequence("Sprites/Player/idle/Cyborg_idle_", 4);
        runSprites  = ResourceManager.loadSequence("Sprites/Player/run/Cyborg_run_", 6);
        hurtSprites = ResourceManager.loadSequence("Sprites/Player/hurt/Cyborg_hurt_", 2);
        aerialSprites = ResourceManager.loadImage("Sprites/Player/run/Cyborg_run_6.png");
        shootingSprite = ResourceManager.loadImage("Sprites/Player/shoot/shootingSprite.png");
        heartImage = ResourceManager.loadImage("Sprites/heart.png");
        ammoBoxImage = ResourceManager.loadImage("Sprites/ammo.png");

        this.scoreManager = scoreManager;
        this.ammo = difficultyConfig.getInitialAmmo();
        this.shootCooldown = (int) Math.round(
                NORMAL_SHOOT_COOLDOWN * difficultyConfig.getShootCooldownMultiplier()
        );
        this.ammoRewardPerKill = difficultyConfig.getAmmoRewardPerKill();
    }

    @Override
    public void update(double dt) {

        // Handle invulnerability timeout
        this.updateHurtStatus(INVINCIBLE_DURATION);

        // Handle shooting cooldown expiry
        if (justShot && System.currentTimeMillis() - lastShot > shootCooldown) {
            justShot = false;
        }

        selectAnimation();
        updateAnimation(dt);
    }

    public void takeDamage(int dmg, boolean enemyOnRight) {
        if (isHurt) return;

        health -= dmg;
        isHurt = true;
        lastHurtTime = System.currentTimeMillis();

        // Apply vertical & horizontal knockback impulse
        velocity = -6;
        speed = enemyOnRight ? -4 : 4;
    }

    public InputManager getInput() { return input; }
    public boolean getPrevJumpPressed() { return prevJumpPressed; }
    public void setPrevJumpPressed(boolean p) { this.prevJumpPressed = p; }

    public Image getHeartImage() { return heartImage; }
    public Image getAmmoBoxImage() { return ammoBoxImage; }
    public int getMaxSpeed() { return MAX_SPEED; }

    public Bullet shoot() {

        long now = System.currentTimeMillis();

        // Not allowed if cooling down or no ammo
        if (justShot || ammo <= 0) {
            return null;
        }

        ammo--;
        justShot = true;
        lastShot = now;

        boolean facingRight = this.isFacingRight();
        int bulletSpeed = (facingRight ? 10 : -10);

        // Offset so bullet appears at gun muzzle
        int bx = this.x + (facingRight ? 30 : -10);
        int by = this.y + 20;

        return new Bullet(bx, by, bulletSpeed);
    }

    public int getAmmo() { return ammo; }
    public void addAmmo() { ammo += ammoRewardPerKill;}  // Ammo reward when enemy dead

    private void selectAnimation() {

        if (justShot) {
            currentFrames = new Image[]{shootingSprite};
            return;
        }

        if (isHurt) {
            currentFrames = hurtSprites;
            return;
        }

        if (!onGround) {
            currentFrames = new Image[]{aerialSprites};
            return;
        }

        if (Math.abs(speed) > 0) {
            currentFrames = runSprites;
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

        // Align sprite to be centered horizontally and bottom-aligned vertically
        double offsetX = (PLAYER_HITBOX_WIDTH - spriteW) / 2.0;
        double offsetY = (PLAYER_HITBOX_HEIGHT - spriteH);

        double renderX = sx + offsetX;
        double renderY = sy + offsetY;

        if (right) {
            gc.drawImage(sprite, renderX, renderY);
        } else {
            // Draw mirrored sprite when facing left
            gc.drawImage(
                    sprite,
                    renderX + spriteW,
                    renderY,
                    -spriteW,
                    spriteH
            );
        }
    }

    public void collect(Coin collectible) {
        scoreManager.addScore(collectible.getValue());
    }

    public void onEnemyDefeated() {
        scoreManager.addScore(10);
    }
}
