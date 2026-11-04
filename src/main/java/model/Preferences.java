package model;

/**
 * Stores player-selected preferences.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Preferences {

    private DifficultyLevel difficultyLevel = DifficultyLevel.NORMAL;
    private String themeColor = "BLACK";
    private boolean musicEnabled = true;

    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel level) { this.difficultyLevel = level; }

    public String getThemeColor() { return themeColor; }
    public void setThemeColor(String themeColor) { this.themeColor = themeColor; }

    public boolean isMusicEnabled() { return musicEnabled; }
    public void setMusicEnabled(boolean musicEnabled) { this.musicEnabled = musicEnabled; }
}
