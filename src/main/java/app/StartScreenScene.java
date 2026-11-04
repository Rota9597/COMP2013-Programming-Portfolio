package app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DifficultyLevel;
import model.PreferencesManager;

/**
 * Start screen scene used to configure player preferences
 * before starting the main game.
 *
 * <p>This class displays the initial UI and stores the player's
 * selected settings for use when the game begins.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class StartScreenScene {

    private final Scene scene;
    private final Runnable startGameCallback;

    /**
     * Creates the start screen and initialises all UI controls.
     *
     * @param stage the JavaFX stage used for display
     * @param startGameCallback callback triggered when the player starts the game
     */
    public StartScreenScene(Stage stage, Runnable startGameCallback) {
        this.startGameCallback = startGameCallback;

        // Difficulty selection
        ComboBox<DifficultyLevel> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll(DifficultyLevel.values());
        difficultyBox.setValue(DifficultyLevel.NORMAL);
        PreferencesManager.setDifficulty(DifficultyLevel.NORMAL);
        difficultyBox.setOnAction(e ->
            PreferencesManager.setDifficulty(difficultyBox.getValue())
        );

        // Theme Selection
        ComboBox<String> themeBox = new ComboBox<>();
        themeBox.getItems().addAll("BLACK", "BLUE", "PINK", "PURPLE", "YELLOW", "RED", "GREEN", "ORANGE");
        themeBox.setValue("BLACK");
        PreferencesManager.setThemeColor("BLACK");
        themeBox.setOnAction(e ->
                PreferencesManager.setThemeColor(themeBox.getValue())
        );

        // Music Toggle
        CheckBox musicToggle = new CheckBox("Background Music");
        musicToggle.setSelected(true);
        PreferencesManager.setMusicEnabled(true);
        musicToggle.setOnAction(e ->
                PreferencesManager.setMusicEnabled(musicToggle.isSelected())
        );

        // Start Button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGameCallback.run());

        // Layout
        VBox layout = new VBox(20,
                difficultyBox,
                themeBox,
                musicToggle,
                startButton
        );
        layout.setAlignment(Pos.CENTER);

        this.scene = new Scene(layout, 1280, 720);
    }

    public Scene getScene() { return scene;}
}
