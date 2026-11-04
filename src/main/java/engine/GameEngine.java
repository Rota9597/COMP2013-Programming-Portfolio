package engine;

import app.GameApp;
import input.InputManager;
import model.*;

/**
 * Core game engine responsible for coordinating all gameplay systems.
 *
 * <p>This class manages the main update loop and invokes
 * physics, combat, input, and camera systems each frame.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class GameEngine {

    private final SceneManager sceneManager;
    private final PhysicsSystem physicsSystem;
    private final CombatSystem combatSystem;
    private final CameraSystem cameraSystem;
    private final PlayerControlSystem playerControlSystem;
    private final EnemyBehaviorSystem enemyBehaviorSystem;
    private final DifficultyConfig difficultyConfig;

    // Y threshold below which falling results in death.
    private static final double FALL_DEATH_Y = 720;

    private boolean isWon = false;
    private int deathCount = 0;

    // Timestamp of when the current run started.
    private long startTime = System.currentTimeMillis();

    // Cached enemy spawn count derived from difficulty
    private int enemySpawnCount;
    // Cached enemy speed multiplier derived from difficulty
    private double enemySpeedMultiplier;

    /**
     * Creates a game engine with the given scene manager and difficulty.
     *
     * @param sceneManager manager controlling the active scene
     * @param difficultyConfig difficulty configuration for this game session
     */
    public GameEngine(SceneManager sceneManager, DifficultyConfig difficultyConfig) {
        this.sceneManager = sceneManager;
        this.physicsSystem = new PhysicsSystem();
        this.combatSystem = new CombatSystem();
        this.cameraSystem = new CameraSystem(1280);
        this.playerControlSystem = new PlayerControlSystem();
        this.enemyBehaviorSystem = new EnemyBehaviorSystem();
        this.difficultyConfig = difficultyConfig;

        applyDifficulty(difficultyConfig);
    }

    /**
     * Creates a game engine using the default difficulty.
     *
     * @param sceneManager manager controlling the active scene
     */
    public GameEngine(SceneManager sceneManager) {
        this(sceneManager, new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
    }

    /**
     * Updates the game state for one frame.
     *
     * @param dt time elapsed since the previous frame (in seconds)
     */
    public void update(double dt) {

        // If the player has already won, update only the win screen logic.
        if (isWon) {
            handleWinScreenInput();
            return;
        }

        Scene scene = sceneManager.getActiveScene();
        if (scene == null) return;

        // Check player state
        checkPlayerDeath(scene);

        // Handle player input
        playerControlSystem.update(scene, dt);

        // Handle enemy behavior
        enemyBehaviorSystem.update(scene);

        // Apply gravity, movement and collision resolution
        physicsSystem.applyPhysics(scene, dt);

        // Process combat interactions (hit detection)
        combatSystem.handleEnemyPlayerCollision(scene);
        combatSystem.handleBulletEnemyCollision(scene);
        combatSystem.handleRewardsAndCleanup(scene);
        combatSystem.handlePlayerCollectibles(scene);

        // Update internal object state (animations, timers, etc.)
        scene.update(dt);
        // Update camera to follow the player
        cameraSystem.updateCamera(scene.getPlayer());

        // Remove game objects flagged for deletion
        scene.cleanupObjects();

        // Re-evaluate state after cleanup
        checkPlayerDeath(scene);
        checkWinCondition(scene);
    }

    private void checkPlayerDeath(Scene scene) {
        Player player = scene.getPlayer();
        if (player == null) return;

        boolean deadByHealth = (player.getHealth() <= 0);
        boolean deadByFall = (player.getY() > FALL_DEATH_Y);

        if (deadByHealth || deadByFall || (player.getInput().isKeyPressed("ESCAPE"))) {
            incrementDeathCount();
            restartLevel();
        }
    }

    private void checkWinCondition(Scene scene) {
        Player player = scene.getPlayer();
        final int WIN_X_COORDINATE = 8000;

        if (player != null && player.getX() > WIN_X_COORDINATE) {
            isWon = true;
        }
    }

    private void restartLevel() {

        // Create a new input
        InputManager input = new InputManager();
        input.bindToScene(GameApp.getFXScene());

        // Create a new scene
        Scene newScene = LevelBuilder.buildLevel(input, difficultyConfig);
        sceneManager.loadScene(newScene);
        this.cameraSystem.setMapWidth(newScene.getMapWidth());
    }

    public void incrementDeathCount() {
        this.deathCount++;
    }
    public int getDeathCount() {
        return deathCount;
    }

    public long getGameTimeMillis() { return System.currentTimeMillis() - startTime;}

    public Scene getActiveScene() {
        return sceneManager.getActiveScene();
    }

    public CameraSystem getCamera() { return cameraSystem;}

    public boolean isWon() {
        return isWon;
    }

    private void handleWinScreenInput() {

        Scene scene = sceneManager.getActiveScene();
        if (scene == null || scene.getPlayer() == null) return;

        InputManager input = scene.getPlayer().getInput();
        if (input == null) return;

        if (input.isKeyPressed("ESCAPE")) {
            // Reset statistics upon restarting after victory
            this.deathCount = 0;
            this.startTime = System.currentTimeMillis();
            restartLevel();
            this.isWon = false;
        }
        if (input.isKeyPressed("ENTER")) {
            System.exit(0);
        }
    }

    /**
     * Applies difficulty-related parameters to the current game state.
     *
     * @param config difficulty configuration to apply
     */
    public void applyDifficulty(DifficultyConfig config) {
        this.enemySpawnCount = config.getEnemyCount();
        this.enemySpeedMultiplier = config.getEnemySpeedMultiplier();

        Scene scene = sceneManager.getActiveScene();
        if (scene != null) {
            scene.getScoreManager().setScoreMultiplier(config.getScoreMultiplier());
        }
    }

    public int getEnemySpawnCount() {
        return enemySpawnCount;
    }

    public double getEnemySpeedMultiplier() {
        return enemySpeedMultiplier;
    }
}
