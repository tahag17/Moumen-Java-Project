import cleaners.CleaningService;
import config.Config;
import gui.JobChartService;
import job.JobDetails;
import job.JobRepository;
import mapper.JobDetailsMapper;
import scrapers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainGUI {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Job Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

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
        JButton mapAndInsertButton = new JButton("Map and Insert Job Details");
        JButton generateChartButton = new JButton("Generate Job Chart");
        JButton exitButton = new JButton("Exit");

        // Add actions for buttons
        scrapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Example: Scrape and clean data for MJob
                    mjob.scrap();
                    bayt.scrap();
                    forceEmploi.scrap();
                    wetech.scrap();
                    emploiMa.scrap();
                    rekrut.scrap();
                    talentTectra.scrap();
                    cleaningService.CleanData(mjob, "mjob.json", "mjob_data.json");
                    cleaningService.CleanData(rekrut, "rekrut.json", "rekrut_data.json");
                    cleaningService.CleanData(emploiMa, "emploima_jobs.json", "emploima_data.json");
                    cleaningService.CleanData(talentTectra, "talenttectra_jobs.json", "talenttectracleaned_data.json");

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
                    File jsonFileEmploi = new File(Config.BASE_PATH + "emploi_data.json");
                    File jsonFileTalent = new File(Config.BASE_PATH + "talenttectracleaned_data.json");

                    // Instantiate the mapper
                    JobDetailsMapper mapper = new JobDetailsMapper();

                    // Map the cleaned JSON file to a list of JobDetails
                    //todo: clean the mJob file as it should be in order for it to be mapped to jobdetails
//                    List<JobDetails> jobDetailsListMjob = mapper.mapJsonFileToJobDetails(jsonFileMjob);
                    List<JobDetails> jobDetailsListRekrut = mapper.mapJsonFileToJobDetails(jsonFileRekrut);
                    List<JobDetails> jobDetailsListEmploi = mapper.mapJsonFileToJobDetails(jsonFileEmploi);
                    List<JobDetails> jobDetailsListTalent = mapper.mapJsonFileToJobDetails(jsonFileTalent);

                    // Instantiate JobRepository and insert data
                    JobRepository repository = new JobRepository();
//                    repository.insertJobDetails(jobDetailsListMjob);
                    repository.insertJobDetails(jobDetailsListRekrut);
                    repository.insertJobDetails(jobDetailsListEmploi);
                    repository.insertJobDetails(jobDetailsListTalent);

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
        panel.add(exitButton);

        // Add panel to frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
