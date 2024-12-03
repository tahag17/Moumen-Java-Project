package gui;

import database.DataFetcher;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class JobChartService {

    public void generateJobCharts() {
        // Fetch data from the database
        DataFetcher dataFetcher = new DataFetcher();
        Map<String, Integer> educationLevelData = dataFetcher.fetchEducationLevelData();
        Map<String, Integer> experienceLevelData = dataFetcher.fetchExperienceLevelData();

        // Create and display the first chart for education level
        createAndShowChartWindow("Job Education Level Distribution", educationLevelData);

        // Create and display the second chart for experience level
        createAndShowChartWindow("Job Experience Level Distribution", experienceLevelData);
    }

    // Method to create and display a chart window
    private void createAndShowChartWindow(String title, Map<String, Integer> data) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allow closing individual windows
        frame.setSize(800, 600);

        // Create a donut chart with the provided data
        JFreeChart chart = createDonutChart(createDataset(data));

        // Add the chart to a panel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Create a dataset for the pie chart
    private DefaultPieDataset createDataset(Map<String, Integer> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Populate dataset with data from the map
        int totalJobs = data.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label = entry.getKey() == null ? "Unknown" : entry.getKey();
            double percentage = (entry.getValue() / (double) totalJobs) * 100;
            dataset.setValue(label, percentage);
        }

        return dataset;
    }

    // Create a pie chart with a donut appearance
    private JFreeChart createDonutChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                null,   // No chart title here; the window title suffices
                dataset,
                true,   // Show legend
                true,   // Tooltips
                false   // URLs
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setCircular(true);
        plot.setLabelGap(0.02);

        return chart;
    }
}
