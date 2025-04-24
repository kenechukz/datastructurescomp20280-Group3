package tree;

import java.io.IOException;
import java.util.Arrays;

public class TreapSort {
    public static <K extends Comparable<K>> void treapSort(K[] arr) throws IOException {
        Treap<K, K> treap = new Treap<>();
        int i;

        // Put values into array
        for (i = 0; i < arr.length; i++) {
            treap.put(arr[i], arr[i]);
        }
         i=0;
        // Replace in array in inorder traversal
        for (K key : treap.keySet()) {
            arr[i++] = key;
        }
    }
}


