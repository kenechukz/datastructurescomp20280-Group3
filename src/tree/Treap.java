package tree;

import interfaces.Entry;
import interfaces.Position;
import utils.MapEntry;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

public class Treap<K extends Comparable<K>, V> extends TreeMap<K, V> {
    private Random rnd;

    public Treap() {
        super();
        rnd = new Random();
    }

    public Treap(Comparator<K> comp) {
        super(comp);
        rnd = new Random();
    }

    //generate random priorities for new nodes
    private int randomPriority() {
        return rnd.nextInt();
    }


    //re-implemented treeSearch cause it's private in TreeMap
    private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
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

    //same thing as treeSearch just re-implementing
    private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
        tree.set(p, entry);
        tree.addLeft(p, null);
        tree.addRight(p, null);
    }

    //bubbles up till parents prio is not lower than p's
    private void bubbleUp(Position<Entry<K, V>> p) throws IOException {
        while (!tree.isRoot(p)) {
            Position<Entry<K, V>> parent = tree.parent(p);
            if (tree.getAux(p) > tree.getAux(parent))
                tree.rotate(p);
            else
                break;
        }
    }

    //inserts key,value pari into treap. Sets random value for the priority. after insert, node is bubbled up until heap property is restored.
    @Override
    public V put(K key, V value) throws IllegalArgumentException, IOException {
        checkKey(key);
        MapEntry<K, V> newEntry = new MapEntry<>(key, value);
        if (tree.isEmpty()) {
            // Create the root and assign a random priority
            tree.addRoot(newEntry);
            tree.addLeft(tree.root(), null);
            tree.addRight(tree.root(), null);
            tree.setAux(tree.root(), randomPriority());
            return null;
        }
        Position<Entry<K, V>> p = treeSearch(tree.root(), key);
        if (tree.isExternal(p)) {
            expandExternal(p, newEntry);
            // Set a random priority for the new node
            tree.setAux(p, randomPriority());
            // Bubble up the node if its priority is greater than its parent's
            bubbleUp(p);
            return null;
        } else {
            V old = p.getElement().getValue();
            tree.set(p, newEntry);
            return old;
        }
    }


    //bubble down node at position p by rotations with child that has higher prio. goes until both children of p is external
    private void bubbleDown(Position<Entry<K, V>> p) throws IOException {
        while (!(tree.isExternal(tree.left(p)) && tree.isExternal(tree.right(p)))) {
            Position<Entry<K, V>> left = tree.left(p);
            Position<Entry<K, V>> right = tree.right(p);
            // If a child is external, treat its priority as minimal
            int leftPriority = tree.isExternal(left) ? Integer.MIN_VALUE : tree.getAux(left);
            int rightPriority = tree.isExternal(right) ? Integer.MIN_VALUE : tree.getAux(right);
            if (leftPriority > rightPriority)
                tree.rotate(left);
            else
                tree.rotate(right);
        }
    }


    //removes entry with given key from treap. bubbles down till its a leaf node and then uses tree's remove method to remove.
    //removes an external node from it first as you can't delete node with 2 children
    @Override
    public V remove(K key) throws IllegalArgumentException, IOException {
        checkKey(key);
        Position<Entry<K, V>> p = treeSearch(tree.root(), key);
        if (tree.isExternal(p))
            return null;
        V old = p.getElement().getValue();
        // Bubble down the node so that it becomes a leaf
        bubbleDown(p);
        Position<Entry<K, V>> leaf = tree.left(p); // one of the external nodes
        tree.remove(leaf);
        tree.remove(p);
        return old;
    }

}
