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
        while (true) {
            if (solution == null) {
                int inserted = StdRandom.uniform(101);
                if (StdRandom.uniform() < 0.5) {
                    solution.addFirst(inserted);
                    student.addFirst(inserted);
                }
                else {
                    student.addLast(inserted);
                    solution.addLast(inserted);
                }
            } else {
                double random = StdRandom.uniform();
                if (random < 0.25) {
                    int inserted = StdRandom.uniform(9);
                    student.addLast(inserted);
                    solution.addLast(inserted);
                } else if (random < 0.5) {
                    int inserted = StdRandom.uniform(14);
                    solution.addFirst(inserted);
                    student.addFirst(inserted);
                } else if (random < 0.75) {
                    assertEquals(solution.removeFirst(), student.removeFirst());
                } else {
                    assertEquals(solution.removeLast(), student.removeLast());
                }
            }
        }

    }
}
