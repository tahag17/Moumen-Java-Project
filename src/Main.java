import java.io.File;
import java.io.IOException;
import java.util.List;

import cleaners.CleaningService;
import cleaners.EmploiMaCleaner;
import cleaners.RekrutCleaner;
import cleaners.TalentTecraCleaner;
import config.Config;  // Import the Config class
import gui.JobChartService;
import machine_learning.MLService;
import scrapers.*;
import job.JobDetails;
import job.JobRepository;
import mapper.JobDetailsMapper;

public class Main {
    public static void main(String[] args) {
        // Initialisation des instances des scrapers
        ForceEmploi forceEmploi = new ForceEmploi();
        Bayt bayt = new Bayt();
        EmploiMa emploiMa = new EmploiMa();
        Rekrut rekrut = new Rekrut();
        TalentTectra talentTectra = new TalentTectra();
        wetech wetech = new wetech();
        MJob mjob = new MJob();

        // Initialize CleaningService
        CleaningService cleaningService = new CleaningService();

        // Scraping et nettoyage (Statique)
        try {
            // Scraping and cleaning data
            //mjob.scrap();
            cleaningService.CleanData(mjob, "mjob.json", "mjob_data.json");

            // Add other scraping calls here if needed:
            // bayt.scrap();
            // forceEmploi.scrap();
            // wetech.scrap();

        } catch (IOException e) {
            System.err.println("Erreur lors de l'exécution du scraping et du nettoyage : " + e.getMessage());
            e.printStackTrace();
            return; // If scraping/cleaning fails, skip further processing
        }

        // Job details mapping and insertion
        try {
            // Path to your cleaned JSON file
            File jsonFile = new File(Config.BASE_PATH + "mjob_data.json");

            // Instantiate the mapper
            JobDetailsMapper mapper = new JobDetailsMapper();

            // Map the cleaned JSON file to a list of JobDetails
            List<JobDetails> jobDetailsList = mapper.mapJsonFileToJobDetails(jsonFile);

            // Instantiate JobRepository and insert data
            JobRepository repository = new JobRepository();
            repository.insertJobDetails(jobDetailsList);

            System.out.println("Job details and skills inserted successfully!");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du mappage et de l'insertion des détails de l'emploi : " + e.getMessage());
            e.printStackTrace();
        }

//        //Generer la Chart:
//        JobChartService jobChartService = new JobChartService();
//        jobChartService.generateJobCharts();
//
//        // Instantiate the MLService and call the runMLPredictions method
//        MLService mlService = new MLService();
//        try {
//            mlService.runMLPredictions();  // Run the machine learning predictions
//        } catch (Exception e) {
//            e.printStackTrace();  // Handle any exceptions that occur during ML predictions
//        }
    }
}
