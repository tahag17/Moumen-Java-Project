import java.io.File;
import java.io.IOException;

import cleaners.CleaningService;
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
        //Initialize CleaningService
        CleaningService cleaningService = new CleaningService();

        // scraping dynamique
//             bayt.scrap();
//             forceEmploi.scrap();
//             wetech.scrap();

        try {
            // Scraping et nettoyage (Statique)
            rekrut.scrap();
            cleaningService.CleanData(rekrut, "Rekrut.json", "Rekrutcleaned_data.json");

            emploiMa.scrap();
            cleaningService.CleanData(emploiMa, "emploima_jobs.json", "emploimacleaned_data.json");
//
            talentTectra.scrap();
            cleaningService.CleanData(talentTectra, "talenttectra_jobs.json", "talenttectracleaned_data.json");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
