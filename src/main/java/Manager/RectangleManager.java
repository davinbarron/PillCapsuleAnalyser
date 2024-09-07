package Manager;

import Application.ImageProcessor;
import Application.PillSelection;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.*;

public class RectangleManager {
    // ImageProcessor instance used for various image processing tasks
    private final ImageProcessor imageProcessor;

    // Constants used for calculations
    private static final double RECTANGLE_CENTER_FACTOR = 2.0;
    private static final int THRESHOLD_FACTOR = 5;

    // Constructor that initializes the ImageProcessor instance
    public RectangleManager(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    // Method to create rectangles on a StackPane based on disjoint set bounds and sizes
    public void createRectangles(StackPane stackPane, ImageView newImageView, Image originalImage, Map<Integer, int[]> disjointSetBounds, Map<Integer, Integer> disjointSetSizes) {
        // Calculate threshold based on disjoint set sizes
        int threshold = calculateThreshold(disjointSetSizes);

        // Sort entries by Y coordinate
        List<Map.Entry<Integer, int[]>> sortedEntries = sortEntriesByYCoordinate(disjointSetBounds);

        // Counter for the rectangles
        int rectangleCounter = 0;

        // Iterate over sorted entries and add rectangles and text nodes to StackPane
        for (Map.Entry<Integer, int[]> entry : sortedEntries) {
            if (disjointSetSizes.get(entry.getKey()) > threshold) {
                rectangleCounter++;
                addRectangleAndTextNodeToStackPane(stackPane, newImageView, originalImage, entry, rectangleCounter, disjointSetSizes);
            }
        }
    }

    // Method to create a Rectangle with specified bounds, width, and height
    private Rectangle createRectangle(int[] bounds, double rectWidth, double rectHeight, double originalWidth, double originalHeight) {
        // Create a new Rectangle
        Rectangle rect = new Rectangle((bounds[1] - bounds[0]) * rectWidth, (bounds[3] - bounds[2]) * rectHeight);

        // Set Rectangle properties
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLUE);
        rect.setTranslateX(((bounds[0] + bounds[1]) / RECTANGLE_CENTER_FACTOR - originalWidth / RECTANGLE_CENTER_FACTOR) * rectWidth);
        rect.setTranslateY(((bounds[2] + bounds[3]) / RECTANGLE_CENTER_FACTOR - originalHeight / RECTANGLE_CENTER_FACTOR) * rectHeight);

        return rect;
    }

    //------------------------
    // Calculations
    //------------------------
    // Method to calculate the width of a rectangle
    private double calculateRectangleWidth(ImageView newImageView, Image originalImage) {
        return newImageView.getFitWidth() / originalImage.getWidth();
    }

    // Method to calculate the height of a rectangle
    private double calculateRectangleHeight(ImageView newImageView, Image originalImage) {
        return newImageView.getFitHeight() / originalImage.getHeight();
    }

    // Method to calculate the threshold based on disjoint set sizes
    private int calculateThreshold(Map<Integer, Integer> disjointSetSizes) {
        int maxSize = Collections.max(disjointSetSizes.values());
        return maxSize / THRESHOLD_FACTOR;
    }

    // Method to sort entries by Y coordinate
    private List<Map.Entry<Integer, int[]>> sortEntriesByYCoordinate(Map<Integer, int[]> disjointSetBounds) {
        return disjointSetBounds.entrySet().stream() //Sequence of elements
                .sorted(Comparator.comparingInt(entry -> entry.getValue()[2])) //Sort the entries
                .toList(); //call to list
    }

    //------------------------
    // Adding
    //------------------------

    // Method to add a rectangle and a text node to a StackPane
    private void addRectangleAndTextNodeToStackPane(StackPane stackPane, ImageView newImageView, Image originalImage, Map.Entry<Integer, int[]> entry, int rectangleCounter, Map<Integer, Integer> disjointSetSizes) {
        // Create a new Rectangle and add it to the StackPane
        Rectangle rect = createRectangle(entry.getValue(), calculateRectangleWidth(newImageView, originalImage), calculateRectangleHeight(newImageView, originalImage), originalImage.getWidth(), originalImage.getHeight());
        stackPane.getChildren().add(rect);

        // Add the size of the rectangle to the ImageProcessor's rectangle sizes
        imageProcessor.getRectangleSizes().put(rect, disjointSetSizes.get(entry.getKey()));

        // Get the most common PillSelection within the bounds
        PillSelection mostCommonPillSelection = getMostCommonPillSelection(entry.getValue());
        String pillName = mostCommonPillSelection != null ? mostCommonPillSelection.getName() : "Unknown";

        // Create a new Text node and add it to the StackPane
        Text text = createTextNode(rectangleCounter, rect, pillName);
        stackPane.getChildren().add(text);

        // Add the Text node to the ImageProcessor's number texts
        getNumberTexts().add(text);

        // Handle tooltip for the Rectangle
        handleTooltip(rect, pillName, rectangleCounter);
    }

    //------------------------
    // Getters
    //------------------------

    // Method to get the number texts from the ImageProcessor
    public List<Text> getNumberTexts() {
        return imageProcessor.getNumberTexts();
    }

    // Method to get the most common PillSelection within the bounds
    private PillSelection getMostCommonPillSelection(int[] bounds) {
        // Create a map to count the occurrences of each PillSelection
        Map<PillSelection, Integer> pillSelectionCounts = new HashMap<>();

        // Iterate over the bounds and count the occurrences of each PillSelection
        for (int y = bounds[2]; y <= bounds[3]; y++) {
            for (int x = bounds[0]; x <= bounds[1]; x++) {
                PillSelection pillSelection = imageProcessor.getConversionManager().pillSelectionArray[y][x];
                if (pillSelection != null) {
                    pillSelectionCounts.put(pillSelection, pillSelectionCounts.getOrDefault(pillSelection, 0) + 1);
                }
            }
        }

        // Return the PillSelection with the maximum count
        return pillSelectionCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }

    //--------------------
    // Tooltip Creation
    //--------------------

    // Method to create a Text node with the given index, rectangle, and pill name
    private Text createTextNode(int index, Rectangle rect, String pillName) {
        // Create a new Text node
        Text text = new Text(index + ": " + pillName);

        // Set the position of the Text node
        text.setTranslateX(rect.getTranslateX());
        text.setTranslateY(rect.getTranslateY());

        return text;
    }

    // Method to handle tooltip for a Rectangle
    private void handleTooltip(Rectangle rect, String pillName, int rectangleCounter) {
        // Create a new Tooltip
        Tooltip tooltip = new Tooltip();

        // Set the mouse press event for the Rectangle to show the Tooltip
        rect.setOnMousePressed(event -> {
            tooltip.setText("Pill/Capsule Name: " + pillName + "\n" +
                    "Pill/Capsule Number: " + rectangleCounter + "\n" +
                    "Estimated Size (pixel units): " + imageProcessor.getRectangleSizes().get(rect));
            tooltip.show(rect, event.getScreenX(), event.getScreenY());
        });

        // Set the mouse release event for the Rectangle to hide the Tooltip
        rect.setOnMouseReleased(event -> tooltip.hide());
    }
}