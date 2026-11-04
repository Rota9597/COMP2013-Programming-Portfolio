package engine;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game scene that owns and manages all active game objects.
 *
 * <p>The scene stores game entities and provides a lifecycle for
 * updating and safely adding or removing objects during gameplay.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Scene {

    private final List<GameObject> objects = new ArrayList<>();
    private final List<GameObject> objectsToAdd = new ArrayList<>();
    private final List<Platform> platforms = new ArrayList<>();
    private final List<BackgroundLayer> backgroundLayers = new ArrayList<>();

    private ScoreManager scoreManager;
    private DifficultyConfig difficultyConfig;

    private Player player;
    private int mapWidth = 7200;

    public void setMapWidth(int w) { this.mapWidth = w; }
    public int getMapWidth() { return mapWidth; }

    public void setPlayer(Player p) {
        this.player = p;
    }
    public Player getPlayer() {
        return this.player;
    }

    public void addBackgroundLayer(BackgroundLayer bg) {
        backgroundLayers.add(bg);
    }
    public List<BackgroundLayer> getBackgroundLayers() {
        return backgroundLayers;
    }

    public void addPlatform(Platform p) {
        p.setSceneReference(this);
        platforms.add(p);
    }
    public List<Platform> getPlatforms() {
        return platforms;
    }
    public List<GameObject> getObjects() {
        return objects;
    }

    /**
     * Adds a game object to the scene using deferred insertion.
     *
     * @param obj the object to add
     */
    public void addObject(GameObject obj) {
        if (obj != null) {
            objectsToAdd.add(obj);
            obj.setSceneReference(this); // Allow object to query its scene context
        }
    }

    /**
     * Updates all active game objects for the current frame.
     *
     * @param dt time elapsed since the previous frame
     */
    public void update(double dt) {
        for (GameObject obj : objects) {
            obj.update(dt);
        }
        // Safe list merging — ensures objects can be spawned during iteration
        objects.addAll(objectsToAdd);
        objectsToAdd.clear();
    }

    /**
     * Removes game objects marked for deletion.
     */
    public void cleanupObjects() {
        objects.removeIf(GameObject::shouldRemove);
    }

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public void setDifficultyConfig(DifficultyConfig difficultyConfig) {
        this.difficultyConfig = difficultyConfig;
    }

    public DifficultyConfig getDifficultyConfig() {
        return difficultyConfig;
    }
}
