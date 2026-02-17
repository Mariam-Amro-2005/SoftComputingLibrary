# Neural Network Library

A modular, extensible, and high-performance Java library for building, training, and evaluating Feedforward Neural Networks. This library was developed as part of a Soft Computing course to provide a reusable framework independent of specific use cases, following clean code principles and flexible design patterns.

---

## 🚀 Overview

This library provides a complete pipeline for deep learning, from data preprocessing and normalization to sophisticated model architecture configuration using a fluent Builder API. It supports full backpropagation, various activation functions, weight initialization strategies, and evaluation metrics.

### Key Features

* **Modular Architecture:** Easily swap layers, activation functions, loss functions, and optimizers.
* **Fluent Builder API:** Construct complex neural networks with a readable, chainable syntax.
* **Full Training Pipeline:** Includes forward propagation, backward propagation (chain rule), and mini-batch gradient descent.
* **Data Utilities:** Built-in support for Z-Score and Min-Max normalization, as well as automated train-test splitting.
* **Comprehensive Evaluation:** Metrics for both classification (Accuracy) and regression (MSE).

---

## 🏗️ Core Components

### 1. Neural Network Model (`NeuralNetwork`)

The central coordinator that manages layers and handles the training loop.

* **Training:** Supports configurable `epochs`, `batchSize`, and `learningRate`.
* **Early Stopping:** Can terminate training once a specific loss threshold is reached.
* **Prediction:** Simple API for generating outputs from single or batch inputs.

### 2. Layers (`DenseLayer`)

The primary building block of the network.

* **Fully Connected:** Each neuron in a layer connects to every neuron in the subsequent layer.
* **Gradient Tracking:** Automatically caches inputs and activations during the forward pass to compute gradients efficiently during backpropagation.

### 3. Activation Functions

Used to introduce non-linearity into the model.

* **ReLU (Rectified Linear Unit):** Efficient and avoids vanishing gradients.
* **Sigmoid:** Ideal for binary classification.
* **Tanh:** Hyperbolic tangent for zero-centered data.
* **Linear:** Typically used for regression output layers.

### 4. Weight Initializers

Proper initialization is critical for convergence.

* **Xavier/Glorot:** Optimized for Sigmoid and Tanh activations.
* **He Initializer:** Specifically designed for ReLU-based networks.
* **Random Uniform:** Basic initialization within a specified range.

### 5. Loss Functions

Quantifies the difference between predicted and actual values.

* **MSE (Mean Squared Error):** Standard for regression tasks.
* **Cross-Entropy:** The go-to loss for multi-class classification.

### 6. Optimizers

Updates network weights based on computed gradients.

* **SGD (Stochastic Gradient Descent):** Reliable and classic optimization with adjustable learning rates.

---

## 📊 Evaluation & Metrics (`Accuracy`)

The `Accuracy` class provides static utilities to evaluate model performance after or during training:

* **`classification(double[][] predicted, double[][] actual)`**:
* Computes the percentage of correct predictions.
* Uses `argMax` to determine the predicted class index for multi-class outputs.


* **`mse(double[][] predicted, double[][] actual)`**:
* Calculates the Mean Squared Error.
* Essential for regression problems like house price prediction.



---

## 🛠️ Usage Example

Building and training a model is straightforward using the `NeuralNetworkBuilder`.

```java
// 1. Prepare and Normalize Data
NormalizationStats stats = Dataset.computeNormalizationStats(X_train, NormalizationType.MIN_MAX);
double[][] X_normalized = Dataset.normalize(X_train, stats);

// 2. Build the Network
NeuralNetwork model = NeuralNetworkBuilder.create()
        .loss("mse")
        .optimizer("sgd", 0.01)
        .dense(4, 16, "relu")   // Input layer (4 features) to Hidden (16 neurons)
        .dense(16, 8, "relu")   // Hidden to Hidden
        .dense(8, 1, "linear")  // Output layer (1 neuron for regression)
        .build();

// 3. Train
int epochs = 1000;
int batchSize = 32;
model.train(X_normalized, y_train, epochs, batchSize, 0.001);

// 4. Evaluate
double[][] predictions = model.predict(X_normalized);
double finalMse = Accuracy.mse(predictions, y_train);
System.out.println("Final Training MSE: " + finalMse);

```

---

## 📈 Data Preprocessing (`Dataset`)

The library includes a robust `Dataset` utility to ensure your data is ready for neural network processing:

* **Normalization:** Scaling features prevents certain variables from dominating the gradient updates.
* **Train-Test Split:** `trainTestSplit(double[][] data, double trainFraction, long seed)` ensures you have a dedicated validation set to test generalization.

---

## 🧪 Case Study: House Price Prediction

The library includes a `HousePriceMain` demonstration that applies the NN to a real-world regression problem. It predicts house prices based on features like square footage, number of bedrooms, bathrooms, and age. This case study showcases the full workflow:

1. Loading raw data.
2. Calculating normalization statistics from the training set.
3. Applying those statistics to the test set (to avoid data leakage).
4. Training a deep ReLU-based network.
5. Calculating the final MSE to verify convergence.

---

## ⚙️ Technical Requirements

* **Java:** 17 or higher.
* **Architecture:** Modular design using the **Strategy Pattern** for activations/losses and the **Builder Pattern** for network assembly.
* **Clean Code:** Highly readable, commented code following standard Java naming conventions and SOLID principles.