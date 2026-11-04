package engine;

import input.InputManager;
import model.Bullet;
import model.Player;
import model.CloudEffect;

/**
 * Interprets player input and updates the player's intended actions.
 *
 * <p>This system translates keyboard input into movement, jumping
 * and shooting requests without directly applying physics.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class PlayerControlSystem {

    /**
     * Updates the player's input-driven actions for the current frame.
     *
     * @param scene the active game scene
     * @param dt time elapsed since the previous frame
     */
    public void update(Scene scene, double dt) {

        Player player = scene.getPlayer();
        if (player == null || player.isDead() || player.isHurt()) {
            return;
        }

        InputManager input = player.getInput();
        final int MAX_SPEED = player.getMaxSpeed();

        // Horizontal movement intent (left/right)
        int speed = player.getSpeed();

        if (input.isKeyPressed("A") && input.isKeyPressed("D")) {
            speed = 0;
        } else if (input.isKeyPressed("A")) {
            speed -= 1;
            player.setFacingRight(false);
        } else if (input.isKeyPressed("D")) {
            speed += 1;
            player.setFacingRight(true);
        } else {
            speed = 0;
        }

        // Limit the max speed
        speed = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, speed));
        player.setSpeed(speed);

        // Jump logic
        boolean jumpPressed = input.isKeyPressed("W");
        boolean justPressed = jumpPressed && !player.getPrevJumpPressed();

        int jumpCounter = player.getJumpCounter();

        if (justPressed && jumpCounter <= 1) {

            if (jumpCounter == 1) {
                player.setVelocity(-6);  // Double jump
                // Spawn a cloud jump effect
                scene.addObject(new CloudEffect(
                        player.getX() + player.getWidth() / 2,
                        player.getY() + player.getHeight()
                ));
            } else {
                player.setVelocity(-8); // Normal jump
            }

            player.addJumpCounter(1);
            player.setOnGround(false);
        }
        player.setPrevJumpPressed(jumpPressed);

        // Adaptive gravity control during jump
        double gravity = player.getGravity();
        double v = player.getVelocity();
        boolean grounded = player.getOnGround();

        if (grounded || !jumpPressed || v > 0) {
            gravity = 0.5;
        }

        // Holding the jump key reduces gravity
        if (jumpPressed && v < 0 && gravity > 0.25) {
            final double V1_GRAV_REDUCTION_PER_SECOND = 1.2;
            gravity -= V1_GRAV_REDUCTION_PER_SECOND * dt;
            if (gravity < 0.25) gravity = 0.25;
        }

        player.setGravity(gravity);

        // Shooting
        if (input.isKeyPressed("SPACE")) {
            Bullet bullet = player.shoot();
            if (bullet != null) {
                scene.addObject(bullet);
            }
        }
    }
}