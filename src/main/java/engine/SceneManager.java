package engine;

/**
 * Manages transitions between game scenes and tracks the active scene.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class SceneManager {

    private Scene activeScene;

    /**
     * Sets the active scene.
     *
     * @param scene the scene to activate
     */
    public void loadScene(Scene scene) {
        this.activeScene = scene;
    }

    public Scene getActiveScene() {
        return activeScene;
    }
}
