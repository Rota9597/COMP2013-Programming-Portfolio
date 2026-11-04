package model;

/**
 * Factory for creating {@link DifficultyConfig} instances.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class DifficultyFactory {

    public DifficultyConfig getConfig(DifficultyLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Difficulty level cannot be null");
        }

        return switch (level) {
            case EASY -> new DifficultyConfig(
                    5,      // enemyCount
                    5,      // collectibleCount
                    0.8,    // enemySpeedMultiplier
                    0.9,    // shootCooldownMultiplier
                    0.9,    // spawnRateMultiplier
                    0.8,     // scoreMultiplier
                    20,     // initialAmmo
                    3       // ammoRewardPerKill
            );
            case NORMAL -> new DifficultyConfig(
                    13,
                    10,
                    1.0,
                    1.0,
                    1.0,
                    1.0,
                    10,     // initialAmmo
                    2       // ammoRewardPerKill
            );
            case HARD -> new DifficultyConfig(
                    17,
                    15,
                    1.3,
                    1.2,
                    1.2,
                    1.3,
                    10,     // initialAmmo
                    1       // ammoRewardPerKill
            );
        };
    }
}
