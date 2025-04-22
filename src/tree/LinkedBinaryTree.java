package tree;

import interfaces.BinaryTree;
import interfaces.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Concrete implementation of a binary tree using a node-based, linked
 * structure.
 */
public class LinkedBinaryTree<E extends Comparable<E>> implements BinaryTree<E> {


    protected Node<E> root = null; // root of the tree

    //num of nodes in tree
    protected int size = 0;

    public LinkedBinaryTree() {
    }

    public static <E extends Comparable<E>> LinkedBinaryTree<E> makeRandom(int n, E[] arr) {
        if (n != arr.length)
            throw new IllegalArgumentException("Array length must be equal to n");

        LinkedBinaryTree<E> tree = new LinkedBinaryTree<>();
        //build tree structure using randomTree
        tree.root = randomTree(null, 0, n - 1, arr);
        //set the tree size
        tree.size = n;
        return tree;
    }

    public static LinkedBinaryTree<Integer> makeRandom(int n) {
        if (n < 1)
            throw new IllegalArgumentException("n must be at least 1");

        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        return makeRandom(n, arr);
    }

    public static <E> Node<E> randomTree(Node<E> parent, int first, int last, E[] arr) {
        if (first > last) {
            return null;
        }

        //randomly select an index between first and last
        int randomIndex = first + (int)(Math.random() * (last - first + 1));

        //create new node with the selected element
        Node<E> node = new Node<>(arr[randomIndex], parent, null, null);

        //recursively build left and right subtrees from the remaining parts of the array
        node.left = randomTree(node, first, randomIndex - 1, arr);
        node.right = randomTree(node, randomIndex + 1, last, arr);

        return node;
    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the Position of p's sibling (or null if no sibling exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the sibling (or null if no sibling exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> sibling(Position<E> p) {

        Node<E> node = validate(p);
        Node<E> parent = node.parent;
        if (parent == null)
            return null;
        if (node == parent.left)
            return parent.right;
        else
            return parent.left;


    }

    @Override
    public Iterator<E> iterator() {
        return new ElementIterator();
    }

    /**
     * Returns true if Position p has one or more children.
     *
     * @param p A valid Position within the tree
     * @return true if p has at least one child, false otherwise
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    @Override
    public boolean isInternal(Position<E> p) {
        return numChildren(p) > 0;
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using an inorder traversal
     *
     * @param p        Position serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void inorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        if (left(p) != null)
            inorderSubtree(left(p), snapshot);
        snapshot.add(p);
        if (right(p) != null)
            inorderSubtree(right(p), snapshot);
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using a preorder traversal
     *
     * @param p        Position serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        snapshot.add(p);
        for (Position<E> c : children(p))
            preorderSubtree(c, snapshot);
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in preorder.
     *
     * @return iterable collection of the tree's positions in preorder
     */
    public Iterable<Position<E>> preorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty())
            preorderSubtree(root, snapshot);
        return snapshot;
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in inorder.
     *
     * @return iterable collection of the tree's positions reported in inorder
     */
    public Iterable<Position<E>> inorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty())
            inorderSubtree(root(), snapshot);
        return snapshot;
    }

    /**
     * Adds positions of the subtree rooted at Position p to the given
     * snapshot using a postorder traversal
     *
     * @param p        Position serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        if (left(p) != null)
            postorderSubtree(left(p), snapshot);
        if (right(p) != null)
            postorderSubtree(right(p), snapshot);
        snapshot.add(p);
    }

    /**
     * Returns an iterable collection of positions of the tree, reported in postorder.
     *
     * @return iterable collection of the tree's positions in postorder
     */
    public Iterable<Position<E>> postorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty())
            postorderSubtree(root(), snapshot);
        return snapshot;
    }

    public Iterable<Position<E>> positions() {

        return inorder();
    }

    /**
     * Returns the number of levels separating Position p from the root.
     *
     * @param p A valid Position within the tree
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public int depth(Position<E> p) throws IllegalArgumentException {
        if (isRoot(p)) {
            return 0;
        }
        else {
            return 1 + depth(parent(p));
        }
    }

    /**
     * Returns the height of the tree.
     * <p>
     * Note: This implementation works, but runs in O(n^2) worst-case time.
     */
    private int heightBad() {
        int h = 0;
        for (Position<E> p : positions()) {
            h = Math.max(h, depth(p));
        }
        return h;
    }

    /**
     * Returns the height of the subtree rooted at Position p.
     *
     * @param p A valid Position within the tree
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public int height(Position<E> p) throws IllegalArgumentException {
        int h = 0;
        for (Position<E> c : children(p)) {
            h = Math.max(h, 1 + height(c));
        }
        return h;
    }

    public int diameter(Position<E> p){
        if (p == null){
            return 0;
        }
        Position<E> leftChild = left(p);
        Position<E> rightChild = right(p);
        int leftHeight = (leftChild != null) ? height(leftChild) + 1 : 0;
        int rightHeight = (rightChild != null) ? height(rightChild) + 1: 0;
        int leftDiameter = (leftChild != null) ? diameter(leftChild) : 0;
        int rightDiameter = (rightChild != null) ? diameter(rightChild) : 0;
        return Math.max(leftHeight + rightHeight + 1, Math.max(leftDiameter, rightDiameter));
    }

//    public int[] rbtQ5 (){
//        int[] averages;
//        for(int i = 50; i <= 5000; i += 50){
//            int average = 0;
//            for(int j = 0; j < 50; j++){
//
//            }
//        }
//    }



    /**
     * Returns true if Position p represents the root of the tree.
     *
     * @param p A valid Position within the tree
     * @return true if p is the root of the tree, false otherwise
     */
    public boolean isRoot(Position<E> p) {
        return p == root();
    }
    // nonpublic utility

    /**
     * Returns true if Position p does not have any children.
     *
     * @param p A valid Position within the tree
     * @return true if p has zero children, false otherwise
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public boolean isExternal(Position<E> p) {
        Node<E> node = validate(p);
        return (node.left == null && node.right == null);
    }



    /**
     * Returns an iterable collection of the Positions representing p's children.
     *
     * @param p A valid Position within the tree
     * @return iterable collection of the Positions of p's children
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public Iterable<Position<E>> children(Position<E> p) {
        Node<E> node = validate(p);
        List<Position<E>> children = new ArrayList<>(2);

        if (node.getLeft() != null) {
            children.add(node.getLeft());
        }
        if (node.getRight() != null) {
            children.add(node.getRight());
        }

        return children;
    }

    /**
     * Returns the number of children of Position p.
     *
     * @param p A valid Position within the tree
     * @return number of children of Position p
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public int numChildren(Position<E> p) {
        Node<E> node = validate(p);
        int count = 0;
        if (node.getLeft() != null)
            count++;
        if (node.getRight() != null)
            count++;
        return count;
    }

    //function to find minimum value node in a given BST
    private Node<E> findMinimum(Node<E> n) {
        // TODO
        return null;
    }

    //function to find minimum value node in a given BST
    private Node<E> findMaximum(Node<E> n) {
        // TODO
        return null;
    }

    //recursive function to find an inorder successor
    private Node<E> inorderSuccessor(Node<E> node, Node<E> succ, E key) {
        // TODO
        return null;

    }

    private Node<E> inorderPredecessor(Node<E> node, Node<E> pred, E key) {
        // TODO
        return null;
    }

    public Position<E> inorderSuccessor(E key) {
        return inorderSuccessor(root, null, key);
    }

    public Position<E> inorderPredecessor(E key) {
        return inorderPredecessor(root, null, key);
    }


    /**
     * Returns an iterable collection of positions of the tree in breadth-first order.
     *
     * @return iterable collection of the tree's positions in breadth-first order
     */
    public Iterable<Position<E>> breadthfirst() {
        // TODO
        return null;
    }

    public void construct(E[] inorder, E[] preorder) {
        if (inorder == null || preorder == null || inorder.length != preorder.length){
            throw new IllegalArgumentException("Invalid input arrays");
        }
        this.root = construct_tree(inorder, preorder, 0, preorder.length - 1, 0, inorder.length - 1, null);
    }

    private Node<E> construct_tree(E[] inorder, E[] preorder, int pStart, int pEnd, int iStart, int iEnd, Node<E> parent) {
        //base case
        if (pStart > pEnd || iStart > iEnd){
            return null;
        }
        //first preorder element is root
        E rootVal = preorder[pStart];
        Node<E> root = new Node<>(rootVal, parent, null, null);

        int inorderIndex = -1;
        for (int i= iStart; i <= iEnd; i++) {
            if (inorder[i].equals(rootVal)) {
                inorderIndex = i;
                break;
            }
        }
        if (inorderIndex == -1) {
            throw new IllegalArgumentException("inconsistent inorder and preorder arrays");
        }

        int leftSubtreeSize = inorderIndex - iStart;
        root.left = construct_tree(inorder, preorder, pStart + 1, pStart + leftSubtreeSize, iStart, inorderIndex - 1, root);
        root.right = construct_tree(inorder, preorder, pStart + leftSubtreeSize + 1, pEnd, inorderIndex + 1, iEnd, root);

        return root;
    }



    /**
     * Factory function to create a new node storing element e.
     */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
        return new Node<E>(e, parent, left, right);
    }

    /**
     * Verifies that a Position belongs to the appropriate class, and is not one
     * that has been previously removed. Note that our current implementation does
     * not actually verify that the position belongs to this particular list
     * instance.
     *
     * @param p a Position (that should belong to this tree)
     * @return the underlying Node instance for the position
     * @throws IllegalArgumentException if an invalid position is detected
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node<E> node)) throw new IllegalArgumentException("Not valid position type");
        // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    /**
     * Returns the number of nodes in the tree.
     *
     * @return number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     *
     * @return root Position of the tree (or null if tree is empty)
     */
    public Position<E> root() {
        return root;
    }

    /**
     * Returns the Position of p's parent (or null if p is root).
     *
     * @param p A valid Position within the tree
     * @return Position of p's parent (or null if p is root)
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getParent();
    }

    /**
     * Returns the Position of p's left child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the left child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getLeft();
    }

    // update methods supported by this class

    /**
     * Returns the Position of p's right child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the right child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getRight();
    }

    /**
     * Places element e at the root of an empty tree and returns its new Position.
     *
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalStateException if the tree is not empty
     */
    public Position<E> addRoot(E e) throws IllegalStateException {
        if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
        this.root = createNode(e, null, null, null);
        this.size = 1;
        return this.root;
    }

    /*
     * Create a detached node!
     */
    public Position<E> add(E e, Position<E> parent, Position<E> left, Position<E> right) {
        // TODO
        return null;
    }

    /**
     * Creates a new left child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the left of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p already has a left child
     */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
        // check if p is not null
        // check if p does already have a left child
        Node<E> n = ((Node<E>) p);
        if (n.getLeft() != null) throw new IllegalStateException("already has a left child");
        Node<E> child = createNode(e, n, null, null);
        n.setLeft(child);
        this.size++;
        return child;

    }

    /**
     * Creates a new right child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the right of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p already has a right child
     */
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {

        Node<E> n = (Node<E>) p;
        if (n.getRight() != null)
            throw new IllegalStateException("already has a right child");

        Node<E> child = createNode(e, n, null, null);
        n.setRight(child);
        this.size++;
        return child;
    }

    /**
     * Replaces the element at Position p with element e and returns the replaced
     * element.
     *
     * @param p the relevant Position
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        // TODO
        Node<E> node = validate(p);
        E temp = node.getElement( );
        node.setElement(e);
        return temp;
    }

    public void setRoot(Position<E> e) throws IllegalArgumentException {
        // TODO
        Node<E> node = validate(e);
        root = node;
    }

    /**
     * Removes the node at Position p and replaces it with its child, if any.
     *
     * @param p the relevant Position
     * @return element that was removed
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p has two children.
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);  // ensure p is a Node
        if (numChildren(node) == 2)
            throw new IllegalArgumentException("p has two children");


        Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
        if (child != null)
            child.setParent(node.getParent());//grandparent becomes parent

        //if removing root
        if (node == root) {
            root = child;
        } else {
            Node<E> parent = node.getParent();
            if (node == parent.getLeft())
                parent.setLeft(child);
            else
                parent.setRight(child);
        }
        size--;

        E temp = node.getElement();
        node.setElement(null);
        node.setLeft(null);
        node.setRight(null);
        node.setParent(null);
        return temp;
    }




    public void createLevelOrder(ArrayList<E> l) {
        root = createLevelOrderHelper(l, root, 0);
    }

    private Node<E> createLevelOrderHelper(java.util.ArrayList<E> l, Node<E> parent, int i) {
        if (i < l.size()) {
            Node<E> n = createNode(l.get(i), parent, null, null);

            n.left = createLevelOrderHelper(l, n, 2 * i + 1);
            n.right = createLevelOrderHelper(l, n, 2 * i + 2);

            ++size;
            return n;
        }
        return null;
    }

    public void createLevelOrder(E[] arr) {
        root = createLevelOrderHelper(arr, root, 0);
    }

    private Node<E> createLevelOrderHelper(E[] arr, Node<E> parent, int i) {
        if (i < arr.length) {

            Node<E> n = createNode(arr[i], parent, null, null);
            n.left = createLevelOrderHelper(arr, n, 2 * i + 1);
            n.right = createLevelOrderHelper(arr, n, 2 * i + 2);
            ++size;
            return n;
        }
        return null;
    }

    public String toBinaryTreeString() {
        BinaryTreePrinter<E> btp = new BinaryTreePrinter<>(this);
        return btp.print();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(Position<E> p : positions()) {
            if(p.getElement() != null){
                sb.append(p.getElement());
                sb.append(", ");
            }
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();   };


    /*
     * Nested static class for a binary tree node.
     */
    protected static class Node<E> implements Position<E> {
        private E element;
        private Node<E> left, right, parent;

        public Node(E e, Node<E> p, Node<E> l, Node<E> r) {
            element = e;
            left = l;
            right = r;
            parent = p;
        }

        // accessor
        public E getElement() {
            return element;
        }

        // modifiers
        public void setElement(E e) {
            element = e;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> n) {
            left = n;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> n) {
            right = n;
        }

        public Node<E> getParent() {
            return parent;
        }

        public void setParent(Node<E> n) {
            parent = n;
        }

        public String toString() {
            // (e)
            StringBuilder sb = new StringBuilder();
            if (element == null) {
                sb.append("\u29B0");
            } else {
                sb.append(element);
            }
            // sb.append(" l:").append(left.element).append(" r:").append(right.element);
            // sb.append();
            return sb.toString();
        }
    }

    /* This class adapts the iteration produced by positions() to return elements. */
    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = positions().iterator();

        public boolean hasNext() {
            return posIterator.hasNext();
        }

        public E next() {
            return posIterator.next().getElement();
        }

        public void remove() {
            posIterator.remove();
        }
    }

}


