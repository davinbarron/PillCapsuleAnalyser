package benchmark;

import Application.ImageProcessor;
import Manager.BoundaryManager;
import javafx.scene.image.WritableImage;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class BoundaryManagerBenchmark {

    private BoundaryManager boundaryManager;
    private WritableImage image;

    @Setup(Level.Trial)
    public void setup() {
        ImageProcessor imageProcessor = new ImageProcessor();
        image = new WritableImage(400, 400);
        imageProcessor.setup(image);
        boundaryManager = new BoundaryManager(imageProcessor);
    }

    @Benchmark
    public void benchmarkSetBoundary() {
        boundaryManager.setBoundary(image);
    }
}

