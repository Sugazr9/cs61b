package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] storage;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        this.capacity = capacity;
        storage = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        fillCount = 0;
    }

    private int next(int index) {
        if (fillCount == 0) {
            return index;
        } else if (index == capacity - 1) {
            return 0;
        }
        return index + 1;
    }
    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (fillCount == capacity) {
            throw new RuntimeException("Ring Buffer Overflow");
        }
        storage[next(last)] = x;
        last = next(last);
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        T value = storage[first];
        storage[first] = null;
        fillCount -= 1;
        first = next(first);
        return value;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        if (fillCount == 0) {
            throw new RuntimeException("Nothing in buffer!");
        }
        return storage[first];
    }

    private class myIterator<T> implements Iterator<T> {
        int current = first;
        public T next() {
            T result = (T) storage[current];
            if (fillCount == 0) {
            } else if (current == capacity - 1) {
                current = 0;
            } else {
                current++;
            }
            return result;
        }
        public boolean hasNext() {
            return current != last;
        }
    }

    public Iterator<T> iterator() {
        return new myIterator<T>();
    }

}
