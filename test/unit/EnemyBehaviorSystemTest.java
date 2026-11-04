package unit;

import engine.EnemyBehaviorSystem;
import engine.Scene;
import engine.ScoreManager;
import model.DifficultyFactory;
import model.DifficultyLevel;
import model.Enemy;
import model.Player;
import input.InputManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyBehaviorSystemTest {

    @Test
    void testEnemyChasesPlayerRightWalking() {
        EnemyBehaviorSystem ebs = new EnemyBehaviorSystem();
        Scene scene = new Scene();
        Enemy enemy = new Enemy(0, 0);
        Player player = new Player(300, 0, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.addObject(enemy);
        scene.setPlayer(player);
        scene.update(0);  // Trigger the merge logic

        ebs.update(scene);

        assertTrue(enemy.isFacingRight(), "Enemy facing right");
        assertEquals(enemy.getMaxWalkSpeed(), enemy.getSpeed(), "Enemy walk");
    }

    @Test
    void testEnemyChasesPlayerLeftRunning() {
        EnemyBehaviorSystem ebs = new EnemyBehaviorSystem();
        Scene scene = new Scene();
        Enemy enemy = new Enemy(100, 0);
        Player player = new Player(0, 0, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.addObject(enemy);
        scene.setPlayer(player);
        scene.update(0);

        for (int i = 0; i < 3; i++) {
            ebs.update(scene);
        }

        assertFalse(enemy.isFacingRight(), "Enemy facing left");
        assertEquals(-enemy.getMaxRunSpeed(), enemy.getSpeed(), "Enemy run");
    }

    @Test
    void testEnemyStopsWhenFarAway() {
        EnemyBehaviorSystem ebs = new EnemyBehaviorSystem();
        Scene scene = new Scene();
        Enemy enemy = new Enemy(0, 0);
        Player player = new Player(500, 0, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.addObject(enemy);
        scene.setPlayer(player);
        scene.update(0);

        ebs.update(scene);

        assertEquals(0, enemy.getSpeed(),
                "When the player is too far (>400), enemy should stop chasing.");
    }

    @Test
    void testEnemyStopsWhenTooClose() {
        EnemyBehaviorSystem ebs = new EnemyBehaviorSystem();
        Scene scene = new Scene();
        Enemy enemy = new Enemy(0, 0);
        Player player = new Player(10, 0, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.addObject(enemy);
        scene.setPlayer(player);
        scene.update(0);

        enemy.setSpeed(1);
        ebs.update(scene);

        assertEquals(0, enemy.getSpeed(),
                "When the player is too close (<20), enemy should stop chasing.");
    }

    @Test
    void testEnemyConditionalJump() {
        EnemyBehaviorSystem ebs = new EnemyBehaviorSystem();
        Scene scene = new Scene();
        Enemy enemy = new Enemy(100, 100);
        // The player is above the enemy (Y < 100) and the horizontal distance in range (dx > 40)
        Player player = new Player(150, 50, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        scene.addObject(enemy);
        scene.setPlayer(player);
        scene.update(0);

        // Precondition: On the ground, and not jumping
        enemy.setOnGround(true);
        enemy.resetJumpCounter();

        ebs.update(scene);

        assertEquals(-6, enemy.getVelocity(), "Enemy vertical speed is -6.");
        assertEquals(1, enemy.getJumpCounter(), "jumpCounter increase 1");
        assertFalse(enemy.getOnGround(), "Marked not on ground");

        // Try jump again (failed)
        ebs.update(scene);

        assertEquals(1, enemy.getJumpCounter(), "jumpCounter remain unchanged");
    }
}
