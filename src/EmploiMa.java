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

public class EmploiMa {

    public void scrap() {
        String baseUrl = "https://www.emploi.ma/recherche-jobs-maroc";
        int currentPage = 1;
        boolean hasNextPage = true;
        List<Job> jobList = new ArrayList<>();

        // Set the path to the same directory as rekrut.txt
        String outputDirPath = "C:\\Users\\HP\\Documents\\GitHub\\Moumen-Java-Project\\outputs\\";
        String outputFilePath = outputDirPath + "emploima_jobs.json";

        try {
            // Loop through pages until there are no more "Next" links
            while (hasNextPage) {
                String url = baseUrl + "?page=" + currentPage;
                Document document = Jsoup.connect(url).get();

                System.out.println("Scraping Page: " + currentPage);

                // Select job postings on the current page
                Elements jobs = document.select(".card-job-detail");

                // Loop through each job posting
                for (Element job : jobs) {
                    // Extract job title
                    String title = job.select("h3 > a").text();

                    // Extract experience level and competence
                    String niveauEtude = job.select("ul > li:contains(Niveau d\\´études requis) strong").text();
                    String niveauExperience = job.select("ul > li:contains(Niveau d\\'expérience) strong").text();
                    String competence = job.select("ul > li:contains(Compétences clés) strong").text();

                    // Add job to the list
                    Job jobObj = new Job(title, niveauEtude, niveauExperience, competence);
                    jobList.add(jobObj);
                }

                // Check if there's a next page
                Element nextPageLink = document.selectFirst(".pager-next a");
                if (nextPageLink != null) {
                    currentPage++;
                } else {
                    hasNextPage = false;
                }
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

    public static void main(String[] args) {
        EmploiMa emploiMa = new EmploiMa();
        emploiMa.scrap();
    }

    // Job class to represent a job posting
    public static class Job {
        private String title;
        private String niveauEtude;
        private String niveauExperience;
        private String competence;

        public Job(String title, String niveauEtude, String niveauExperience, String competence) {
            this.title = title;
            this.niveauEtude = niveauEtude;
            this.niveauExperience = niveauExperience;
            this.competence = competence;
        }

        // Getters and setters (if needed)
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNiveauEtude() {
            return niveauEtude;
        }

        public void setNiveauEtude(String niveauEtude) {
            this.niveauEtude = niveauEtude;
        }

        public String getNiveauExperience() {
            return niveauExperience;
        }

        public void setNiveauExperience(String niveauExperience) {
            this.niveauExperience = niveauExperience;
        }

        public String getCompetence() {
            return competence;
        }

        public void setCompetence(String competence) {
            this.competence = competence;
        }
    }
}
