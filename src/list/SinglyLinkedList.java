package list;

import interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {
        // TODO

        public Node(E e, Node<E> n) {
            // TODO
        }

        // Accessor methods

    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)

    /**
     * The last node of the list
     */
    private Node<E> tail = null;               // last node of the list (or null if empty)

    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() { }              // constructs an initially empty list

    //@Override
    public int size() {
        return size;
    }

    //@Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        // TODO
        return null;
    }

    @Override
    public void add(int position, E e) {
        // TODO
    }


    @Override
    public void addFirst(E e) {
        // TODO
    }

    @Override
    public void addLast(E e) {
        // TODO
    }

    @Override
    public E remove(int position) {
        // TODO
        return null;
    }

    @Override
    public E removeFirst() {
        // TODO
        return null;
    }

    @Override
    public E removeLast() {
        // TODO
        return null;
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        @Override
        public boolean hasNext() {
            // TODO
            return false;
        }

        @Override
        public E next() {
            // TODO
            return null;
        }
        // TODO
    }

    public String toString() {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);
    }
}
