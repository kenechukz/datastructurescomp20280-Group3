package list;

import interfaces.List;

import java.util.Iterator;
import java.util.LinkedList;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        //TODO

        public Node(E e, Node<E> p, Node<E> n) {
            //TODO
        }


    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        //TODO
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        //TODO
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        //TODO
        return null;
    }

    @Override
    public void add(int i, E e) {
        //TODO
    }

    @Override
    public E remove(int i) {
        //TODO
        return null;
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
        // TODO
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {
        //TODO
        return null;
    }

    public E first() {
        //TODO
        return null;
    }

    public E last() {
        //TODO
        return null;
    }

    @Override
    public E removeFirst() {
        //TODO
        return null;
    }

    @Override
    public E removeLast() {
        //TODO
        return null;
    }

    @Override
    public void addLast(E e) {
        //TODO

    }

    @Override
    public void addFirst(E e) {
        // TODO

    }

    public String toString() {
        // TODO
        return null;
    }

    public void reverseInplace() {
        // TODO
    }

    public static void main(String [] args) {
        Integer [] arr = {1,2,3,4,5,6,7,8,9};
        DoublyLinkedList<Integer> dl = new DoublyLinkedList<>();
        for(Integer i : arr) dl.addLast(i);
        System.out.println("forward list: " + dl);
        dl.reverseInplace();
        System.out.println("reverse list: " + dl);
    }
}
