package Manager;

import Application.PillSelection;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PillCapsuleManagerTest {
    private PillCapsuleManager pillCapsuleManager;
    private PillSelection pillSelection;

    @BeforeEach
    void setUp() {
        // Initialize your PillCapsuleManager and PillSelection objects here
        pillCapsuleManager = new PillCapsuleManager();
        pillSelection = new PillSelection("Test Pill", Color.RED, 0.5, 10, 20);
    }

    @Test
    void addPillSelection() {
        // Test the addPillSelection method here
        pillCapsuleManager.addPillSelection(pillSelection);
        assertTrue(pillCapsuleManager.getPillSelections().contains(pillSelection));
    }

    @Test
    void getPillSelections() {
        // Test the getPillSelections method here
        pillCapsuleManager.addPillSelection(pillSelection);
        assertEquals(1, pillCapsuleManager.getPillSelections().size());
        assertEquals(pillSelection, pillCapsuleManager.getPillSelections().get(0));
    }
}
