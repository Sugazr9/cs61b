package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    boolean[][] grid;
    int size;
    int opened;
    WeightedQuickUnionUF connected;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new boolean[N][N];
        size = N;
        opened = 0;
        connected = new WeightedQuickUnionUF(N * N + 2);
    }

    private void checkArgs(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    public void open(int row, int col) {
        checkArgs(row, col);
        if (!grid[row][col]) {
            grid[row][col] = true;
            opened++;
            int oneD = rcTo1D(row, col);
            try {
                if (isOpen(row - 1, col)) {
                    connected.union(oneD, rcTo1D(row - 1, col));
                }
            } catch (IndexOutOfBoundsException e) {
                connected.union(oneD, size * size);
            }
            try {
                if (isOpen(row + 1, col)) {
                    connected.union(oneD, rcTo1D(row + 1, col));
                }
            } catch (IndexOutOfBoundsException e) {
                connected.union(oneD, size * size + 1);
            }
            try {
                if (isOpen(row, col + 1)) {
                    connected.union(oneD, rcTo1D(row, col + 1));
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (isOpen(row, col - 1)) {
                    connected.union(oneD, rcTo1D(row, col - 1));
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }
    }

    private int rcTo1D(int row, int col) {
        checkArgs(row, col);
        return row*size + col;
    }

    public boolean isOpen(int row, int col) {
        checkArgs(row, col);
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        checkArgs(row, col);
        return connected.connected(rcTo1D(row, col), size * size);
    }

    public int numberOfOpenSites() {
        return opened;

    }

    public boolean percolates() {
        return connected.connected(size * size, size * size + 1);
    }

    public static void main(String[] args) {
        Percolation a = new Percolation(9);
    }
}
