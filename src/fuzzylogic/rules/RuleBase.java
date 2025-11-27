package fuzzylogic.rules;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class RuleBase {

    private final List<Rule> rules = new ArrayList<>();
    private final AbstractRuleParser parser;

    public RuleBase(AbstractRuleParser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("RuleParser cannot be null");
        }
        this.parser = parser;
    }

    public void addRule(Rule rule) {
        if (rule != null) {
            rules.add(rule);
        }
    }

    public void removeRule(int index) {
        if (index >= 0 && index < rules.size()) {
            rules.remove(index);
        }
    }

    public void enableRule(int index) {
        if (index >= 0 && index < rules.size()) {
            rules.get(index).setEnabled(true);
        }
    }

    public void disableRule(int index) {
        if (index >= 0 && index < rules.size()) {
            rules.get(index).setEnabled(false);
        }
    }

    public List<Rule> getEnabledRules() {
        return rules.stream().filter(Rule::isEnabled).toList();
    }

    public List<Rule> getAllRules() {
        return List.copyOf(rules); // immutable copy to prevent external modification
    }

    public void saveToFile(String path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            for (Rule r : rules) {
                writer.write(r.toString());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String path) throws IOException {
        rules.clear();
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            if (!line.isBlank()) {
                try {
                    rules.add(parser.parse(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid rule: " + line + " -> " + e.getMessage());
                }
            }
        }
    }

    public void addRuleFromString(String ruleStr) {
        if (ruleStr == null || ruleStr.isBlank()) return;
        rules.add(parser.parse(ruleStr));
    }

}
