package tree;

import interfaces.AbstractMap;
import interfaces.Entry;
import interfaces.SortedMap;
import utils.DefaultComparator;

import java.util.Comparator;

/**
 * An abstract base class to ease the implementation of the SortedMap interface.
 *
 * The base class provides four means of support: 1) It defines a PQEntry class
 * as a concrete implementation of the entry interface 2) It provides an
 * instance variable for a general Comparator and protected methods, compare(a,
 * b), that can compare either two entries or two keys using the comparator. 3)
 * It provides a boolean checkKey method that verifies that a given key is
 * appropriate for use with the comparator
 *
 */
public abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

	// instance variable for an AbstractSortedMap
	/** The comparator defining the ordering of keys in the map. */
	private final Comparator<K> comp;

	/**
	 * Initializes the comparator for the map.
	 * 
	 * @param c comparator defining the order of keys in the map
	 */
	protected AbstractSortedMap(Comparator<K> c) {
		comp = c;
	}

	/** Initializes the map with a default comparator. */
	protected AbstractSortedMap() {
		this(new DefaultComparator<K>()); // default comparator uses natural ordering
	}

	/** Method for comparing two entries according to key */
	protected int compare(Entry<K, V> a, Entry<K, V> b) {
		return comp.compare(a.getKey(), b.getKey());
	}

	/** Method for comparing a key and an entry's key */
	protected int compare(K a, Entry<K, V> b) {
		if (b == null) {
			// If b is null, we can't access b.getKey()
			// Typically, null is considered "smaller" than any non-null value
			return (a == null) ? 0 : 1;
		}
		if (a == null) {
			// If a is null but b isn't, a is considered "smaller"
			return -1;
		}
		// Both are non-null, proceed with normal comparison
		return comp.compare(a, b.getKey());
	}

	/** Method for comparing a key and an entry's key */
	protected int compare(Entry<K, V> a, K b) {
		return comp.compare(a.getKey(), b);
	}

	/** Method for comparing two keys */
	protected int compare(K a, K b) {
		return comp.compare(a, b);
	}

	protected void checkKey(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("key cannot be null");
		}

		try {
			// Try to use the comparator with this key (to check type compatibility)
			comp.compare(key, key);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Incompatible key type");
		}
	}

}
