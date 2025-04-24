package priorityqueue;

import interfaces.Entry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An implementation of a priority queue using an array-based heap.
 */
public class HeapPriorityQueue<K extends Comparable<K>, V extends Comparable<V>> extends AbstractPriorityQueue<K, V> {

	protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

	public ArrayList<K> keys() {
		// TODO
		return null;
	}

	public ArrayList<V> values() {
		// TODO
		return null;
	}

	// Constructors
	public HeapPriorityQueue() {
		super();
	}

	public HeapPriorityQueue(Comparator<K> comp) {
		super(comp);
	}

	public HeapPriorityQueue(K[] keys, V[] values) {
		int n = keys.length;
		heap = IntStream.range(0, keys.length)
				.mapToObj(i -> new PQEntry<>(keys[i], values[i]))
				.collect(Collectors.toCollection(ArrayList::new));
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(i);
		}
	}

	// Utility methods for heap
	protected int parent(int j) {
		return (j - 1) / 2;
	}

	protected int left(int j) {
		return 2 * j + 1;
	}

	protected int right(int j) {
		return 2 * j + 2;
	}

	protected boolean hasLeft(int j) {
		return left(j) < heap.size();
	}

	protected boolean hasRight(int j) {
		return right(j) < heap.size();
	}

	// Swaps the entries at indices i and j of the array list
	protected void swap(int i, int j) {
		Entry<K, V> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}

	// Moves the entry at index j higher, if necessary, to restore the heap property
	protected void upheap(int j) {
		while (j > 0 && heap.get(j).compareTo(heap.get(parent(j))) < 0) {
			swap(j, parent(j));
			j = parent(j);
		}
	}

	// Moves the entry at index j lower, if necessary, to restore the heap property
	protected void downheap(int j) {
		while (hasLeft(j)) {
			int leftChild = left(j);
			int rightChild = right(j);
			int smallerChild = leftChild;

			if (hasRight(j) && heap.get(rightChild).compareTo(heap.get(leftChild)) < 0) {
				smallerChild = rightChild;
			}

			if (heap.get(j).compareTo(heap.get(smallerChild)) <= 0) {
				break;
			}

			swap(j, smallerChild);
			j = smallerChild;
		}
	}

	// Bottom-up construction of the heap in linear time
	protected void heapify(int i) {
		int min = i;
		int n = heap.size();

		if (hasLeft(i) && heap.get(left(i)).compareTo(heap.get(i)) < 0) {
			min = left(i);
		}

		if (hasRight(i) && heap.get(right(i)).compareTo(heap.get(min)) < 0) {
			min = right(i);
		}

		if (min != i) {
			swap(i, min);
			heapify(min);
		}
	}

	// Public methods for the priority queue

	@Override
	public int size() {
		return heap.size();
	}

	@Override
	public Entry<K, V> min() {
		return heap.isEmpty() ? null : heap.get(0);
	}

	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		Entry<K, V> entry = new PQEntry<>(key, value);
		heap.add(entry);
		upheap(heap.size() - 1); // Ensure the heap property is maintained
		return entry;
	}

	@Override
	public Entry<K, V> removeMin() {
		if (heap.isEmpty()) {
			return null;
		}
		swap(0, heap.size() - 1);  // Move the last element to the root
		Entry<K, V> min = heap.remove(heap.size() - 1);  // Remove the root
		downheap(0);  // Restore the heap property
		return min;
	}

	public String toString() {
		return heap.toString();
	}

	// Heapsort method (in-place sorting)
	public static <V extends Comparable<V>> void heapsort(V[] arr) {
		// Convert the array to a heap
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			downheap(arr, i, arr.length);
		}

		// Extract the elements from the heap one by one and place them in sorted order
		for (int end = arr.length - 1; end > 0; end--) {
			// Swap the root (min) with the last element
			V temp = arr[0];
			arr[0] = arr[end];
			arr[end] = temp;

			// Restore the heap property for the reduced heap
			downheap(arr, 0, end);
		}
	}

	// Helper method for heapsort
	protected static <V extends Comparable<V>> void downheap(V[] arr, int start, int end) {
		int root = start;
		while (root * 2 + 1 < end) { // While the left child exists
			int leftChild = root * 2 + 1;
			int rightChild = root * 2 + 2;
			int minChild = leftChild;

			// Find the smaller child
			if (rightChild < end && arr[rightChild].compareTo(arr[leftChild]) < 0) {
				minChild = rightChild;
			}

			// If the root is smaller than the smallest child, the heap property is restored
			if (arr[root].compareTo(arr[minChild]) <= 0) {
				break;
			}

			// Swap the root with the smallest child
			V temp = arr[root];
			arr[root] = arr[minChild];
			arr[minChild] = temp;

			root = minChild; // Move the root to the smallest child
		}
	}
}
