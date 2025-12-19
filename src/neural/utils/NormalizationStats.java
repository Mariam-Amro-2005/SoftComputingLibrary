package neural.utils;

public class NormalizationStats {

    public final double[] min;
    public final double[] max;
    public final double[] mean;
    public final double[] std;

    public NormalizationStats(
            double[] min,
            double[] max,
            double[] mean,
            double[] std
    ) {
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.std = std;
    }
}
