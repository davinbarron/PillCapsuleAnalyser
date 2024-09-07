package Application;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PrintToConsole {
    private final Controller controller;

    public PrintToConsole(Controller controller) {
        this.controller = controller;
    }

    public void printDisjointSets() {
        // Get the image from the ImageView
        Image image = controller.getCurrentImageView().getImage();

        // Print the dimensions of the image to verify it's the correct one
        System.out.println("Image dimensions: " + image.getWidth() + "x" + image.getHeight());

        // Initialize the disjoint sets
        UnionFind unionFind = controller.getImageProcessor().initializeDisjointSets(image);

        // Print the disjoint sets
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int p = r * width + c;  // Map the pixel at (r, c) to an integer
                System.out.println("Set at (" + r + ", " + c + ") with root: " + unionFind.find(p));
            }
        }
    }

    public void printUnionFindResults() {
        // Get the image from the currentImageView
        Image image = controller.getCurrentImageView().getImage();

        // Initialize the disjoint sets
        UnionFind unionFind = controller.getImageProcessor().initializeDisjointSets(image);

        // Perform the union-find operation
        controller.getImageProcessor().unionFind(unionFind, image);  // Pass the image here

        // Print the number of disjoint sets left
        System.out.println("Number of disjoint sets: " + unionFind.components());
    }

    public void printBoundarySets() {
        // Get the image from the currentImageView
        Image image = controller.getCurrentImageView().getImage();

        // Print the dimensions of the image to verify it's the correct one
        System.out.println("Image dimensions: " + image.getWidth() + "x" + image.getHeight());

        // Initialize the disjoint sets
        UnionFind unionFind = controller.getImageProcessor().initializeDisjointSets(image);

        // Perform the union-find operation
        controller.getImageProcessor().unionFind(unionFind, image);

        // Get the boundary sets
        boolean[][] boundary = controller.getImageProcessor().setBoundary(image);

        // Print the boundary sets
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (boundary[r][c]) {
                    System.out.println("Boundary at (" + r + ", " + c + ")");
                }
            }
        }
    }

    public void printSizesOfDisjointSetsInRectangles() {
        // Get the StackPane from the current tab
        Tab currentTab = controller.getTabPane().getSelectionModel().getSelectedItem();
        StackPane stackPane = (StackPane) currentTab.getContent();

        // Iterate over the children of the StackPane
        for (Node node : stackPane.getChildren()) {
            // Check if the node is a Rectangle
            if (node instanceof Rectangle rect) {

                // Get the size of the disjoint set within the rectangle
                int size = controller.getImageProcessor().getSizeOfDisjointSetInRectangle(rect);

                // Print the size
                System.out.println("Size of disjoint set in rectangle: " + size);
            }
        }
    }

    public void printRectangleNames() {
        // Get the StackPane from the current tab
        Tab currentTab = controller.getTabPane().getSelectionModel().getSelectedItem();
        StackPane stackPane = (StackPane) currentTab.getContent();

        // Iterate over the children of the StackPane
        for (Node node : stackPane.getChildren()) {
            // Check if the node is a Text node
            if (node instanceof Text textNode) {
                // Print the text of the Text node
                System.out.println(textNode.getText());
            }
        }
    }

    public void printRectangleNamesAndSizes() {
        // Get the StackPane from the current tab
        Tab currentTab = controller.getTabPane().getSelectionModel().getSelectedItem();
        StackPane stackPane = (StackPane) currentTab.getContent();

        // Iterate over the children of the StackPane
        for (Node node : stackPane.getChildren()) {
            // Check if the node is a Rectangle
            if (node instanceof Rectangle rect) {

                // Get the size of the disjoint set within the rectangle
                int size = controller.getImageProcessor().getSizeOfDisjointSetInRectangle(rect);

                // Get the name of the rectangle
                String name = null;
                for (Node textNode : stackPane.getChildren()) {
                    if (textNode instanceof Text && textNode.getTranslateX() == rect.getTranslateX() && textNode.getTranslateY() == rect.getTranslateY()) {
                        name = ((Text) textNode).getText();
                        break;
                    }
                }

                // Print the name and size
                System.out.println("Rectangle " + name + ": size = " + size);
            }
        }
    }
}