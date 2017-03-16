package hw2;                       

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    double[] values;
    double average;
    double sd;
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        average = 0.0;
        sd = 0.0;
        values = new double[T];
        while (T != 0) {
            Percolation trial = new Percolation(N);
            double times = 0;
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
            values[T - 1] += times / (N * N);
            T--;
        }
    }

    public double mean() {
        if (average == 0.0) {
            int n = values.length;
            double sum = 0.0;
            for (double x : values) {
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
                for (double x : values) {
                    sum += Math.pow(x - mean(), 2);
                }
                sd = Math.pow(sum / n, 0.5);
            }
        }
        return sd;
    }

    public double confidenceLow() {
        return this.mean() - (1.96 * stddev() / Math.pow(values.length, 0.5));
    }

    public double confidenceHigh() {
        return this.mean() + (1.96 * stddev() / Math.pow(values.length, 0.5));
    }
}                       
