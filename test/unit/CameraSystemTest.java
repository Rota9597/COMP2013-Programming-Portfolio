package unit;

import engine.CameraSystem;
import engine.ScoreManager;
import model.DifficultyFactory;
import model.DifficultyLevel;
import model.Player;
import input.InputManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CameraSystemTest {

    private static final double SMOOTHING = 0.1;

    @Test
    void testCameraFollowCenter() {
        CameraSystem camera = new CameraSystem(1280);
        Player player = new Player(2000, 300, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL) );
        camera.setMapWidth(7200);

        camera.updateCamera(player);

        double expected = (2000 - 1280 / 2) * SMOOTHING;
        assertEquals(expected, camera.getCameraX(),
                "After the first update of the camera, it should smoothly move to 10% of the target position.");
    }

    @Test
    void testCameraClampLeftBoundary() {
        CameraSystem camera = new CameraSystem(1280);
        Player player = new Player(40, 300, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        camera.setMapWidth(7200);

        camera.updateCamera(player);

        assertEquals(0, camera.getCameraX(),
                "The camera should be clamped to the left boundary at 0.");
    }

    @Test
    void testCameraClampRightBoundary() {
        CameraSystem camera = new CameraSystem(1280);
        Player player = new Player(7900, 300, new InputManager(), new ScoreManager(), new DifficultyFactory().getConfig(DifficultyLevel.NORMAL));
        camera.setMapWidth(7200);

        // Multiple updates were made to allow the camera to converge and trigger the right boundary clamp.
        for (int i = 0; i < 50; i++) {
            camera.updateCamera(player);
        }

        assertEquals(7200 - 1280, camera.getCameraX());
    }
}
