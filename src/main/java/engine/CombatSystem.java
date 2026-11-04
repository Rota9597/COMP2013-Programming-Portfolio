package engine;

import model.*;

import java.util.List;

/**
 * Handles combat-related interactions within a game scene.
 *
 * <p>This system processes collisions between players, enemies,
 * projectiles and collectibles during the game update loop.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class CombatSystem {

    /**
     * Handles physical collisions between the player and enemies.
     *
     * @param scene the active game scene
     */
    public void handleEnemyPlayerCollision(Scene scene) {

        Player player = scene.getPlayer();
        if (player == null) return;

        List<GameObject> objects = scene.getObjects();

        for (GameObject obj : objects) {
            if (obj instanceof Enemy enemy) {

                if (intersects(player, enemy)) {

                    // Determine enemy attack director
                    boolean enemyOnRight = enemy.getX() > player.getX();
                    player.takeDamage(1, enemyOnRight);
                }
            }
        }
    }

    /**
     * Handles collisions between bullets and enemies.
     *
     * @param scene the active game scene
     */
    public void handleBulletEnemyCollision(Scene scene) {

        List<GameObject> objects = scene.getObjects();

        for (GameObject obj : objects) {
            if (obj instanceof Bullet bullet) {

                for (GameObject obj2 : objects) {
                    if (obj2 instanceof Enemy enemy) {

                        if (intersects(bullet, enemy)) {
                            enemy.takeDamage(bullet.getDamage());
                            bullet.markForRemoval();
                        }
                    }
                }
            }
        }
    }

    /**
     * Processes defeated enemies and applies related rewards.
     *
     * @param scene the active game scene
     */
    public void handleRewardsAndCleanup(Scene scene) {
        Player player = scene.getPlayer();
        if (player == null) return;

        List<GameObject> objects = scene.getObjects();

        for (GameObject obj : objects) {
            if (obj instanceof Enemy enemy) {

                if (enemy.isDead()) {
                    player.addAmmo();
                    player.onEnemyDefeated();
                    enemy.markForRemoval();
                }
            }
        }
    }

    /**
     * Handles player interactions with collectible objects.
     *
     * @param scene the active game scene
     */
    public void handlePlayerCollectibles(Scene scene) {

        Player player = scene.getPlayer();
        if (player == null) return;

        List<GameObject> objects = scene.getObjects();

        for (GameObject obj : objects) {
            if (!(obj instanceof Coin coin)) continue;

            if (intersects(player, coin)) {
                player.collect(coin);
                coin.markForRemoval();
            }
        }
    }

    private boolean intersects(GameObject a, GameObject b) {

        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
