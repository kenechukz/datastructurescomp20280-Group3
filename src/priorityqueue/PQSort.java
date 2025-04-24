package priorityqueue;

public class PQSort {
    public static <K extends Comparable<K>> void pqsort(K[] array) {
        HeapPriorityQueue<K, K> pq = new HeapPriorityQueue<>();

        // Insert each element into the priority queue with itself as the value
        for (K element : array) {
            pq.insert(element, element);
        }

        // Remove them in order and place back into the array
        int i = 0;
        while (!pq.isEmpty()) {
            array[i++] = pq.removeMin().getKey();
        }
    }
}