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
        String[] result = new String[asciis.length];
        System.arraycopy(asciis, 0, result, 0, start);
        int k = start;
        for (int i = start; i < asciis.length; i++) {
            String curr = asciis[i];
            if (curr.length() < start + 1) {
                result[k] = curr;
                k++;
            }
        }
        if (k > end) {
            return;
        }
        int newStart = k;
        for (int i = 0; i < 256; i++) {
            for (String x : asciis) {
                if (x.length() > index && (int) x.charAt(index) == i) {
                    result[k] = x;
                    k++;
                }
            }
        }
        asciis = result;
        sortHelper(asciis, newStart, end, index + 1);
    }
}
