// Import statements remain unchanged
import cleaners.CleaningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import database.PostgreSQLConnection;
import gui.JobChartService;
import job.JobDetails;
import job.JobRepository;
import job.JobService;
import machine_learning.decisionTreePrediction;
import mapper.JobDetailsMapper;
import scrapers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainGUI {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Job Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        // Create instances for services
        ForceEmploi forceEmploi = new ForceEmploi();
        Bayt bayt = new Bayt();
        EmploiMa emploiMa = new EmploiMa();
        Rekrut rekrut = new Rekrut();
        TalentTectra talentTectra = new TalentTectra();
        wetech wetech = new wetech();
        MJob mjob = new MJob();

        CleaningService cleaningService = new CleaningService();
        JobChartService jobChartService = new JobChartService();

        // Add buttons for functionalities
        JButton scrapeButton = new JButton("Scrape and Clean Data");
        JButton visualizeDataButton = new JButton("Visualize Cleaned Data");
        JButton mapAndInsertButton = new JButton("Map and Insert Job Details");
        JButton generateChartButton = new JButton("Generate Job Chart");
        JButton visualizeDatabaseButton = new JButton("Visualize Data from Database");
        JButton exitButton = new JButton("Exit");

        // Add actions for buttons
        scrapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Your existing implementation
            }
        });

        visualizeDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Your existing implementation
            }
        });

        mapAndInsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Your existing implementation
            }
        });

        generateChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Your existing implementation
            }
        });

        visualizeDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve data from the database
                    JobRepository repository = new JobRepository(new PostgreSQLConnection());
                    JobService service = new JobService();
                    List<JobDetails> jobDetailsList = repository.getAllJobDetails();

                    // Display the data in a text area
                    StringBuilder dataBuilder = new StringBuilder();
                    for (JobDetails job : jobDetailsList) {
                        dataBuilder.append(service.formatJobDetails(job)).append("\n\n");
                    }

                    JTextArea textArea = new JTextArea(20, 50);
                    textArea.setText(dataBuilder.toString());
                    textArea.setEditable(false);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    JOptionPane.showMessageDialog(frame, scrollPane, "Database Data", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error retrieving data from database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

                    exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Add buttons to panel
        panel.add(scrapeButton);
        panel.add(visualizeDataButton);
        panel.add(mapAndInsertButton);
        panel.add(generateChartButton);
        panel.add(visualizeDatabaseButton);
        panel.add(exitButton);

        // Add panel to frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
