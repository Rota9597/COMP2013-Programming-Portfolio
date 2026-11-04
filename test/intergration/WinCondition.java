package intergration;

import engine.GameEngine;
import engine.LevelBuilder;
import engine.Scene;
import input.InputManager;
import model.Player;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WinCondition {

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

    @Test
    void testWinConditionAndRestart() {
        InputManager input = new InputManager();
        GameEngine engine = new GameEngine(null);
        Scene scene = LevelBuilder.buildLevel(null);
        engine.getCamera().setMapWidth(scene.getMapWidth());

        Player player = scene.getPlayer();
        assertNotNull(player);

        // At the beginning, the player cannot win.
        assertFalse(engine.isWon(),
                "Initially game should not be won");

        // The player reaches the victory coordinate (x > 8000)
        player.setX(8100);
        engine.update(0.016);

        assertTrue(engine.isWon(),
                "Player should win when X > 8000");

        // Simulate ESC to restart the game
        forceKeyPress(input, "ESC");

        assertFalse(engine.isWon(),
                "After restart game should no longer be won");
        assertEquals(20, player.getX(), 0.001,
                "After restart, player X position must be reset to start location.");
        assertEquals(10, player.getAmmo(),
                "After restart, player ammo must be reset to 10.");
        assertEquals(0, player.getSpeed(),
                "After restart, player speed must be 0.");
    }
}
