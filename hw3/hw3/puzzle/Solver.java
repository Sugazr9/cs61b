package hw3.puzzle;

import java.util.*;

public class Solver {
    private ArrayList<WorldState> solved;

    public Solver(WorldState initial) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Node first = new Node(initial);
        queue.add(first);
        while(!queue.peek().state.isGoal()) {
            Node step = queue.remove();
            for (WorldState neighbor : step.state.neighbors()) {
                if (step.previous != null && !step.previous.state.equals(neighbor)) {
                    queue.add(new Node(neighbor, step, step.moves + 1));
                }
            }
        }
        Node end = queue.remove();
        WorldState[] temp = new WorldState[end.moves];
        for (int i = end.moves - 1; i > -1; i--) {
            temp[i] = end.state;
            end = end.previous;
        }
        solved = new ArrayList<>();
        Collections.addAll(solved, temp);
    }
    public int moves() {
        return solved.size();
    }
    public Iterable<WorldState> solution() {
        return (ArrayList<WorldState>) solved.clone();
    }

    private class Node implements Comparable{
        WorldState state;
        int moves;
        Node previous;
        int eta;
        int total;

        Node (WorldState step) {
            this(step, null, 0);
        }

        Node(WorldState step, Node prev, int steps) {
            state = step;
            moves = steps;
            previous = prev;
            eta = step.estimatedDistanceToGoal();
            total = eta + moves;
        }

        @Override
        public int compareTo(Object o) {
            if (o == null) {
                throw new NullPointerException();
            } else if (!(o instanceof Node)) {
                throw new ClassCastException();
            } else {
                Node comparing = (Node) o;
                return comparing.total - total;
            }
        }
    }
}
