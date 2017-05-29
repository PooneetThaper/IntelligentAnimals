/**
 * Node for the queue to be used for breadth first search
 * @param <E> The type that is stored in the Node
 */
public class Node<E> {
    /**
     * The data that is stored in this Node
     */
    private E data;

    /**
     * Reference to the next Node in the list (queue is a linked list).
     */
    private Node<E> next;

    /**
     * Reference to the previous Node in the list (queue is a linked list).
     */
    private Node<E> before;

    /**
     * Creates a Node with all the fields as null.
     */
    public Node() {
        data = null;
        next = null;
        before = null;
    }

    /**
     * Creates a Node with a given value.
     * @param value The value to be inserted into the node.
     */
    public Node(E value) {
        data = value;
        next = null;
        before = null;
    }

    /**
     * Getter function for data.
     * @return The data stored in this node.
     */
    public E getData() {
        return data;
    }

    /**
     * Setter function for data.
     * @param value Value to be stored as this Node's data.
     */
    public void setData(E value) {
        data = value;
    }

    /**
     * Getter function for next.
     * @return Reference to the next Node in the list.
     */
    public Node<E> getNext() {
        return next;
    }

    /**
     * Setter function for next.
     * @param input Reference to be stored as this Node's next.
     */
    public void setNext(Node<E> input) {
        this.next = input;
    }

    /**
     * Getter function for before.
     * @return Reference to the previous Node in the list.
     */
    public Node<E> getBefore() {
        return before;
    }

    /**
     * Setter function for before.
     * @param input Reference to be stored in this Node's before.
     */
    public void setBefore(Node<E> input) {
        this.before = input;
    }
}
