package hw4.hash;

import java.util.List;
import java.util.LinkedList;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        LinkedList[] boxes = new LinkedList[M];
        int N = oomages.size();
        for (int i = 0; i < boxes.length; i++) {
            boxes[i]= new LinkedList<Oomage>();
        }
        for (Oomage o : oomages) {
            int boxNum = (o.hashCode() & 0x7FFFFFFF) % M;
            boxes[boxNum].addFirst(o);
        }
        for (LinkedList box : boxes) {
            if (box.size() < N / 50 || box.size() > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
