package synthesizer;

/**
 * Created by Arvind on 2/17/2017.
 */
public interface BoundedQueue<T> {
    int capacity();    // max size of queue
    int fillCount();  //size of queue
    void enqueue(T x);   //add item to end
    T dequeue();   // remove first item and return
    T peek();  //return value of first
    default boolean isEmpty() {
        return fillCount() == 0;
    }
    default boolean isFull() {
        return fillCount() == capacity();
    }
}
