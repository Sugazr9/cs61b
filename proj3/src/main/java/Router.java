import java.util.LinkedList;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                                double destlon, double destlat) {
        long start = g.closest(stlon, stlat);
        long end = g.closest(destlon, destlat);
        ArrayList<Long> visited = new ArrayList<>();
        PriorityQueue<Node> a = new PriorityQueue<>();
        Node starter = new Node(null, start, 0, g.distance(start, end));
        a.add(starter);
        while (a.peek().current != end) {
            Node curr = a.remove();
            visited.add(curr.current);
            for (Long v : g.adjacent(curr.current)) {
                if (!visited.contains(v)) {
                    double trekked = curr.trekked + g.distance(curr.current, v);
                    Node vertex = new Node(curr, v, trekked, g.distance(v, end));
                    a.add(vertex);
                }
            }
        }
        Node result = a.remove();
        LinkedList<Long> order = new LinkedList<>();
        while (result != null) {
            order.addFirst(result.current);
            result = result.previous;
        }
        return order;
    }

    private static class Node implements Comparable {
        Node previous;
        long current;
        double trekked;
        double total;

        Node(Node prev, long curr, double done, double left) {
            previous = prev;
            current = curr;
            trekked = done;
            total = left + done;
        }

        @Override
        public int compareTo(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            if (getClass() != o.getClass()) {
                throw new ClassCastException();
            }
            Node compare = (Node) o;
            if (total < compare.total) {
                return -1;
            } else if (total == compare.total) {
                return 0;
            }
            return 1;
        }
    }
}
