package fuzzylogic.client.DriverRisk;

import fuzzylogic.rules.Rule;
import fuzzylogic.rules.RuleBase;
import fuzzylogic.rules.SugenoConsequent;
import fuzzylogic.rules.Antecedent;
import fuzzylogic.rules.LogicalOperator;
import fuzzylogic.variables.LinguisticVariable;

public class DriverRiskSugenoRules {

    public static void addRules(RuleBase rb,
                                LinguisticVariable speed,
                                LinguisticVariable road,
                                LinguisticVariable vis,
                                LinguisticVariable risk) {

        double LOW = 10;
        double MED = 40;
        double HIGH = 70;
        double EXTREME = 90;

        // ================================
        // Rule 1:
        // IF speed is Fast AND visibility is Poor THEN AccidentRisk = Extreme
        // ================================
        Rule r1 = new Rule();
        r1.addAntecedent(new Antecedent(speed, "Fast"));
        r1.addOperator(LogicalOperator.AND);
        r1.addAntecedent(new Antecedent(vis, "Poor"));
        r1.addConsequent(new SugenoConsequent(risk, EXTREME));
        rb.addRule(r1);

        // ================================
        // Rule 2:
        // IF speed is Slow AND road is Good THEN AccidentRisk = Low
        // ================================
        Rule r2 = new Rule();
        r2.addAntecedent(new Antecedent(speed, "Slow"));
        r2.addOperator(LogicalOperator.AND);
        r2.addAntecedent(new Antecedent(road, "Good"));
        r2.addConsequent(new SugenoConsequent(risk, LOW));
        rb.addRule(r2);

        // ================================
        // Rule 3:
        // IF road is Wet AND visibility is Foggy THEN AccidentRisk = Medium
        // ================================
        Rule r3 = new Rule();
        r3.addAntecedent(new Antecedent(road, "Wet"));
        r3.addOperator(LogicalOperator.AND);
        r3.addAntecedent(new Antecedent(vis, "Foggy"));
        r3.addConsequent(new SugenoConsequent(risk, MED));
        rb.addRule(r3);

        // ================================
        // Rule 4:
        // IF speed is Normal AND visibility is Clear THEN AccidentRisk = Low
        // ================================
        Rule r4 = new Rule();
        r4.addAntecedent(new Antecedent(speed, "Normal"));
        r4.addOperator(LogicalOperator.AND);
        r4.addAntecedent(new Antecedent(vis, "Clear"));
        r4.addConsequent(new SugenoConsequent(risk, LOW));
        rb.addRule(r4);

        // ================================
        // Rule 5:
        // IF speed is Fast AND road is Bad THEN AccidentRisk = High
        // ================================
        Rule r5 = new Rule();
        r5.addAntecedent(new Antecedent(speed, "Fast"));
        r5.addOperator(LogicalOperator.AND);
        r5.addAntecedent(new Antecedent(road, "Bad"));
        r5.addConsequent(new SugenoConsequent(risk, HIGH));
        rb.addRule(r5);
    }
}
