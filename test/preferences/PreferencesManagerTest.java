package preferences;

import model.DifficultyLevel;
import model.Preferences;
import model.PreferencesManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreferencesManagerTest {

    @Test
    void defaultPreferencesExist() {
        Preferences prefs = PreferencesManager.getPreferences();
        assertNotNull(prefs);
    }

    @Test
    void setDifficultyUpdatesPreferences() {
        PreferencesManager.setDifficulty(DifficultyLevel.HARD);

        Preferences prefs = PreferencesManager.getPreferences();
        assertEquals(DifficultyLevel.HARD, prefs.getDifficultyLevel());
    }

    @Test
    void setThemeColorUpdatesPreferences() {
        PreferencesManager.setThemeColor("BLUE");

        Preferences prefs = PreferencesManager.getPreferences();
        assertEquals("BLUE", prefs.getThemeColor());
    }

    @Test
    void musicToggleStoredCorrectly() {
        PreferencesManager.setMusicEnabled(false);

        Preferences prefs = PreferencesManager.getPreferences();
        assertFalse(prefs.isMusicEnabled());
    }
}
