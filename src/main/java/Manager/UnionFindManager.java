package Manager;

import Application.ImageProcessor;
import Application.UnionFind;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

// The UnionFindManager class is responsible for performing a union-find operation on an image.
public class UnionFindManager {
    // The ImageProcessor instance used for various image processing tasks.
    private final ImageProcessor imageProcessor;

    // Constructor that initializes the ImageProcessor instance.
    public UnionFindManager(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    // This method performs a union-find operation on the given image.
    public void unionFind(UnionFind unionFind, Image image) {
        // Set up the image processor with the given image.
        imageProcessor.setup(image);

        // Iterate over each pixel in the image.
        for (int y = 0; y < imageProcessor.getHeight(); y++) {
            for (int x = 0; x < imageProcessor.getWidth(); x++) {
                // Process the union-find operation for the current pixel.
                processUnionFind(unionFind, x, y);
            }
        }
    }

    // This method processes the union-find operation for a single pixel at the given coordinates.
    public void processUnionFind(UnionFind unionFind, int x, int y) {
        // Get the color of the current pixel.
        Color color = imageProcessor.getPixelReader().getColor(x, y);

        // If the color of the pixel is white, perform the union-find operation.
        if (color.equals(Color.WHITE)) {
            // Map the pixel at (y, x) to an integer.
            int p = y * imageProcessor.getWidth() + x;

            // Perform the union-find operation in all four directions.
            unifyIfWhite(unionFind, p, x, y, 1, 0);  // right
            unifyIfWhite(unionFind, p, x, y, 0, 1);  // below
            unifyIfWhite(unionFind, p, x, y, -1, 0); // left
            unifyIfWhite(unionFind, p, x, y, 0, -1); // above
        }
    }

    // This method performs the union-find operation if the pixel at the given coordinates is white.
    public void unifyIfWhite(UnionFind unionFind, int p, int x, int y, int dx, int dy) {
        // Calculate the new coordinates. Used to check the pixel around the current pixel.
        int nx = x + dx; //New coordinate using current position and direction.
        int ny = y + dy;

        // If the new coordinates are within the image bounds and the pixel at the new coordinates is white, perform the union operation.
        if (nx >= 0 && nx < imageProcessor.getWidth() && ny >= 0 && ny < imageProcessor.getHeight() && imageProcessor.getPixelReader().getColor(nx, ny).equals(Color.WHITE)) {
            // Map the pixel at (ny, nx) to an integer.
            int q = ny * imageProcessor.getWidth() + nx; //Look at the y as rows and width/x as words in a row

            // Perform the union operation.
            unionFind.unionBySize(p, q); //People beside us are the same - join
        }
    }
}
