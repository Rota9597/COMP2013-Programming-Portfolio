package unit;

import engine.PlayerControlSystem;
import engine.Scene;
import engine.ScoreManager;
import model.DifficultyFactory;
import model.DifficultyLevel;
import model.Player;
import input.InputManager;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerControlSystemTest {

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
    void testJumpCounterIncrement() {
        PlayerControlSystem pcs = new PlayerControlSystem();
        Scene scene = new Scene();

        InputManager input = new InputManager();
        Player p = new Player(100, 100, input, new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.setPlayer(p);
        scene.addObject(p);
        scene.update(0);  // Trigger the merge logic

        // simulate pressing W
        forceKeyPress(input, "W");
        pcs.update(scene, 0.016);

        assertEquals(1, p.getJumpCounter(),
                "After first jump, jumpCounter should be 1");
        assertEquals(-8.0, p.getVelocity(), 0.001,
                "The velocity after a single jump should be -8.0.");

        forceRemoveKey(input, "W");
        pcs.update(scene, 0.016);
        forceKeyPress(input, "W");
        pcs.update(scene, 0.016);

        assertEquals(2, p.getJumpCounter(),
                "After double jump, jumpCounter should be 2");
        assertEquals(-6.0, p.getVelocity(), 0.001,
                "The velocity after a double jump should be -6.0");
    }

    @Test
    void testHorizontalMovement() {
        PlayerControlSystem pcs = new PlayerControlSystem();
        Scene scene = new Scene();
        InputManager input = new InputManager();
        Player p = new Player(100, 100, input, new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.setPlayer(p);
        scene.update(0);

        // Ensure that the player's initial speed is set to 0
        assertEquals(0, p.getSpeed(), "Initial speed is 0");

        // Test for rightward acceleration (D)
        forceKeyPress(input, "D");

        // First update: speed += 1
        pcs.update(scene, 0.016);
        assertEquals(1, p.getSpeed(), "speed should be 1");
        assertTrue(p.isFacingRight(),
                "When pressing D, player should facing right.");

        // Continue to accelerate will reach MAX_SPEED
        for (int i = 0; i < 10; i++) {
            pcs.update(scene, 0.016);
        }
        assertEquals(5, p.getSpeed(), "Max speed is 5.");


        // Test by both releasing keys A and D (stop)
        forceRemoveKey(input, "D");
        pcs.update(scene, 0.016);

        assertEquals(0, p.getSpeed(),
                "Both releasing keys A and D, speed = 0");


        // Test for leftward acceleration (A)
        forceKeyPress(input, "A");

        // First update: speed -= 1
        pcs.update(scene, 0.016);
        assertEquals(-1, p.getSpeed(), "speed should be -1");
        assertFalse(p.isFacingRight(),
                "When pressing A, player should facing left.");

        // Continue to accelerate will reach -MAX_SPEED
        for (int i = 0; i < 10; i++) {
            pcs.update(scene, 0.016);
        }
        assertEquals(-5, p.getSpeed(), "Max speed is -5.");

        // Test by both pressing keys A and D (stop)
        forceKeyPress(input, "D");
        pcs.update(scene, 0.016);

        assertEquals(0, p.getSpeed(),
                "Both pressing keys A and D, speed = 0");
    }

    @Test
    void testShootingCooldown() {
        PlayerControlSystem pcs = new PlayerControlSystem();
        Scene scene = new Scene();
        InputManager input = new InputManager();
        Player p = new Player(100, 100, input, new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.setPlayer(p);
        scene.addObject(p);
        scene.update(0);

        int initialAmmo = p.getAmmo();

        // The first shot (successful)
        forceKeyPress(input, "SPACE");
        pcs.update(scene, 0.016);

        assertEquals(initialAmmo - 1, p.getAmmo(), "Ammo decrease 1");
        scene.update(0); // Run the "update" function to merge the new Bullet
        assertEquals(2, scene.getObjects().size(),
                "there should be 2 objects (Player + Bullet) in the Scene.");

        // Try to shoot again in cooling period (failed)
        pcs.update(scene, 0.016);

        assertEquals(initialAmmo - 1, p.getAmmo(), "Ammo not change");
        scene.update(0);
        assertEquals(2, scene.getObjects().size(), "No new object be created");
    }
}
