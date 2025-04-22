package priorityqueue;

/*
 */

import interfaces.Entry;
//import tree.BinaryTreeTex;
import tree.LinkedBinaryTree;
import utils.Timer;
import utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * An implementation of a priority queue using an array-based heap.
 */

public class HeapPriorityQueue<K extends Comparable<K>, V extends Comparable<V> > extends AbstractPriorityQueue<K, V> {

	protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

	public ArrayList<K> keys() {
		// TODO
		ArrayList<K> list = new ArrayList<>();
		for (Entry<K, V> entry : heap) {
			list.add(entry.getKey());
		}
		return list;
	}

	public ArrayList<V> values() {
		// TODO
		ArrayList<V> list = new ArrayList<>();
		for (Entry<K, V> entry : heap) {
			list.add(entry.getValue());
		}
		return list;
	}
	/**
	 * Creates an empty priority queue based on the natural ordering of its keys.
	 */
	public HeapPriorityQueue() {
		super();
	}

	/**
	 * Creates an empty priority queue using the given comparator to order keys.
	 *
	 * @param comp comparator defining the order of keys in the priority queue
	 */
	public HeapPriorityQueue(Comparator<K> comp) {
		super(comp);
	}

	/**
	 * Creates a priority queue initialized with the respective key-value pairs. The
	 * two arrays given will be paired element-by-element. They are presumed to have
	 * the same length. (If not, entries will be created only up to the length of
	 * the shorter of the arrays)
	 *
	 * @param keys   an array of the initial keys for the priority queue
	 * @param values an array of the initial values for the priority queue
	 */
	public HeapPriorityQueue(K[] keys, V[] values) {
		// TODO
	}

	// protected utilities
	protected int parent(int j) {
		if(j==0 ){
			return -1;
		}
		return (j-1)/2;
	}

	protected int left(int j) {
		// TODO
		return 2*j+1;

	}

	protected int right(int j) {
		// TODO
		return 2*j+2;
	}

	protected boolean hasLeft(int j) {
		// TODO
		return left(j) < heap.size();
	}

	protected boolean hasRight(int j) {
		// TODO
		return right(j) < heap.size();

	}

	/** Exchanges the entries at indices i and j of the array list. */
	protected void swap(int i, int j) {
		// TODO
		Entry<K, V> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}

	/**
	 * Moves the entry at index j higher, if necessary, to restore the heap
	 * property.
	 */
	protected void upheap(int j) {
		// TODO
		// keep doing until satisfied
		// get the parent of j
		// compare keys of current position j with parent(j)
		// if heap order satisfied -> done
		// else bubble up
		while(j>0){
			int parent = parent(j);
			if(compare(heap.get(j), heap.get(parent)) >= 0){
				break;
			}
			swap(parent, j);
			j=parent;
		}
	}

	/*
	heapsort in place
	 */
	static<V extends Comparable<V> > void heapsort(V[] arr) {
		// TODO

	}
	protected static<V extends Comparable<V> > void downheap(V[] arr, int start, int end) {
		// TODO
		// while not at the bottom tree (leaf)
		// if key_parent >= key_child stop
		// find smallest child and swap

	}

	/**
	 * Moves the entry at index j lower, if necessary, to restore the heap property.
	 */
	protected void downheap(int j) {
		// TODO
		// while not at the bottom tree (leaf)
		// if key_parent >= key_child stop
		// find smallest child and swap
		while(hasLeft(j)) {
			int leftIndex = left(j);
			int smallestIndex = leftIndex;

			if(hasRight(j)) {
				int rightIndex = right(j);
				if(compare(heap.get(rightIndex), heap.get(smallestIndex)) < 0){
					smallestIndex = rightIndex;
				}
			}
			if(compare(heap.get(smallestIndex), heap.get(j)) >= 0){
				break;
			}
			swap(j, smallestIndex);
			j = smallestIndex;
		}
	}

	/** Performs a bottom-up construction of the heap in linear time. */
	protected void heapify() {
		// TODO
		int startIndex = parent(heap.size() - 1);
		for (int j = startIndex; j >= 0; j--) {
			downheap(j);
		}
	}

	// public methods

	/**
	 * Returns the number of items in the priority queue.
	 *
	 * @return number of items
	 */
	@Override
	public int size() {
		return heap.size();
	}

	/**
	 * Returns (but does not remove) an entry with minimal key.
	 *
	 * @return entry having a minimal key (or null if empty)
	 */
	@Override
	public Entry<K, V> min() {
		return heap.get(0);
	}

	/**
	 * Inserts a key-value pair and return the entry created.
	 *
	 * @param key   the key of the new entry
	 * @param value the associated value of the new entry
	 * @return the entry storing the new key-value pair
	 * @throws IllegalArgumentException if the key is unacceptable for this queue
	 */
	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		// TODO
		Entry<K, V> entry = new PQEntry<>(key, value);
		heap.add(entry);
		upheap(heap.size() - 1);  // Restore heap property.
		return entry;
	}

	/**
	 * Removes and returns an entry with minimal key.
	 *
	 * @return the removed entry (or null if empty)
	 */
	@Override
	public Entry<K, V> removeMin() {
		// TODO
		if (heap.isEmpty())
			return null;  // Or throw exception
		Entry<K, V> minEntry = heap.get(0);
		int lastIndex = heap.size() - 1;
		swap(0, lastIndex);         // Swap min with the last element.
		heap.remove(lastIndex);     // removes the min element
		if (!heap.isEmpty())
			downheap(0);            // Restore heap property.
		return minEntry;
	}

	public String toString() {
		return heap.toString();
	}



	public static void main(String[] args) {
	}


}
