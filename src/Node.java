/**
 * Created by Pthaper on 3/31/17.
 */
public class Node<E> {
    private E data;
    private Node<E> next;
    private Node<E> before;

    public Node(){
        data = null;
        next = null;
        before = null;
    }

    public Node(E value){
        data = value;
        next = null;
        before = null;
    }

    public E getData(){
        return data;
    }

    public void setData(E value){
        data = value;
    }

    public Node<E> getNext(){
        return next;
    }

    public void setNext(Node<E> input){
        this.next = input;
    }

    public Node<E> getBefore(){
        return before;
    }

    public void setBefore(Node<E> input){
        this.before = input;
    }

}
