package benchmark;

import Application.UnionFind;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class UnionFindBenchmark {

    private UnionFind uf;

    @Setup(Level.Trial)
    public void setUp() {
        uf = new UnionFind(1000);
        for (int i = 0; i < uf.size() - 1; i++) {
            uf.unionBySize(i, i + 1);
        }
    }

    @Benchmark
    public void benchmarkFind() {
        for (int i = 0; i < uf.size(); i++) {
            uf.find(i);
        }
    }

    @Benchmark
    public void benchmarkConnected() {
        for (int i = 0; i < uf.size() - 1; i++) {
            uf.connected(i, i + 1);
        }
    }

    @Benchmark
    public void benchmarkComponentSize() {
        for (int i = 0; i < uf.size(); i++) {
            uf.componentSize(i);
        }
    }

    @Benchmark
    public void benchmarkUnionBySize() {
        uf = new UnionFind(1000);
        for (int i = 0; i < uf.size() - 1; i++) {
            uf.unionBySize(i, i + 1);
        }
    }
}

