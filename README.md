## 🏗 Core Architecture: The Fuzzy Inference System

The module follows a strictly decoupled pipeline, ensuring that the engine can process rules regardless of whether they lead to a fuzzy set (Mamdani) or a crisp value (Sugeno).

### 1. The Rule Anatomy

Rules are structured as a collection of **Antecedents** (the "IF" part) and **Consequents** (the "THEN" part).

* **Antecedents**: Link a `LinguisticVariable` to a specific `FuzzySet` label (e.g., "Speed is High").
* **Polymorphic Consequents**: The engine uses the `Consequent` interface to support two distinct behaviors:
* **Mamdani**: Maps to a `FuzzySet` on the output variable.
* **Sugeno**: Maps directly to a crisp `constant` value or linear function.



### 2. Rule Base & Persistence

The `RuleBase` acts as the system's central "brain," managing the collection of rules.

* **Dynamic Control**: Rules can be enabled, disabled, or weighted individually at runtime to tweak system behavior without code changes.
* **File I/O**: Supports saving and loading rule sets via an `AbstractRuleParser`, allowing logic to be stored in external configuration files.

---

## 🔢 Logic Operators: The Mathematical Core

The engine's reasoning is powered by swappable mathematical strategies. You can mix and match these to change how the system "thinks":

| Category | Implemented Types | Description |
| --- | --- | --- |
| **T-Norms (AND)** | `MinTNorm`, `ProductTNorm` | Combines multiple IF conditions. |
| **S-Norms (OR)** | `MaxSnorm`, `SumSnorm` | Combines multiple overlapping rules. |
| **Implications** | `MinImplication`, `ProductImplication` | Scales the output fuzzy set based on input strength. |

---

## 📈 Case Studies: Practical Applications

### Mamdani: Driver Risk Assessment

Evaluates driving safety based on **Speed** and **Braking Intensity**.

* **Input**: Speed (0–120 km/h), Braking (0–100%).
* **Output**: Risk Level (Fuzzy: Low, Medium, High).
* **Defuzzification**: Uses `CentroidDefuzzifier` to provide a specific risk percentage.

### Sugeno: Restaurant Tipping

A functional approach to calculating a tip based on **Service Quality** and **Food Quality**.

* **Input**: Service (0–10), Food (0–10).
* **Output**: Tip (Crisp: 5%, 15%, 25%).
* **Logic**: Uses weighted averages for fast, precise numerical calculation.

---

## 🛠 Extensibility: Custom Logic

The library is designed to be a "plug-and-play" framework. You can extend the system's reasoning capabilities in three steps:

1. **Implement the Interface**: Create a new class implementing `TNorm`, `SNorm`, or `MembershipFunction`.
2. **Factory Registration**: If adding a new Membership Function, add it to the `MembershipFactory`.
3. **Fluent Assembly**: Use the `FuzzySystemBuilder` to inject your custom logic into the engine.

```java
FuzzyEngine engine = new FuzzySystemBuilder()
    .setMode(FuzzyEngine.Mode.MAMDANI)
    .setMamdaniInference(new MamdaniInference(
        new ProductTNorm(),      // Custom AND
        new MaxSNorm(),          // Custom OR
        new ProductImplication() // Custom THEN
    ))
    .setDefuzzifier(new CentroidDefuzzifier())
    .addRule(myCustomRule)
    .build();

```
