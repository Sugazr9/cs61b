/**
 * Created by Arvind on 1/31/2017.
 */
/** Performs some basic array tests. */
public class ArrayDequeTest {

    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static boolean checkSize(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Prints a nice message based on whether a test passed. 
     * The \n means newline. */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    /** Adds a few things to the array, checking isEmpty() and size() are correct, 
     * finally printing the results. 
     *
     * && is the "and" operation. */
    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        ArrayDequeImproved<Integer> lld1 = new ArrayDequeImproved<Integer>();

        boolean passed = checkEmpty(true, lld1.isEmpty());



        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        lld1.addFirst(2);
        passed = checkSize(1, lld1.size()) && passed;
        passed = checkEmpty(false, lld1.isEmpty()) && passed;


        lld1.removeLast();
        passed = checkEmpty(true, lld1.isEmpty()) && passed;
        lld1.addLast(4);
        lld1.removeFirst();
        passed = checkEmpty(true, lld1.isEmpty()) && passed;
        lld1.addFirst(8);
        lld1.addLast(4);
        lld1.addFirst(10);
        lld1.addLast(11);
        lld1.addLast(3);
        for (int i = 0; i < 40; i++) {
            if (i % 2 != 0) {
                lld1.addFirst(i * 10);
            }
            else {
                lld1.addLast(i*10);
            }
        }
        for (int i = 0; i < 40; i++) {
            if (i % 2 != 0) {
                lld1.removeLast();
            }
            else {
                lld1.removeFirst();
            }
        }
        passed = checkSize(5, lld1.size()) && passed;
        lld1.printDeque();

        System.out.println(lld1.get(2));
        printTestStatus(passed);
    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addIsEmptySizeTest();

    }
} 