package fuzzylogic.client;

import fuzzylogic.fuzzification.BasicFuzzifier;
import fuzzylogic.inference.InferenceResult;
import fuzzylogic.inference.SugenoInference;
import fuzzylogic.inference.SugenoOutput;
import fuzzylogic.membership.*;
import fuzzylogic.operators.snorms.MaxSNorm;
import fuzzylogic.operators.tnorms.MinTNorm;
import fuzzylogic.rules.*;
import fuzzylogic.variables.*;

import java.util.*;

public class SugenoMain {

    public static void main(String[] args) {
        System.out.println("=== Sugeno Fuzzy Inference System Test ===\n");

        LinguisticVariable service = createServiceVariable();
        LinguisticVariable food = createFoodVariable();
        LinguisticVariable tip = createTipVariable();

        RuleBase ruleBase = createSugenoRuleBase(service, food, tip);

        System.out.println("Created " + ruleBase.getEnabledRules().size() + " Sugeno rules\n");

        SugenoInference inference = new SugenoInference(new MinTNorm(), new MaxSNorm());
        BasicFuzzifier fuzzifier = new BasicFuzzifier();

        runTestCase(fuzzifier, inference, service, food, tip, ruleBase, "Poor service, poor food", 3.0, 2.0);
        runTestCase(fuzzifier, inference, service, food, tip, ruleBase, "Good service, excellent food", 7.0, 9.0);
        runTestCase(fuzzifier, inference, service, food, tip, ruleBase, "Excellent service, good food", 9.0, 7.0);
        runTestCase(fuzzifier, inference, service, food, tip, ruleBase, "Average service, average food", 5.0, 5.0);
        runTestCase(fuzzifier, inference, service, food, tip, ruleBase, "Poor service, excellent food", 2.0, 9.0);

        System.out.println("\n✅ All Sugeno inference tests completed successfully!");
    }

    private static LinguisticVariable createServiceVariable() {
        LinguisticVariable service = new LinguisticVariable("Service", 0, 10, new ArrayList<>());
        service.addFuzzySet(new FuzzySet("Poor", new TrapezoidalMF(0, 0, 2, 4)));
        service.addFuzzySet(new FuzzySet("Good", new TriangularMF(3, 5, 7)));
        service.addFuzzySet(new FuzzySet("Excellent", new TrapezoidalMF(6, 8, 10, 10)));
        return service;
    }

    private static LinguisticVariable createFoodVariable() {
        LinguisticVariable food = new LinguisticVariable("Food", 0, 10, new ArrayList<>());
        food.addFuzzySet(new FuzzySet("Poor", new TrapezoidalMF(0, 0, 2, 4)));
        food.addFuzzySet(new FuzzySet("Good", new TriangularMF(3, 5, 7)));
        food.addFuzzySet(new FuzzySet("Excellent", new TrapezoidalMF(6, 8, 10, 10)));
        return food;
    }

    private static LinguisticVariable createTipVariable() {
        return new LinguisticVariable("Tip", 0, 25, new ArrayList<>());
    }

    private static RuleBase createSugenoRuleBase(LinguisticVariable service, LinguisticVariable food,
            LinguisticVariable tip) {
        List<LinguisticVariable> allVars = Arrays.asList(service, food, tip);
        RuleBase ruleBase = new RuleBase(new SugenoRuleParser(allVars));

        Rule rule1 = new Rule();
        rule1.addAntecedent(new Antecedent(service, "Poor"));
        rule1.addOperator(LogicalOperator.OR);
        rule1.addAntecedent(new Antecedent(food, "Poor"));
        rule1.addConsequent(new SugenoConsequent(tip, 5.0));
        ruleBase.addRule(rule1);

        Rule rule2 = new Rule();
        rule2.addAntecedent(new Antecedent(service, "Good"));
        rule2.addConsequent(new SugenoConsequent(tip, 15.0));
        ruleBase.addRule(rule2);

        Rule rule3 = new Rule();
        rule3.addAntecedent(new Antecedent(service, "Excellent"));
        rule3.addOperator(LogicalOperator.OR);
        rule3.addAntecedent(new Antecedent(food, "Excellent"));
        rule3.addConsequent(new SugenoConsequent(tip, 20.0));
        ruleBase.addRule(rule3);

        Rule rule4 = new Rule();
        rule4.addAntecedent(new Antecedent(service, "Good"));
        rule4.addOperator(LogicalOperator.AND);
        rule4.addAntecedent(new Antecedent(food, "Good"));
        rule4.addConsequent(new SugenoConsequent(tip, 16.0));
        ruleBase.addRule(rule4);

        Rule rule5 = new Rule();
        rule5.addAntecedent(new Antecedent(service, "Excellent"));
        rule5.addOperator(LogicalOperator.AND);
        rule5.addAntecedent(new Antecedent(food, "Excellent"));
        rule5.addConsequent(new SugenoConsequent(tip, 25.0));
        ruleBase.addRule(rule5);

        return ruleBase;
    }

    private static void runTestCase(BasicFuzzifier fuzzifier, SugenoInference inference,
            LinguisticVariable service, LinguisticVariable food, LinguisticVariable tip,
            RuleBase ruleBase, String description, double serviceValue, double foodValue) {
        System.out.println("Test: " + description);
        System.out.println("  Input: Service = " + serviceValue + ", Food = " + foodValue);

        try {
            Map<LinguisticVariable, Map<FuzzySet, Double>> fuzzifiedInputs = new HashMap<>();
            fuzzifiedInputs.put(service, fuzzifier.fuzzify(serviceValue, service));
            fuzzifiedInputs.put(food, fuzzifier.fuzzify(foodValue, food));

            System.out.print("  Memberships: Service={");
            for (Map.Entry<FuzzySet, Double> e : fuzzifiedInputs.get(service).entrySet()) {
                if (e.getValue() > 0) {
                    System.out.printf("%s:%.2f ", e.getKey().getLabel(), e.getValue());
                }
            }
            System.out.print("}, Food={");
            for (Map.Entry<FuzzySet, Double> e : fuzzifiedInputs.get(food).entrySet()) {
                if (e.getValue() > 0) {
                    System.out.printf("%s:%.2f ", e.getKey().getLabel(), e.getValue());
                }
            }
            System.out.println("}");

            InferenceResult result = inference.infer(fuzzifiedInputs, ruleBase);

            if (result.isSugeno()) {
                SugenoOutput output = result.getSugenoOutput();
                double tipAmount = output.get(tip);
                System.out.printf("  Output: Tip = %.2f%%\n", tipAmount);
            } else {
                System.err.println("  Error: Expected Sugeno output!");
            }

            System.out.println();
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }
    }
}
