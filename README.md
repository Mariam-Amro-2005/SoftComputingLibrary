# 🧬 Soft Computing Java Library

### A Modular Framework for Artificial Intelligence & Bio-Inspired Computing

Welcome to the **Soft Computing Java Library**. This project is a multi-phase implementation of core AI paradigms: **Evolutionary Computing (Genetic Algorithms)**, **Fuzzy Inference Systems**, and **Artificial Neural Networks**.

Built with a focus on clean code, the Strategy pattern, and the Builder pattern, this library is designed to be highly extensible, allowing researchers and developers to plug in custom operators, membership functions, or layer types with minimal friction.

---

## 📑 Table of Contents

1. Genetic Algorithm Engine (Phase 1)
2. Fuzzy Logic System (Phase 2)
3. Neural Network Framework (Phase 3)
4. Design Patterns Used
5. Installation & Requirements

---

## 🧬 Genetic Algorithm Engine (Phase 1)

The GA module provides a robust framework for solving optimization and search problems using Darwinian principles.

### Key Features

* **Representation Agnostic**: Native support for Binary, Integer, Floating-Point, and custom Object-based (Permutation) chromosomes.
* **Pluggable Operators**:
* **Selection**: Tournament, Roulette Wheel.
* **Crossover**: Single-point, Two-point, Uniform, and Order-Based (for permutations).
* **Mutation**: Bit-flip, Swap, and Gaussian Range mutations.
* **Replacement**: Steady-state, Elitism, and Generational.
* **Performance Tracking**: Integrated `PerformanceMetrics` class that exports fitness history to CSV and generates JavaFX visualizations.

### Case Study: CPU Job Scheduling

The library includes a real-world scheduling case study where the GA optimizes the sequence of jobs to minimize total waiting time and maximize CPU utilization.

```java
GeneticAlgorithmEngine ga = new GeneticAlgorithmEngine.Builder(params, fitnessFunction)
    .withSelection(new TournamentSelection(3))
    .withCrossover(new OrderCrossover())
    .build();

```

---

## 🌫️ Fuzzy Logic System (Phase 2)

The Fuzzy Logic module enables "approximate reasoning" for systems where binary true/false logic is insufficient.

### Key Features

* **Linguistic Variables**: Define complex input/output variables with overlapping Fuzzy Sets.
* **Membership Functions**: Includes Triangular and Trapezoidal implementations (extensible via the `MembershipFunction` interface).
* **Multiple Inference Engines**:
* **Mamdani**: For intuitive, human-like linguistic output.
* **Sugeno**: For mathematically efficient, crisp-weighted average output.


* **Dynamic Rule Base**: A powerful `RuleBase` API that supports adding rules via code or parsing them from external strings/files.

### Case Study: Risk Assessment & Tipping

The library demonstrates Mamdani inference for "Driver Risk Assessment" and Sugeno inference for a "Service-to-Tip" calculation system.

```java
FuzzyEngine engine = new FuzzySystemBuilder()
    .setMode(FuzzyEngine.Mode.MAMDANI)
    .addRule(new Rule("IF Speed IS High AND Braking IS Soft THEN Risk IS High"))
    .build();

```

---

## 🧠 Neural Network Framework (Phase 3)

A modular, feedforward Neural Network library designed for supervised learning tasks (Regression and Classification).

### Key Features

* **Layer-Based Architecture**: Add `DenseLayer` objects with configurable input/output dimensions.
* **Activation Functions**: Support for `ReLU`, `Sigmoid`, `Tanh`, and `Linear`.
* **Weight Initializers**: Xavier/He initialization for stable training, alongside Random Uniform options.
* **Training & Optimization**:
* Full implementation of **Backpropagation** using the chain rule.
* Stochastic Gradient Descent (**SGD**) optimizer.
* Support for **MSE** (Regression) and **Cross-Entropy** (Classification) loss functions.


* **Data Utilities**: Includes `Dataset` utilities for Train/Test splitting and Min-Max/Z-Score normalization.

### Case Study: House Price Prediction

A regression model that predicts property values based on features like square footage, bedrooms, and age, utilizing the `NeuralNetworkBuilder` for a clean, fluent API.

```java
NeuralNetwork model = NeuralNetworkBuilder.create()
    .dense(4, 16, "relu")
    .dense(16, 1, "linear")
    .loss("mse")
    .optimizer("sgd", 0.001)
    .build();

```

---

## 🛠 Design Patterns Used

To ensure the library remains a professional-grade tool, we utilized several software design patterns:

* **Builder Pattern**: Used in `GeneticAlgorithmEngine` and `NeuralNetworkBuilder` to manage complex object creation.
* **Strategy Pattern**: Used for Crossover, Mutation, and Activation functions to allow runtime switching of algorithms.
* **Factory Pattern**: Centralized creation of operators and layers via `OperatorFactory` and `LayerFactory`.
* **Registry Pattern**: Used in `GeneInitializerRegistry` to decouple chromosome creation from specific data types.

---

## 🚀 Installation & Requirements

* **Language**: Java 17+
* **Graphics**: JavaFX (required only for GA performance visualization)
* **Build System**: Maven or Gradle (recommended for dependency management)

### Running the Demos

Each phase contains a `Main` or `App` class within the `client` package:

1. **GA**: Run `genetic.client.Main` or `CLIApp`.
2. **Fuzzy**: Run `fuzzylogic.client.MamdaniMain` or `SugenoMain`.
3. **Neural**: Run `neural.client.housepricing.HousePriceMain`.
