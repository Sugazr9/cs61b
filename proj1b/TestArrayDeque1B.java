/**
 * Created by Arvind on 2/4/2017.
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {

    @Test
    public void randomTest() {
        StudentArrayDeque student = new StudentArrayDeque<Integer>();
        ArrayDequeSolution solution = new ArrayDequeSolution();
        OperationSequence log = new OperationSequence();
        for (int i = 0; i <= 250; i++) {
            if (solution.size() == 0) {
                int inserted = StdRandom.uniform(101);
                if (StdRandom.uniform() < 0.5) {
                    log.addOperation(new DequeOperation("addFirst", inserted));
                    solution.addFirst(inserted);
                    student.addFirst(inserted);
                } else {
                    log.addOperation(new DequeOperation("addLast", inserted));
                    student.addLast(inserted);
                    solution.addLast(inserted);
                }
            } else {
                double random = StdRandom.uniform();
                if (random < 0.25) {
                    int inserted = StdRandom.uniform(9);
                    log.addOperation(new DequeOperation("addLast", inserted));
                    student.addLast(inserted);
                    solution.addLast(inserted);
                } else if (random < 0.5) {
                    int inserted = StdRandom.uniform(14);
                    log.addOperation(new DequeOperation("addFirst", inserted));
                    solution.addFirst(inserted);
                    student.addFirst(inserted);
                } else if (random < 0.75) {
                    log.addOperation(new DequeOperation("removeFirst"));
                    assertEquals(log.toString(), solution.removeFirst(), student.removeFirst());
                } else {
                    log.addOperation(new DequeOperation("removeLast"));
                    assertEquals(log.toString(), solution.removeLast(), student.removeLast());
                }
            }
        }

    }
}
