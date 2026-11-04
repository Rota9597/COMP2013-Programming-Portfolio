package engine;

import model.Player;

/**
 * Camera system that tracks the player's horizontal position.
 *
 * <p>The camera follows the player smoothly while remaining
 * within the bounds of the current level.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class CameraSystem {

    private double cameraX = 0;
    private final int windowWidth;
    private int mapWidth = 0;
    private final double smoothing = 0.1;

    /**
     * Creates a camera system for a window of the given width.
     *
     * @param windowWidth the width of the visible game window in pixels
     */
    public CameraSystem(int windowWidth) { this.windowWidth = windowWidth;}

    public void setMapWidth(int mapWidth) { this.mapWidth = mapWidth;}

    /**
     * Updates the camera position to follow the given player.
     *
     * @param player the player to track
     */
    public void updateCamera(Player player) {

        // Desired camera position: player centered on the screen horizontally
        int targetX = player.getX() - windowWidth / 2;

        // Smooth follow using linear interpolation
        cameraX += (targetX - cameraX) * smoothing;

        // Clamp to map boundaries
        if (cameraX < 0) cameraX = 0;
        if (cameraX > mapWidth - windowWidth) {
            cameraX = mapWidth - windowWidth;
        }
    }

    /**
     * Converts a world-space X coordinate into screen-space.
     *
     * @param worldX world-space X position
     * @return screen-space X position
     */
    public double applyX(double worldX) { return worldX - cameraX;}

    public double getCameraX() { return cameraX;}
}
