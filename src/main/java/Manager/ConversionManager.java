package Manager;

import Application.ImageProcessor;
import Application.PillSelection;
import Application.UnionFind;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// The ConversionManager class is responsible for converting and refining images.
public class ConversionManager {
    // An instance of ImageProcessor to process the image.
    private final ImageProcessor imageProcessor;
    // A 2D array to store the PillSelection for each pixel.
    PillSelection[][] pillSelectionArray;
    // A writable image that can be modified.
    private WritableImage writableImage;
    // Constants for hue and value clamping.
    private static final int MAX_HUE = 360;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 1;

    // Constructor that initializes the ImageProcessor instance.
    public ConversionManager(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    // Method to convert an image to black and white.
    public ImageView convertToBlackAndWhite(ImageView originalImageView, double hueAdjustment, double saturationFactor, double brightnessFactor) {
        // Adjust the saturation and brightness of the image.
        Image adjustedImage = adjustSaturationAndBrightness(originalImageView.getImage(), hueAdjustment, saturationFactor, brightnessFactor);
        // Set the pixel reader for the image processor.
        imageProcessor.setPixelReader(adjustedImage.getPixelReader());
        // Initialize the writable image.
        writableImage = new WritableImage((int) adjustedImage.getWidth(), (int) adjustedImage.getHeight());

        // Process the image.
        processImage(adjustedImage);

        // Create a new ImageView with the processed image.
        return imageProcessor.createNewImageView(writableImage, originalImageView.getFitWidth(), originalImageView.getFitHeight());
    }

    // Method to initialize the PillSelection array.
    private void initializePillSelectionArray(Image image) {
        pillSelectionArray = new PillSelection[(int) image.getHeight()][(int) image.getWidth()];
    }

    //--------------------
    // Process
    //--------------------

    // Method to process a pixel.
    private void processPixel(int y, int x) {
        // Get the color of the pixel.
        Color color = imageProcessor.getPixelReader().getColor(x, y);
        // Get the closest PillSelection for the color.
        PillSelection closestSelection = getClosestSelection(color);
        // Set the color of the pixel in the writable image.
        writableImage.getPixelWriter().setColor(x, y, closestSelection != null ? Color.WHITE : Color.BLACK);
        // If there is the closest selection, set the PillSelection for the pixel.
        if (closestSelection != null) {
            pillSelectionArray[y][x] = closestSelection;
        }
    }

    // Method to process each pixel in the image.
    private void processPixels(Image image) {
        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                processPixel(x, y);
            }
        }
    }

    // Method to process an image.
    private void processImage(Image image) {
        // Initialize the PillSelection array.
        initializePillSelectionArray(image);
        // Process each pixel in the image.
        processPixels(image);
    }

    //--------------------
    // Getters
    //--------------------

    // Method to get the closest PillSelection for a color.
    private PillSelection getClosestSelection(Color color) {
        // Find the PillSelection with the minimum color distance that is below the color threshold.
        return imageProcessor.getPillSelectionManager().getPillSelections().stream() //Sequence of elements
                .min(Comparator.comparingDouble(selection -> getColorDistance(color, selection.getColor()))) //Find the minimum element
                .filter(selection -> getColorDistance(color, selection.getColor()) <= selection.getColorThreshold()) //Filter elements that meet condition
                .orElse(null); //Return if found otherwise null
    }

    // Method to calculate the color distance between two colors.
    private double getColorDistance(Color color1, Color color2) {
        // The color distance is the sum of the absolute differences of the red, green, and blue components.
        return Math.abs(color1.getRed() - color2.getRed()) +
                Math.abs(color1.getGreen() - color2.getGreen()) +
                Math.abs(color1.getBlue() - color2.getBlue());
    }

    //--------------------
    // Adjust
    //--------------------

    // Method to adjust a pixel.
    private void adjustPixel(PixelReader pixelReader, PixelWriter pixelWriter, int x, int y, double hueAdjustment, double saturationFactor, double brightnessFactor) {
        // Get the color of the pixel.
        Color color = pixelReader.getColor(x, y);
        // Calculate the new hue.
        double newHue = (color.getHue() + hueAdjustment) % MAX_HUE;  // Ensure the hue stays within the range [0, 360)
        // Create the adjusted color.
        Color adjustedColor = Color.hsb(
                newHue,
                clamp(color.getSaturation() * saturationFactor),
                clamp(color.getBrightness() * brightnessFactor)
        );
        // Set the color of the pixel in the writable image.
        pixelWriter.setColor(x, y, adjustedColor);
    }

    // Method to adjust the pixels in an image.
    private void adjustPixels(double hueAdjustment, double saturationFactor, double brightnessFactor) {
        for (int y = 0; y < writableImage.getHeight(); y++) {
            for (int x = 0; x < writableImage.getWidth(); x++) {
                adjustPixel(imageProcessor.getPixelReader(), writableImage.getPixelWriter(), x, y, hueAdjustment, saturationFactor, brightnessFactor);
            }
        }
    }

    // Method to adjust the saturation and brightness of an image.
    public Image adjustSaturationAndBrightness(Image image, double hueAdjustment, double saturationFactor, double brightnessFactor) {
        // Set the pixel reader for the image processor.
        imageProcessor.setPixelReader(image.getPixelReader());
        // Initialize the writable image.
        writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        // Adjust the pixels in the image.
        adjustPixels(hueAdjustment, saturationFactor, brightnessFactor);
        // Return the adjusted image.
        return writableImage;
    }

    //--------------------
    // Refine
    //--------------------

    // Method to refine a black and white image.
    public ImageView refineBlackAndWhiteImage(ImageView originalImageView) {
        // Get the image from the ImageView.
        Image adjustedImage = originalImageView.getImage();
        // Perform union-find on the image.
        UnionFind unionFind = performUnionFind(adjustedImage);
        // Set the boundary of the image.
        boolean[][] boundary = imageProcessor.setBoundary(adjustedImage);
        // Filter the disjoint sets in the image.
        Set<Integer> keptRoots = filterDisjointSets(adjustedImage, unionFind, boundary);
        // Update the image with the kept roots.
        WritableImage writableImage = updateImage(adjustedImage, unionFind, keptRoots);
        // Create a new ImageView with the updated image.
        return createNewImageView(writableImage, originalImageView.getFitWidth(), originalImageView.getFitHeight());
    }

    // Method to perform union-find on an image.
    private UnionFind performUnionFind(Image image) {
        // Initialize the disjoint sets for the image.
        UnionFind unionFind = imageProcessor.initializeDisjointSets(image);
        // Perform union-find on the disjoint sets.
        imageProcessor.unionFind(unionFind, image);
        // Return the union-find data structure.
        return unionFind;
    }

    // Method to update an image with the kept roots.
    private WritableImage updateImage(Image image, UnionFind unionFind, Set<Integer> keptRoots) {
        // Create a new writable image.
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        // Update each pixel in the image.
        for (int x = 0; x < image.getHeight(); x++) {
            for (int y = 0; y < image.getWidth(); y++) {
                // Find the root of the disjoint set for the pixel.
                int root = unionFind.find(x * (int) image.getWidth() + y);
                // Set the color of the pixel in the writable image.
                writableImage.getPixelWriter().setColor(y, x, keptRoots.contains(root) ? Color.WHITE : Color.BLACK);
            }
        }
        // Return the updated image.
        return writableImage;
    }

    //--------------------
    // Filter
    //--------------------

    // Method to filter the disjoint sets in an image.
    private Set<Integer> filterDisjointSets(Image image, UnionFind unionFind, boolean[][] boundary) {
        // Get the sizes of the disjoint sets in the image.
        Map<Integer, Integer> disjointSetSizes = imageProcessor.getDisjointSetSizes(image, unionFind, boundary);
        // Filter the disjoint sets by size and return the roots of the kept sets.
        return filterBySize(disjointSetSizes);
    }

    // Method to filter a map of disjoint set sizes by size.
    private Set<Integer> filterBySize(Map<Integer, Integer> disjointSetSizes) {
        // Filter the entries in the map by size and return the keys of the kept entries.
        return disjointSetSizes.entrySet().stream()
                .filter(this::testSize)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    //--------------------
    // Other
    //--------------------

    // Method to create a new ImageView with a given image.
    public ImageView createNewImageView(Image image, double fitWidth, double fitHeight) {
        // Create a new ImageView with the image.
        ImageView imageView = new ImageView(image);
        // Set the fit width and height of the ImageView.
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        // Preserve the aspect ratio of the image.
        imageView.setPreserveRatio(true);
        // Return the new ImageView.
        return imageView;
    }

    // Method to test if a disjoint set is within the size range of any PillSelection.
    boolean testSize(Map.Entry<Integer, Integer> entry) {
        // Get the size of the disjoint set.
        int size = entry.getValue();
        // Test if the size is within the size range of any PillSelection.
        return imageProcessor.getPillSelectionManager().getPillSelections().stream().anyMatch(selection -> size >= selection.getMinSize() && size <= selection.getMaxSize());
    }

    // Method to clamp a value between MIN_VALUE and MAX_VALUE.
    private double clamp(double value) {
        return Math.max(MIN_VALUE, Math.min(MAX_VALUE, value));
    }
}