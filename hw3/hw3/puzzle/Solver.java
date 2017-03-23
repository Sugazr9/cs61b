package hw3.puzzle;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private ArrayList<WorldState> solved;

    public Solver(WorldState initial) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Node first = new Node(initial);
        queue.add(first);
        while (!queue.peek().state.isGoal()) {
            Node step = queue.remove();
            for (WorldState neighbor : step.state.neighbors()) {
                if (step.previous == null) {
                    queue.add(new Node(neighbor, step, step.moves + 1));
                } else if (!step.previous.state.equals(neighbor)) {
                    queue.add(new Node(neighbor, step, step.moves + 1));
                }
            }
        }
        Node end = queue.remove();
        WorldState[] temp = new WorldState[end.moves + 1];
        for (int i = end.moves; i > -1; i--) {
            temp[i] = end.state;
            end = end.previous;
        }
        solved = new ArrayList<>();
        Collections.addAll(solved, temp);
    }

    public int moves() {
        return solved.size() - 1;
    }

    public Iterable<WorldState> solution() {
        return (ArrayList<WorldState>) solved.clone();
    }

    private class Node implements Comparable {
        WorldState state;
        int moves;
        Node previous;
        int eta;

        Node(WorldState step) {
            this(step, null, 0);
        }

        Node(WorldState step, Node prev, int steps) {
            state = step;
            previous = prev;
            moves = steps;
            eta = step.estimatedDistanceToGoal() + moves;
        }

        @Override
        public int compareTo(Object o) {
            if (o == null) {
                throw new NullPointerException();
            } else if (!(o instanceof Node)) {
                throw new ClassCastException();
            } else {
                Node comparing = (Node) o;
                return comparing.eta - eta;
            }
        }
    }
}
