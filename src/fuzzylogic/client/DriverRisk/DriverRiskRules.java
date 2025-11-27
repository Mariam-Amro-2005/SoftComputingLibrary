package fuzzylogic.client.DriverRisk;

import fuzzylogic.rules.*;
import fuzzylogic.variables.LinguisticVariable;

public class DriverRiskRules {

    public static void addRules(RuleBase ruleBase,
                                LinguisticVariable speed,
                                LinguisticVariable road,
                                LinguisticVariable visibility,
                                LinguisticVariable risk) {

        Rule r1 = new Rule();
        r1.addAntecedent(new Antecedent(speed, "Fast"));
        r1.addAntecedent(new Antecedent(visibility, "Poor"));
        r1.addOperator(LogicalOperator.AND);
        r1.addConsequent(new MamdaniConsequent(risk, risk.getFuzzySetByName("Extreme")));
        ruleBase.addRule(r1);

        Rule r2 = new Rule();
        r2.addAntecedent(new Antecedent(speed, "Normal"));
        r2.addAntecedent(new Antecedent(road, "Good"));
        r2.addAntecedent(new Antecedent(visibility, "Clear"));
        r2.addOperator(LogicalOperator.AND);
        r2.addOperator(LogicalOperator.AND);
        r2.addConsequent(new MamdaniConsequent(risk, risk.getFuzzySetByName("Low")));
        ruleBase.addRule(r2);

    }
}
