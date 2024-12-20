package gui;

import database.DataFetcher;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class JobChartService {

    public void generateJobCharts() {
        DataFetcher dataFetcher = new DataFetcher();

        Map<String, Integer> regionData = dataFetcher.fetchRegionData();
        Map<String, Integer> contractTypeData = dataFetcher.fetchContractTypeData();
        Map<String, Integer> experienceData = dataFetcher.fetchExperienceData();
        Map<String, Integer> educationLevelData = dataFetcher.fetchEducationLevelData();
        Map<String, Integer> secteursData = dataFetcher.fetchTopSecteursDActivite();
        Map<String, Integer> hardSkillsData = dataFetcher.fetchTopHardSkills();

        createAndShowPieChart("Job Distribution by Region", regionData);
        createAndShowBarChart("Job Distribution by Contract Type", contractTypeData);
        createAndShowBarChart("Job Distribution by Experience", experienceData); // Bar chart for Experience
        createAndShowPieChart("Job Distribution by Education Level", educationLevelData); // Pie chart for Education Level
        createAndShowBarChart("Top 20 Sectors of Activity", secteursData);
        createAndShowBarChart("Top 20 Hard Skills", hardSkillsData);
    }

    private void createAndShowPieChart(String title, Map<String, Integer> data) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        DefaultPieDataset dataset = new DefaultPieDataset();
        data.forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        frame.add(chartPanel);

        frame.setVisible(true);
    }

    private void createAndShowBarChart(String title, Map<String, Integer> data) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((key, value) -> dataset.addValue(value, "Jobs", key));

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Category",
                "Count",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        frame.add(chartPanel);

        frame.setVisible(true);
    }
}
