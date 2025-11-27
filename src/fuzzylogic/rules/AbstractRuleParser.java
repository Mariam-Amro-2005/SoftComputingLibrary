package fuzzylogic.rules;

import fuzzylogic.variables.LinguisticVariable;
import fuzzylogic.variables.FuzzySet;

import java.util.*;

public abstract class AbstractRuleParser {

    protected final Map<String, LinguisticVariable> variableRegistry;

    public AbstractRuleParser(Collection<LinguisticVariable> variables) {
        variableRegistry = new HashMap<>();
        for (LinguisticVariable var : variables) {
            variableRegistry.put(var.getName().toLowerCase(), var);
        }
    }

    public abstract Rule parse(String ruleStr);


    protected List<String> tokenize(String ruleStr) {
        return Arrays.asList(ruleStr.replace(",", " , ").trim().split("\\s+"));
    }

    protected LinguisticVariable getVariableByName(String name) {
        LinguisticVariable var = variableRegistry.get(name.toLowerCase());
        if (var == null)
            throw new IllegalArgumentException("Unknown variable: " + name);
        return var;
    }

    protected FuzzySet getFuzzySet(LinguisticVariable var, String label) {
        FuzzySet set = var.getFuzzySetByName(label);
        if (set == null)
            throw new IllegalArgumentException("Variable '" + var.getName() +
                    "' has no fuzzy set named '" + label + "'");
        return set;
    }

    protected LogicalOperator parseOperator(String op) {
        return switch (op.toUpperCase()) {
            case "AND" -> LogicalOperator.AND;
            case "OR" -> LogicalOperator.OR;
            default -> throw new IllegalArgumentException("Unknown logical operator: " + op);
        };
    }
}
