//package fuzzylogic.core;
//
//import fuzzylogic.defuzzification.Defuzzifier;
//import fuzzylogic.fuzzification.BasicFuzzifier;
//import fuzzylogic.fuzzification.Fuzzifier;
//import fuzzylogic.inference.InferenceEngine;
//import fuzzylogic.inference.InferenceResult;
//import fuzzylogic.rules.RuleBase;
//import fuzzylogic.variables.FuzzySet;
//import fuzzylogic.variables.LinguisticVariable;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class FuzzyEngine {
//
//    private final Map<String, LinguisticVariable> inputVariables;
//    private final Map<String, LinguisticVariable> outputVariables;
//
//    private final RuleBase ruleBase;
//    private final InferenceEngine inferenceEngine;
//    private final Defuzzifier defuzzifier;       // Only used for Mamdani
//    private final Fuzzifier fuzzifier = new BasicFuzzifier();
//
//    public FuzzyEngine(
//            Map<String, LinguisticVariable> inputVars,
//            Map<String, LinguisticVariable> outputVars,
//            RuleBase ruleBase,
//            InferenceEngine inferenceEngine,
//            Defuzzifier defuzzifier
//    ) {
//        this.inputVariables = inputVars;
//        this.outputVariables = outputVars;
//        this.ruleBase = ruleBase;
//        this.inferenceEngine = inferenceEngine;
//        this.defuzzifier = defuzzifier;
//    }
//
//    // -------------------------------------------------------
//    //                     FULL PIPELINE
//    // -------------------------------------------------------
//
//    public Map<String, Double> infer(Map<String, Double> crispInputs) {
//
//        // --------------------------------
//        // 1. Fuzzification
//        // --------------------------------
//        Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs = new HashMap<>();
//
//        for (var e : crispInputs.entrySet()) {
//            String varName = e.getKey();
//            double crispValue = e.getValue();
//
//            LinguisticVariable lv = inputVariables.get(varName);
//            if (lv == null)
//                throw new RuntimeException("Unknown input variable: " + varName);
//
//            fuzzifiedInputs.put(lv, fuzzifier.fuzzify(crispValue, lv));
//        }
//
//        // --------------------------------
//        // 2. Inference
//        // --------------------------------
//        InferenceResult result =
//                inferenceEngine.infer(fuzzifiedInputs, outputVariables, ruleBase);
//
//        // --------------------------------
//        // 3. Sugeno → already crisp
//        // --------------------------------
//        if (result.isCrisp()) {
//            return result.getCrispOutputs();
//        }
//
//        // --------------------------------
//        // 4. Mamdani Defuzzification
//        // --------------------------------
//        Map<String, Double> crispOutputs = new HashMap<>();
//
//        for (var entry : result.getFuzzyOutputs().entrySet()) {
//            String outputName = entry.getKey();
//            Map<FuzzySet, Double> aggregated = entry.getValue();
//
//            double crispValue = defuzzifier.defuzzify(aggregated);
//            crispOutputs.put(outputName, crispValue);
//        }
//
//        return crispOutputs;
//    }
//}
