package genetic.util;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceMetrics {
    private long startTime;
    private long endTime;
    private long startMemory;
    private long endMemory;
    private int generationsRun;
    private double finalFitness;

    private final List<Double> bestFitnessHistory = new ArrayList<>();
    private final List<Double> avgFitnessHistory = new ArrayList<>();

    // ✅ Start metrics tracking
    public void start() {
        System.gc();
        Runtime rt = Runtime.getRuntime();
        this.startMemory = rt.totalMemory() - rt.freeMemory();
        this.startTime = System.nanoTime();
    }

    // ✅ Stop metrics tracking
    public void stop() {
        this.endTime = System.nanoTime();
        Runtime rt = Runtime.getRuntime();
        this.endMemory = rt.totalMemory() - rt.freeMemory();
    }

    public void recordFitness(double best, double avg) {
        bestFitnessHistory.add(best);
        avgFitnessHistory.add(avg);
    }

    public void setGenerationsRun(int generationsRun) {
        this.generationsRun = generationsRun;
    }

    public void setFinalFitness(double finalFitness) {
        this.finalFitness = finalFitness;
    }

    public long getElapsedMillis() {
        return (endTime - startTime) / 1_000_000;
    }

    public long getMemoryUsedBytes() {
        return Math.max(0, endMemory - startMemory);
    }

    public void resetHistory() {
        bestFitnessHistory.clear();
        avgFitnessHistory.clear();
    }

    // ✅ Print runtime summary
    public void printReport(String caseName) {
        System.out.println("\n=== PERFORMANCE REPORT: " + caseName + " ===");
        System.out.printf("🕒 Runtime: %.3f seconds%n", getElapsedMillis() / 1000.0);
        System.out.printf("📈 Generations Executed: %d%n", generationsRun);
        System.out.printf("🎯 Final Fitness: %.6f%n", finalFitness);
        System.out.printf("💾 Memory Used: %.2f MB%n", getMemoryUsedBytes() / (1024.0 * 1024.0));
        System.out.println("=============================================\n");
    }

    // ✅ Export fitness data to CSV
    public void exportToCSV(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Generation,BestFitness,AverageFitness\n");
            for (int i = 0; i < bestFitnessHistory.size(); i++) {
                writer.write(String.format("%d,%.6f,%.6f%n",
                        i + 1,
                        bestFitnessHistory.get(i),
                        avgFitnessHistory.get(i)));
            }
            System.out.printf("📊 Fitness data exported to %s%n", filename);
        } catch (IOException e) {
            System.err.println("⚠️ Failed to export fitness history: " + e.getMessage());
        }
    }

    // ✅ Visualize fitness evolution directly
    public void visualize() {
        FitnessVisualizer.bestSeries = bestFitnessHistory;
        FitnessVisualizer.avgSeries = avgFitnessHistory;
        Application.launch(FitnessVisualizer.class);
    }

    /** JavaFX inner class for visualization */
    public static class FitnessVisualizer extends Application {
        static List<Double> bestSeries;
        static List<Double> avgSeries;

        @Override
        public void start(Stage stage) {
            stage.setTitle("Genetic Algorithm Fitness Evolution");

            // === Axes ===
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Generation");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Fitness");

            // === Line Chart ===
            LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Fitness Over Generations");
            lineChart.setAnimated(false);
            lineChart.setCreateSymbols(false); // cleaner lines
            lineChart.setLegendVisible(true);
            lineChart.setLegendSide(Side.BOTTOM);

            // === Data Series ===
            XYChart.Series<Number, Number> bestSeriesChart = new XYChart.Series<>();
            bestSeriesChart.setName("Best Fitness");

            XYChart.Series<Number, Number> avgSeriesChart = new XYChart.Series<>();
            avgSeriesChart.setName("Average Fitness");

            for (int i = 0; i < bestSeries.size(); i++) {
                bestSeriesChart.getData().add(new XYChart.Data<>(i + 1, bestSeries.get(i)));
                avgSeriesChart.getData().add(new XYChart.Data<>(i + 1, avgSeries.get(i)));
            }

            lineChart.getData().addAll(bestSeriesChart, avgSeriesChart);

            // === Layout container ===
            VBox root = new VBox();
            root.getChildren().add(lineChart);
            root.setSpacing(5);
            root.setPadding(new Insets(5, 5, 10, 5));
            root.setAlignment(Pos.CENTER);

            // === Scene ===
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        }

    }
}
