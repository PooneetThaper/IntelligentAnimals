/**
 * Created by Pthaper on 3/31/17.
 */
public class Queue<E> {
    private Node<E> head, base;
    private int length;

    public Queue() {
        base = new Node();
    }

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

    public boolean isEmpty() {
        return length == 0;
    }

    public int getLength() {
        return length;
    }

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
