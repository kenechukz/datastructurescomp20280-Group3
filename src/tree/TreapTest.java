package tree;

import interfaces.Entry;
import interfaces.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TreapTest {
    private Treap <Integer, String> treap = new Treap<>();

    @BeforeEach
    public void setUp(){
        treap = new Treap<>();
    }

    //tests the put method and get method
    @Test
    public void testInsertionAndGet() throws Exception {
        // Insert keys and verify retrieval
        treap.put(10, "ten");
        treap.put(20, "twenty");
        treap.put(5, "five");

        assertEquals("ten", treap.get(10));
        assertEquals("twenty", treap.get(20));
        assertEquals("five", treap.get(5));
        // Check that a non-existent key returns null
        assertNull(treap.get(30));
    }

    //tests updating a value for existing key
    @Test
    public void testUpdate() throws Exception {
        treap.put(15, "fifteen");
        // Update same key and verify that the old value is returned.
        String oldValue = treap.put(15, "updated fifteen");
        assertEquals("fifteen", oldValue);
        assertEquals("updated fifteen", treap.get(15));
    }

    //tests removal of nodes
    @Test
    public void testRemoval() throws Exception {
        // Insert a series of keys
        treap.put(10, "ten");
        treap.put(5, "five");
        treap.put(15, "fifteen");
        treap.put(3, "three");
        treap.put(7, "seven");

        // Remove a leaf node
        String removed = treap.remove(3);
        assertEquals("three", removed);
        assertNull(treap.get(3));

        // Remove an internal node (with one child)
        removed = treap.remove(5);
        assertEquals("five", removed);
        assertNull(treap.get(5));

        // Remove an internal node that has two children
        removed = treap.remove(10);
        assertEquals("ten", removed);
        assertNull(treap.get(10));

        // The other nodes should remain
        assertEquals("fifteen", treap.get(15));
        assertEquals("seven", treap.get(7));
    }

    //uses inorder traversal from entryset to return keys in sorted order
    @Test
    public void testBSTOrderingUsingEntrySet() throws Exception {
        // Insert keys in various orders
        treap.put(30, "thirty");
        treap.put(10, "ten");
        treap.put(20, "twenty");
        treap.put(40, "forty");
        treap.put(25, "twenty-five");

        //retrieving entries
        Iterable<Entry<Integer, String>> entries = treap.entrySet();
        List<Integer> keys = new ArrayList<>();
        for (Entry<Integer, String> entry : entries) {
            keys.add(entry.getKey());
        }

        //verifying keys are ascending
        for (int i = 1; i < keys.size(); i++) {
            assertTrue(keys.get(i - 1) <= keys.get(i), "Keys are not in ascending order");
        }
    }


    @Test
    public void testHeapProperty() throws Exception {
        // Insert a series of keys so that multiple rotations are likely
        int[] keys = {10, 20, 5, 15, 25, 2, 7, 12, 17};
        for (int key : keys) {
            treap.put(key, "value" + key);
        }

        // Recursively validate the heap property starting from the root.
        Position<Entry<Integer, String>> root = treap.tree.root();
        assertTrue(validateHeapProperty(root), "Heap property violated in the treap");
    }

    private boolean validateHeapProperty(Position<Entry<Integer, String>> p) {
        //external nodes hold property by default
        if (treap.tree.isExternal(p))
            return true;

        Position<Entry<Integer, String>> left = treap.tree.left(p);
        Position<Entry<Integer, String>> right = treap.tree.right(p);
        int parentPriority = treap.tree.getAux(p);

        if (!treap.tree.isExternal(left)) {
            int leftPriority = treap.tree.getAux(left);
            if (parentPriority < leftPriority)
                return false;
        }
        if (!treap.tree.isExternal(right)) {
            int rightPriority = treap.tree.getAux(right);
            if (parentPriority < rightPriority)
                return false;
        }

        //recurse through the subtrees
        return validateHeapProperty(left) && validateHeapProperty(right);
    }
}
