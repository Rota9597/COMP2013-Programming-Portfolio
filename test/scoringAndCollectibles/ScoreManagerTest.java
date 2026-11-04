package scoringAndCollectibles;

import engine.ScoreManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreManagerTest {

    private ScoreManager scoreManager;

    @BeforeEach
    void setup() {
        scoreManager = new ScoreManager();
    }

    @Test
    void scoreStartsAtZero() {
        assertEquals(0, scoreManager.getScore());
    }

    @Test
    void addScoreIncreasesScore() {
        scoreManager.addScore(10);
        assertEquals(10, scoreManager.getScore());
    }

    @Test
    void multipleScoreUpdatesAccumulate() {
        scoreManager.addScore(5);
        scoreManager.addScore(10);

        assertEquals(15, scoreManager.getScore());
    }

    @Test
    void resetClearsScore() {
        scoreManager.addScore(20);
        scoreManager.reset();

        assertEquals(0, scoreManager.getScore());
    }

    @Test
    void scoreMultiplierApplied() {
        scoreManager.setScoreMultiplier(1.3);
        scoreManager.addScore(10);

        assertEquals(13, scoreManager.getScore());
    }
}
