package Application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Map;

// The Controller class is responsible for handling user interactions with the GUI.
public class Controller {

    // GUI components
    @FXML
    private ImageView imageView;  // The ImageView to display the original image
    @FXML
    private ImageView currentImageView;  // The ImageView to display the current image
    @FXML
    private ImageView bwImageView;  // The ImageView to display the black and white image
    private ImageView originalBWImageView;
    @FXML
    private TabPane tabPane;  // The TabPane to hold the tabs for different images
    @FXML
    private TextField pillNameField;  // The TextField to input the name of the pill
    @FXML
    private TextField colorThresholdField;  // The TextField to input the color threshold
    @FXML
    private TextField minSizeField;  // The TextField to input the minimum size of the pill
    @FXML
    private TextField maxSizeField;  // The TextField to input the maximum size of the pill
    @FXML
    private Slider hueAdjustmentFactorSlider;  // The Slider to adjust the hue of the image
    @FXML
    private Slider saturationFactorSlider;  // The Slider to adjust the saturation of the image
    @FXML
    private Slider brightnessFactorSlider;  // The Slider to adjust the brightness of the image
    @FXML
    private TextField rescaleWidthField;  // The TextField to input the new width for rescaling the image
    @FXML
    private TextField rescaleHeightField;  // The TextField to input the new height for rescaling the image
    @FXML
    private TextField pillNameInput;  // The TextField to input the name of the pill to be highlighted
    @FXML
    private ListView<String> listView;  // The ListView to display the list of pill selections
    @FXML
    private Button resetButton;  // The Button to reset the image scale
    @FXML
    CheckBox checkBox = new CheckBox("Show Numbers");  // The CheckBox to toggle the visibility of numbers
    private double originalWidth;  // The original width of the image
    private double originalHeight;  // The original height of the image

    // The ImageProcessor to process the image
    private final ImageProcessor imageProcessor = new ImageProcessor();
    // The PrintToConsole to print results to the console
    private final PrintToConsole printToConsole = new PrintToConsole(this);

    // The initialize method to set up the event listeners
    public void initialize() {
        // Add an event listener to the CheckBox
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> toggleNumberVisibility(newValue));

        // Add a listener to the hue adjustment slider
        hueAdjustmentFactorSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateImage());

        // Add a listener to the saturation adjustment slider
        saturationFactorSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateImage());

        // Add a listener to the brightness adjustment slider
        brightnessFactorSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateImage());
    }

    // The Exit method to exit the application
    @FXML
    public void Exit() {
        Platform.exit();
    }

    //----------------
    // File Opener
    //----------------

    // The openImage method to open an image file
    @FXML
    public void openImage() {
        File file = getFileFromChooser();
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setOnMouseClicked(this::addSelection);

            // Store the original dimensions
            originalWidth = image.getWidth();
            originalHeight = image.getHeight();

            // Enable the reset button
            resetButton.setDisable(false);
        }
    }

    // The getFileFromChooser method to get a file from the FileChooser
    private File getFileFromChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

    //----------------
    // Scaling
    //----------------

    // The rescaleImage method to rescale the image
    @FXML
    public void rescaleImage() {
        // Get the original image from the imageView
        Image originalImage = imageView.getImage();

        // Get the new dimensions from the user
        double newWidth = Double.parseDouble(rescaleWidthField.getText());
        double newHeight = Double.parseDouble(rescaleHeightField.getText());

        // Rescale the image and update the imageView
        imageProcessor.rescale(imageView, originalImage, newWidth, newHeight);
    }

    // The resetImageScale method to reset the image scale
    @FXML
    public void resetImageScale() {
        // Get the original image from the imageView
        Image originalImage = imageView.getImage();

        // Reset the image scale to the original dimensions
        imageProcessor.rescale(imageView, originalImage, originalWidth, originalHeight);
    }

    //----------------
    // Selections
    //----------------

    // The addSelection method to add a selection to the image
    public void addSelection(MouseEvent event) {
        String colorThreshold = colorThresholdField.getText();
        String pillName = pillNameField.getText();
        int minSize = Integer.parseInt(minSizeField.getText());
        int maxSize = Integer.parseInt(maxSizeField.getText());
        imageProcessor.addSelection(event, imageView, colorThreshold, pillName, minSize, maxSize);

        // Update the ListView
        listView.getItems().add("Pill Name: " + pillName + ", Color Threshold: " + colorThreshold + ", Size Range: " + minSize + "-" + maxSize);
    }

    // The undoLastSelection method to undo the last selection
    @FXML
    public void undoLastSelection() {
        if (!imageProcessor.getPillSelections().isEmpty()) {
            // Remove the last selection from the imageProcessor
            imageProcessor.undoLastSelection();

            // Remove the last item from the ListView
            listView.getItems().remove(listView.getItems().size() - 1);
        }
    }

    // The resetSelections method to reset all selections
    @FXML
    public void resetSelections() {
        // Clear all selections from the imageProcessor
        imageProcessor.resetSelections();

        // Clear all items from the ListView
        listView.getItems().clear();
    }

    //----------------
    // Toggle
    //----------------

    // The toggleNumberVisibility method to toggle the visibility of numbers
    private void toggleNumberVisibility(Boolean showNumbers) {
        // If the CheckBox is selected, show the numbers
        if (showNumbers) {
            for (Text number : imageProcessor.getNumberTexts()) {
                number.setVisible(true);
            }
        }
        // If the CheckBox is not selected, hide the numbers
        else {
            for (Text number : imageProcessor.getNumberTexts()) {
                number.setVisible(false);
            }
        }
    }

    //----------------
    // Convert
    //----------------

    public void onConvertButtonClick() {
        adjustImage();
        createNewTab("Converted Image", bwImageView);
        originalBWImageView = copyImageView(bwImageView);
    }

    private void adjustImage() {
        double hueAdjustment = hueAdjustmentFactorSlider.getValue();
        double saturationFactor = saturationFactorSlider.getValue();
        double brightnessFactor = brightnessFactorSlider.getValue();
        bwImageView = imageProcessor.convertToBlackAndWhite(imageView, hueAdjustment, saturationFactor, brightnessFactor);
    }

    private ImageView copyImageView(ImageView imageView) {
        return new ImageView(imageView.getImage());
    }

    private void updateImage() {
        adjustImage();
        currentImageView.setImage(bwImageView.getImage());
    }

    public void refineImage() {
        if (originalBWImageView != null) {
            ImageView refinedImageView = imageProcessor.refineBlackAndWhiteImage(originalBWImageView);
            updateImageViews(refinedImageView);
        } else {
            System.out.println("Please convert the image to black and white first.");
        }
    }

    private void updateImageViews(ImageView imageView) {
        bwImageView.setImage(imageView.getImage());
        currentImageView.setImage(imageView.getImage());
    }

    //----------------
    // Rectangles
    //----------------

    // The superimposeRectangles method to superimpose rectangles on the image
    public void superimposeRectangles() {
        Image originalImage = getOriginalImage();
        Image bwImage = getBWImage();

        UnionFind unionFind = imageProcessor.initializeDisjointSets(bwImage);
        imageProcessor.unionFind(unionFind, bwImage);

        boolean[][] boundary = imageProcessor.setBoundary(bwImage);
        ImageView newImageView = imageProcessor.createNewImageView(originalImage, bwImage.getWidth(), bwImage.getHeight());
        StackPane stackPane = createStackPane(newImageView);

        Map<Integer, int[]> disjointSetBounds = imageProcessor.getDisjointSetBounds(bwImage, unionFind, boundary);
        Map<Integer, Integer> disjointSetSizes = imageProcessor.getDisjointSetSizes(bwImage, unionFind, boundary);

        imageProcessor.createRectangles(stackPane, newImageView, originalImage, disjointSetBounds, disjointSetSizes);
        createNewTabWithStackPane("Image with Rectangles", stackPane);

        //set the visibility of the numbers right after the rectangles are superimposed
        toggleNumberVisibility(checkBox.isSelected());
    }

    private StackPane createStackPane(ImageView newImageView) {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(newImageView);
        return stackPane;
    }

    private void createNewTabWithStackPane(String tabName, StackPane content) {
        Tab newTab = new Tab(tabName, content);
        tabPane.getTabs().add(newTab);
    }

    //----------------
    // Highlight Pill
    //----------------

    // The highlightPill method to highlight a pill in the image
    public void highlightPill() {
        String pillName = pillNameInput.getText();
        highlightPill(pillName);
    }

    private void highlightPill(String pillName) {
        // Get the StackPane from the current tab
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        StackPane stackPane = (StackPane) currentTab.getContent();

        int counter = 1;
        // Iterate over the children of the StackPane
        for (Node node : stackPane.getChildren()) {
            // Check if the node is a Text node
            if (node instanceof Text textNode) {
                // Get the name of the pill from the text node
                String nodeName = textNode.getText().split(": ")[1];
                if (pillName.isEmpty() || nodeName.equals(pillName)) {
                    // Update the text of the Text node
                    textNode.setText(counter + ": " + nodeName);
                    textNode.setVisible(true);
                    counter++;
                } else {
                    textNode.setVisible(false);
                }
            }
        }
    }

    //-------------------------
    // Coloured Disjoint Sets
    //-------------------------

    public void displayColoredImage() {
        Image bwImage = getBWImage();
        Image coloredImage = imageProcessor.colorDisjointSets(bwImage);
        ImageView imageView = new ImageView(coloredImage);
        createNewTab("Colored Image", imageView);
    }

    public void randomlyColoredImage() {
        Image bwImage = getBWImage();
        Image coloredImage = imageProcessor.colorDisjointSetsRandomly(bwImage);
        ImageView imageView = new ImageView(coloredImage);
        createNewTab("Randomly Colored Image", imageView);
    }

    private <T extends Node> void createNewTab(String tabName, T content) {
        AnchorPane anchorPane = new AnchorPane(content);
        Tab newTab = new Tab(tabName, anchorPane);
        tabPane.getTabs().add(newTab);

        if (content instanceof ImageView) {
            currentImageView = (ImageView) content;
        }
    }

    //----------------
    // Getters
    //----------------

    public Image getOriginalImage() {
        return imageView.getImage();
    }

    public Image getBWImage() {
        return bwImageView.getImage();
    }

    public ImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    public ImageView getCurrentImageView() {
        return currentImageView;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    //------------------
    // Print Methods
    //------------------

    public void printDisjointSets() {
        printToConsole.printDisjointSets();
    }

    public void printUnionFindResults() {
        printToConsole.printUnionFindResults();
    }

    public void printBoundarySets() {
        printToConsole.printBoundarySets();
    }

    public void printSizesOfDisjointSetsInRectangles() {
        printToConsole.printSizesOfDisjointSetsInRectangles();
    }

    public void printRectangleNames() {
        printToConsole.printRectangleNames();
    }

    public void printRectangleNamesAndSizes() {
        printToConsole.printRectangleNamesAndSizes();
    }
}