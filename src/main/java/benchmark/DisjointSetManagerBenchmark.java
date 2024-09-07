package benchmark;

import Application.ImageProcessor;
import Manager.DisjointSetManager;
import javafx.scene.image.WritableImage;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class DisjointSetManagerBenchmark {

    private DisjointSetManager disjointSetManager;
    private WritableImage image;

    @Setup(Level.Trial)
    public void setup() {
        ImageProcessor imageProcessor = new ImageProcessor();
        image = new WritableImage(400, 400);
        imageProcessor.setup(image);
        disjointSetManager = new DisjointSetManager(imageProcessor);
    }

    @Benchmark
    public void benchmarkInitializeDisjointSets() {
        disjointSetManager.initializeDisjointSets(image);
    }
}

