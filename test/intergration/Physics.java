package intergration;

import engine.LevelBuilder;
import engine.Scene;
import engine.PhysicsSystem;
import model.Platform;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Physics {

    @Test
    void testGravityAndFloorCollision() {

        Scene scene = LevelBuilder.buildLevel(null);
        PhysicsSystem physics = new PhysicsSystem();
        scene.update(0);  // Trigger the merge logic
        Player player = scene.getPlayer();
        assertNotNull(player, "Player must exist");

        // Simulate player in the air
        player.setOnGround(false);
        player.setVelocity(0);

        double startY = player.getY();

        for (int i = 0; i < 10; i++) {
            physics.applyPhysics(scene, 0.016);
        }

        assertTrue(player.getY() > startY,
                "Player must fall downward due to gravity");

        // Simulate player landing on the platform.
        // Manually position the player at the exact landing spot.
        player.setY(200);
        player.setVelocity(5);
        player.setOnGround(false);

        Platform testPlatform = new Platform(
                player.getX(),
                250,
                48,
                48,
                null
        );
        scene.addPlatform(testPlatform);

        for (int i = 0; i < 10; i++) {
            physics.applyPhysics(scene, 0.016);
        }

        assertTrue(player.getOnGround(),
                "Player should be standing on platform");

        assertEquals(testPlatform.getY() - player.getHeight(),
                player.getY(),
                "Player Y must align exactly on top of platform");

        // Simulate jumping from below and colliding with the bottom of the platform
        player.setY(testPlatform.getY() + testPlatform.getHeight() + 5);
        player.setVelocity(-8);

        double before_v = player.getVelocity();

        for (int i = 0; i < 6; i++) {
            physics.applyPhysics(scene, 0.016);
        }

        double after_v = player.getVelocity();

        assertTrue(after_v > before_v,
                "Velocity should flip to downward after hitting ceiling");
    }
}
