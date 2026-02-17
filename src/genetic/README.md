# Soft Computing Library: Genetic Algorithms Module 🧬

The **Genetic Algorithms (GA) Module** is a high-performance, modular Java framework designed to solve complex optimization problems. Built with a focus on **Software Design Patterns**, it offers a "pluggable" architecture where every component—from individual genes to global replacement strategies—is fully customizable and interchangeable.

---

## 🏗 Core Architecture: The "Genetic" Hierarchy

The module follows a strictly decoupled data structure, ensuring that the evolution engine doesn't need to know the specifics of the data it is optimizing. This is achieved through a **Registry-based Initialization** system.

### 1. The Generic Gene Interface (`Gene<T>`)

Every gene implements a common interface, allowing for a variety of data types while maintaining a unified API for mutation and crossover.

* **Binary:** Standard boolean-based genes.
* **Integer / Floating Point:** Numerical genes for continuous optimization.
* **JOB:** A specialized gene type created for combinatorial scheduling problems.

### 2. The Chromosome Wrapper

A `Chromosome` is a self-contained unit of evolution. Instead of hardcoding how genes are created, it delegates this task to the **`GeneInitializerRegistry`**.

* **`List<Gene<?>>`:** The genetic blueprint, initialized dynamically via the Registry.
* **`fitness`:** The calculated quality of the individual.
* **`RepresentationType`:** An enum (BINARY, INTEGER, FLOATING_POINT, JOB) that acts as a key to retrieve the correct `GeneInitializer`.



---

## 🛠 Evolutionary Strategies (The Strategy Pattern)

All operators leverage common interfaces, allowing you to switch heuristics with a single line of code.

| Category | Implemented Strategies |
| --- | --- |
| **Selection** | RouletteWheelSelection, TournamentSelection |
| **Crossover** | NPointCrossover, OrderCrossover, UniformCrossover |
| **Mutation** | BitFlipMutation, FloatingPointMutation, SwapMutation |
| **Replacement** | ElitismReplacement, GenerationalReplacement, SteadyStateReplacement |

---

## 🎮 Interactive CLI Configurator

For users who prefer a hands-on approach over coding, the module includes a **`CLIApp`**. This interactive tool walks you through the configuration of a Genetic Algorithm step-by-step:

1. **Parameter Tuning:** Set population size, generation limits, and mutation ranges.
2. **Strategy Selection:** Choose your preferred selection, crossover, and replacement methods via a menu.
3. **Real-time Evolution:** Watch the engine run and view a final performance report immediately.

---

## 💻 Usage Example: CPU Job Scheduling

The module demonstrates its power through a built-in case study optimizing CPU task sequences to minimize execution time.

```java
// Configure via the Builder Pattern
GAParameters params = new GAParameters.Builder()
        .setPopulationSize(100)
        .setGenerations(500)
        .setRepresentationType("JOB")
        .build();

// Inject specific strategies
GeneticAlgorithmEngine ga = new GeneticAlgorithmEngine.Builder(params, new CPUJobScheduling())
        .withSelection(new RouletteWheelSelection())
        .withCrossover(new OrderCrossover()) // Ideal for sequences
        .withReplacement(new ElitismReplacement())
        .build();

// Run and monitor
PerformanceMetrics metrics = new PerformanceMetrics();
metrics.start();
Chromosome best = ga.run(metrics);
metrics.stop();

```

---

## 📊 Performance & Visualization

The `PerformanceMetrics` class ensures you never lose track of your model's progress:

* **CSV Export:** Automatically saves fitness history for external analysis in Excel or Python.
* **JavaFX Plotting:** Generates live line charts comparing **Best Fitness vs. Average Fitness** across generations.
* **Resource Tracking:** Monitors total execution time and heap memory usage delta.

---

## 🛠 Extensibility: Adding Custom Representations

The library is designed to be a "plug-and-play" framework. You can add support for new problem domains (like Image Evolution or Traveling Salesperson) in three steps without modifying the core engine code:

1. **Implement `Gene<T>`:** Create a class for your new data type.
2. **Define `GeneInitializer`:** Create a functional strategy (or lambda) that defines how to generate a list of your custom genes.
3. **Register:** Map a new `RepresentationType` enum value to your initializer in the `GeneInitializerRegistry`.

```java
// Example: Registering a custom 'Color' representation
GeneInitializerRegistry.register(RepresentationType.COLOR, (length, random) -> {
    List<Gene<?>> genes = new ArrayList<>();
    for (int i = 0; i < length; i++) genes.add(new ColorGene(random));
    return genes;
});

```
