package engine;

import model.GameObject;
import model.Character;
import model.Platform;

import java.util.List;

/**
 * Physics simulation system responsible for applying movement
 * and resolving collisions within the game world.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class PhysicsSystem {

    /**
     * Applies physics simulation to all objects in the scene.
     *
     * @param scene the active game scene
     * @param dt time elapsed since the previous frame
     */
    public void applyPhysics(Scene scene, double dt) {

        List<GameObject> objects = scene.getObjects();
        List<Platform> platforms = scene.getPlatforms();

        for (GameObject obj : objects) {

            // Apply gravity first
            applyGravity(obj, dt);

            // Then apply movement and collision
            handleHorizontalMovement(obj, platforms);
            handleVerticalMovement(obj, platforms);
        }
    }

    private void applyGravity(GameObject obj, double dt) {

        if (!obj.isAffectedByGravity())  return;

        double v = obj.getVelocity();
        double gravity = 0.5;

        // Player may have different gravity values
        if (obj instanceof Character character) {
            gravity = character.getGravity();
        }

        // Convert legacy v1 gravity values to a real-time acceleration model
        double V1_ACCEL_PER_SECOND = 50;

        v += gravity * V1_ACCEL_PER_SECOND * dt;
        obj.setVelocity(v);
    }

    private void handleHorizontalMovement(GameObject obj, List<Platform> platforms) {

        int stepX = obj.getSpeed();
        int currentX = obj.getX();
        int currentY = obj.getY();
        int nextX = obj.getX() + stepX;

        Platform hitX = null;
        if (stepX != 0) {
            for (Platform p : platforms) {
                if (intersects(obj, p, nextX, currentY)) {
                    hitX = p;
                    break;
                }
            }
        }

        if (hitX == null) {
            obj.setX(nextX);
        } else {
            // Collision → snap back and stop
            obj.setX(currentX);
            obj.setSpeed(0);
        }
    }

    private void handleVerticalMovement(GameObject obj, List<Platform> platforms) {

        int currentX = obj.getX();
        double velocity = obj.getVelocity();
        int stepY = (int) Math.round(velocity);
        int nextY = obj.getY() + stepY;

        Platform hitY = null;
        if (stepY != 0) {
            for (Platform p : platforms) {
                if (intersects(obj, p, currentX, nextY)) {
                    hitY = p;
                    break;
                }
            }
        }

        if (hitY == null) {
            obj.setY(nextY);
            if (obj instanceof Character c) {
                c.setOnGround(false);
            }
        } else {

            // Ceiling collision
            if (velocity < 0) {
                // 碰撞响应（保持不变）
                velocity = -(velocity / 4.0);
                obj.setVelocity(velocity);
                obj.setY(hitY.getY() + hitY.getHeight());
                return;
            }

            // Landing on platform
            if (velocity > 0) {
                // 碰撞响应（保持不变）
                if (velocity > 1.5) velocity = velocity / 1.5;

                obj.setVelocity(velocity);
                obj.setY(hitY.getY() - obj.getHeight());

                if (obj instanceof Character c) {
                    c.onGroundHit();
                }
            }
        }
    }

    private boolean intersects(GameObject a, Platform p, int predictedX, int predictedY) {
        return (
                predictedX < p.getX() + p.getWidth() &&
                        predictedX + a.getWidth() > p.getX() &&
                        predictedY < p.getY() + p.getHeight() &&
                        predictedY + a.getHeight() > p.getY()
        );
    }
}
