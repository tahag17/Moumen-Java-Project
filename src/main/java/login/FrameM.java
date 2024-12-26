package login;

import cleaners.CleaningService;
import config.Config;
import database.PostgreSQLConnection;
import gui.JobChartService;
import job.JobDetails;
import job.JobRepository;
import job.JobService;
import machine_learning.decisionTreePrediction;
import machine_learning.predictionNaiveBayesEducationLevel;
import mapper.JobDetailsMapper;
import scrapers.EmploiMa;
import scrapers.MJob;
import scrapers.Rekrut;
import scrapers.TalentTectra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FrameM {
    public void setMainFrame(){
        // Create the frame
        JFrame frame = new JFrame("Job Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        // Create panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        // Create instances for services
        //ForceEmploi forceEmploi = new ForceEmploi();
        //Bayt bayt = new Bayt();
        EmploiMa emploiMa = new EmploiMa();
        Rekrut rekrut = new Rekrut();
        TalentTectra talentTectra = new TalentTectra();
        //wetech wetech = new wetech();
        MJob mjob = new MJob();

        CleaningService cleaningService = new CleaningService();
        JobChartService jobChartService = new JobChartService();

        // Add buttons for functionalities
        JButton scrapeButton = new JButton("Scrape and Clean Data");
        JButton mapAndInsertButton = new JButton("Map and Insert Job Details");
        JButton generateChartButton = new JButton("Generate Job Chart");
        JButton ExpLButton = new JButton("Predict Experience Level");
        JButton StudButton = new JButton("Predict Study Level");
        JButton visualizeDatabaseButton = new JButton("Visualize Data from Database");
        JButton exitButton = new JButton("Exit");

        // Add actions for buttons
        scrapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Example: Scrape and clean data for MJob
                    mjob.scrap();
                    //bayt.scrap();
                    //forceEmploi.scrap();
                    //wetech.scrap();
                    emploiMa.scrap();
                    rekrut.scrap();
                    talentTectra.scrap();
                    cleaningService.CleanData(mjob, "mjob.json", "mjob_data.json");
                    cleaningService.CleanData(rekrut, "rekrut.json", "rekrut_data.json");
                    cleaningService.CleanData(emploiMa, "emploima_jobs.json", "emploima_data.json");
                    cleaningService.CleanData(talentTectra, "talenttectra_jobs.json", "talenttectracleaned_data.json");
                    //cleaningService.CleanData(wetech, "WeTech.json", "WeTechdata.json");
                    //cleaningService.CleanData(bayt, "bayt_jobs.json", "bayt_data.json");
                    //cleaningService.CleanData(forceEmploi, "ForceEmploi.json", "ForceEmploi_Data.json");


                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error during scraping/cleaning: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mapAndInsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Path to your cleaned JSON file
                    File jsonFileMjob = new File(Config.BASE_PATH + "mjob_data.json");
                    File jsonFileRekrut = new File(Config.BASE_PATH + "rekrut_data.json");
                    File jsonFileEmploi = new File(Config.BASE_PATH + "emploima_data.json");
                    File jsonFileTalent = new File(Config.BASE_PATH + "talenttectracleaned_data.json");
                    //File jsonFileBayt = new File(Config.BASE_PATH + "bayt_data.json");
                    //File jsonFileForceEmploi = new File(Config.BASE_PATH + "ForceEmploi_Data.json");
                    //File jsonFileWeTech = new File(Config.BASE_PATH + "WeTechdata.json");

                    // Instantiate the mapper
                    JobDetailsMapper mapper = new JobDetailsMapper();

                    // Map the cleaned JSON file to a list of JobDetails
                    java.util.List<JobDetails> jobDetailsListMjob = mapper.mapJsonFileToJobDetails(jsonFileMjob);
                    java.util.List<JobDetails> jobDetailsListRekrut = mapper.mapJsonFileToJobDetails(jsonFileRekrut);
                    java.util.List<JobDetails> jobDetailsListEmploi = mapper.mapJsonFileToJobDetails(jsonFileEmploi);
                    java.util.List<JobDetails> jobDetailsListTalent = mapper.mapJsonFileToJobDetails(jsonFileTalent);
                    //List<JobDetails> jobDetailsListBayt = mapper.mapJsonFileToJobDetails(jsonFileBayt);
                    //List<JobDetails> jobDetailsListForceEmploi = mapper.mapJsonFileToJobDetails(jsonFileForceEmploi);
                    //List<JobDetails> jobDetailsListWeTech = mapper.mapJsonFileToJobDetails(jsonFileWeTech);

                    // Instantiate JobRepository and insert data
                    JobRepository repository = new JobRepository(new PostgreSQLConnection());
                    repository.insertJobDetails(jobDetailsListMjob);
                    repository.insertJobDetails(jobDetailsListRekrut);
                    repository.insertJobDetails(jobDetailsListEmploi);
                    repository.insertJobDetails(jobDetailsListTalent);
                    //repository.insertJobDetails(jobDetailsListBayt);
                    //repository.insertJobDetails(jobDetailsListForceEmploi);
                    //repository.insertJobDetails(jobDetailsListWeTech);

                    System.out.println("Job details and skills inserted successfully!");
                    JOptionPane.showMessageDialog(frame, "Job details and skills inserted successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error during mapping/insertion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        generateChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jobChartService.generateJobCharts();
                    JOptionPane.showMessageDialog(frame, "Job charts generated successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error during chart generation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ExpLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decisionTreePrediction.ModelGui();
            }

        });
        StudButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                predictionNaiveBayesEducationLevel.ModelGui();
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
        panel.add(mapAndInsertButton);
        panel.add(generateChartButton);
        panel.add(ExpLButton);
        panel.add(StudButton);
        panel.add(visualizeDatabaseButton);
        panel.add(exitButton);

        // Add panel to frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
