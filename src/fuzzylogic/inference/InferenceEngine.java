package fuzzylogic.inference;

import fuzzylogic.rules.RuleBase;
import fuzzylogic.variables.*;

import java.util.Map;

public interface InferenceEngine {
    InferenceResult infer(
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs,
            RuleBase ruleBase
    );
}

