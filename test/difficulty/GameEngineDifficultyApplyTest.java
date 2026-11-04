package difficulty;

import engine.GameEngine;
import engine.SceneManager;
import model.DifficultyConfig;
import model.DifficultyFactory;
import model.DifficultyLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameEngineDifficultyApplyTest {

    @Test
    void gameEngineAppliesDifficultyConfig() {
        DifficultyFactory factory = new DifficultyFactory();
        DifficultyConfig hardConfig = factory.getConfig(DifficultyLevel.HARD);

        GameEngine engine = new GameEngine(new SceneManager(), hardConfig);
        engine.applyDifficulty(hardConfig);

        assertEquals(17, engine.getEnemySpawnCount());
        assertEquals(1.3, engine.getEnemySpeedMultiplier());
    }

}
