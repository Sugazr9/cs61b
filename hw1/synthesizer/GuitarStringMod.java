package synthesizer;
//package <package name>;

//Make sure this class is public
public class GuitarStringMod {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private final BoundedQueueTake2<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarStringMod(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer2<>(capacity);
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        for (int i = 0; i < buffer.fillCount(); i++) {
            buffer.dequeue();
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.enqueue(Math.random() - 0.5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double first = buffer.dequeue();
        double second = buffer.peek();
        double new_val = DECAY * 0.5 * (first + second);
        buffer.enqueue(new_val);
    }

    // Return the double at the front of the buffer.
    public double sample() {
        return buffer.peek();
    }
}
