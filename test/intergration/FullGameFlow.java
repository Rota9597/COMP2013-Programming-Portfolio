package intergration;

import engine.GameEngine;
import engine.LevelBuilder;
import engine.Scene;
import engine.PhysicsSystem;
import model.Bullet;
import model.Enemy;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FullGameFlow {

    @Test
    void testCompleteGameplayLifecycle() {

        // Create games and scenes
        GameEngine engine = new GameEngine(null);
        Scene scene = LevelBuilder.buildLevel(null);

        PhysicsSystem physics = new PhysicsSystem();

        Player player = scene.getPlayer();
        assertNotNull(player);

        // Player shoots enemy and enemy dies
        Enemy enemy = new Enemy(player.getX() + 20, player.getY());
        scene.addObject(enemy);
        scene.update(0);

        int ammoBeforeKill = player.getAmmo();

        Bullet bullet1 = player.shoot();
        Bullet bullet2 = player.shoot();
        scene.addObject(bullet1);
        scene.addObject(bullet2);
        scene.update(0);

        physics.applyPhysics(scene, 0.016);
        engine.update(0.016);

        assertTrue(enemy.isDead(),
                "Enemy should die after HP <= 0");

        assertEquals(ammoBeforeKill, player.getAmmo(),
                "Player should gain 2 ammo after enemy death");

        // Player fall to death, deathCounter increased
        int attemptsBefore = engine.getDeathCount();

        player.setY(900); // FALL_DEATH_Y = 720
        engine.update(0.016);

        assertTrue(engine.getDeathCount() > attemptsBefore,
                "Attempts should increase when player dies");

        // The player restart the game after death.
        Player newPlayer = engine.getActiveScene().getPlayer();

        assertNotSame(player, newPlayer,
                "Player instance should be recreated");
        assertEquals(3, newPlayer.getHealth(),
                "New Player should start with full health");
        assertEquals(20, newPlayer.getX(),
                "New Player should be reset to the starting X position.");

        // Player reach to win position
        newPlayer.setX(8100);

        engine.update(0.016);

        assertTrue(engine.isWon(),
                "Game should be won when newPlayer reaches endpoint");
    }
}
