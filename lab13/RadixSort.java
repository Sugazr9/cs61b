/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis)
    {
        String[] result = asciis.clone();
        sortHelper(result, 0, result.length - 1, 0);
        return result;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        if (start >= end) {
            return;
        }
        int[] count = new int[267];
        for (int i = start; i < end; i++) {
            String x = asciis[i];
            if (x.length() < index + 1) {
                count[0] += 1;
            } else {
                count[(int) x.charAt(index) + 1] += 1;
            }
        }
        for (int i = 1; i < 267; i++) {
            count[i] += count[i - 1];
        }
        String[] result = new String[asciis.length];
        System.arraycopy(asciis, 0, result, 0, start);
        int newStart = count[0] + start;
        for (int j = start; j < end; j ++) {
            String s = asciis[j];
            if (s.length() < index + 1) {
                count[0]--;
                result[count[0] + start] = s;
            } else {
                int n = (int) s.charAt(index);
                count[n + 1]--;
                result[count[n + 1] + start] = s;
            }
        }
        System.arraycopy(result, 0, asciis, 0, result.length);
        sortHelper(asciis, newStart, end, index + 1);
    }
}
