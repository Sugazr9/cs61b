/**
 * Created by Arvind on 2/6/2017.
 */
public class OffByOne implements CharacterComparator{
    @Override
    public boolean equalChars(char x, char y) {
         if (Math.abs(x - y) == 1) {
             return true;
         }
         return false;
    }
}
