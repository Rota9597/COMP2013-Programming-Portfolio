package unit;

import resources.ResourceManager;
import javafx.scene.image.Image;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceManagerTest {

    @Test
    void testImageLoads() {
        Image img = ResourceManager.loadImage("Sprites/heart.png");
        assertNotNull(img);
    }

    @Test
    void testSequenceLoads() {
        var frames = ResourceManager.loadSequence("Sprites/Player/idle/Cyborg_idle_", 4);
        assertEquals(4, frames.length);
    }
}
