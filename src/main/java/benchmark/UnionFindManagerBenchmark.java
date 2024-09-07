package benchmark;

import Application.ImageProcessor;
import Application.UnionFind;
import Manager.UnionFindManager;
import javafx.scene.image.WritableImage;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class UnionFindManagerBenchmark {

    private UnionFindManager unionFindManager;
    private UnionFind unionFind;
    private WritableImage image;

    @Setup(Level.Trial)
    public void setup() {
        ImageProcessor imageProcessor = new ImageProcessor();
        image = new WritableImage(400, 400);
        imageProcessor.setup(image);
        int size = imageProcessor.getWidth() * imageProcessor.getHeight();
        unionFind = new UnionFind(size);
        unionFindManager = new UnionFindManager(imageProcessor);
    }

    @Benchmark
    public void benchmarkUnionFind() {
        unionFindManager.unionFind(unionFind, image);
    }
}


