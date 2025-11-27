package fuzzylogic.rules;

import fuzzylogic.variables.LinguisticVariable;
import fuzzylogic.variables.FuzzySet;

import java.util.List;

public class MamdaniRuleParser extends AbstractRuleParser {

    public MamdaniRuleParser(List<LinguisticVariable> variables) {
        super(variables);
    }

    @Override
    public Rule parse(String ruleStr) {
        Rule rule = new Rule();

        List<String> tokens = tokenize(ruleStr);
        int i = 0;
        if (!tokens.get(i).equalsIgnoreCase("IF"))
            throw new IllegalArgumentException("Rule must start with IF");

        i++; // skip "IF"

        // Parse antecedents
        while (i < tokens.size() && !tokens.get(i).equalsIgnoreCase("THEN")) {
            String varName = tokens.get(i++);
            if (!tokens.get(i++).equalsIgnoreCase("IS"))
                throw new IllegalArgumentException("Expected 'IS' after variable name");
            String setLabel = tokens.get(i++);
            LinguisticVariable var = getVariableByName(varName);
            FuzzySet set = getFuzzySet(var, setLabel);
            rule.addAntecedent(new Antecedent(var, set.getLabel()));

            // Check for AND / OR
            if (i < tokens.size() && (tokens.get(i).equalsIgnoreCase("AND") || tokens.get(i).equalsIgnoreCase("OR"))) {
                rule.addOperator(parseOperator(tokens.get(i++)));
            }
        }

        if (i >= tokens.size() || !tokens.get(i).equalsIgnoreCase("THEN"))
            throw new IllegalArgumentException("Expected THEN keyword in rule");

        i++; // skip THEN

        // Parse consequents (comma-separated)
        while (i < tokens.size()) {
            if (tokens.get(i).equals(",")) {
                i++; // skip comma
                continue;
            }
            String varName = tokens.get(i++);
            if (!tokens.get(i++).equalsIgnoreCase("IS"))
                throw new IllegalArgumentException("Expected 'IS' in consequent");
            String setLabel = tokens.get(i++);
            LinguisticVariable var = getVariableByName(varName);
            FuzzySet set = getFuzzySet(var, setLabel);
            rule.addConsequent(new MamdaniConsequent(var, set));
        }

        return rule;
    }
}
