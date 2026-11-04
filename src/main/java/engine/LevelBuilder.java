package engine;

import javafx.scene.image.Image;
import model.*;
import input.InputManager;
import resources.ResourceManager;

import java.util.List;

/**
 * Factory class for constructing fully initialised game scenes.
 *
 * <p>This class assembles all components required for a playable level,
 * including terrain, entities and difficulty-specific configuration.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class LevelBuilder {

    /* Enemy spawn coordinates per difficulty */

    private static final int[][] EASY_ENEMY_POSITIONS = {
            {1475, 230}, {2750, 320}, {4970, 530}, {6397, 196}, {6600, 520}
    };

    private static final int[][] NORMAL_ENEMY_POSITIONS = {
            {1475, 230}, {2570, 196}, {2750, 320}, {3060, 470},
            {4219, 100}, {4900, 530}, {4970, 530}, {5040, 539},
            {6397, 196}, {6540, 520}, {6600, 520}, {6660, 520},
            {6720, 520}
    };

    private static final int[][] HARD_ENEMY_POSITIONS = {
            {1475, 230}, {1950, 170}, {2570, 196}, {2750, 320}, {3060, 470},
            {4200, 100}, {4000, 150}, {4900, 530}, {4970, 530}, {5040, 539},
            {5600, 300}, {5800, 150}, {6397, 196}, {6540, 520}, {6600, 520},
            {6660, 520}, {6720, 520}
    };

    /* Coin spawn coordinates per difficulty */

    private static final int[][] EASY_COIN_POSITIONS = {
            {450, 450}, {1500, 450}, {2000, 300} ,{4200, 100}, {5800, 500}
    };

    private static final int[][] NORMAL_COIN_POSITIONS = {
            {450, 450}, {800, 450}, {2400, 200}, {1500, 450}, {2000, 300},
            {4200, 100}, {4970, 580}, {5800, 500}, {5600, 550}, {5850, 150}
    };

    private static final int[][] HARD_COIN_POSITIONS = {
            {450, 450}, {800, 450}, {2400, 200}, {1500, 450}, {2000, 300},
            {4200, 100}, {4970, 580}, {5800, 500}, {5600, 550}, {5850, 150},
            {3700, 200}, {4750, 580}, {5400, 550}, {6390, 200}, {6600, 520}
    };

    /**
     * Builds a complete game scene using the given input system and difficulty.
     *
     * @param input input system to bind to the player
     * @param difficultyConfig difficulty configuration for the level
     * @return a fully initialised game scene
     */
    public static Scene buildLevel(InputManager input, DifficultyConfig difficultyConfig) {

        Scene scene = new Scene();
        ScoreManager scoreManager= new ScoreManager();
        scene.setScoreManager(scoreManager);
        scene.setDifficultyConfig(difficultyConfig);
        scene.setMapWidth(8300);  // Set world width — adjusted so visual framing matches v1

        // Load platforms from tile map
        List<TileBlock> tiles = TileManager.loadTiles("Map.txt");
        for (TileBlock t : tiles) {
            scene.addPlatform(t);
        }

        // Load parallax backgrounds
        Image[] bg = ResourceManager.loadBackgroundLayers();
        scene.addBackgroundLayer(new BackgroundLayer(bg[0], 0.0625));
        scene.addBackgroundLayer(new BackgroundLayer(bg[1], 0.25));
        scene.addBackgroundLayer(new BackgroundLayer(bg[2], 0.5));
        scene.addBackgroundLayer(new BackgroundLayer(bg[3], 1));

        // Create player
        Player player = new Player(20, 300, input, scoreManager, difficultyConfig);
        scene.setPlayer(player);
        scene.addObject(player);

        // Spawn enemies at predefined coordinates
        int[][] enemyPositions;

        switch (difficultyConfig.getEnemyCount()) {
            case 5 -> enemyPositions = EASY_ENEMY_POSITIONS;
            case 13 -> enemyPositions = NORMAL_ENEMY_POSITIONS;
            case 17 -> enemyPositions = HARD_ENEMY_POSITIONS;
            default -> enemyPositions = NORMAL_ENEMY_POSITIONS;
        }

        for (int[] pos : enemyPositions) {
            Enemy enemy = new Enemy(pos[0], pos[1]);
            enemy.setSceneReference(scene);  // Allow enemies to query their containing scene
            scene.addObject(enemy);
        }

        // Spawn coins at predefined coordinates
        int[][] coinPositions;

        switch (difficultyConfig.getCollectibleCount()) {
            case 5 -> coinPositions = EASY_COIN_POSITIONS;
            case 10 -> coinPositions = NORMAL_COIN_POSITIONS;
            case 15 -> coinPositions = HARD_COIN_POSITIONS;
            default -> coinPositions = NORMAL_COIN_POSITIONS;
        }

        for (int[] pos : coinPositions) {
            Coin coin = new Coin(pos[0], pos[1]);
            coin.setSceneReference(scene);
            scene.addObject(coin);
        }

        return scene;
    }

    public static Scene buildLevel(InputManager input) {
        return buildLevel(input, new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
    }
}
