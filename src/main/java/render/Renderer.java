package render;

import engine.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import engine.Scene;
import engine.CameraSystem;
import model.*;
import resources.ResourceManager;

/**
 * Responsible for rendering all visual elements of the game.
 *
 * Rendering order:
 *   1) Parallax background layers
 *   2) Static platforms
 *   3) Dynamic objects (Player, Enemies, Bullets, Effects)
 *   4) HUD (UI overlays)
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class Renderer {

    private static Color themeColor = Color.BLACK; // 默认主题

    public static void setThemeColor(Color color) { themeColor = color;}

    /** Draws the entire world state for the current frame. */
    public static void drawScene(GraphicsContext gc, Scene scene, CameraSystem camera) {

        if (scene == null || camera == null) return;

        drawBackground(gc, scene, camera);
        drawPlatforms(gc, scene, camera);
        drawObjects(gc, scene, camera);
    }

    private static void drawBackground(GraphicsContext gc, Scene scene, CameraSystem camera) {

        for (BackgroundLayer bg : scene.getBackgroundLayers()) {

            Image img = bg.getImage();
            if (img == null) continue;

            double factor = bg.getSpeedFactor();
            double offset = -camera.getCameraX() * factor;

            for (double x = offset; x < scene.getMapWidth(); x += 1280) {
                gc.drawImage(img, x, 0, 1280, 720);
            }
        }
    }

    private static void drawPlatforms(GraphicsContext gc, Scene scene, CameraSystem camera) {

        for (Platform p : scene.getPlatforms()) {

            double sx = camera.applyX(p.getX());
            double sy = p.getY();

            Image tile = p.getTileImage();

            if (tile != null)
                gc.drawImage(tile, sx, sy, p.getWidth(), p.getHeight());
        }
    }

    private static void drawObjects(GraphicsContext gc, Scene scene, CameraSystem camera) {

        for (GameObject obj : scene.getObjects()) {
            obj.render(gc, camera);
        }
    }

    /** Draws the in-game HUD (health, ammo, score, timer). */
    public static void drawHUD(GraphicsContext gc, Player player, CameraSystem camera, GameEngine engine) {

        if (player == null) return;

        int health = player.getHealth();
        int ammo = player.getAmmo();
        int deathCounter = engine.getDeathCount();
        long gameTimer = engine.getGameTimeMillis();
        int score = engine.getActiveScene().getScoreManager().getScore();

        Image heartImage = player.getHeartImage();
        Image ammoBoxImage = player.getAmmoBoxImage();

        // Format time → MM:SS
        long totalSeconds = gameTimer / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String timer = String.format("%02d:%02d", minutes, seconds);

        // Timer (large font)
        gc.setFont(ResourceManager.V1_FONT_60);
        gc.setFill(themeColor);
        gc.fillText(timer, 510, 68);

        // Attempts counter
        gc.setFont(ResourceManager.V1_FONT_20);
        gc.fillText("ATTEMPTS: " + deathCounter, 20, 110);

        // Score achieving
        gc.setFont(ResourceManager.V1_FONT_20);
        gc.fillText("SCORE: " + score, 20, 140);

        // Health hearts
        if (heartImage != null) {
            if (health > 0) gc.drawImage(heartImage, 20, 20);
            if (health > 1) gc.drawImage(heartImage, 89, 20);
            if (health > 2) gc.drawImage(heartImage, 158, 20);
        }

        // Ammo display
        if (ammoBoxImage != null) {
            gc.drawImage(ammoBoxImage, 1040, 20);
            gc.setFont(ResourceManager.V1_FONT_60);
            gc.fillText(String.valueOf(ammo), 1130, 68);
        }

        // Gameplay instructions
        double cameraOffset = camera.getCameraX();

        gc.setFont(ResourceManager.V1_FONT_20);
        gc.setFill(themeColor);

        String[] controls = new String[]{
                "USE WASD to Move",
                "You can Double Jump",
                "Use SPACE to Shoot",
                "Press ESCAPE to Restart"
        };
        int printAtY = 250;
        for (String control : controls) {
            gc.fillText(control, 470 - cameraOffset, printAtY);
            printAtY += 30;
        }

        // End game tunnel hint
        double WIN_HINT_X = 6800 - cameraOffset;

        gc.fillText("This is The Winner Tunnel", WIN_HINT_X, 310);
        gc.fillText("Just Keep Walking!", WIN_HINT_X, 340);
    }

    /** Draws the final “You Won” ending screen. */
    public static void drawWinScreen(GraphicsContext gc, GameEngine engine) {

        Scene scene = engine.getActiveScene();
        if (scene == null) return;

        // Draw the first two static background layers
        int layerCount = 0;
        for (BackgroundLayer bg : scene.getBackgroundLayers()) {
            if (layerCount >= 2) break;
            Image img = bg.getImage();
            if (img != null) gc.drawImage(img, 0, 0, 1280, 720);
            layerCount++;
        }

        int attempts = engine.getDeathCount();
        int score = scene.getScoreManager().getScore();

        gc.setFont(ResourceManager.V1_FONT_60);
        gc.setFill(themeColor);

        gc.fillText("You Won!", 460, 220);
        gc.fillText("It Took You", 375, 300);
        gc.fillText(attempts + " Attempts", 405, 380);
        gc.fillText("Final Score", 405, 420);
        gc.fillText(String.valueOf(score), 460, 490);
        gc.fillText("Press ESC to Restart,", 150, 460);
        gc.fillText("Or ENTER to exit", 270, 540);
    }
}
