package engine;

import model.TileBlock;
import model.TileType;
import resources.ResourceManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads tile-based level geometry from a map file.
 *
 * @author Shuyu Zheng
 * @version v3
 * @since v3
 */
public class TileManager {

    /**
     * Loads tile blocks from a map file.
     *
     * @param mapPath path to the map file
     * @return a list of tile blocks representing solid geometry
     */
    public static List<TileBlock> loadTiles(String mapPath) {

        List<TileBlock> tiles = new ArrayList<>();
        InputStream is = ResourceManager.loadMap(mapPath);

        if (is == null) {
            throw new RuntimeException("Map file not found: " + mapPath);
        }

        Scanner sc = new Scanner(is);

        int tileY = 0;

        // Iterate through each line
        while (sc.hasNextLine()) {

            String line = sc.nextLine();
            char[] chars = line.toCharArray();

            int tileX = 0;

            // Iterate through each character
            for (char c : chars) {

                TileType type = TileType.fromChar(c);

                // Skip empty space, create TileBlock only for solid tiles
                if (type != TileType.EMPTY) {
                    tiles.add(new TileBlock(tileX, tileY, type));
                }

                // Move to next tile column
                tileX += 48;
            }

            // Move to next tile row
            tileY += 48;
        }

        return tiles;
    }
}
