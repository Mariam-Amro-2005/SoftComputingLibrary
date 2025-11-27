package fuzzylogic.core;

import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.fuzzification.Fuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.inference.SugenoInference;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.variables.FuzzySet;
import fuzzylogic.variables.LinguisticVariable;

import java.util.HashMap;
import java.util.Map;

public class FuzzyEngine {

    public enum Mode { MAMDANI, SUGENO }

    private final RuleBase ruleBase;
    private final Fuzzifier fuzzifier;
    private final Mode mode;

    private final MamdaniInference mamdaniInference;
    private final Defuzzifier defuzzifier;

    private final SugenoInference sugenoInference;

    public FuzzyEngine(RuleBase ruleBase,
                       Mode mode,
                       MamdaniInference mamdaniInference,
                       Defuzzifier defuzzifier,
                       SugenoInference sugenoInference,
                       Fuzzifier fuzzifier) {

        this.ruleBase = ruleBase;
        this.mode = mode;
        this.fuzzifier = fuzzifier != null ? fuzzifier : new BasicFuzzifier();

        if (mode == Mode.MAMDANI) {
            if (mamdaniInference == null)
                throw new IllegalArgumentException("MamdaniInference must be set for MAMDANI mode");
            if (defuzzifier == null)
                throw new IllegalArgumentException("Defuzzifier must be set for MAMDANI mode");

            this.mamdaniInference = mamdaniInference;
            this.defuzzifier = defuzzifier;
            this.sugenoInference = null;
        } else if (mode == Mode.SUGENO) {
            if (sugenoInference == null)
                throw new IllegalArgumentException("SugenoInference must be set for SUGENO mode");

            this.sugenoInference = sugenoInference;
            this.mamdaniInference = null;
            this.defuzzifier = null;
        } else {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }


    public Map<LinguisticVariable, Double> evaluate(Map<LinguisticVariable, Double> inputs) {
        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs = new HashMap<>();
        for (var entry : inputs.entrySet()) {
            fuzzifiedInputs.put(entry.getKey(), fuzzifier.fuzzify(entry.getValue(), entry.getKey()));
        }

        Map<LinguisticVariable, Double> outputs = new HashMap<>();

        if (mode == Mode.MAMDANI) {
            Map<LinguisticVariable, Map<FuzzySet, Double>> inferred =
                    mamdaniInference.infer(fuzzifiedInputs, ruleBase);

            for (var entry : inferred.entrySet()) {
                outputs.put(entry.getKey(), defuzzifier.defuzzify(entry.getValue()));
            }

        } else {
            Map<LinguisticVariable, Double> inferred = sugenoInference.infer(fuzzifiedInputs, ruleBase);
            outputs.putAll(inferred);
        }

        return outputs;
    }

    public SugenoInference getSugenoInference() {
        return sugenoInference;
    }


}
