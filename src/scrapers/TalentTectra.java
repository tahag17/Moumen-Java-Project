package scrapers;

import config.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TalentTectra {
    public void scrap() {
        // Initialize a JSON array to store job objects
        JsonArray jobList = new JsonArray();

        String outputDirPath = Config.BASE_PATH;
        String outputFilePath = outputDirPath + "talenttectra_jobs.json";

        try {
            String url = "https://talent-tectra.com/s3/annonces";
            Document doc = Jsoup.connect(url).get();
            Elements jobs = doc.select(".card-body");

            for (Element job : jobs) {
                // Extract and format job data
                String sActivity = job.select("p.mb-0").first().text();
                String expe = job.select("p.mb-0").first().nextElementSibling().text();
                String nEtude = job.select("p.mb-0").first().nextElementSibling().nextElementSibling().text();
                String function = job.select("p.mb-0").first().nextElementSibling().nextElementSibling().nextElementSibling().text();

                // Create a JSON object for the job
                JsonObject jobObj = new JsonObject();
                jobObj.addProperty("experienceLevel", expe);
                jobObj.addProperty("function", function);
                jobObj.addProperty("activity", sActivity);
                jobObj.addProperty("niveauEtude", nEtude);

                // Add the job object to the list
                jobList.add(jobObj);
            }

            // Ensure directory exists
            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Write JSON to file
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                fileWriter.write(jobList.toString());
                System.out.println("Job data saved to " + outputFilePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
