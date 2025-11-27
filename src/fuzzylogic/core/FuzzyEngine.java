package fuzzylogic.core;

import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.fuzzification.Fuzzifier;
import fuzzylogic.inference.InferenceEngine;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.Map;

public class FuzzyEngine {

    private final Fuzzifier fuzzifier;
    private final InferenceEngine inferenceEngine;
    private final Defuzzifier defuzzifier;

    public FuzzyEngine(Fuzzifier fuzzifier,
                       InferenceEngine inferenceEngine,
                       Defuzzifier defuzzifier, RuleBase ruleBase) {
        this.fuzzifier = fuzzifier;
        this.inferenceEngine = inferenceEngine;
        this.defuzzifier = defuzzifier;
    }

    public double evaluate(Map<LinguisticVariable, Double> crispInputs, RuleBase ruleBase) {

        // Step 1: Fuzzify all inputs
        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzified = new HashMap<>();
        for (var entry : crispInputs.entrySet()) {
            fuzzified.put(entry.getKey(),
                    fuzzifier.fuzzify(entry.getValue(), entry.getKey()));
        }

        // Step 2: Run inference
        Map<FuzzySet, Double> inferenceOutput = inferenceEngine.infer(fuzzified, ruleBase);

        // Step 3: Defuzzify to get crisp output
        return defuzzifier.defuzzify(inferenceOutput);
    }
}

