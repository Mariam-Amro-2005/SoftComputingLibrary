package fuzzylogic.core;

import fuzzylogic.defuzzification.Defuzzifier;
import fuzzylogic.inference.MamdaniInference;
import fuzzylogic.inference.SugenoInference;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.rules.Rule;
import fuzzylogic.variables.LinguisticVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuzzySystemBuilder {

    private final List<Rule> rules = new ArrayList<>();
    private RuleBase ruleBase;
    private FuzzyEngine.Mode mode;
    private MamdaniInference mamdaniInference;
    private SugenoInference sugenoInference;
    private Map<LinguisticVariable, Defuzzifier> defuzzifiers = new HashMap<>();

    public FuzzySystemBuilder setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
        return this;
    }

    public FuzzySystemBuilder addRule(Rule rule) {
        this.rules.add(rule);
        return this;
    }

    public FuzzySystemBuilder setMode(FuzzyEngine.Mode mode) {
        this.mode = mode;
        return this;
    }

    public FuzzySystemBuilder setMamdaniInference(MamdaniInference inference) {
        this.mamdaniInference = inference;
        return this;
    }

    public FuzzySystemBuilder setSugenoInference(SugenoInference inference) {
        this.sugenoInference = inference;
        return this;
    }

    public FuzzySystemBuilder addDefuzzifier(LinguisticVariable variable, Defuzzifier defuzzifier) {
        this.defuzzifiers.put(variable, defuzzifier);
        return this;
    }

    public FuzzyEngine build() {
        if (ruleBase == null) throw new IllegalStateException("RuleBase must be set");
        rules.forEach(ruleBase::addRule);

        if (mode == null) throw new IllegalStateException("Mode must be set");

        return new FuzzyEngine(ruleBase, mode, mamdaniInference, defuzzifiers, sugenoInference);
    }
}
