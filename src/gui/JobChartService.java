package gui;

import database.DataFetcher;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class JobChartService {

    public void generateJobCharts() {
        // Fetch data from the database
        DataFetcher dataFetcher = new DataFetcher();
        Map<String, Integer> educationLevelData = dataFetcher.fetchEducationLevelData();
        Map<String, Integer> experienceLevelData = dataFetcher.fetchExperienceLevelData();

        // Create and display the first chart for education level (still a donut chart)
        createAndShowDonutChartWindow("Job Education Level Distribution", educationLevelData);

        // Create and display the second chart for experience level (bar chart)
        createAndShowBarChartWindow("Job Experience Level Distribution", experienceLevelData);
    }

    // Method to create and display a donut chart window
    private void createAndShowDonutChartWindow(String title, Map<String, Integer> data) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a donut chart with the provided data
        JFreeChart chart = createDonutChart(createPieDataset(data));

        // Add the chart to a panel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Method to create and display a bar chart window
    private void createAndShowBarChartWindow(String title, Map<String, Integer> data) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a bar chart with the provided data
        JFreeChart chart = createBarChart(createCategoryDataset(data), title);

        // Add the chart to a panel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Create a dataset for the pie chart
    private org.jfree.data.general.DefaultPieDataset createPieDataset(Map<String, Integer> data) {
        org.jfree.data.general.DefaultPieDataset dataset = new org.jfree.data.general.DefaultPieDataset();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String label = entry.getKey() == null ? "Unknown" : entry.getKey();
            dataset.setValue(label, entry.getValue());
        }

        return dataset;
    }

    // Create a dataset for the bar chart
    private DefaultCategoryDataset createCategoryDataset(Map<String, Integer> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String category = entry.getKey() == null ? "Unknown" : entry.getKey();
            dataset.addValue(entry.getValue(), "Jobs", category);
        }

        return dataset;
    }

    // Create a pie chart with a donut appearance
    private JFreeChart createDonutChart(org.jfree.data.general.DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                null,   // No chart title here; the window title suffices
                dataset,
                true,   // Show legend
                true,   // Tooltips
                false   // URLs
        );

        org.jfree.chart.plot.PiePlot plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setCircular(true);
        plot.setLabelGap(0.02);

        return chart;
    }

    // Create a bar chart
    private JFreeChart createBarChart(DefaultCategoryDataset dataset, String title) {
        return ChartFactory.createBarChart(
                title,       // Chart title
                "Experience Level", // Domain axis label
                "Job Count",         // Range axis label
                dataset,              // Data
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,                 // Include legend
                true,                 // Tooltips
                false                 // URLs
        );
    }
}
