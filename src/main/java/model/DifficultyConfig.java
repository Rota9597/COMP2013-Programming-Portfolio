package model;

/**
 * Holds gameplay parameters based on selected difficulty.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class DifficultyConfig {

    private final int enemyCount;
    private final int collectibleCount;

    private final double enemySpeedMultiplier;
    private final double shootCooldownMultiplier;
    private final double spawnRateMultiplier;
    private final double scoreMultiplier;

    private final int initialAmmo;
    private final int ammoRewardPerKill;

    public DifficultyConfig(
            int enemyCount,
            int collectibleCount,
            double enemySpeedMultiplier,
            double shootCooldownMultiplier,
            double spawnRateMultiplier,
            double scoreMultiplier,
            int initialAmmo,
            int ammoRewardPerKill
    ) {
        this.enemyCount = enemyCount;
        this.collectibleCount = collectibleCount;
        this.enemySpeedMultiplier = enemySpeedMultiplier;
        this.shootCooldownMultiplier = shootCooldownMultiplier;
        this.spawnRateMultiplier = spawnRateMultiplier;
        this.scoreMultiplier = scoreMultiplier;
        this.initialAmmo = initialAmmo;
        this.ammoRewardPerKill = ammoRewardPerKill;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public int getCollectibleCount() {
        return collectibleCount;
    }

    public double getEnemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }

    public double getShootCooldownMultiplier() {
        return shootCooldownMultiplier;
    }

    public double getSpawnRateMultiplier() {
        return spawnRateMultiplier;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }
    public int getInitialAmmo() { return initialAmmo; }
    public int getAmmoRewardPerKill() { return ammoRewardPerKill; }
}
