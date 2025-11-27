package fuzzylogic.rules;

import fuzzylogic.variables.LinguisticVariable;

import java.util.List;

public class SugenoRuleParser extends AbstractRuleParser {

    public SugenoRuleParser(List<LinguisticVariable> variables) {
        super(variables);
    }

    @Override
    public Rule parse(String ruleStr) {
        Rule rule = new Rule();
        List<String> tokens = tokenize(ruleStr);
        int i = 0;

        if (!tokens.get(i).equalsIgnoreCase("IF"))
            throw new IllegalArgumentException("Rule must start with IF");
        i++; // skip IF

        // Parse antecedents
        while (i < tokens.size() && !tokens.get(i).equalsIgnoreCase("THEN")) {
            String varName = tokens.get(i++);
            if (!tokens.get(i++).equalsIgnoreCase("IS"))
                throw new IllegalArgumentException("Expected 'IS' after variable name");
            String setLabel = tokens.get(i++);
            LinguisticVariable var = getVariableByName(varName);
            rule.addAntecedent(new Antecedent(var, setLabel));

            if (i < tokens.size() && (tokens.get(i).equalsIgnoreCase("AND") || tokens.get(i).equalsIgnoreCase("OR"))) {
                rule.addOperator(parseOperator(tokens.get(i++)));
            }
        }

        if (i >= tokens.size() || !tokens.get(i).equalsIgnoreCase("THEN"))
            throw new IllegalArgumentException("Expected THEN keyword in rule");
        i++; // skip THEN

        // Parse Sugeno consequents (Variable = value)
        while (i < tokens.size()) {
            if (tokens.get(i).equals(",")) {
                i++;
                continue;
            }
            String varName = tokens.get(i++);
            if (!tokens.get(i++).equals("="))
                throw new IllegalArgumentException("Expected '=' in Sugeno consequent");
            double value;
            try {
                value = Double.parseDouble(tokens.get(i++));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid numeric value in Sugeno consequent");
            }
            LinguisticVariable var = getVariableByName(varName);
            rule.addConsequent(new SugenoConsequent(var, value));
        }

        return rule;
    }
}
