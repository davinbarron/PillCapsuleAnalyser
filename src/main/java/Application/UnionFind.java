package Application;

public class UnionFind {

    // The number of elements in this union find
    private final int size; //Total population

    // Used to track the size of each of the component
    private final int[] sz; //Size of each tree

    // id[i] points to the parent of i, if id[i] = i then i is a root node
    private final int[] id; //Oldest ancestor/root of the tree

    // Tracks the number of components/disjoint sets in the union find
    private int numComponents; //Num. Of Separate trees

    // Constructor initializes an empty union find data structure with N isolated sets.
    public UnionFind(int size) {
        if (size <= 0) throw new IllegalArgumentException("Size <= 0 is not allowed");
        this.size = numComponents = size;
        sz = new int[size];
        id = new int[size];
        for (int i = 0; i < size; i++) {
            id[i] = i; // Link to itself (self root)
            sz[i] = 1; // Each component is originally of size one
        }
    }

    // While looking for the root, we make each node in the path point to its grandparent.
    public int find(int p) {
        while (id[p] != p) {
            id[p] = id[id[p]]; // Path compression
            p = id[p];
        }
        return p;
    }

    // This method checks if the two elements are in the same set or not
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    // This method returns the size of the set element belongs to
    public int componentSize(int p) {
        return sz[find(p)];
    }

    // This method returns the total number of elements in the Union-Find
    public int size() {
        return size;
    }

    // This method returns the total number of sets in the Union-Find
    public int components() {
        return numComponents;
    }

    // This method merges the set containing element p and the set containing element q
    public void unionBySize(int p, int q) {

        // These elements are already in the same group!
        if (connected(p, q)) return;

        int rootp = find(p);
        int rootq = find(q);

        // Determine which root is bigger based on the size of the components
        int biggerRoot = sz[rootp] < sz[rootq] ? rootp : rootq;
        int smallerRoot = biggerRoot == rootp ? rootq : rootp;
        int smallSize = sz[smallerRoot];

        // Make the smaller root point to the bigger root
        id[smallerRoot] = biggerRoot;

        // Update the size of the bigger root
        sz[biggerRoot] += smallSize;

        // Decrease the number of components
        numComponents--;
    }
}
