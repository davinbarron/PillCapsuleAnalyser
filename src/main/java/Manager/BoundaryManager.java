package Manager;

import Application.ImageProcessor;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

// The BoundaryManager class is responsible for managing the boundaries of an image.
public class BoundaryManager {
    // An instance of ImageProcessor to process the image.
    private final ImageProcessor imageProcessor;

    // Constructor that initializes the ImageProcessor instance.
    public BoundaryManager(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    // Method to set the boundary of the image.
    // It initializes a boolean array representing the boundary of the image,
    // and sets each element to true if the corresponding pixel is a boundary pixel.
    public boolean[][] setBoundary(Image image) {
        // Set up the image processor with the image.
        imageProcessor.setup(image);

        // Initialize the boundary array with all false.
        boolean[][] boundary = new boolean[imageProcessor.getHeight()][imageProcessor.getWidth()];

        // Iterate over each pixel in the image.
        for (int y = 0; y < imageProcessor.getHeight(); y++) {
            for (int x = 0; x < imageProcessor.getWidth(); x++) {
                // Set the boundary array element to true if the pixel is a boundary pixel.
                boundary[y][x] = isBoundaryPixel(x, y);
            }
        }

        // Return the boundary array.
        return boundary;
    }

    // Method to check if a pixel is a boundary pixel.
    // A pixel is considered a boundary pixel if it is white and has a black pixel to its left or right.
    public boolean isBoundaryPixel(int x, int y) {
        // Get the color of the pixel.
        Color color = imageProcessor.getPixelReader().getColor(x, y);

        // If the pixel is white, check the pixels to its left and right.
        if (color.equals(Color.WHITE)) {
            // Return true if the pixel to the left or right is black.
            return (x > 0 && imageProcessor.getPixelReader().getColor(x - 1, y).equals(Color.BLACK)) ||
                    (x < imageProcessor.getWidth() - 1 && imageProcessor.getPixelReader().getColor(x + 1, y).equals(Color.BLACK));
        }

        // If the pixel is not white, it is not a boundary pixel.
        return false;
    }
}
