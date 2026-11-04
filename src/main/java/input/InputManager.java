package input;

import javafx.scene.Scene;
import java.util.HashSet;
import java.util.Set;

/**
 * Centralised handler for keyboard input state.
 *
 * <p>This class tracks pressed keys and allows systems to
 * query whether a key is currently held down.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class InputManager {

    private final Set<String> keysPressed = new HashSet<>();

    /**
     * Checks whether the given key is currently pressed.
     *
     * @param key key identifier (case-insensitive)
     * @return true if the key is pressed
     */
    public boolean isKeyPressed(String key) {
        return keysPressed.contains(key.toUpperCase());
    }

    /**
     * Binds this input manager to a JavaFX scene to receive keyboard events.
     *
     * @param scene the scene to bind input handling to
     */
    public void bindToScene(Scene scene) {
        scene.setOnKeyPressed(e -> keysPressed.add(e.getCode().toString()));
        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode().toString()));
    }
}
