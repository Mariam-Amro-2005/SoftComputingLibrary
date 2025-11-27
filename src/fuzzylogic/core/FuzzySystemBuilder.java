package fuzzylogic.core;

import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.fuzzification.Fuzzifier;
import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.inference.SugenoInference;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.rules.Rule;
import fuzzylogic.variables.LinguisticVariable;

import java.util.ArrayList;
import java.util.List;

public class FuzzySystemBuilder {

    private RuleBase ruleBase;
    private final List<Rule> rules = new ArrayList<>();
    private FuzzyEngine.Mode mode;
    private MamdaniInference mamdaniInference;
    private SugenoInference sugenoInference;
    private Defuzzifier defuzzifier;
    private Fuzzifier fuzzifier;

    /** Set the rule base for the system */
    public FuzzySystemBuilder setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
        return this;
    }

    /** Add a single rule to the system */
    public FuzzySystemBuilder addRule(Rule rule) {
        if (rule != null) this.rules.add(rule);
        return this;
    }

    /** Set the mode (MAMDANI or SUGENO) */
    public FuzzySystemBuilder setMode(FuzzyEngine.Mode mode) {
        this.mode = mode;
        return this;
    }

    /** Set Mamdani inference engine (required for MAMDANI mode) */
    public FuzzySystemBuilder setMamdaniInference(MamdaniInference inference) {
        this.mamdaniInference = inference;
        return this;
    }

    /** Set Sugeno inference engine (required for SUGENO mode) */
    public FuzzySystemBuilder setSugenoInference(SugenoInference inference) {
        this.sugenoInference = inference;
        return this;
    }

    /** Set a single system-wide defuzzifier (required for MAMDANI mode) */
    public FuzzySystemBuilder setDefuzzifier(Defuzzifier defuzzifier) {
        this.defuzzifier = defuzzifier;
        return this;
    }

    public FuzzySystemBuilder setFuzzifier(Fuzzifier fuzzifier) {
        this.fuzzifier = fuzzifier;
        return this;
    }

    /** Build the FuzzyEngine */
    public FuzzyEngine build() {
        if (ruleBase == null) throw new IllegalStateException("RuleBase must be set");
        rules.forEach(ruleBase::addRule);

        if (mode == null) throw new IllegalStateException("Mode must be set");

        if (mode == FuzzyEngine.Mode.MAMDANI) {
            if (mamdaniInference == null)
                throw new IllegalStateException("MamdaniInference must be set for MAMDANI mode");
            if (defuzzifier == null)
                throw new IllegalStateException("Defuzzifier must be set for MAMDANI mode");
        } else if (mode == FuzzyEngine.Mode.SUGENO) {
            if (sugenoInference == null)
                throw new IllegalStateException("SugenoInference must be set for SUGENO mode");
        }

        // Build the engine
        return new FuzzyEngine(
                ruleBase,
                mode,
                mamdaniInference,
                defuzzifier,
                sugenoInference,
                fuzzifier != null ? fuzzifier : new BasicFuzzifier()
        );
    }
}
