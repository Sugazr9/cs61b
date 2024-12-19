package synthesizer;

public abstract class AbstractBoundedQueueElectricBoogaloo<T> implements BoundedQueueTake2<T>{
    protected int fillCount;
    protected int capacity;
    public int capacity() {
        return this.capacity;
    };
    public int fillCount() {
        return this.fillCount;
    };
    public boolean isEmpty() {
        return fillCount() == 0;
    };
    public boolean isFull() {
        return capacity() == fillCount();
    };
}
