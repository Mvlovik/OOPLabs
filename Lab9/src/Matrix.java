import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private final double[][] data;

    public Matrix(double[][] data) {
        this.data = data;
    }

    public static Matrix random(int rows, int cols) {
        Random r = new Random();
        double[][] d = new double[rows][cols];
        for (double[] row : d)
            for (int j = 0; j < row.length; j++)
                row[j] = r.nextInt(20) - 10;
        return new Matrix(d);
    }

    public int rows() { return data.length; }
    public int cols() { return data[0].length; }

    public void print() {
        for (double[] row : data) {
            StringBuilder sb = new StringBuilder();
            for (double v : row) sb.append(String.format("%6.0f", v));
            System.out.println(sb);
        }
    }

    public Matrix add(Matrix o) {
        check(o);
        double[][] r = new double[rows()][cols()];
        for (int i = 0; i < rows(); i++)
            for (int j = 0; j < cols(); j++)
                r[i][j] = data[i][j] + o.data[i][j];
        return new Matrix(r);
    }

    public Matrix addParallel(Matrix o, int threads) throws InterruptedException {
        check(o);
        double[][] r = new double[rows()][cols()];
        int n = rows();
        int parts = Math.min(threads, n);
        Thread[] ts = new Thread[parts];
        for (int t = 0; t < parts; t++) {
            final int from = t * n / parts;
            final int to = (t + 1) * n / parts;
            ts[t] = new Thread(() -> {
                for (int i = from; i < to; i++)
                    for (int j = 0; j < cols(); j++)
                        r[i][j] = data[i][j] + o.data[i][j];
            }, "T" + (t + 1));
            ts[t].start();
        }
        for (Thread t : ts) t.join();
        return new Matrix(r);
    }

    private void check(Matrix o) {
        if (rows() != o.rows() || cols() != o.cols())
            throw new IllegalArgumentException("size");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix m)) return false;
        if (rows() != m.rows() || cols() != m.cols()) return false;
        for (int i = 0; i < rows(); i++)
            for (int j = 0; j < cols(); j++)
                if (Double.compare(data[i][j], m.data[i][j]) != 0) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }
}
