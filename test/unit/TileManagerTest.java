package unit;

import engine.TileManager;
import model.TileBlock;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TileManagerTest {

    @Test
    void testLoadMapNotEmpty() {
        List<TileBlock> tiles = TileManager.loadTiles("Map.txt");
        assertFalse(tiles.isEmpty(),
                "The list of loaded maps should not be empty to ensure the successful loading of the maps.");
    }

    @Test
    void testTilePositionsAreAligned() {
        List<TileBlock> tiles = TileManager.loadTiles("Map.txt");

        for (TileBlock t : tiles) {
            assertEquals(0, t.getX() % 48);
            assertEquals(0, t.getY() % 48);
        }
    }
}
