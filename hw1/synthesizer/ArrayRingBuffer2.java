package synthesizer;

public class ArrayRingBuffer2<T> extends AbstractBoundedQueueElectricBoogaloo<T>{

    private final T[] ring_buffer;
    private int first;
    private int last;

    public ArrayRingBuffer2(int total_size) {
        ring_buffer = (T[]) new Object[total_size];
        first = 0;
        last = 0;
        capacity = total_size;
        fillCount = 0;
    }
    private int increment(int i) {
        if (i != capacity - 1) {
            return i + 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        else {
            T val = ring_buffer[first];
            ring_buffer[first] = null;
            if (size() != 1) {first = increment(first);}
            fillCount -= 1;
            return val;
        }
    }

    @Override
    public T peek() {
        return ring_buffer[first];
    }

    @Override
    public void enqueue(T x) {
        int new_last;
        if (isFull()) {
            return;
        }
        else if (size() == 0) {
            new_last = last;
        }
        else {
            new_last = increment(last);
        }
        ring_buffer[new_last] = x;
        last = new_last;
        fillCount += 1;
    }

    public int size() {
        return fillCount;
    }

}
