package hashtable;

import interfaces.Entry;
import interfaces.Position;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Map implementation using hash table with separate chaining.
 */

public class ChainHashMap<K extends Comparable<K>, V> extends AbstractHashMap<K, V> {
	// a fixed capacity array of UnsortedTableMap that serve as buckets
	private UnsortedTableMap<K, V>[] table; // initialized within createTable

	/** Creates a hash table with capacity 11 and prime factor 109345121. */
	public ChainHashMap() {
		super();
	}

	/** Creates a hash table with given capacity and prime factor 109345121. */
	public ChainHashMap(int cap) {
		super(cap);
	}

	/** Creates a hash table with the given capacity and prime factor. */
	public ChainHashMap(int cap, int p) {
		super(cap, p);
	}

	/** Creates an empty table having length equal to current capacity. */
	@Override
	@SuppressWarnings({ "unchecked" })
	protected void createTable() {
		// TODO
		table = new UnsortedTableMap[capacity];
	}

	@Override
	public double loadFactor() {
		// TODO
		return n/capacity;
	}

	@Override
	public int numCollisions() {
		int collisions = 0;

		for(int i = 0; i < capacity; i++) {
			// If the bucket exists and has entries
			if(table[i] != null) {
				// The number of collisions in this bucket is (size - 1)
				// since the first entry isn't considered a collision
				int bucketSize = table[i].size();
				if(bucketSize > 1) {
					collisions += (bucketSize - 1);
				}
			}
		}
		return collisions;
	}
	/**
	 * Returns value associated with key k in bucket with hash value h. If no such
	 * entry exists, returns null.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @return associate value (or null, if no such entry)
	 */
	@Override
	protected V bucketGet(int h, K k) {
		// TODO
		if(table[h] == null) {
			return null;
		}
		return table[h].get(k);
	}

	/**
	 * Associates key k with value v in bucket with hash value h, returning the
	 * previously associated value, if any.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @param v the value to be associated
	 * @return previous value associated with k (or null, if no such entry)
	 */
	@Override
	protected V bucketPut(int h, K k, V v) {
		// TODO
		if(table[h] == null) {
			table[h] = new UnsortedTableMap<>();
		}

		int oldSize = table[h].size();
		V oldValue = table[h].put(k, v);

		if(table[h].size() > oldSize){
			n++;
		}
		return oldValue;
	}		


	/**
	 * Removes entry having key k from bucket with hash value h, returning the
	 * previously associated value, if found.
	 * 
	 * @param h the hash value of the relevant bucket
	 * @param k the key of interest
	 * @return previous value associated with k (or null, if no such entry)
	 */
	@Override
	protected V bucketRemove(int h, K k) {
		// TODO
		if(table[h] == null) {
			return null;
		}

		int oldSize = table[h].size();
		V value = table[h].remove(k);

		if(table[h].size() < oldSize){
			n--;
		}
		return value;
	}

	/**
	 * Returns an iterable collection of all key-value entries of the map.
	 *
	 * @return iterable collection of the map's entries
	 */
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		ArrayList<Entry<K, V>> buffer = new ArrayList<>();

		// Iterate through all buckets
		for (int h = 0; h < capacity; h++) {
			if (table[h] != null) {
				// For each bucket, add all its entries to the buffer
				for (Entry<K, V> entry : table[h].entrySet()) {
					buffer.add(entry);
				}
			}
		}

		return buffer;
	}
	
	public String toString() {
		return entrySet().toString();
	}

	public static void main(String[] args) {
		ChainHashMap<Integer, String> m = new ChainHashMap<Integer, String>();
		m.put(1, "One");
		m.put(10, "Ten");
		m.put(11, "Eleven");
		m.put(20, "Twenty");
		
		System.out.println("m: " + m);
		
		m.remove(11);
		System.out.println("m: " + m);
	}
}
