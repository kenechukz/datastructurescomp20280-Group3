package tree;

import interfaces.Entry;
import interfaces.Position;
import utils.MapEntry;

import java.io.IOException;
//import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * An implementation of a sorted map using a binary search tree.
 */

public class TreeMap<K extends Comparable<K>, V> extends AbstractSortedMap<K, V> {


	public BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();

	/** Constructs an empty map using the natural ordering of keys. */
	public TreeMap() {
		super(); // the AbstractSortedMap constructor
		tree.addRoot(null); // create a sentinel leaf as root
	}

	/**
	 * Constructs an empty map using the given comparator to order keys.
	 *
	 * @param comp comparator defining the order of keys in the map
	 */
	public TreeMap(Comparator<K> comp) {
		super(comp); // the AbstractSortedMap constructor
		tree.addRoot(null); // create a sentinel leaf as root
	}

	/**
	 * Remove all entries from this map.
	 */


	public void clear() {
		// re-initialize the empty tree
		tree = new BalanceableBinaryTree<>();
		tree.size = 0;
		tree.addRoot(null);

	}



	/**
	 * Returns the number of entries in the map.
	 *
	 * @return number of entries in the map
	 */
	@Override
	public int size() {
		return (tree.size() - 1) / 2; // only internal nodes have entries
	}

	protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) throws IOException {
		return tree.restructure(x);
	}

	/**
	 * Rebalances the tree after an insertion of specified position. This version of
	 * the method does not do anything, but it can be overridden by subclasses.
	 *
	 * @param p the position which was recently inserted
	 */
	protected void rebalanceInsert(Position<Entry<K, V>> p) throws IOException {
		// LEAVE EMPTY
	}

	/**
	 * Rebalances the tree after a child of specified position has been removed.
	 * This version of the method does not do anything, but it can be overridden by
	 * subclasses.
	 *
	 * @param p the position of the sibling of the removed leaf
	 */
	protected void rebalanceDelete(Position<Entry<K, V>> p) throws IOException {
		// LEAVE EMPTY
	}

	/**
	 * Rebalances the tree after an access of specified position. This version of
	 * the method does not do anything, but it can be overridden by a subclasses.
	 *
	 * @param p the Position which was recently accessed (possibly a leaf)
	 */
	protected void rebalanceAccess(Position<Entry<K, V>> p) throws IOException {
		// LEAVE EMPTY
	}

	/** Utility used when inserting a new entry at a leaf of the tree */
	private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
		// TODO
		tree.set(p, entry);

		tree.addLeft(p, null);
		tree.addRight(p, null);
	}

	/**
	 * Returns the position in p's subtree having the given key (or else the
	 * terminal leaf).
	 *
	 * @param key a target key
	 * @param p   a position of the tree serving as root of a subtree
	 * @return Position holding key, or last node reached during search
	 */
	private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
		// TODO
		if(tree.isExternal(p))
			return p;
		int comp = compare(key, p.getElement());
		if(comp == 0)
			return p;
		else if (comp < 0)
			return treeSearch(tree.left(p), key);
		else
			return treeSearch(tree.right(p), key);
	}

	/**
	 * Returns position with the minimal key in the subtree rooted at Position p.
	 *
	 * @param p a Position of the tree serving as root of a subtree
	 * @return Position with minimal key in subtree
	 */
	protected Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) {
		// TODO
		Position<Entry<K, V>> walk = p;
		while (tree.isInternal(walk))
			walk = tree.left(walk);
		return tree.parent(walk);
	}

	/**
	 * Returns the position with the maximum key in the subtree rooted at p.
	 *
	 * @param p a Position of the tree serving as root of a subtree
	 * @return Position with maximum key in subtree
	 */
	protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) {
		// TODO
		Position<Entry<K, V>> walk = p;
		while (tree.isInternal(walk))
			walk = tree.right(walk);
		return tree.parent(walk);
	}

	/**
	 * Returns the value associated with the specified key, or null if no such entry
	 * exists.
	 *
	 * @param key the key whose associated value is to be returned
	 * @return the associated value, or null if no such entry exists
	 */
	@Override
	public V get(K key) throws IllegalArgumentException, IOException {
		// TODO
		checkKey(key);
		Position<Entry <K,V>> p = treeSearch(tree.root(), key);
		rebalanceAccess(p);
		if(tree.isExternal(p))
			return null;
		return p.getElement().getValue();
	}

	/**
	 * Associates the given value with the given key. If an entry with the key was
	 * already in the map, this replaced the previous value with the new one and
	 * returns the old value. Otherwise, a new entry is added and null is returned.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with the key (or null, if no such
	 *         entry)
	 */
	@Override
	public V put(K key, V value) throws IllegalArgumentException, IOException {
		// TODO
		checkKey(key);
		Entry<K,V> newEntry = new MapEntry<>(key, value);

		if (tree.isEmpty()) {
			tree.addRoot(newEntry);
			return null;
		}
		Position<Entry <K,V>> p = treeSearch(tree.root(), key);
		if(tree.isExternal(p)){
			expandExternal(p, newEntry);
			rebalanceInsert(p);
			return null;
		}else{
			V old = p.getElement().getValue();
			tree.set(p, newEntry);
			rebalanceAccess(p);
			return old;
		}
	}

	/**
	 * Removes the entry with the specified key, if present, and returns its
	 * associated value. Otherwise does nothing and returns null.
	 *
	 * @param key the key whose entry is to be removed from the map
	 * @return the previous value associated with the removed key, or null if no
	 *         such entry exists
	 */
	@Override
	public V remove(K key) throws IllegalArgumentException, IOException {
		// TODO
		checkKey(key);
		Position<Entry <K,V>> p = treeSearch(tree.root(), key);

		if(tree.isExternal(p)){
			rebalanceInsert(p);
			return null;
		}else{
			V old = p.getElement().getValue();
			if(tree.isInternal(tree.left(p)) && tree.isInternal(tree.right(p))) {
				Position<Entry <K,V>> replacement = treeMax(tree.left(p));
				tree.set(p, replacement.getElement());
				p = replacement;
			}
			Position<Entry<K,V>> leaf = (tree.isExternal(tree.left(p)) ? tree.left(p) : tree.right(p));
			Position<Entry<K,V>> sib = tree.sibling(leaf);
			tree.remove(leaf);
			tree.remove(p);
			rebalanceDelete(p);
			return old;
		}
	}

	// additional behaviors of the SortedMap interface

	/**
	 * Returns the entry having the least key (or null if map is empty).
	 *
	 * @return entry with least key (or null if map is empty)
	 */
	@Override
	public Entry<K, V> firstEntry() {
		// TODO
		if (isEmpty()) return null;
		return treeMin(tree.root()).getElement();
	}

	/**
	 * Returns the entry having the greatest key (or null if map is empty).
	 *
	 * @return entry with greatest key (or null if map is empty)
	 */
	@Override
	public Entry<K, V> lastEntry() {
		// TODO
		if (isEmpty()) return null;
		return treeMax(tree.root()).getElement();
	}

	/**
	 * Returns the entry with least key greater than or equal to given key (or null
	 * if no such key exists).
	 *
	 * @return entry with least key greater than or equal to given (or null if no
	 *         such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
		// TODO
		// Check if the key is valid, may throw IllegalArgumentException
		checkKey(key);

		// Search for the key in the tree
		Position<Entry<K, V>> p = treeSearch(tree.root(), key);

		// If an exact match is found, return the element immediately
		if (tree.isInternal(p))
			return p.getElement();

		// Otherwise, climb upward until we find a node that is a left child of its parent.
		// In that case, the parent's key is the least key greater than the given key.
		while (!tree.isRoot(p)) {
			if (p == tree.left(tree.parent(p)))
				return tree.parent(p).getElement();
			else
				p = tree.parent(p);
		}

		// If no such parent exists, then there is no key in the tree that is strictly greater.
		return null;
	}

	/**
	 * Returns the entry with greatest key less than or equal to given key (or null
	 * if no such key exists).
	 *
	 * @return entry with greatest key less than or equal to given (or null if no
	 *         such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
		// TODO
		checkKey(key);
		Position<Entry<K, V>> p = treeSearch(tree.root(), key);
		if(tree.isInternal(p))
			return p.getElement();
		while(!tree.isRoot(p)){
			if(p == tree.right(tree.parent(p)))
				return tree.parent(p).getElement();
			else
				p = tree.parent(p);
		}
		return null;
	}

	/**
	 * Returns the entry with greatest key strictly less than given key (or null if
	 * no such key exists).
	 *
	 * @return entry with greatest key strictly less than given (or null if no such
	 *         entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
		// TODO
		checkKey(key); // may throw IllegalArgumentException
		Position<Entry<K,V>> p = treeSearch(tree.root( ), key);
		if (tree.isInternal(p) && tree.isInternal(tree.left(p)))
			return treeMax(tree.left(p)).getElement( ); // this is the predecessor to p
		// otherwise, we had failed search, or match with no left child
		while (!tree.isRoot(p)) {
			if (p == tree.right(tree.parent(p)))
				return tree.parent(p).getElement( ); // parent has next lesser key
			else
				p = tree.parent(p);
			}
		return null;
	}

	/**
	 * Returns the entry with least key strictly greater than given key (or null if
	 * no such key exists).
	 *
	 * @return entry with least key strictly greater than given (or null if no such
	 *         entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
		// TODO
		checkKey(key); // may throw IllegalArgumentException
		Position<Entry<K,V>> p = treeSearch(tree.root(), key);

		// If a matching node is found and it has a right child,
		// then the higher entry is the minimal element in the right subtree.
		if (tree.isInternal(p) && tree.isInternal(tree.right(p)))
			return treeMin(tree.right(p)).getElement();

		// Otherwise, search upward until we find a node that is a left child
		// of its parent; then the parent will be the successor.
		while (!tree.isRoot(p)) {
			if (p == tree.left(tree.parent(p)))
				return tree.parent(p).getElement();
			else
				p = tree.parent(p);
		}
		return null;
	}

	// Support for iteration

	/**
	 * Returns an iterable collection of all key-value entries of the map.
	 *
	 * @return iterable collection of the map's entries
	 */
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		// TODO
		ArrayList<Entry<K,V>> buffer = new ArrayList<>(size( ));
		for (Position<Entry<K,V>> p : tree.inorder( ))
			if (tree.isInternal(p)) buffer.add(p.getElement( ));
		return buffer;
	}



	@Override
	public double loadFactor() {
		return 0;
	}

	@Override
	public int numCollisions() {
		return 0;
	}


	public String toString() {
		return tree.toString();
	}

	/**
	 * Returns an iterable containing all entries with keys in the range from
	 * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
	 *
	 * @return iterable with keys in desired range
	 * @throws IllegalArgumentException if <code>fromKey</code> or
	 *                                  <code>toKey</code> is not compatible with
	 *                                  the map
	 */
	@Override
	public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
		// TODO
		ArrayList<Entry<K,V>> buffer = new ArrayList<>(size( ));
		if (compare(fromKey, toKey) < 0) // ensure that fromKey < toKey
			subMapRecurse(fromKey, toKey, tree.root( ), buffer);
		return buffer;
	}

	// utility to fill subMap buffer recursively (while maintaining order)
	private void subMapRecurse(K fromKey, K toKey, Position<Entry<K, V>> p,
							   ArrayList<Entry<K, V>> buffer) {
		// TODO
		if (tree.isInternal(p))
			if (compare(p.getElement( ), fromKey) < 0)
			// p's key is less than fromKey, so any relevant entries are to the right
		subMapRecurse(fromKey, toKey, tree.right(p), buffer);
		else {
			subMapRecurse(fromKey, toKey, tree.left(p), buffer); // first consider left subtree
			if (compare(p.getElement( ), toKey) < 0) { // p is within range
				buffer.add(p.getElement( )); // so add it to buffer, and consider
				subMapRecurse(fromKey, toKey, tree.right(p), buffer); // right subtree as well
				}
			}
	}
	
	protected void rotate(Position<Entry<K, V>> p) {
		tree.rotate(p);
	}




	public String toBinaryTreeString() {
		BinaryTreePrinter< Entry<K, V> > btp = new BinaryTreePrinter<>(this.tree);
		return btp.print();
	}

	public static void main(String[] args) throws IOException {
	}


}
