package Manager;

import Application.ImageProcessor;
import Application.UnionFind;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class UnionFindManagerTest {
    private ImageProcessor imageProcessor;
    private UnionFind unionFind;
    private Image image;
    private UnionFindManager unionFindManager;
    private PixelReader pixelReader;

    @BeforeEach
    void setup() {
        // Create mock objects for your tests
        imageProcessor = mock(ImageProcessor.class);
        unionFind = mock(UnionFind.class);
        image = mock(Image.class);
        pixelReader = mock(PixelReader.class);

        // Define the behavior
        when(imageProcessor.getPixelReader()).thenReturn(pixelReader);  // When getPixelReader() is called, return the mock PixelReader
        when(pixelReader.getColor(anyInt(), anyInt())).thenReturn(Color.WHITE);  // When getColor() is called with any integers, return WHITE

        // Initialize the object with the mock objects
        unionFindManager = new UnionFindManager(imageProcessor);
    }

    @Test
    void testUnionFind() {
        unionFindManager.unionFind(unionFind, image);

        // Verify that setup(image) was called exactly once on imageProcessor
        verify(imageProcessor, times(1)).setup(image);
    }

    @Test
    void testProcessUnionFind() {
        unionFindManager.processUnionFind(unionFind, 0, 0);

        // Verify that getColor(0, 0) was called exactly once on pixelReader
        verify(pixelReader, times(1)).getColor(0, 0);
    }

    @Test
    void testUnifyIfWhite() {
        // Define additional behavior for your mocks
        when(imageProcessor.getWidth()).thenReturn(2);  // When getWidth() is called, return 2
        when(imageProcessor.getHeight()).thenReturn(2);  // When getHeight() is called, return 2
        when(pixelReader.getColor(1, 0)).thenReturn(Color.WHITE);  // When getColor(1, 0) is called, return WHITE

        // Call the method
        unionFindManager.unifyIfWhite(unionFind, 0, 0, 0, 1, 0);

        // Verify that getColor(1, 0) was called exactly once on pixelReader
        verify(pixelReader, times(1)).getColor(1, 0);
    }
}


