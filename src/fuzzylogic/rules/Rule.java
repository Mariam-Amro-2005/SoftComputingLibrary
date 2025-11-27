package fuzzylogic.rules;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private final List<Antecedent> antecedents = new ArrayList<>();
    private final List<LogicalOperator> operators = new ArrayList<>();
    private final List<Consequent> consequents = new ArrayList<>();

    private double weight = 1.0;
    private boolean enabled = true;

    public void addAntecedent(Antecedent a) {
        antecedents.add(a);
    }

    public void addOperator(LogicalOperator op) {
        operators.add(op);
    }

    public void addConsequent(Consequent c) {
        consequents.add(c);
    }

    public List<Antecedent> getAntecedents() {
        return antecedents;
    }

    public List<LogicalOperator> getOperators() {
        return operators;
    }

    public List<Consequent> getConsequents() {
        return consequents;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean e) {
        this.enabled = e;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IF ");

        for (int i = 0; i < antecedents.size(); i++) {
            sb.append(antecedents.get(i));
            if (i < operators.size()) {
                sb.append(" ").append(operators.get(i)).append(" ");
            }
        }

        sb.append(" THEN ");

        for (int i = 0; i < consequents.size(); i++) {
            sb.append(consequents.get(i));
            if (i < consequents.size() - 1) sb.append(", ");
        }

        return sb.toString();
    }
}
