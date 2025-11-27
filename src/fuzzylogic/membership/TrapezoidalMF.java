package fuzzylogic.membership;

import java.util.ArrayList;
import java.util.List;

public class TrapezoidalMF implements MembershipFunction {

    private final double a, b, c, d;
    private final List<LineSegment> segments = new ArrayList<>();

    public TrapezoidalMF(double a, double b, double c, double d) {
        if (!(a <= b && b <= c && c <= d)) {
            throw new IllegalArgumentException("Trapezoidal MF requires a <= b <= c <= d");
        }

        if (a == b && b == c && c == d) {
            throw new IllegalArgumentException("Invalid trapezoidal MF: all points equal.");
        }

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        buildSegments();
    }

    private void buildSegments() {

        if (b > a) {
            double m1 = 1.0 / (b - a);
            double c1 = -a * m1;
            segments.add(new LineSegment(a, b, m1, c1));
        }

        if (c > b) {
            segments.add(new LineSegment(b, c, 0, 1)); // horizontal line at y=1
        }

        if (d > c) {
            double m2 = -1.0 / (d - c);
            double c2 = 1.0 - m2 * c;
            segments.add(new LineSegment(c, d, m2, c2));
        }
    }

    @Override
    public double compute(double x) {
        if (x <= a || x >= d) return 0;
        if (x >= b && x <= c) return 1; // plateau

        for (LineSegment seg : segments) {
            if (x >= seg.startX && x <= seg.endX) {
                return seg.compute(x);
            }
        }
        return 0;
    }

    @Override
    public String getName() {
        return "Trapezoidal(" + a + "," + b + "," + c + "," + d + ")";
    }

    @Override
    public MembershipFunction copy() {
        return new TrapezoidalMF(a, b, c, d);
    }
}
