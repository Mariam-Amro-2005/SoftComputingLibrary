package fuzzylogic.membership;

import java.util.ArrayList;
import java.util.List;

public class TriangularMF implements MembershipFunction {

    private final double a, b, c;
    private final List<LineSegment> segments = new ArrayList<>();

    public TriangularMF(double a, double b, double c) {
        if (!(a <= b && b <= c)) {
            throw new IllegalArgumentException("Triangular MF requires a <= b <= c");
        }

        if (a == b && b == c) {
            throw new IllegalArgumentException("Invalid triangular MF: all points equal.");
        }

        this.a = a;
        this.b = b;
        this.c = c;

        buildSegments();
    }

    private void buildSegments() {
        if (b > a) {
            double m1 = 1.0 / (b - a);
            double c1 = -a * m1;
            segments.add(new LineSegment(a, b, m1, c1));
        }

        if (c > b) {
            double m2 = -1.0 / (c - b);
            double c2 = 1.0 - m2 * b;
            segments.add(new LineSegment(b, c, m2, c2));
        }
    }

    @Override
    public double compute(double x) {
        if (x <= a || x >= c) return 0;
        if (x == b) return 1;

        for (LineSegment seg : segments) {
            if (x >= seg.startX && x <= seg.endX) {
                return seg.compute(x);
            }
        }
        return 0;
    }

    @Override
    public String getName() {
        return "Triangular(" + a + "," + b + "," + c + ")";
    }

    @Override
    public MembershipFunction copy() {
        return new TriangularMF(a, b, c);
    }
}
