package Application;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PillSelectionTest {
    private PillSelection pillSelection;

    @BeforeEach
    void setUp() {
        // Initialize your PillSelection object here
        pillSelection = new PillSelection("Test Pill", Color.RED, 0.5, 10, 20);
    }

    @Test
    void getName() {
        // Test the getName method here
        assertEquals("Test Pill", pillSelection.getName());
    }

    @Test
    void setName() {
        // Test the setName method here
        pillSelection.setName("New Test Pill");
        assertEquals("New Test Pill", pillSelection.getName());
    }

    @Test
    void getColor() {
        // Test the getColor method here
        assertEquals(Color.RED, pillSelection.getColor());
    }

    @Test
    void setColor() {
        // Test the setColor method here
        pillSelection.setColor(Color.BLUE);
        assertEquals(Color.BLUE, pillSelection.getColor());
    }

    @Test
    void getColorThreshold() {
        // Test the getColorThreshold method here
        assertEquals(0.5, pillSelection.getColorThreshold());
    }

    @Test
    void setColorThreshold() {
        // Test the setColorThreshold method here
        pillSelection.setColorThreshold(0.7);
        assertEquals(0.7, pillSelection.getColorThreshold());
    }

    @Test
    void getMinSize() {
        // Test the getMinSize method here
        assertEquals(10, pillSelection.getMinSize());
    }

    @Test
    void setMinSize() {
        // Test the setMinSize method here
        pillSelection.setMinSize(15);
        assertEquals(15, pillSelection.getMinSize());
    }

    @Test
    void getMaxSize() {
        // Test the getMaxSize method here
        assertEquals(20, pillSelection.getMaxSize());
    }

    @Test
    void setMaxSize() {
        // Test the setMaxSize method here
        pillSelection.setMaxSize(25);
        assertEquals(25, pillSelection.getMaxSize());
    }

    @Test
    void testToString() {
        // Test the toString method here
        String expected = "Name: Test Pill, Color: 0xff0000ff, Color Threshold: 0.5, Min Size: 10, Max Size: 20";
        assertEquals(expected, pillSelection.toString());
    }
}
