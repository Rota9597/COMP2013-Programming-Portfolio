package engine;

import model.DifficultyConfig;
import model.GameObject;
import model.Enemy;
import model.Player;

/**
 * Controls enemy movement and behaviour during gameplay.
 *
 * <p>This system updates enemy actions based on the player's position
 * and the current difficulty settings.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class EnemyBehaviorSystem {

    /**
     * Updates behaviour for all enemies in the given scene.
     *
     * @param scene the active game scene
     */
    public void update(Scene scene) {

        Player player = scene.getPlayer();
        if (player == null) return;

        DifficultyConfig config = scene.getDifficultyConfig();
        double enemySpeedMultiplier = (config != null) ? config.getEnemySpeedMultiplier() : 1.0;

        for (GameObject obj : scene.getObjects()) {
            if (obj instanceof Enemy enemy) {

                // If dead or currently in hurt state, skip processing
                if (enemy.isDead()) { continue;}
                if (enemy.isHurt()) {
                    // When hurt, apply speed deceleration
                    enemy.setSpeed(enemy.getSpeed() / 2);
                    continue;
                }

                // Chasing logic
                double dx = player.getX() - enemy.getX();
                double dy = player.getY() - enemy.getY();

                final int MAX_DISTANCE = enemy.getIdleRange(); // Maximum detection range 400
                final int RUNNING_RANGE = enemy.getRunRange(); // Range for running chase 200
                final int MAX_WALK_SPEED = (int) Math.round(enemy.getMaxWalkSpeed() * enemySpeedMultiplier);
                final int MAX_RUN_SPEED = (int) Math.round(enemy.getMaxRunSpeed() * enemySpeedMultiplier);
                final int VERTICAL_LIMIT = enemy.getChaseVerticalLimit(); // Max vertical chase allowance 100
                final int STOP_CHASE_RANGE = enemy.getClosestChasingRange();  // Min chase allowance 20

                double distanceFromPlayer = Math.hypot(dx, dy);
                boolean isChasing = (distanceFromPlayer <= MAX_DISTANCE) &&
                        ((enemy.getY() - player.getY()) < VERTICAL_LIMIT);

                int speed = enemy.getSpeed();

                if (isChasing) {
                    // Movement max speed increases if player is closer
                    int currentMaxSpeed = (distanceFromPlayer <= RUNNING_RANGE) ? MAX_RUN_SPEED : MAX_WALK_SPEED;

                    if (Math.abs(dx) > STOP_CHASE_RANGE) {

                        if (dx < 0) { // Player is left
                            if (speed > -currentMaxSpeed) speed -= 1;
                            enemy.setFacingRight(false);
                        } else { // Player is right
                            if (speed < currentMaxSpeed) speed += 1;
                            enemy.setFacingRight(true);
                        }
                    } else {
                        // Enemy stop chase when too close to player
                        speed = 0;
                    }

                    // Jumping logic (only while chasing)
                    boolean dist_con = (Math.hypot(dx, dy) <= 200 && Math.abs(dx) > 40);

                    // Reset jump counter on ground
                    if (enemy.getOnGround()) enemy.resetJumpCounter();

                    // Jump if player is above the enemy and jumping is allowed
                    if (enemy.getJumpCounter() < 1 && player.getY() < enemy.getY() && (dist_con || !enemy.getOnGround())) {
                        enemy.setVelocity(-6);
                        enemy.addJumpCounter( 1);
                        enemy.setOnGround(false);
                        enemy.setFacingRight(player.getX() > enemy.getX());
                    }

                } else {
                    speed = 0; // Player too far away → idle
                }
                // Apply computed speed
                enemy.setSpeed(speed);
            }
        }
    }
}