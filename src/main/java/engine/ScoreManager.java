package engine;

/**
 * Manages the player's score during a gameplay session.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class ScoreManager {

    private int score = 0;
    private double scoreMultiplier = 1.0;

    public ScoreManager() {
        this.score = 0;
    }

    /**
     * Adds the given amount to the current score.
     *
     * @param amount points to add (must be non-negative)
     */
    public void addScore(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Score increment cannot be negative");
        }
        score += Math.round(amount * scoreMultiplier);
    }

    public int getScore() { return score;}

    public void reset() { score = 0;}

    /**
     * Sets the score multiplier (e.g. based on difficulty).
     *
     * @param multiplier multiplier to apply to all future score additions
     */
    public void setScoreMultiplier(double multiplier) {
        if (multiplier <= 0) {
            throw new IllegalArgumentException("Score multiplier must be positive");
        }
        this.scoreMultiplier = multiplier;
    }
}
