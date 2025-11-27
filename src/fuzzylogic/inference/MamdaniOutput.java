package fuzzylogic.inference;

import fuzzylogic.variables.FuzzySet;
import java.util.Map;

public class MamdaniOutput {
    private final Map<FuzzySet, Double> aggregatedOutput;

    public MamdaniOutput(Map<FuzzySet, Double> aggregatedOutput) {
        this.aggregatedOutput = aggregatedOutput;
    }

    public Map<FuzzySet, Double> getAggregatedOutput() {
        return aggregatedOutput;
    }
}
