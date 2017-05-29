/**
 * Queue for use in breadth first search
 * @param <E> The type that is stored in this Queue
 */
public class Queue<E> {
    /**
     * The current top Node in this Queue
     */
    private Node<E> head;

    /**
     * A base Node to build the Queue on top of.
     */
    private Node<E> base;

    /**
     * Current length of this Queue.
     */
    private int length;

    /**
     * Creates Queue by initializing the base.
     */
    public Queue() {
        base = new Node();
    }

    /**
     * Creates a new Node of this Queue's type with given value and inserts that into the Queue.
     * @param item The value that is to be inserted into the Queue.
     */
    public void enqueue(E item) {
        Node n = new Node(item);
        if (base.getBefore() == null) {
            n.setNext(base);
            base.setBefore(n);
            head = n;
        } else {
            Node<E> last = base.getBefore();
            n.setNext(base);
            last.setNext(n);
            base.setBefore(n);
            n.setBefore(last);
        }
        length++;
    }

    /**
     * Pops the top Node from the Queue and returns it's value.
     * @return The value of the top Node in this Queue.
     */
    public E dequeue() {
        assert(length > 0);
        E retval = head.getData();
        Node<E> temp = head.getNext();
        head.setNext(null);
        temp.setBefore(null);
        head = temp;
        length--;
        return retval;
    }

    /**
     * Returns a boolean indicating if this Queue is empty or not.
     * @return Boolean indicating if this Queue is empty (true means empty).
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns the length of this Queue.
     * @return Int for length of this Queue.
     */
    public int getLength() {
        return length;
    }

    /**
     * Searches through the Queue to check if the given input is already contained in Queue.
     * @param object The value that is being searched for in this Queue.
     * @return Boolean to indicate whether this Queue contains the given value.
     */
    public boolean contains(E object) {
        if (length == 0) {
            return false;
        }
        for(Node<E> i = head; i != base; i = i.getNext()) {
            //For testing purposes (to see if i.getData() was working)
            //System.out.println((i.getData()));

            if ((i.getData()).equals(object)) {
                return true;
            }
        }
        return false;
    }
}
