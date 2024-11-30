package cleaners;

import config.Config;
import scrapers.EmploiMa;
import scrapers.Rekrut;
import scrapers.TalentTectra;

import java.io.File;
import java.io.IOException;

public class CleaningService {
    public void CleanData(Object scraper, String inputFileName, String outputFileName) throws IOException {
        File inputFile = new File(Config.BASE_PATH + inputFileName);
        File outputFile = new File(Config.BASE_PATH + outputFileName);

                if (!inputFile.exists()) {
            throw new IOException("Input file " + inputFileName + " not found.");
        }

                if (scraper instanceof Rekrut) {
            RekrutCleaner.cleanData(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        } else if (scraper instanceof EmploiMa) {
            EmploiMaCleaner.cleanData(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        } else if (scraper instanceof TalentTectra) {
            TalentTecraCleaner.cleanData(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        } else {
            throw new IllegalArgumentException("Unsupported scraper type: " + scraper.getClass().getName());
        }

        System.out.println("Data cleaned and filtered successfully for: " + inputFileName);
    }
    }

