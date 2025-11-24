package fuzzylogic.rules;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class RuleBase {

    private final List<Rule> rules = new ArrayList<>();

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void removeRule(int index) {
        rules.remove(index);
    }

    public void enableRule(int index) {
        rules.get(index).setEnabled(true);
    }

    public void disableRule(int index) {
        rules.get(index).setEnabled(false);
    }

    public List<Rule> getEnabledRules() {
        return rules.stream().filter(Rule::isEnabled).toList();
    }

    public List<Rule> getAllRules() {
        return rules;
    }

    public void saveToFile(String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Rule r : rules) {
                writer.write(r.toString());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String path,
                             RuleParser parser) throws IOException {

        rules.clear();
        for (String line : Files.readAllLines(Paths.get(path))) {
            rules.add(parser.parse(line));
        }
    }
}
