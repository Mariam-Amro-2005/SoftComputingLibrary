//package fuzzylogic.core;
//
//import fuzzylogic.defuzzification.Defuzzifier;
//import fuzzylogic.fuzzification.BasicFuzzifier;
//import fuzzylogic.fuzzification.Fuzzifier;
//import fuzzylogic.inference.InferenceEngine;
//import fuzzylogic.rules.Rule;
//import fuzzylogic.rules.RuleBase;
//import fuzzylogic.variables.LinguisticVariable;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class FuzzySystemBuilder {
//
//    private Fuzzifier fuzzifier = new BasicFuzzifier();
//    private InferenceEngine inferenceEngine;
//    private Defuzzifier defuzzifier;
//    private RuleBase ruleBase = new RuleBase();
//    private Map<String, LinguisticVariable> variables = new HashMap<>();
//
//    public FuzzySystemBuilder withFuzzifier(Fuzzifier fuzzifier) {
//        this.fuzzifier = fuzzifier;
//        return this;
//    }
//
//    public FuzzySystemBuilder withInferenceEngine(InferenceEngine engine) {
//        this.inferenceEngine = engine;
//        return this;
//    }
//
//    public FuzzySystemBuilder withDefuzzifier(Defuzzifier defuzzifier) {
//        this.defuzzifier = defuzzifier;
//        return this;
//    }
//
//    public FuzzySystemBuilder addVariable(LinguisticVariable var) {
//        this.variables.put(var.getName(), var);
//        return this;
//    }
//
//    public FuzzySystemBuilder addRule(Rule rule) {
//        this.ruleBase.addRule(rule);
//        return this;
//    }
//
//    public FuzzyEngine build() {
//        return new FuzzyEngine(fuzzifier, inferenceEngine, defuzzifier, ruleBase);
//    }
//}
