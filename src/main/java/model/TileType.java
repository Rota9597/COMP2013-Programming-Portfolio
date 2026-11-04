package model;

import javafx.scene.image.Image;
import resources.ResourceManager;

/**
 * Defines all tile categories used for constructing static level geometry.
 *
 * Each TileType is mapped to:
 * - a character symbol used in the text-based map file
 * - a texture path used for rendering
 * - a physical height (affects hitbox size)
 *
 * TileType is used by TileManager to convert map characters
 * into actual TileBlock objects placed into the world.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public enum TileType {

    // Represents empty space — not rendered and not colliding.
    EMPTY('0', null, 0),

    T1('1', "Tiles/1_FrameTopLeftCorner.png", 48),
    T2('2', "Tiles/2_FrameTopRightCorner.png", 48),
    T3('3', "Tiles/3_FrameBottomLeftCorner.png", 48),
    T4('4', "Tiles/4_FrameBottomRightCorner.png", 48),
    T5('5', "Tiles/5_FrameTopMid.png", 48),
    T6('6', "Tiles/6_FrameLeftMid.png", 48),
    T7('7', "Tiles/7_FrameRightMid.png", 48),
    T8('8', "Tiles/8_FrameBottomMod.png", 48),
    T9('9', "Tiles/9_FrameMid.png", 48),

    TA('A', "Tiles/A_Box.png", 48),

    TB('B', "Tiles/B_HalfSlab.png", 24),
    TC('C', "Tiles/C_IndustrialTabLeft.png", 24),
    TD('D', "Tiles/D_IndustrialSlabMid.png", 24),
    TE('E', "Tiles/E_IndustrialSlabRight.png", 24),

    TF('F', "Tiles/F_LightPole.png", 48),
    TG('G', "Tiles/G_LightTop.png", 48),

    TH('H', "Tiles/H_TreadLeft.png", 24),
    TI('I', "Tiles/I_TreadMid.png", 24),
    TJ('J', "Tiles/J_TreadRight.png", 24);

    private final char symbol;
    private final String path;
    private final int height;

    TileType(char symbol, String path, int height) {
        this.symbol = symbol;
        this.path = path;
        this.height = height;
    }

    public static TileType fromChar(char c) {
        for (TileType t : values()) {
            if (t.symbol == c) return t;
        }
        return EMPTY;
    }

    public Image getImage() {
        if (path == null) return null;
        return ResourceManager.loadImage(path);
    }

    public int getHeight() { return height;}
}
