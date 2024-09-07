package Manager;

import Application.ImageProcessor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BoundaryManagerTest {
    private ImageProcessor imageProcessor;
    private Image image;
    private BoundaryManager boundaryManager;
    private PixelReader pixelReader;

    @BeforeEach
    void setup() {
        // Create mock objects for your tests
        imageProcessor = mock(ImageProcessor.class);
        image = mock(Image.class);
        pixelReader = mock(PixelReader.class);

        // Define the behavior
        when(imageProcessor.getPixelReader()).thenReturn(pixelReader);  // When getPixelReader() is called, return the mock PixelReader
        when(pixelReader.getColor(anyInt(), anyInt())).thenReturn(Color.WHITE);  // When getColor() is called with any integers, return WHITE

        // Initialize the object with the mock objects
        boundaryManager = new BoundaryManager(imageProcessor);
    }

    @Test
    void testSetBoundary() {
        boundaryManager.setBoundary(image);

        // Verify that setup(image) was called exactly once on imageProcessor
        verify(imageProcessor, times(1)).setup(image);
    }

    @Test
    void testIsBoundaryPixel() {
        // Define additional behavior for your mocks
        when(imageProcessor.getWidth()).thenReturn(2);  // When getWidth() is called, return 2
        when(imageProcessor.getHeight()).thenReturn(2);  // When getHeight() is called, return 2
        when(pixelReader.getColor(0, 1)).thenReturn(Color.WHITE);  // When getColor(0, 1) is called, return WHITE
        when(pixelReader.getColor(1, 1)).thenReturn(Color.BLACK);  // When getColor(1, 1) is called, return BLACK

        // Call the method
        boolean isBoundary = boundaryManager.isBoundaryPixel(0, 1);

        // Verify that getColor(0, 1) and getColor(1, 1) were each called exactly once on pixelReader
        verify(pixelReader, times(1)).getColor(0, 1);
        verify(pixelReader, times(1)).getColor(1, 1);

        // Assert that isBoundary is true
        assertTrue(isBoundary);
    }
}
