package audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * Singleton controller responsible for background music playback.
 *
 * <p>This class provides a centralised interface for loading and controlling
 * looping background music based on player preferences.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class MusicPlayer {

    private static MusicPlayer instance;
    private MediaPlayer mediaPlayer;

    private MusicPlayer() {
        try {
            Media media = new Media(
                    Objects.requireNonNull(getClass().getResource("/backgroundmusic.mp3")).toExternalForm()
            );
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            System.out.println("Failed to load background music");
        }
    }

    /** @return the singleton instance of {@code MusicPlayer} */
    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Applies the user's music preference.
     *
     * @param enabled true to enable music playback, false to disable it
     */
    public void applyPreference(boolean enabled) {
        if (mediaPlayer == null) return;

        if (enabled) {
            mediaPlayer.play();
        } else {
            mediaPlayer.stop();
        }
    }
}
