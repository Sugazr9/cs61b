import java.util.Map;
import java.util.ArrayList;

public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader reader = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) reader.readObject();
        BitSequence bits = (BitSequence) reader.readObject();
        ArrayList<Character> chars = new ArrayList<>();
        while (bits.length() != 0) {
            Match curr = trie.longestPrefixMatch(bits);
            chars.add(curr.getSymbol());
            bits = bits.allButFirstNBits(curr.getSequence().length());
        }
        char[] toWrite = new char[chars.size()];
        for (int i = 0; i < toWrite.length; i++) {
            toWrite[i] = chars.get(i);
        }
        FileUtils.writeCharArray(args[1], toWrite);
    }
}
