package hw3.puzzle;

import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[][] current;
    private final int N;

    public Board(int[][] tiles) {
        N = tiles.length;
        current = new int[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                current[row][col] = tiles[row][col];
            }
        }
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i > N - 1 || j < 0 || j > N - 1) {
            throw new IndexOutOfBoundsException();
        }
        return current[i][j];
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int misplaced = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!(i == N - 1 && j == N - 1)) {
                    if (current[i][j] != N * i + j + 1) {
                        misplaced++;
                    }
                }
            }
        }
        return misplaced;
    }

    public int manhattan() {
        int total = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int val = current[i][j];
                if (val != 0 && val != N * i + j + 1) {
                    int row = (val - 1) / N;
                    int col = (val - 1) % N;
                    int distance = Math.abs(i - row) + Math.abs(j - col);
                    total += distance;
                }
            }
        }
        return total;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!(i == N - 1 && j == N - 1)) {
                    if (current[i][j] != N * i + j + 1) {
                        return false;
                    }
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
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
