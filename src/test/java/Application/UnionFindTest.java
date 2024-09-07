package Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnionFindTest {

    private UnionFind uf;

    @BeforeEach
    void setUp() {
        uf = new UnionFind(5);
        uf.unionBySize(0, 1);
        uf.unionBySize(2, 3);
    }

    @Test
    void find() {
        assertEquals(uf.find(0), uf.find(1));
        assertEquals(uf.find(2), uf.find(3));
        assertNotEquals(uf.find(0), uf.find(2));
    }

    @Test
    void connected() {
        assertTrue(uf.connected(0, 1));
        assertTrue(uf.connected(2, 3));
        assertFalse(uf.connected(0, 2));
        assertFalse(uf.connected(0, 3));
        assertFalse(uf.connected(1, 2));
        assertFalse(uf.connected(1, 3));
    }

    @Test
    void componentSize() {
        assertEquals(2, uf.componentSize(0));
        assertEquals(2, uf.componentSize(1));
        assertEquals(2, uf.componentSize(2));
        assertEquals(2, uf.componentSize(3));
        assertEquals(1, uf.componentSize(4));
    }

    @Test
    void size() {
        assertEquals(5, uf.size());
    }

    @Test
    void components() {
        assertEquals(3, uf.components());
    }

    @Test
    void unionBySize() {
        uf.unionBySize(1, 2);
        assertTrue(uf.connected(1, 2));
        assertEquals(4, uf.componentSize(1));
        assertEquals(2, uf.components());
    }
}
