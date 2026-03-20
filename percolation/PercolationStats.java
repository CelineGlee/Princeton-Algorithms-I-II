import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    /** perform independent trials on an n-by-n grid */
    private int numOfExperiments;
    private double[] resultRecords;
    private final double meanValue;
    private final double stdValue;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n or trails are out of index");
        }

        numOfExperiments = trials;
        resultRecords = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }

            int numOfOpens = percolation.numberOfOpenSites();
            resultRecords[i] = (double) numOfOpens / (n * n);


        }
        this.meanValue = StdStats.mean(resultRecords);
        this.stdValue = StdStats.stddev(resultRecords);
    }

    /** sample mean of percolation threshold */
    public double mean() {
        return meanValue;
    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        return stdValue;
    }


    /** low endpoint of 95% confidence interval */
    public double confidenceLo() {
        return meanValue - ((1.96 * stdValue) / Math.sqrt(numOfExperiments));
    }

    /** high endpoint of 95% confidence interval */
    public double confidenceHi() {
        return meanValue + ((1.96 * stdValue) / Math.sqrt(numOfExperiments));
    }

    /** test client (see below) */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, trails);
        System.out.printf("%-24s = %f%n", "mean", percolationStats.mean());
        System.out.printf("%-24s = %f%n", "stddev", percolationStats.stddev());
        System.out.printf("%-24s = [%f, %f]%n", "95%% confidence interval",
                          percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
