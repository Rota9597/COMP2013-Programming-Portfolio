package unit;

import engine.PhysicsSystem;
import engine.Scene;
import engine.ScoreManager;
import model.DifficultyFactory;
import model.DifficultyLevel;
import model.Player;
import model.Platform;
import input.InputManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhysicsSystemTest {

    @Test
    void testGravityApplied() {
        PhysicsSystem physics = new PhysicsSystem();
        Scene scene = new Scene();

        Player player = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        scene.addObject(player);
        scene.update(0);  // Trigger the merge logic

        double expected = player.getVelocity() + player.getGravity() * 0.016 * 50;
        physics.applyPhysics(scene, 0.016);

        assertEquals(expected, player.getVelocity(),
                "The vertical velocity should be equal to the calculation result.");
    }

    @Test
    void testPlayerLandsOnPlatform() {
        PhysicsSystem physics = new PhysicsSystem();
        Scene scene = new Scene();

        Player player = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        Platform ground = new Platform(100, 200, 48, 48, null);

        scene.addObject(player);
        scene.addPlatform(ground);

        player.setVelocity(5);

        // simulate falling
        for (int i = 0; i < 30; i++) {
            physics.applyPhysics(scene, 0.016);
        }

        assertTrue(player.getOnGround());
    }

    @Test
    void testHorizontalMovementAndCollision() {
        PhysicsSystem physics = new PhysicsSystem();
        Scene scene = new Scene();

        Player player = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        // Wall position: In front of the player
        Platform wall = new Platform(135, 80, 48, 48, null);

        scene.addObject(player);
        scene.addPlatform(wall);
        scene.update(0);

        player.setSpeed(5);
        player.setAffectedByGravity(false);  // Excluding the influence of gravity

        // First update: Player is moving, no collision yet
        physics.applyPhysics(scene, 0.016);
        assertEquals(105, player.getX(), "Players should move horizontally.");

        // Second update: Player will collide with the wall.
        physics.applyPhysics(scene, 0.016);

        assertEquals(0, player.getSpeed(), "After the collision, the speed should be 0.");
        assertTrue(player.getX() < wall.getX() + player.getWidth(),
                "The player's position should stop in front of the wall (without crossing the wall)");
    }

    @Test
    void testCeilingCollision() {
        PhysicsSystem physics = new PhysicsSystem();
        Scene scene = new Scene();

        Player player = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        // Ceiling position: Above the player
        Platform ceiling = new Platform(100, 48, 48, 48, null);

        scene.addObject(player);
        scene.addPlatform(ceiling);
        scene.update(0);

        double initialUpVelocity = -8.0;
        player.setVelocity(initialUpVelocity);

        // Loop until collision
        int maxFrames = 50;
        for (int i = 0; i < maxFrames && player.getVelocity() < 0; i++) {
            // Use a very small dt to precisely simulate
            physics.applyPhysics(scene, 0.001);
        }

        // After the collision, the speed should reverse and decrease.
        double expectedBounceVelocity = -(initialUpVelocity / 4.0);

        assertEquals(expectedBounceVelocity, player.getVelocity(), 0.1,
                "After the collision, the speed should reverse and decrease");
        assertEquals(ceiling.getY() + ceiling.getHeight(), player.getY(),
                "The Y coordinate of the player should be adjusted to be below the ceiling.");
    }
}
