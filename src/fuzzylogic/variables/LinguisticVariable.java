package fuzzylogic.variables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinguisticVariable {

    private String name;
    private double domainStart;
    private double domainEnd;
    private List<FuzzySet> sets;

    public LinguisticVariable(String name, double domainStart, double domainEnd, List<FuzzySet> sets) {
        this.name = name;
        this.domainStart = domainStart;
        this.domainEnd = domainEnd;
        setSets(sets); // deep copy
    }

    // ---------------- Getters ---------------- //

    public String getName() {
        return name;
    }

    public double getDomainStart() {
        return domainStart;
    }

    public double getDomainEnd() {
        return domainEnd;
    }

    public List<FuzzySet> getSets() {
        return Collections.unmodifiableList(sets);
    }

    // ---------------- Setters ---------------- //

    public void setName(String name) {
        this.name = name;
    }

    public void setDomainStart(double domainStart) {
        this.domainStart = domainStart;
    }

    public void setDomainEnd(double domainEnd) {
        this.domainEnd = domainEnd;
    }


    public void setSets(List<FuzzySet> sets) {
        if (sets == null) {
            this.sets = new ArrayList<>();
            return;
        }

        this.sets = new ArrayList<>();
        for (FuzzySet s : sets) {
            this.sets.add(s.copy());
        }
    }

    // ---------------- Helpers ---------------- //


    public FuzzySet getFuzzySetByName(String label) {
        if (label == null) return null;

        for (FuzzySet set : sets) {
            if (set.getLabel().equalsIgnoreCase(label)) {
                return set;
            }
        }
        return null;
    }

    public void addFuzzySet(FuzzySet set) {
        if (set != null) {
            sets.add(set.copy());
        }
    }

    @Override
    public String toString() {
        return "LinguisticVariable{" +
                "name='" + name + '\'' +
                ", domain=[" + domainStart + ", " + domainEnd + "]" +
                ", sets=" + sets +
                '}';
    }
}
