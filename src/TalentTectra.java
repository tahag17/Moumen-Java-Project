import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TalentTectra {
    public void scrap() {
        List<JobDetails> jobList = new ArrayList<>();

        // Set the path to the same directory as rekrut.txt
        String outputDirPath = "C:\\Users\\Electronic Store\\Downloads\\projectos\\projectos\\outputs\\";
        String outputFilePath = outputDirPath + "talenttectra_jobs.json";

        try {
            String url = "https://talent-tectra.com/s3/annonces";
            Document doc = Jsoup.connect(url).get();
            Elements jobs = doc.select(".card-body");

            for (Element job : jobs) {
                // Extract job title (not used here but you could store it if needed)
                String title = job.select("h5").text();

                // Extract secteur d'activité (sector)
                Element secteurActivity = job.select("p.mb-0").first();
                String sActivity = secteurActivity.text();

                // Extract experience level
                Element experience = secteurActivity.nextElementSibling();
                String expe = experience.text();

                // Extract niveau d'etude (education level)
                Element niveauEtude = experience.nextElementSibling();
                String nEtude = niveauEtude.text();

                // Extract fonction (function/role)
                Element fonction = niveauEtude.nextElementSibling();
                String function = fonction.text();

                // Create JobDetails object and add it to the list
                JobDetails jobObj = new JobDetails(expe, function, sActivity, nEtude);
                jobList.add(jobObj);
            }

            // Convert the job list to JSON
            Gson gson = new Gson();
            String json = gson.toJson(jobList);

            // Ensure directory exists
            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Write JSON to file
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                fileWriter.write(json);
                System.out.println("Job data saved to " + outputFilePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
