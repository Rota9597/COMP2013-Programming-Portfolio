package model;

/**
 * Abstract base class representing a controllable character entity.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public abstract class Character extends GameObject {

    protected boolean facingRight = true;
    protected boolean onGround = false;
    protected int jumpCounter = 0;
    protected double gravity = 0.5;  // initial gravity

    protected int health;
    protected boolean isHurt = false;
    protected long lastHurtTime;

    public Character(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setAffectedByGravity(true);
    }

    public boolean isDead() { return health <= 0 || getY() > 900;}

    public void onGroundHit() {
        this.onGround = true;
        this.resetJumpCounter();
    }

    public void setOnGround(boolean v) {
        this.onGround = v;
    }
    public boolean getOnGround() {
        return this.onGround;
    }
    public void resetJumpCounter() {
        this.jumpCounter = 0;
    }
    public void addJumpCounter(int value) {
        this.jumpCounter += value;
    }
    public int getJumpCounter() { return jumpCounter;}

    protected void updateHurtStatus(int duration) {
        if (isHurt && System.currentTimeMillis() - lastHurtTime > duration) {
            isHurt = false;
        }
    }

    public boolean isHurt() { return isHurt; }
    public int getHealth() { return health; }

    public boolean isFacingRight() { return facingRight; }
    public void setFacingRight(boolean value) { this.facingRight = value; }

    public double getGravity() { return gravity; }
    public void setGravity(double g) { this.gravity = g; }
}