package neural.utils;

public final class Matrix {

    private Matrix() {
        // Utility class — prevent instantiation
    }

    /* =========================
       Shape & Validation
       ========================= */

    private static void validateSameShape(double[][] A, double[][] B) {
        if (A.length != B.length || A[0].length != B[0].length)
            throw new IllegalArgumentException("Matrix shape mismatch");
    }

    /* =========================
       Core Operations
       ========================= */

    // Matrix multiplication (dot product)
    public static double[][] dot(double[][] A, double[][] B) {
        int aRows = A.length;
        int aCols = A[0].length;
        int bRows = B.length;
        int bCols = B[0].length;

        if (aCols != bRows)
            throw new IllegalArgumentException(
                    "Dot product dimension mismatch: " +
                            aRows + "x" + aCols + " · " + bRows + "x" + bCols
            );

        double[][] result = new double[aRows][bCols];

        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                double sum = 0.0;
                for (int k = 0; k < aCols; k++) {
                    sum += A[i][k] * B[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    // Add matrices (supports bias broadcasting: [1 x n])
    public static double[][] add(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;

        double[][] result = new double[rows][cols];

        if (B.length == 1 && B[0].length == cols) {
            // Bias broadcasting
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    result[i][j] = A[i][j] + B[0][j];
        } else {
            validateSameShape(A, B);
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    result[i][j] = A[i][j] + B[i][j];
        }

        return result;
    }

    // Subtract matrices
    public static double[][] subtract(double[][] A, double[][] B) {
        validateSameShape(A, B);

        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[i][j] = A[i][j] - B[i][j];

        return result;
    }

    /* =========================
       Element-wise Operations
       ========================= */

    // Hadamard (element-wise) product
    public static double[][] hadamard(double[][] A, double[][] B) {
        validateSameShape(A, B);

        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[i][j] = A[i][j] * B[i][j];

        return result;
    }

    // Scalar multiplication
    public static double[][] multiply(double[][] A, double scalar) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[i][j] = A[i][j] * scalar;

        return result;
    }

    /* =========================
       Shape Manipulation
       ========================= */

    // Transpose matrix
    public static double[][] transpose(double[][] A) {
        int rows = A.length;
        int cols = A[0].length;

        double[][] result = new double[cols][rows];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[j][i] = A[i][j];

        return result;
    }

    // Sum rows (used for bias gradients)
    public static double[][] sumRows(double[][] A) {
        int rows = A.length;
        int cols = A[0].length;

        double[][] result = new double[1][cols];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result[0][j] += A[i][j];

        return result;
    }

    /* =========================
       Utility & Debugging
       ========================= */

    public static double[][] copy(double[][] A) {
        int rows = A.length;
        int cols = A[0].length;

        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            System.arraycopy(A[i], 0, result[i], 0, cols);

        return result;
    }

    public static void print(double[][] A) {
        for (double[] row : A) {
            for (double v : row)
                System.out.printf("%8.4f ", v);
            System.out.println();
        }
        System.out.println();
    }
}
