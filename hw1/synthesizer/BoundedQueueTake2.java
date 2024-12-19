package synthesizer;

public interface BoundedQueueTake2<T> {

    public int size();
    public int capacity();
    public int fillCount();
    public void enqueue(T x);
    public T dequeue();
    public T peek();
    public default boolean isEmpty() {
        return size() == 0;
    };
    public default boolean isFull() {
        return size() == capacity();
    };
}
