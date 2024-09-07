package benchmark;

import Application.ImageProcessor;
import Manager.ConversionManager;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class ConversionManagerBenchmark {

    private ConversionManager conversionManager;
    private ImageView originalImageView;

    @Setup(Level.Trial)
    public void setup() {
        ImageProcessor imageProcessor = new ImageProcessor();
        WritableImage image = new WritableImage(400, 400);
        originalImageView = new ImageView(image);
        imageProcessor.setup(image);
        conversionManager = new ConversionManager(imageProcessor);
    }

    @Benchmark
    public void benchmarkConvertToBlackAndWhite() {
        conversionManager.convertToBlackAndWhite(originalImageView, 0.5, 1.2, 0.8);
    }
}