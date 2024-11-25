import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rekrut {
    private static final Logger LOGGER = Logger.getLogger(Rekrut.class.getName());
    private static final int MAX_RETRIES = 3; // Retry up to 3 times on timeout
    private final String baseUrl = "https://www.rekrute.com/offres.html";
    private final String urlParams = "?workExperienceId%5B0%5D=1&workExperienceId%5B1%5D=2";
    private final String outputFilePath = "C:\\Users\\HP\\Documents\\GitHub\\Moumen-Java-Project\\outputs\\rekrut.json";

    public Rekrut() {}

    public void scrap() {
        int currentPage = 1;
        boolean hasNextPage = true;

        try {
            // Ensure directory exists
            File outputFile = new File(outputFilePath);
            File outputDir = outputFile.getParentFile();
            if (!outputDir.exists()) {
                if (outputDir.mkdirs()) {
                    LOGGER.info("Created missing directory: " + outputDir.getAbsolutePath());
                }
            }

            // Create list to hold job data
            List<Job> jobList = new ArrayList<>();

            while (hasNextPage) {
                String url = constructUrl(currentPage);
                LOGGER.info("Scraping Page: " + currentPage);

                Document doc = null;
                int attempt = 0;

                while (attempt < MAX_RETRIES) {
                    try {
                        doc = Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                                .timeout(10000)
                                .get();
                        break;
                    } catch (IOException e) {
                        attempt++;
                        LOGGER.warning("Timeout occurred (Attempt " + attempt + "/" + MAX_RETRIES + ")");
                        if (attempt == MAX_RETRIES) {
                            LOGGER.severe("Failed to fetch page " + currentPage + " after " + MAX_RETRIES + " attempts. Skipping...");
                            return; // Stop scraping if page fails to load after retries
                        }
                    }
                }

                Elements jobs = doc.select(".section");
                if (jobs.isEmpty()) {
                    LOGGER.warning("No jobs found on page " + currentPage);
                    break;
                }

                for (Element job : jobs) {
                    String title = job.select("h2 > a.titreJob").text().trim();
                    String activity = job.select(".holder > .info > ul > li:contains(Secteur d\\'activité) a").text().trim();
                    String fonction = job.select(".holder > .info > ul > li:contains(Fonction) a").text().trim();
                    String experience = job.select(".holder > .info > ul > li:contains(Expérience requise) a").text().trim();
                    String niveauEtude = job.select(".holder > .info > ul > li:contains(Niveau d\\'étude demandé) a").text().trim();

                    if (title.isEmpty()) {
                        LOGGER.warning("Job title is missing on page " + currentPage);
                        continue;
                    }

                    // Create a Job object and add it to the list
                    Job jobObject = new Job(title, experience, fonction, activity, niveauEtude);
                    jobList.add(jobObject);
                }

                Element nextPageElement = doc.selectFirst(".next");
                hasNextPage = nextPageElement != null && !nextPageElement.hasClass("disabled");
                if (hasNextPage) {
                    currentPage++;
                }
            }

            // Serialize the list of jobs to JSON and write to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(outputFilePath)) {
                gson.toJson(jobList, writer);
                LOGGER.info("Job data successfully written to JSON file.");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while scraping jobs: ", e);
        }
    }

    private String constructUrl(int page) {
        if (page == 1) {
            return baseUrl + "?s=1&p=1&o=1" + urlParams;
        } else {
            return baseUrl + "?p=" + page + "&s=1&o=1" + urlParams;
        }
    }

    // Job class to represent job data
    public static class Job {
        private String title;
        private String experience;
        private String function;
        private String activity;
        private String educationLevel;

        public Job(String title, String experience, String function, String activity, String educationLevel) {
            this.title = title;
            this.experience = experience;
            this.function = function;
            this.activity = activity;
            this.educationLevel = educationLevel;
        }

        // Getters and setters for Gson serialization (optional, depends on your requirements)
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getEducationLevel() {
            return educationLevel;
        }

        public void setEducationLevel(String educationLevel) {
            this.educationLevel = educationLevel;
        }
    }
}