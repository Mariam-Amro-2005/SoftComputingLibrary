package fuzzylogic.membership;

/**
 * Represents a single line segment of a membership function.
 * Defined by slope m and intercept c over a domain [startX, endX].
 */
public class LineSegment {

    public final double startX;
    public final double endX;
    public final double m;   // slope
    public final double c;   // intercept

    public LineSegment(double startX, double endX, double m, double c) {
        this.startX = startX;
        this.endX = endX;
        this.m = m;
        this.c = c;
    }

    public double compute(double x) {
        return m * x + c;
    }

    @Override
    public String toString() {
        return "LineSegment[" + startX + ", " + endX + "] y = " + m + "x + " + c;
    }
}
