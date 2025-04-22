package list;

import interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E>, Iterable<E> {

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods
        public E getElement() {
            return element;
        }
        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> n) {
            next = n;
        }

    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)

    /**
     * The last node of the list
     */
    //private Node<E> tail = null;               // last node of the list (or null if empty)

    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() { }              // constructs an initially empty list

    //@Override
    public int size() {return size;}

    //@Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        Node<E> curr = head;

        for(int i = 0; i < position; i++) {
            curr = curr.getNext();
        }
        return curr.getElement();
    }

    @Override
    public void add(int position, E e) {
        // TODO
        Node <E> curr = head;
        for(int i = 0; i < position - 1; i++) {
            curr = curr.getNext();
        }

        Node<E> next = curr.getNext();

        Node<E> newNode = new Node(e, next);
        curr.setNext(newNode);
        size++;
    }


    @Override
    public void addFirst(E e) {
        // TODO
        Node<E> newest = new Node(e, head);
        head = newest;
        size++;
    }

    @Override
    public void addLast(E e) {
        // TODO
        Node<E> newest = new Node(e, null);
        Node<E> last = head;
        if(last == null){
            head = newest;
        }
        else{
            while(last.getNext()!=null){
                last = last.getNext();
            }
            last.setNext(newest);
        }
        size++;
    }

    @Override
    public E remove(int position) {
        // TODO
        Node<E> curr = head;
        for(int i = 0; i < position - 1; i++) {
            curr = curr.getNext();
        }

        curr.setNext(curr.getNext().getNext());
        size--;

        return null;
    }

    @Override
    public E removeFirst() {
        // TODO
        if(head.getNext() != null){
            head = head.getNext();
        }
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
        Node curr;
        public SinglyLinkedListIterator() {
            curr = head;
        }
        public boolean hasNext() {
            return curr != null;
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
