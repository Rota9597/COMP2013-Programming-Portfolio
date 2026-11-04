package intergration;

import engine.LevelBuilder;
import engine.PhysicsSystem;
import engine.PlayerControlSystem;
import engine.Scene;
import input.InputManager;
import model.Bullet;
import model.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerMovement {

    @SuppressWarnings("unchecked")
    private void forceKeyPress(InputManager input, String key) {
        try {
            // Retrieve the keysPressed field from the InputManager class
            Field keysPressedField = InputManager.class.getDeclaredField("keysPressed");
            // Set the field as accessible
            keysPressedField.setAccessible(true);

            // Obtain the reference to this private Set from the input instance
            Set<String> keysPressed = (Set<String>) keysPressedField.get(input);

            // Add buttons
            keysPressed.add(key.toUpperCase());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void forceRemoveKey(InputManager input, String key) {
        try {
            Field keysPressedField = InputManager.class.getDeclaredField("keysPressed");
            keysPressedField.setAccessible(true);

            Set<String> keysPressed = (Set<String>) keysPressedField.get(input);

            keysPressed.remove(key.toUpperCase());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testPlayerMovesAndShoots() {

        InputManager input = new InputManager();
        PhysicsSystem physics = new PhysicsSystem();
        Scene scene = LevelBuilder.buildLevel(input);
        PlayerControlSystem pcs = new PlayerControlSystem();
        scene.update(0);  // Trigger the merge logic
        Player player = scene.getPlayer();
        player.setSpeed(player.getMaxSpeed());

        assertNotNull(player, "Player must exist");

        int startX = player.getX();

        // Simulate pressing the D key
        forceKeyPress(input, "D");

        pcs.update(scene, 0.016);
        physics.applyPhysics(scene, 0.016);
        scene.update(0.016);

        assertTrue(player.getX() > startX,
                "Player should move right when D is pressed");

        forceRemoveKey(input, "D");

        // Simulated Jumping
        player.resetJumpCounter();
        player.setOnGround(true);
        player.setVelocity(0);

        player.setPrevJumpPressed(false);
        player.setGravity(0.5);

        // Press W
        forceKeyPress(input, "W");

        pcs.update(scene, 0.016);
        scene.update(0.016);

        assertEquals(-8, player.getVelocity(),
                "Player velocity should be -8 when first jump");

        assertEquals(1, player.getJumpCounter(),
                "After first jump jumpCounter should be 1");

        // Release W
        forceRemoveKey(input, "W");
        pcs.update(scene, 0.016);
        scene.update(0.016);

        // Double jump
        // Press W
        forceKeyPress(input, "W");
        pcs.update(scene, 0.016);
        physics.applyPhysics(scene, 0.016);
        scene.update(0.016);

        assertEquals(2, player.getJumpCounter(),
                "After double jump jumpCounter should be 2");

        // Simulate shooting
        int initialAmmo = player.getAmmo();

        Bullet bullet = player.shoot();

        assertNotNull(bullet, "Player should be able to shoot");
        assertEquals(player.getX() + 30, bullet.getX(), "Bullet X should match Player X on creation.");
        assertTrue(bullet.getSpeed() > 0, "Bullet must have a positive initial X velocity.");

        scene.addObject(bullet);
        scene.update(0);

        assertEquals(initialAmmo - 1, player.getAmmo(),
                "Ammo should decrease by 1 after shooting");

        long bulletCount = scene.getObjects().stream()
                .filter(o -> o instanceof Bullet)
                .count();

        assertTrue(bulletCount >= 1, "There should be at least one bullet in the scene");
    }
}
