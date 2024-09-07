package Application;

import javafx.scene.paint.Color;

/**
 * The PillSelection class represents a selection of a pill with specific characteristics.
 * Each pill has a name, color, color threshold, minimum size, and maximum size.
 */
public class PillSelection {
    private String name;  // The name of the pill
    private Color color;  // The color of the pill
    private double colorThreshold;  // The color threshold for the pill
    private int minSize;  // The minimum size of the pill
    private int maxSize;  // The maximum size of the pill

    /**
     * Constructs a new PillSelection with the given parameters.
     *
     * @param name The name of the pill.
     * @param color The color of the pill.
     * @param colorThreshold The color threshold for the pill.
     * @param minSize The minimum size of the pill.
     * @param maxSize The maximum size of the pill.
     */
    public PillSelection(String name, Color color, double colorThreshold, int minSize, int maxSize) {
        setName(name);
        setColor(color);
        setColorThreshold(colorThreshold);
        setMinSize(minSize);
        setMaxSize(maxSize);
    }

    // Getter and setter methods for each field

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getColorThreshold() {
        return colorThreshold;
    }

    public void setColorThreshold(double colorThreshold) {
        this.colorThreshold = colorThreshold;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Returns a string representation of the PillSelection.
     *
     * @return A string representation of the PillSelection.
     */
    @Override
    public String toString() {
        return "Name: " + name + ", Color: " + color.toString() + ", Color Threshold: " + colorThreshold + ", Min Size: " + minSize + ", Max Size: " + maxSize;
    }
}
