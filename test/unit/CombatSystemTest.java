package unit;

import engine.CombatSystem;
import engine.Scene;
import engine.ScoreManager;
import input.InputManager;
import model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CombatSystemTest {

    @Test
    void testBulletHitsEnemy() {
        CombatSystem cs = new CombatSystem();
        Scene scene = new Scene();

        Enemy enemy = new Enemy(200, 100);
        Bullet bullet = new Bullet(200, 100, 10);

        scene.addObject(enemy);
        scene.addObject(bullet);
        scene.update(0);  // Trigger the merge logic

        cs.handleBulletEnemyCollision(scene);

        assertTrue(bullet.shouldRemove(),
                "After the bullet hits the enemy, it should be marked as removed.");
        assertEquals(1, enemy.getHealth(),
                "The enemy's health should decrease by 1 point after being injured.");
    }

    @Test
    void testEnemyPlayerCollision() {
        CombatSystem cs = new CombatSystem();
        Scene scene = new Scene();

        Player player = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        Enemy enemy = new Enemy(100 + player.getWidth() - 1, 100);

        int initialHealth = player.getHealth();

        scene.addObject(player);
        scene.addObject(enemy);
        scene.setPlayer(player);  // Ensure that the Scene knows which Player it is referring to
        scene.update(0);

        cs.handleEnemyPlayerCollision(scene);

        assertEquals(initialHealth - 1, player.getHealth(), "The player should take 1 point of damage.");
        assertTrue(player.isHurt(), "Players should enter the immune state");
        assertEquals(-6.0, player.getVelocity(), 0.1,
                "The player should be subjected to a vertical pushback with a speed of -6.0.");
        assertEquals(-4, player.getSpeed(),
                "The player should be subjected to a horizontal push (to the left)");
    }

    @Test
    void testEnemyDeathGivesAmmoReward() {
        CombatSystem cs = new CombatSystem();
        Scene scene = new Scene();
        Player player = new Player(100, 100, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        Enemy enemy = new Enemy(100, 100);

        scene.setPlayer(player);
        scene.addObject(enemy);
        scene.update(0);

        int initialAmmo = player.getAmmo();

        // Simulating the enemy being killed
        enemy.takeDamage(2);
        assertTrue(enemy.isDead(), "Marked as dead");

        cs.handleRewardsAndCleanup(scene);

        assertEquals(initialAmmo + 2, player.getAmmo(),
                "Enemy dead, reward 2 ammo");
        assertTrue(enemy.shouldRemove(), "Dead enemy should be marked as remove");
    }
}
