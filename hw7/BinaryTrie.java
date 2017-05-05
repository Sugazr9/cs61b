import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.Map.Entry;

public class BinaryTrie implements Serializable {
    Node home;

    private class Node implements Comparable, Serializable{
        Node zero;
        Node one;
        int freq;
        Character car;

        Node(Character key, Integer value) {
            freq = value;
            car = key;
            zero = null;
            one = null;
        }

        Node(Node first, Node second) {
            zero = first;
            one = second;
            car = null;
            freq = first.freq + second.freq;
        }

        @Override
        public int compareTo(Object o) {
            if (o == null) {
                return 1;
            } else if (o.getClass() != this.getClass()) {
                return 0;
            } else {
                Node curr = (Node) o;
                return freq - curr.freq;
            }
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (Entry x : frequencyTable.entrySet()) {
            queue.add(new Node((Character) x.getKey(), (Integer) x.getValue()));
        }
        while (queue.size() > 1) {
            Node first = queue.remove();
            Node second = queue.remove();
            queue.add(new Node(first, second));
        }
        home = queue.peek();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node curr = home;
        BitSequence prefix = new BitSequence();
        while (curr.car == null) {
            if (querySequence.bitAt(0) == 0) {
                curr = curr.zero;
                prefix = prefix.appended(0);
            } else {
                curr = curr.one;
                prefix = prefix.appended(1);
            }
            if (querySequence.length() == 1) {
                break;
            }
            querySequence = querySequence.allButFirstNBits(1);
        }
        if (curr.car == null) {
            return null;
        }
        return new Match(prefix, curr.car);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> result = new HashMap<>();
        helper(result, home, new BitSequence());
        return result;
    }

    private void helper(Map<Character, BitSequence> map, Node curr, BitSequence bits) {
        if(curr.car != null) {
            map.put(curr.car, bits);
        } else {
            helper(map, curr.one, bits.appended(1));
            helper(map, curr.zero, bits.appended(0));
        }
    }
}