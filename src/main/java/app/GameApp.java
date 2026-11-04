package app;

import audio.MusicPlayer;
import engine.GameEngine;
import engine.LevelBuilder;
import engine.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import input.InputManager;
import model.DifficultyConfig;
import model.DifficultyFactory;
import model.PreferencesManager;
import render.Renderer;
import resources.ResourceManager;

/**
 * Entry point of the CyborgPlatform JavaFX game application.
 *
 * <p>This class initialises core game systems, displays the start screen,
 * and launches the main game loop once player preferences are confirmed.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class GameApp extends Application {

    private GameEngine engine;
    private SceneManager sceneManager;
    private InputManager inputManager;

    /** The active JavaFX Scene used by the game. */
    private static Scene fxScene;

    /**
     * Initialises core systems and displays the start screen.
     *
     * @param stage the primary JavaFX window
     */
    @Override
    public void start(Stage stage) {

        // Preload game resources
        ResourceManager.loadResources();

        // Initialize core systems
        inputManager = new InputManager();
        sceneManager = new SceneManager();

        // Create and display the start screen
        StartScreenScene startScreen =
                new StartScreenScene(stage, () -> startGame(stage));

        stage.setScene(startScreen.getScene());
        stage.setTitle("CyborgPlatform");
        stage.show();
    }

    /**
     * Starts the main game using preferences selected on the start screen.
     *
     * @param stage the JavaFX stage used to display the game scene
     */

    private void startGame(Stage stage) {

        // Retrieve player preferences
        var prefs = PreferencesManager.getPreferences();

        // Apply background music setting
        MusicPlayer.getInstance().applyPreference(prefs.isMusicEnabled());

        // Apply difficulty configuration from preferences
        DifficultyFactory factory = new DifficultyFactory();
        DifficultyConfig config =
                factory.getConfig(prefs.getDifficultyLevel());

        // Create JavaFX Canvas
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Create JavaFX Scene (UI root container)
        StackPane root = new StackPane(canvas);
        Scene fx = new Scene(root);
        GameApp.fxScene = fx;

        // Bind keyboard input to the active JavaFX scene
        inputManager.bindToScene(fx);

        // Attach scene to stage
        stage.setScene(fx);

        // Apply selected UI theme colour
        Renderer.setThemeColor(
                javafx.scene.paint.Color.valueOf(prefs.getThemeColor())
        );

        // Load initial game scene
        engine.Scene level = LevelBuilder.buildLevel(inputManager, config);
        sceneManager.loadScene(level);

        // Initialise game engine
        engine = new GameEngine(sceneManager, config);
        engine.getCamera().setMapWidth(level.getMapWidth());

        // Main game loop
        new AnimationTimer() {
            long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                // Convert nanoseconds to seconds (dt = delta time)
                double dt = (now - lastTime) / 1e9;
                lastTime = now;

                engine.update(dt);
                render(gc, sceneManager.getActiveScene());
            }
        }.start();
    }

    public static Scene getFXScene() { return fxScene;}

    /**
     * Renders the current frame.
     *
     * <p>Depending on the game state, this method either renders the
     * active game world or the victory screen.
     *
     * @param gc    the graphics context used for drawing
     * @param scene the currently active game scene
     */
    private void render(GraphicsContext gc, engine.Scene scene) {

        // Clear screen
        gc.clearRect(0, 0, 1280, 720);

        if (engine.getCamera() == null || scene == null) return;

        if (engine.isWon()) {
            // Render victory UI
            Renderer.drawWinScreen(gc, engine);
        } else {
            // Render game world + UI
            Renderer.drawScene(gc, scene, engine.getCamera());
            Renderer.drawHUD(gc, scene.getPlayer(), engine.getCamera(), engine);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
