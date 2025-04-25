package tree;

import interfaces.Entry;
import interfaces.Position;

import java.io.IOException;
import java.util.Comparator;

/**
 * An implementation of a sorted map using an AVL tree.
 */
public class AVLTreeMap<K extends Comparable<K>, V> extends TreeMap<K, V> {

	/** Constructs an empty map using the natural ordering of keys. */
	public AVLTreeMap() {
		super();
	}

	/**
	 * Constructs an empty map using the given comparator to order keys.
	 *
	 * @param comp comparator defining the order of keys in the map
	 */
	public AVLTreeMap(Comparator<K> comp) {
		super(comp);
	}

	public static void main(String[] args) throws IOException {
		AVLTreeMap<Integer, String> map = new AVLTreeMap<>();
		Integer[] arr = new Integer[]{35, 26, 15};

		for (Integer i : arr) {
			map.put(i, Integer.toString(i));
		}
		map.remove(26);
		// System.out.println(map.toBinaryTreeString());
	}

	/** Returns the height of the given tree position. */
	protected int height(Position<Entry<K, V>> p) {
		return tree.getAux(p);
	}

	/**
	 * Recomputes the height of the given position based on its children's heights.
	 */
	protected void recomputeHeight(Position<Entry<K, V>> p) {
		int leftHeight = (tree.left(p) != null) ? height(tree.left(p)) : 0;
		int rightHeight = (tree.right(p) != null) ? height(tree.right(p)) : 0;
		tree.setAux(p, 1 + Math.max(leftHeight, rightHeight));
	}

	/** Returns whether a position has balance factor between -1 and 1 inclusive. */
	protected boolean isBalanced(Position<Entry<K, V>> p) {
		int leftHeight = (tree.left(p) != null) ? height(tree.left(p)) : 0;
		int rightHeight = (tree.right(p) != null) ? height(tree.right(p)) : 0;
		int balanceFactor = leftHeight - rightHeight;
		return Math.abs(balanceFactor) <= 1;
	}

	/** Returns a child of p with height no smaller than that of the other child. */
	protected Position<Entry<K, V>> tallerChild(Position<Entry<K, V>> p) {
		int leftHeight = (tree.left(p) != null) ? height(tree.left(p)) : 0;
		int rightHeight = (tree.right(p) != null) ? height(tree.right(p)) : 0;

		if (leftHeight > rightHeight) {
			return tree.left(p);
		} else if (rightHeight > leftHeight) {
			return tree.right(p);
		} else {
			// If equal, return child on the same side as p's parent
			if (tree.isRoot(p)) {
				return tree.left(p); // Arbitrary choice
			} else {
				Position<Entry<K, V>> parent = tree.parent(p);
				if (p == tree.left(parent)) {
					return tree.left(p);
				} else {
					return tree.right(p);
				}
			}
		}
	}

	/**
	 * Utility used to rebalance after an insert or removal operation. This
	 * traverses the path upward from p, performing a trinode restructuring when
	 * imbalance is found, continuing until balance is restored.
	 */
	protected void rebalance(Position<Entry<K, V>> p) throws IOException {
		while (p != null) {
			recomputeHeight(p);
			if (!isBalanced(p)) {
				// Perform trinode restructuring
				Position<Entry<K, V>> x = tallerChild(tallerChild(p));
				p = restructure(x);
				recomputeHeight(tree.left(p));
				recomputeHeight(tree.right(p));
				recomputeHeight(p);
			}
			p = tree.parent(p);
		}
	}

	/** Overrides the TreeMap rebalancing hook that is called after an insertion. */
	@Override
	protected void rebalanceInsert(Position<Entry<K, V>> p) throws IOException {
		rebalance(p);
	}

	/** Overrides the TreeMap rebalancing hook that is called after a deletion. */
	@Override
	protected void rebalanceDelete(Position<Entry<K, V>> p) throws IOException {
		rebalance(p);
	}
}
