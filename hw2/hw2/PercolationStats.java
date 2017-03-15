package hw2;                       

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    int[] values;
    double average;
    double sd;
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        average = 0.0;
        sd = 0.0;
        values = new int[T];
        while (T != 0) {
            Percolation trial = new Percolation(N);
            int times = 0;
            while (!trial.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                while (trial.isOpen(row, col)) {
                    row = StdRandom.uniform(N);
                    col = StdRandom.uniform(N);
                }
                trial.open(row, col);
                times++;
            }
            values[T - 1] = times;
            T--;
        }
    }

    public double mean() {
        if (average == 0.0) {
            int n = values.length;
            int sum = 0;
            for (int x : values) {
                sum += x;
            }
            average = sum / n;
        }
        return average;
    }


    public double stddev() {
        if (sd == 0.0) {
            int n = values.length - 1;
            if (n == 0) {
                sd = Double.NaN;
            } else {
                double sum = 0.0;
                for (int x : values) {
                    sum += Math.pow(x - this.mean(), 2);
                }
                sd = Math.pow(sum / n, 0.5);
            }
        }
        return sd;
    }

    public double confidenceLow() {
        return this.mean() - (1.96 * this.stddev() / Math.pow(values.length, 0.5));
    }

    public double confidenceHigh() {
        return this.mean() + (1.96 * this.stddev() / Math.pow(values.length, 0.5));
    }
}                       
