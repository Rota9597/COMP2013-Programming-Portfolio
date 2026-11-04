package model;

import engine.Scene;

/**
 * Abstract base class for all game entities.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public abstract class Entity {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // Reference to the scene that owns this entity.
    // Allows entity to query scene objects and environment.
    protected Scene scene;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setSceneReference(Scene scene) { this.scene = scene;}
    public Scene getScene() { return scene;}

    // Position & Size Getters
    public int getX() { return x;}
    public void setX(int x) { this.x = x;}

    public int getY() { return y;}
    public void setY(int y) { this.y = y;}

    public int getWidth() { return width;}
    public int getHeight() { return height;}
}