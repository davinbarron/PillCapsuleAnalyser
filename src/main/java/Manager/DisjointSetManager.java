package Manager;

import Application.ImageProcessor;
import Application.PillSelection;
import Application.UnionFind;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// The DisjointSetManager class is responsible for managing disjoint sets in an image.
public class DisjointSetManager {
    // An instance of ImageProcessor to process the image.
    private final ImageProcessor imageProcessor;

    // Constructor that initializes the ImageProcessor instance.
    public DisjointSetManager(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    // Method to initialize disjoint sets for an image.
    public UnionFind initializeDisjointSets(Image image) {
        // Set the width and height of the image processor.
        imageProcessor.setWidth((int) image.getWidth());
        imageProcessor.setHeight((int) image.getHeight());
        // Calculate the total number of pixels in the image.
        int size = imageProcessor.getWidth() * imageProcessor.getHeight();
        // Return a new UnionFind data structure with the calculated size.
        return new UnionFind(size);
    }

    //--------------------
    // Getters
    //--------------------
    // Method to get the bounds of each disjoint set in an image.
    public Map<Integer, int[]> getDisjointSetBounds(Image bwImage, UnionFind unionFind, boolean[][] boundary) {
        // Initialize a map to store the bounds for each root.
        Map<Integer, int[]> disjointSetBounds = new HashMap<>();
        // Iterate over the boundary and update the bounds for each root.
        iterateOverBoundary(boundary, bwImage, unionFind, (setRoot, x, y) -> {
            // Initialize the bounds for the root if they don't exist.
            disjointSetBounds.putIfAbsent(setRoot, new int[]{x, x, y, y});  // minX, maxX, minY, maxY
            // Update the bounds for the root.
            int[] bounds = disjointSetBounds.get(setRoot);
            bounds[0] = Math.min(bounds[0], x);
            bounds[1] = Math.max(bounds[1], x);
            bounds[2] = Math.min(bounds[2], y);
            bounds[3] = Math.max(bounds[3], y);
        });
        // Return the map of bounds.
        return disjointSetBounds;
    }

    // Method to get the sizes of each disjoint set in an image.
    public Map<Integer, Integer> getDisjointSetSizes(Image bwImage, UnionFind unionFind, boolean[][] boundary) {
        // Initialize a map to store the size for each root.
        Map<Integer, Integer> disjointSetSizes = new HashMap<>();
        // Iterate over the boundary and update the size for each root.
        iterateOverBoundary(boundary, bwImage, unionFind, (setRoot, x, y) -> disjointSetSizes.put(setRoot, disjointSetSizes.getOrDefault(setRoot, 0) + 1));
        // Return the map of sizes.
        return disjointSetSizes;
    }

    // Method to get the size of a disjoint set in a rectangle.
    public int getSizeOfDisjointSetInRectangle(Rectangle rect) {
        // Return the size from the map.
        return imageProcessor.getRectangleSizes().get(rect);
    }

    //--------------------
    // Colour
    //--------------------

    // Method to generate a random color.
    private Color generateRandomColor() {
        Random rand = new Random();
        return Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
    }

   // Method to color the disjoint sets in an image.
    public Image colorDisjointSets(Image bwImage) {
        // Create a copy of the black and white image.
        WritableImage coloredImage = new WritableImage(bwImage.getPixelReader(), (int) bwImage.getWidth(), (int) bwImage.getHeight());

        // Iterate over the pixels in the image.
        for (int y = 0; y < coloredImage.getHeight(); y++) {
            for (int x = 0; x < coloredImage.getWidth(); x++) {
                // If the pixel is part of a disjoint set, color it with the color of the closest selection.
                PillSelection closestSelection = imageProcessor.getConversionManager().pillSelectionArray[y][x];
                if (closestSelection != null) {
                    coloredImage.getPixelWriter().setColor(x, y, closestSelection.getColor());
                }
            }
        }

        // Return the colored image.
        return coloredImage;
    }

    public Image colorDisjointSetsRandomly(Image bwImage) {
        // Create a copy of the black and white image
        WritableImage coloredImage = new WritableImage(bwImage.getPixelReader(), (int) bwImage.getWidth(), (int) bwImage.getHeight());

        // Initialize the disjoint sets
        UnionFind unionFind = initializeDisjointSets(bwImage);
        imageProcessor.unionFind(unionFind, bwImage);

        // Create a map to store the color for each root
        Map<Integer, Color> rootColors = new HashMap<>();

        // Iterate over the pixels in the image
        for (int y = 0; y < coloredImage.getHeight(); y++) {
            for (int x = 0; x < coloredImage.getWidth(); x++) {
                handlePixel(x, y, unionFind, rootColors, coloredImage);
            }
        }

        return coloredImage;
    }

    //--------------------
    // Other
    //--------------------

    // Method to handle a pixel in the image.
    private void handlePixel(int x, int y, UnionFind unionFind, Map<Integer, Color> rootColors, WritableImage coloredImage) {
        // If the pixel is part of a disjoint set, color it with the color of its root.
        int root = unionFind.find(y * (int) coloredImage.getWidth() + x);
        rootColors.putIfAbsent(root, generateRandomColor());
        Color pixelColor = imageProcessor.getPixelReader().getColor(x, y);
        coloredImage.getPixelWriter().setColor(x, y, pixelColor.equals(Color.BLACK) ? Color.BLACK : rootColors.get(root));
    }

    // Method to iterate over the boundary of an image and apply an operation.
    private void iterateOverBoundary(boolean[][] boundary, Image bwImage, UnionFind unionFind, DisjointSetOperation operation) {
        // Iterate over each pixel in the boundary.
        for (int y = 0; y < boundary.length; y++) {
            for (int x = 0; x < boundary[y].length; x++) {
                // If the pixel is part of the boundary, apply the operation.
                if (boundary[y][x]) {
                    int setRoot = unionFind.find((int) (y * bwImage.getWidth() + x));
                    operation.apply(setRoot, x, y);
                }
            }
        }
    }

    // Interface for a disjoint set operation.
    private interface DisjointSetOperation {
        void apply(int setRoot, int x, int y);
    }
}