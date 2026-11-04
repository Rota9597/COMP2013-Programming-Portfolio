package unit;

import model.Enemy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    @Test
    void testEnemyTakesDamage() {
        Enemy e = new Enemy(100, 100);
        e.setVelocity(0);
        int initialHealth = e.getHealth();

        e.takeDamage(1);

        assertEquals(initialHealth - 1, e.getHealth(), "Health decrease 1");
        assertEquals(-6, e.getVelocity(), "Get a vertical pushback with a speed of -6.");
    }

    @Test
    void testEnemyInvulnerabilityAfterDamage() {
        Enemy e = new Enemy(100, 100);
        int initialHealth = e.getHealth();

        // First damage (successful)
        e.takeDamage(1);
        assertEquals(initialHealth - 1, e.getHealth(), "First damage success");
        assertTrue(e.isHurt(), "Marked is hurt");

        // Try damage again (failed)
        e.takeDamage(1);
        assertEquals(initialHealth - 1, e.getHealth(), "Second damage failed");
    }

    @Test
    void testEnemyDeathByFalling() {
        Enemy e = new Enemy(100, 100);

        assertFalse(e.isDead(), "Check enemy is alive");

        e.setY(901);  // Enemy fall

        assertTrue(e.isDead(), "Enemy should be dead");
    }

    @Test
    void testEnemyDeathByDamaging() {
        Enemy e = new Enemy(100, 100);

        e.takeDamage(2);

        assertTrue(e.isDead(), "Enemy should be dead");
    }
}
