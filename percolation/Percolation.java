import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF percolateUnion;
    private WeightedQuickUnionUF fullCheckPercolateUnion;
    private int count;
    private final int virtualTopIndex;
    private final int virtualBottomIndex;
    private final int length;
    private boolean[] openedSites;

    /** Initialization */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is out of index");
        }

        this.virtualTopIndex = n * n;
        this.virtualBottomIndex = n * n + 1;
        this.length = n;

        percolateUnion = new WeightedQuickUnionUF(n * n + 2);
        fullCheckPercolateUnion = new WeightedQuickUnionUF(n * n + 1);

        openedSites = new boolean[n * n];
    }

    /** Helper function: validate row and column boundary */
    private void validate(int row, int col) {
        if (row < 1 || row > length || col < 1 || col > length) {
            throw new IllegalArgumentException("row and column is out of index");
        }
    }

    /** Helper function: Give each site's 2-d axis in the 1-d grid with index */
    private int getIndex(int row, int col) {
        int index = (row - 1) * length + (col - 1);
        return index;
    }

    /**
     * opens the site (row, col) if it is not open already
     * open first, then check its neighbours
     */
    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) {
            return;
        }

        int index = getIndex(row, col);

        /** mark it as opened */
        openedSites[index] = true;
        count++;

        if (row == 1) {
            percolateUnion.union(index, virtualTopIndex);
            fullCheckPercolateUnion.union(index, virtualTopIndex);
        }

        if (row == length) {
            percolateUnion.union(index, virtualBottomIndex);
        }

        /** check the current opened site's neighbors */
        checkNeighbor(row, col);
    }

    /** Helper Function: Check four direction neighbor connections, is they are opened */
    private void checkNeighbor(int row, int col) {
        validate(row, col);
        int currentIndex = getIndex(row, col);

        int currentTopIndex = getIndex(row - 1, col);
        int currentBtmIndex = getIndex(row + 1, col);
        int currentLeftIndex = getIndex(row, col - 1);
        int currentRightIndex = getIndex(row, col + 1);


        if (row > 1 && isOpen(row - 1, col)) {
            percolateUnion.union(currentIndex, currentTopIndex);
            fullCheckPercolateUnion.union(currentIndex, currentTopIndex);
        }

        if (row < length && isOpen(row + 1, col)) {
            percolateUnion.union(currentIndex, currentBtmIndex);
            fullCheckPercolateUnion.union(currentIndex, currentBtmIndex);
        }

        if (col > 1 && isOpen(row, col - 1)) {
            percolateUnion.union(currentIndex, currentLeftIndex);
            fullCheckPercolateUnion.union(currentIndex, currentLeftIndex);
        }

        if (col < length && isOpen(row, col + 1)) {
            percolateUnion.union(currentIndex, currentRightIndex);
            fullCheckPercolateUnion.union(currentIndex, currentRightIndex);
        }
    }


    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int index = getIndex(row, col);
        return openedSites[index];
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int index = getIndex(row, col);

        if (isOpen(row, col)) {
            if (row == 1) {
                return true;
            }
            return fullCheckPercolateUnion.find(index) == fullCheckPercolateUnion.find(
                    virtualTopIndex);
        }

        return false;
    }

    /** returns the number of open sites */
    public int numberOfOpenSites() {
        return count;
    }

    /**
     * does the system percolate?
     * percolation happened when virtual top site is connected with virtual bottom site
     */
    public boolean percolates() {
        return percolateUnion.find(virtualTopIndex) == percolateUnion.find(virtualBottomIndex);
    }

    /** test client (optional) */
    public static void main(String[] args) {
        Percolation perco = new Percolation(5);
        perco.open(1, 3);
        perco.open(2, 5);
    }
}