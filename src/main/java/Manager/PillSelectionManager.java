package Manager;

import Application.ImageProcessor;
import Application.PillSelection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

// The PillSelectionManager class is responsible for managing pill selections in an image.
public class PillSelectionManager {
    // An instance of ImageProcessor to process the image.
    private final ImageProcessor imageProcessor;
    // Variables to store the ratio of the image dimensions to the ImageView dimensions.
    private double xRatio, yRatio;
    // Variables to store the coordinates of a mouse event.
    private int x, y;

    // Constructor that initializes the ImageProcessor instance.
    public PillSelectionManager(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    //------------------------
    // Selections
    //------------------------

    // Method to add a pill selection to the image.
    public void addSelection(MouseEvent event, ImageView imageView, String colorThresholdField, String pillName, int minSize, int maxSize) {
        // Get the image from the ImageView and set the pixel reader for the image processor.
        Image image = imageView.getImage();
        imageProcessor.setPixelReader(image.getPixelReader());

        // Calculate the ratios of the image dimensions to the ImageView dimensions.
        calculateRatios(image, imageView);
        // Calculate the coordinates of the mouse event.
        calculateCoordinates(event);

        // Get the color of the clicked pixel and the color threshold.
        Color clickedColor = imageProcessor.getPixelReader().getColor(x, y);
        double colorThreshold = Double.parseDouble(colorThresholdField);

        // Create a new PillSelection and add it to the list of pill selections.
        PillSelection pillSelection = new PillSelection(pillName, clickedColor, colorThreshold, minSize, maxSize);
        getPillSelections().add(pillSelection);
        imageProcessor.getPillCapsuleManager().addPillSelection(pillSelection);
    }

    // Method to reset the list of pill selections.
    public void resetSelections() {
        getPillSelections().clear();
    }

    // Method to undo the last pill selection.
    public void undoLastSelection() {
        if (!getPillSelections().isEmpty()) {
            getPillSelections().remove(getPillSelections().size() - 1);
        }
    }

    // Method to get the list of pill selections.
    public List<PillSelection> getPillSelections() {
        return imageProcessor.getPillCapsuleManager().getPillSelections();
    }

    //------------------------
    // Calculations
    //------------------------

    // Method to calculate the ratios of the image dimensions to the ImageView dimensions.
    private void calculateRatios(Image image, ImageView imageView) {
        xRatio = image.getWidth() / imageView.getBoundsInLocal().getWidth();
        yRatio = image.getHeight() / imageView.getBoundsInLocal().getHeight();
    }

    // Method to calculate the coordinates of a mouse event.
    private void calculateCoordinates(MouseEvent event) {
        this.x = (int) (event.getX() * this.xRatio);
        this.y = (int) (event.getY() * this.yRatio);
    }
}