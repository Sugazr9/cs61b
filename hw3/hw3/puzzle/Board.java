package hw3.puzzle;

import java.util.Arrays;

public class Board implements WorldState{
    private int[][] current;
    private final int N;
    public Board(int[][] tiles) {
        current = tiles.clone();
        N = tiles.length;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i > N -1 || j < 0 || j > N -1) {
            throw new IndexOutOfBoundsException();
        }
        return current[i][j];
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {

    }

    public int hamming() {

    }

    public int manhattan() {

    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(i == N -1 && j == N - 1) {
                    break;
                }
                if(current[i][j] != N * i + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (!(y instanceof Board)) {
            return false;
        }
        Board compare = (Board) y;
        return Arrays.deepEquals(compare.current, current);
    }

    // Returns the string representation of the board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
