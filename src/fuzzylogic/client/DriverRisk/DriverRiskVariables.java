package fuzzylogic.client.DriverRisk;

import fuzzylogic.membership.MembershipFactory;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.List;

public class DriverRiskVariables {

    public static LinguisticVariable speed() {
        return new LinguisticVariable("Speed", 0, 200, List.of(
                new FuzzySet("Slow", MembershipFactory.create("trapezoidal", 0, 0, 40, 60)),
                new FuzzySet("Normal", MembershipFactory.create("triangular", 50, 80, 110)),
                new FuzzySet("Fast", MembershipFactory.create("trapezoidal", 100, 140, 200, 200))
        ));
    }

    public static LinguisticVariable roadCondition() {
        return new LinguisticVariable("RoadCondition", 0, 10, List.of(
                new FuzzySet("Bad", MembershipFactory.create("triangular", 0, 0, 4)),
                new FuzzySet("Wet", MembershipFactory.create("triangular", 2, 5, 8)),
                new FuzzySet("Good", MembershipFactory.create("triangular", 6, 10, 10))
        ));
    }

    public static LinguisticVariable visibility() {
        return new LinguisticVariable("Visibility", 0, 1000, List.of(
                new FuzzySet("Poor", MembershipFactory.create("trapezoidal", 0, 0, 100, 300)),
                new FuzzySet("Foggy", MembershipFactory.create("trapezoidal", 200, 400, 600, 800)),
                new FuzzySet("Clear", MembershipFactory.create("trapezoidal", 700, 900, 1000, 1000))
        ));
    }

    public static LinguisticVariable accidentRisk() {
        return new LinguisticVariable("AccidentRisk", 0, 100, List.of(
                new FuzzySet("Low", MembershipFactory.create("triangular", 0, 0, 25)),
                new FuzzySet("Medium", MembershipFactory.create("triangular", 20, 40, 60)),
                new FuzzySet("High", MembershipFactory.create("triangular", 50, 70, 90)),
                new FuzzySet("Extreme", MembershipFactory.create("triangular", 80, 100, 100))
        ));
    }
}
