package intergration;

import engine.LevelBuilder;
import engine.Scene;
import engine.EnemyBehaviorSystem;
import model.Enemy;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyBehavior {

    @Test
    void testEnemyChasesPlayerAndPlayerGetsAmmoOnKill() {

        Scene scene = LevelBuilder.buildLevel(null);
        Player player = scene.getPlayer();
        assertNotNull(player);

        // Set one enemy
        // Player on the right side
        Enemy enemy = new Enemy(player.getX() + 100, player.getY());
        enemy.setSceneReference(scene);
        scene.addObject(enemy);
        scene.update(0);  // Trigger the merge logic

        EnemyBehaviorSystem ebs = new EnemyBehaviorSystem();

        int initialAmmo = player.getAmmo();

        ebs.update(scene);

        assertTrue(enemy.getSpeed() > 0,
                "Enemy should move right when player is on right side");

        assertTrue(enemy.isFacingRight(),
                "Enemy should face right");

        // Player on the left side
        player.setX(enemy.getX() - 200);

        ebs.update(scene);

        assertTrue(enemy.getSpeed() < 0,
                "Enemy should move left when player is on left side");

        assertFalse(enemy.isFacingRight(),
                "Enemy should face left");

        // Simulate enemy death
        enemy.takeDamage(enemy.getHealth());

        enemy.update(0.016);

        assertTrue(enemy.isDead(), "Enemy should die when health <= 0");

        assertTrue(player.getAmmo() > initialAmmo,
                "Player should gain 2 ammo when enemy dies");
    }
}
