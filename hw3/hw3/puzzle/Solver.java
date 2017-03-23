package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solver {
    private ArrayList<WorldState> solved;

    public Solver(WorldState initial) {
        MinPQ<Node> queue = new MinPQ<>(new NodeComparator());
        Node first = new Node(initial);
        queue.insert(first);
        while (!queue.min().state.isGoal()) {
            Node step = queue.delMin();
            for (WorldState neighbor : step.state.neighbors()) {
                if (step.previous == null) {
                    queue.insert(new Node(neighbor, step, step.moves + 1));
                } else if (!step.previous.state.equals(neighbor)) {
                    queue.insert(new Node(neighbor, step, step.moves + 1));
                }
            }
        }
        Node end = queue.delMin();
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

    private class Node {
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
    }

    private class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            if (o1 == o2) {
                return 0;
            } else if (o1.eta < o2.eta) {
                return -1;
            } else if (o1.eta > o2.eta) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
