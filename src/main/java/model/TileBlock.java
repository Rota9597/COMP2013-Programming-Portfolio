package model;

/**
 * Represents a single tile-based platform created from a map definition.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class TileBlock extends Platform {

    private final TileType type;

    /**
     * Constructs a TileBlock at world coordinates using the given tile type.
     *
     * @param x    world X coordinate
     * @param y    world Y coordinate
     * @param type tile definition for this block
     */
    public TileBlock(int x, int y, TileType type) {
        super(
                x,
                y,
                Platform.TILE_SIZE,
                type.getHeight(),
                type.getImage()
        );
        this.type = type;
    }

    public TileType getTileType() {
        return type;
    }
}
