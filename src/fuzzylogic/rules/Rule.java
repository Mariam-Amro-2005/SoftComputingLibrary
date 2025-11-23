package fuzzylogic.rules;

import fuzzylogic.variables.*;

import java.util.Map;

public class Rule {
    private Map<LinguisticVariable, String> antecedents;
    private FuzzySet consequent;
    private double weight;
    private boolean enabled;

    public Rule(Map<LinguisticVariable, String> antecedents, FuzzySet consequent) {
    }

    public Map<Object, Object> getAntecedents() {
        return null;
    }

    public FuzzySet getConsequent() {
        return null;
    }

    public void setEnabled(boolean b) {
        this.enabled = b;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
