package Application;

import Manager.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The ImageProcessor class is responsible for various image processing tasks.
public class ImageProcessor {
    // Instance variables for various managers used in image processing.
    private final PillCapsuleManager pillCapsuleManager = new PillCapsuleManager();
    private final List<Text> numberTexts = new ArrayList<>();
    private final Map<Rectangle, Integer> rectangleSizes = new HashMap<>();
    private final PillSelectionManager pillSelectionManager = new PillSelectionManager(this);
    private final ConversionManager conversionManager = new ConversionManager(this);
    private final DisjointSetManager disjointSetManager = new DisjointSetManager(this);
    private final UnionFindManager unionFindManager = new UnionFindManager(this);
    private final BoundaryManager boundaryManager = new BoundaryManager(this);
    private final RectangleManager rectangleManager = new RectangleManager(this);
    private PixelReader pixelReader;
    private int width, height;

    //------------------------
    // Pill Selections
    //------------------------

    // Method to add a selection of a pill or capsule in the image.
    public void addSelection(MouseEvent event, ImageView imageView, String colorThresholdField, String pillName, int minSize, int maxSize) {
        pillSelectionManager.addSelection(event, imageView, colorThresholdField, pillName, minSize, maxSize);
    }

    // Method to reset all selections.
    public void resetSelections() {
        pillSelectionManager.resetSelections();
    }

    // Method to undo the last selection.
    public void undoLastSelection() {
        pillSelectionManager.undoLastSelection();
    }

    // Method to get all pill selections.
    public List<PillSelection> getPillSelections() {
        return pillSelectionManager.getPillSelections();
    }

    // Getter for the PillCapsuleManager.
    public PillCapsuleManager getPillCapsuleManager() {
        return pillCapsuleManager;
    }

    // Getter for the PillSelectionManager.
    public PillSelectionManager getPillSelectionManager() {
        return pillSelectionManager;
    }

    //------------------------
    // Conversion Processes
    //------------------------

    // Method to convert an image to black and white.
    public ImageView convertToBlackAndWhite(ImageView originalImageView, double hueAdjustment, double saturationFactor, double brightnessFactor) {
        return conversionManager.convertToBlackAndWhite(originalImageView, hueAdjustment, saturationFactor, brightnessFactor);
    }

    // Method to refine a black and white image.
    public ImageView refineBlackAndWhiteImage(ImageView originalImageView) {
        return conversionManager.refineBlackAndWhiteImage(originalImageView);
    }

    // Getter for the ConversionManager.
    public ConversionManager getConversionManager() {
        return conversionManager;
    }

    //------------------------
    // Disjoint Sets
    //------------------------

    // Method to initialize disjoint sets for an image.
    public UnionFind initializeDisjointSets(Image image) {
        return disjointSetManager.initializeDisjointSets(image);
    }

    // Method to get the bounds of disjoint sets in an image.
    public Map<Integer, int[]> getDisjointSetBounds(Image bwImage, UnionFind unionFind, boolean[][] boundary) {
        return disjointSetManager.getDisjointSetBounds(bwImage, unionFind, boundary);
    }

    // Method to get the sizes of disjoint sets in an image.
    public Map<Integer, Integer> getDisjointSetSizes(Image bwImage, UnionFind unionFind, boolean[][] boundary) {
        return disjointSetManager.getDisjointSetSizes(bwImage, unionFind, boundary);
    }

    // Method to get the size of a disjoint set in a rectangle.
    public int getSizeOfDisjointSetInRectangle(Rectangle rect) {
        return disjointSetManager.getSizeOfDisjointSetInRectangle(rect);
    }

    // Method to color disjoint sets in an image.
    public Image colorDisjointSets(Image bwImage) {
        return disjointSetManager.colorDisjointSets(bwImage);
    }

    // Method to color disjoint sets in an image randomly.
    public Image colorDisjointSetsRandomly(Image bwImage) {
        return disjointSetManager.colorDisjointSetsRandomly(bwImage);
    }

    //------------------------
    // Union-Find
    //------------------------

    // Method to perform a union-find operation on an image.
    public void unionFind(UnionFind unionFind, Image image) {
        unionFindManager.unionFind(unionFind, image);
    }

    //------------------------
    // Boundary Processes
    //------------------------

    // Method to set the boundary of an image.
    public boolean[][] setBoundary(Image image) {
        return boundaryManager.setBoundary(image);
    }

    //------------------------
    // Rectangle
    //------------------------

    // Method to create rectangles on a StackPane based on disjoint set bounds and sizes.
    public void createRectangles(StackPane stackPane, ImageView newImageView, Image originalImage, Map<Integer, int[]> disjointSetBounds, Map<Integer, Integer> disjointSetSizes) {
        rectangleManager.createRectangles(stackPane, newImageView, originalImage, disjointSetBounds, disjointSetSizes);
    }

    // Method to get the number texts associated with rectangles.
    public List<Text> getNumberTexts() {
        return numberTexts;
    }

    //------------------------
    // Image Rescaling
    //------------------------

    // Method to rescale an image to the specified width and height.
    public void rescale(ImageView imageView, Image image, double width, double height) {
        // Create a new Image with the specified width and height.
        Image rescaledImage = new Image(image.getUrl(), width, height, true, true);

        // Set the rescaled image to the imageView.
        imageView.setImage(rescaledImage);

        // Adjust the fitHeight and fitWidth properties to match the new dimensions.
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }

    //------------------------
    // Helper Methods
    //------------------------

    // Method to set up the ImageProcessor with the given image.
    public void setup(Image image) {
        setWidth((int) image.getWidth());
        setHeight((int) image.getHeight());
        setPixelReader(image.getPixelReader());
    }

    // Method to create a new ImageView with the given image and dimensions.
    public ImageView createNewImageView(Image image, double fitWidth, double fitHeight) {
        return conversionManager.createNewImageView(image, fitWidth, fitHeight);
    }

    // Getter for the PixelReader.
    public PixelReader getPixelReader() {
        return pixelReader;
    }

    // Setter for the PixelReader.
    public void setPixelReader(PixelReader pixelReader) {
        this.pixelReader = pixelReader;
    }

    // Setter for the width.
    public void setWidth(int width){
        this.width = width;
    }

    // Getter for the width.
    public int getWidth(){
        return width;
    }

    // Setter for the height.
    public void setHeight(int height){
        this.height = height;
    }

    // Getter for the height.
    public int getHeight(){
        return height;
    }

    // Getter for the sizes of the rectangles.
    public Map<Rectangle, Integer> getRectangleSizes() {
        return rectangleSizes;
    }
}