package neural.client.housepricing;

public class HousePriceDataset {

    public final double[][] X; // features
    public final double[][] y; // target prices

    public HousePriceDataset(double[][] X, double[][] y) {
        this.X = X;
        this.y = y;
    }

    public int size() {
        return X.length;
    }
}

