import java.io.File;
import java.io.IOException;

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
            // bayt.scrap();
            // forceEmploi.scrap();
            // wetech.scrap();

            // Scraping et nettoyage pour Rekrut
             //rekrut.scrap();
              //performScrapingAndCleaning(rekrut, "Rekrut.json", "Rekrutcleaned_data.json");

            // Scraping et nettoyage pour EmploiMa
            emploiMa.scrap();
             performScrapingAndCleaning(
                    emploiMa,
                    "emploima_jobs.json",
                    "emploimacleaned_data.json"
            );

            /** talentTectra.scrap();
            performScrapingAndCleaning(
                    talentTectra,
                    "talenttectra_jobs.json",
                    "talenttecracleaned_data.json"
            ); */
        } catch (IOException e) {
            System.err.println("Erreur lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode utilitaire pour le scraping et le nettoyage des données
     *
     * @param scraper       Instance du scraper
     * @param inputFileName Nom du fichier d'entrée (brut)
     * @param outputFileName Nom du fichier de sortie (nettoyé)
     */
    private static void performScrapingAndCleaning(Object scraper, String inputFileName, String outputFileName) throws IOException {
        String basePath = "C:\\Users\\HP\\Documents\\GitHub\\Moumen-Java-Project\\outputs\\";
        File inputFile = new File(basePath + inputFileName);
        File outputFile = new File(basePath + outputFileName);

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
