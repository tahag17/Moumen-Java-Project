import java.io.File;
import java.io.IOException;

import cleaners.EmploiMaCleaner;
import cleaners.RekrutCleaner;
import cleaners.TalentTecraCleaner;
import config.Config;  // Import the Config class
import scrapers.*;

public class Main {
    public static void main(String[] args) {
        // Initialisation des instances des scrapers
        ForceEmploi forceEmploi = new ForceEmploi();
        Bayt bayt = new Bayt();
        EmploiMa emploiMa = new EmploiMa();
        Rekrut rekrut = new Rekrut();
        TalentTectra talentTectra = new TalentTectra();
        wetech wetech = new wetech();

        try {
            // Exemple de scraping dynamique
//             bayt.scrap();
//             forceEmploi.scrap();
//             wetech.scrap();

            // Scraping et nettoyage pour scrapers.Rekrut
//             rekrut.scrap();
//             performScrapingAndCleaning(rekrut, "Rekrut.json", "Rekrutcleaned_data.json");

            // Scraping et nettoyage pour scrapers.EmploiMa
//            emploiMa.scrap();
//            performScrapingAndCleaning(emploiMa, "emploima_jobs.json","emploimacleaned_data.json");
//
            talentTectra.scrap();
            performScrapingAndCleaning(talentTectra, "talenttectra_jobs.json", "talenttectracleaned_data.json");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void performScrapingAndCleaning(Object scraper, String inputFileName, String outputFileName) throws IOException {
        // Use Config.BASE_PATH instead of the hardcoded path
        File inputFile = new File(Config.BASE_PATH + inputFileName);
        File outputFile = new File(Config.BASE_PATH + outputFileName);

        // Vérifiez que le fichier d'entrée existe avant de nettoyer
        if (!inputFile.exists()) {
            throw new IOException("Le fichier d'entrée " + inputFileName + " est introuvable.");
        }

        if (scraper instanceof Rekrut) {
            RekrutCleaner.cleanData(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        } else if (scraper instanceof EmploiMa) {
            EmploiMaCleaner.cleanData(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        } else if (scraper instanceof TalentTectra) {
            TalentTecraCleaner.cleanData(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }

        System.out.println("Données nettoyées et filtrées avec succès pour : " + inputFileName);
    }
}
