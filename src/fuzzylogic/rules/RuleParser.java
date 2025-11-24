package fuzzylogic.rules;

import fuzzylogic.variables.*;

import java.util.*;

public class RuleParser {

    private final Map<String, LinguisticVariable> variables;
    private final Map<String, FuzzySet> outputSets;

    public RuleParser(Map<String, LinguisticVariable> vars,
                      Map<String, FuzzySet> outputs) {
        this.variables = vars;
        this.outputSets = outputs;
    }

    public Rule parse(String ruleText) {

        Rule rule = new Rule();
        ruleText = ruleText.trim();

        // Split IF ... THEN ...
        String[] parts = ruleText.split("THEN");
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid rule syntax: " + ruleText);

        String condPart = parts[0].replace("IF", "").trim();
        String consPart = parts[1].trim();

        // ---- Parse Antecedents ----
        List<String> tokens = tokenizeConditions(condPart);
        parseAntecedents(tokens, rule);

        // ---- Parse Consequents ----
        parseConsequents(consPart, rule);

        return rule;
    }

    private List<String> tokenizeConditions(String cond) {
        // Split by spaces while keeping AND/OR as tokens
        return Arrays.stream(cond.split("\\s+")).toList();
    }

    private void parseAntecedents(List<String> tokens, Rule rule) {
        int i = 0;
        while (i < tokens.size()) {

            String var = tokens.get(i);
            if (var.equalsIgnoreCase("AND") || var.equalsIgnoreCase("OR")) {
                rule.addOperator(var.equalsIgnoreCase("AND")
                        ? LogicalOperator.AND
                        : LogicalOperator.OR);
                i++;
                continue;
            }

            // var, "is", setName
            String isToken = tokens.get(i + 1);
            String setName = tokens.get(i + 2);

            LinguisticVariable lv = variables.get(var);
            if (lv == null)
                throw new RuntimeException("Unknown variable: " + var);

            rule.addAntecedent(new Antecedent(lv, setName));

            i += 3;
        }
    }

    private void parseConsequents(String text, Rule rule) {
        // Format: "Risk is High, Alert is Extreme"
        String[] parts = text.split(",");

        for (String p : parts) {
            p = p.trim();
            String[] tokens = p.split("is");

            String setName = tokens[1].trim();

            FuzzySet fs = outputSets.get(setName);
            if (fs == null)
                throw new RuntimeException("Unknown output fuzzy set: " + setName);

            rule.addConsequent(new Consequent(fs));
        }
    }
}
