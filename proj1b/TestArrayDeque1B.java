/**
 * Created by Arvind on 2/4/2017.
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {

    @Test
    public void RandomArrayTest() {
        StudentArrayDeque student = new StudentArrayDeque<Integer>();
        ArrayDequeSolution solution = new ArrayDequeSolution();
        OperationSequence log = new OperationSequence();
        for(int i = 0; i <= 250; i++) {
            if (solution.size() == 0) {
                int inserted = StdRandom.uniform(101);
                if (StdRandom.uniform() < 0.5) {
                    log.addOperation(new DequeOperation("addFirst", inserted));
                    solution.addFirst(inserted);
                    student.addFirst(inserted);
                    assertEquals(log.toString(), solution.get(0), student.get(0));
                }
                else {
                    log.addOperation(new DequeOperation("addLast", inserted));
                    student.addLast(inserted);
                    solution.addLast(inserted);
                    assertEquals(log.toString(), solution.get(0), student.get(0));
                }
            } else {
                double random = StdRandom.uniform();
                if (random < 0.25) {
                    int inserted = StdRandom.uniform(9);
                    log.addOperation(new DequeOperation("addLast", inserted));
                    student.addLast(inserted);
                    solution.addLast(inserted);
                    assertEquals(log.toString(), solution.get(solution.size()-1), student.get(solution.size()-1));
                } else if (random < 0.5) {
                    int inserted = StdRandom.uniform(14);
                    log.addOperation(new DequeOperation("addFirst", inserted));
                    solution.addFirst(inserted);
                    student.addFirst(inserted);
                    assertEquals(log.toString(), solution.get(0), student.get(0));
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
