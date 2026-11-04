package unit;

import engine.ScoreManager;
import model.DifficultyFactory;
import model.DifficultyLevel;
import model.Player;
import input.InputManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerInitialState() {
        Player p = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));

        assertEquals(3, p.getHealth(), "Initial health should be 3");
        assertEquals(10, p.getAmmo(), "Initial ammo should be 10");
    }

    @Test
    void testPlayerTakesDamageAndInvulnerability() {
        Player p = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        int initialHealth = p.getHealth();

        // First damage (successful)
        p.takeDamage(1, true); // Suppose the enemy is on the right.
        assertEquals(initialHealth - 1, p.getHealth(), "Health decrease 1");
        assertTrue(p.isHurt(), "Player should enter the invincible state");

        // Try damage again (failed)
        p.takeDamage(1, true);
        assertEquals(initialHealth - 1, p.getHealth(), "No effect of damage");
    }

    @Test
    void testPlayerDeathByHealth() {
        Player p = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        p.takeDamage(3, true);

        assertTrue(p.isDead(), "When health value <= 0, the player is dead.");
    }

    @Test
    void testPlayerShootingCooldown() {
        Player p = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        int initialAmmo = p.getAmmo();

        // First shoot (successful)
        p.shoot();
        assertEquals(initialAmmo - 1, p.getAmmo(), "The shot success. Ammo decrease 1");

        // Try shoot again (failed)
        p.shoot();
        assertEquals(initialAmmo - 1, p.getAmmo(), "During cool down time, shoot invalid");
    }
}
