import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char x : inputSymbols) {
            int val = 1;
            if (freq.containsKey(x)) {
                val += freq.get(x);
            }
            freq.put(x, val);
        }
        return freq;
    }

    public static void main(String[] args) {
        ObjectWriter writer = new ObjectWriter(args[0] + ".huf");
        char[] sequence = FileUtils.readFile(args[0]);
        Map<Character, Integer> freq = buildFrequencyTable(sequence);
        BinaryTrie trie = new BinaryTrie(freq);
        writer.writeObject(trie);
        Map<Character, BitSequence> look = trie.buildLookupTable();
        LinkedList<BitSequence> bits = new LinkedList<>();
        for (char x : sequence) {
            bits.add(look.get(x));
        }
        BitSequence zipped = BitSequence.assemble(bits);
        writer.writeObject(zipped);
    }
}