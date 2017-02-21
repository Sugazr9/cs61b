package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        //ArrayRingBuffer arb = new ArrayRingBuffer(10);
        ArrayRingBuffer<Integer> ha = new ArrayRingBuffer<>(4);
        ha.enqueue(4);
        double result = ha.dequeue();
        assertEquals(4.0, result, 0.0);
        ha.enqueue(4);
        ha.enqueue(3);
        ha.dequeue();
        ha.enqueue(2);
        ha.enqueue(1);
        ha.enqueue(0);
        ha.dequeue();
        ha.dequeue();
        ha.dequeue();
        result = ha.dequeue();
        assertEquals(0.0, result, 0.0);
        ha.dequeue();
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
