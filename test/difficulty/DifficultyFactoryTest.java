package difficulty;

import model.DifficultyConfig;
import model.DifficultyFactory;
import model.DifficultyLevel;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyFactoryTest {

    private final DifficultyFactory factory = new DifficultyFactory();

    @Test
    void easyConfigLoadedCorrectly() {
        DifficultyConfig config = factory.getConfig(DifficultyLevel.EASY);

        assertEquals(5, config.getEnemyCount());
        assertEquals(5, config.getCollectibleCount());
        assertEquals(0.8, config.getEnemySpeedMultiplier());
        assertEquals(0.9, config.getShootCooldownMultiplier());
    }

    @Test
    void normalConfigLoadedCorrectly() {
        DifficultyConfig config = factory.getConfig(DifficultyLevel.NORMAL);

        assertEquals(13, config.getEnemyCount());
        assertEquals(10, config.getCollectibleCount());
        assertEquals(1.0, config.getEnemySpeedMultiplier());
    }

    @Test
    void hardConfigLoadedCorrectly() {
        DifficultyConfig config = factory.getConfig(DifficultyLevel.HARD);

        assertEquals(17, config.getEnemyCount());
        assertEquals(15, config.getCollectibleCount());
        assertEquals(1.3, config.getEnemySpeedMultiplier());
        assertEquals(1.2, config.getShootCooldownMultiplier());
    }

    @Test
    void collectibleCountDependsOnDifficulty() {
        DifficultyFactory factory = new DifficultyFactory();

        assertEquals(5,
                factory.getConfig(DifficultyLevel.EASY).getCollectibleCount());
        assertEquals(10,
                factory.getConfig(DifficultyLevel.NORMAL).getCollectibleCount());
        assertEquals(15,
                factory.getConfig(DifficultyLevel.HARD).getCollectibleCount());
    }


}
