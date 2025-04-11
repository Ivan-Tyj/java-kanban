package manager;

public class Node<E> {
    protected E item;
    protected Node<E> next;
    protected Node<E> prev;

    protected Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}