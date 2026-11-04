package intergration;

import engine.LevelBuilder;
import engine.Scene;
import input.InputManager;
import model.Enemy;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadScene {

    @Test
    void testSceneLoadsSuccessfully() {

        InputManager input = new InputManager();
        Scene scene = LevelBuilder.buildLevel(input);
        scene.update(0);  // Trigger the merge logic

        assertNotNull(scene, "Scene should not be null");
        assertNotNull(scene.getPlayer(), "Scene must contain a Player");
        assertEquals(8300, scene.getMapWidth(), "Scene width should be 8300");

        assertEquals(14, scene.getObjects().size(),
                "Scene should contain 1 player and 13 enemies");

        // Validate Player
        Player player = scene.getPlayer();
        assertEquals(3, player.getHealth(), "Player should start with 3 HP");
        assertEquals(10, player.getAmmo(), "Player should start with 10 ammo");

        // Validate platforms exist
        assertTrue(scene.getPlatforms().size() > 50,
                "There should be more than 50 platforms loaded from Map.txt");

        // Validate enemies exist
        long enemyCount = scene.getObjects().stream()
                .filter(o -> o instanceof Enemy)
                .count();

        assertEquals(13, enemyCount,
                "There should contain exactly 13 enemies");
    }
}
