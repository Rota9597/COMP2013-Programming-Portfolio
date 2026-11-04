package model;

/**
 * Central access point for runtime player preferences.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class PreferencesManager {

    private static Preferences preferences = new Preferences();

    public static Preferences getPreferences() { return preferences;}

    public static void setDifficulty(DifficultyLevel level) { preferences.setDifficultyLevel(level);}

    public static void setThemeColor(String color) { preferences.setThemeColor(color);}

    public static void setMusicEnabled(boolean enabled) { preferences.setMusicEnabled(enabled);}
}
