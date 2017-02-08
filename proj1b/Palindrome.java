/**
 * Created by Arvind on 2/5/2017.
 */
public class Palindrome {

    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> a = new ArrayDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            a.addLast(word.charAt(i));
        }
        return a;
    }
    private static String toString(Deque word) {
        String result = "";
        while (word.size() != 0) {
            result += word.removeFirst();
        }
        return result;
    }
    public static boolean isPalindrome(String word) {
        if (word.length() <= 1) {
            return true;
        } else {
            Deque<Character> a = wordToDeque(word);
            if (a.removeFirst() == a.removeLast()) {
                String updated = toString(a);
                return isPalindrome(updated);
            } else {
                return false;
            }
        }
    }
    public static boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() <= 1) {
            return true;
        } else {
            Deque<Character> a = wordToDeque(word);
            if (cc.equalChars(a.removeFirst(), a.removeLast())) {
                String updated = toString(a);
                return isPalindrome(updated);
            } else {
                return false;
            }
        }
    }
}
