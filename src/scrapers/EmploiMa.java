package scrapers;

import config.Config;
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

        String outputDirPath = Config.BASE_PATH;
        String outputFilePath = outputDirPath + "emploima_jobs.json";

        try {
                        while (hasNextPage) {
                String url = baseUrl + "?page=" + currentPage;
                Document document = Jsoup.connect(url).get();

                System.out.println("Scraping Page: " + currentPage);

                                Elements jobs = document.select(".card-job-detail");

                                for (Element job : jobs) {
                                        String title = job.select("h3 > a").text();

                                        String niveauEtude = job.select("ul > li:contains(Niveau d\\´études requis) strong").text();
                    String niveauExperience = job.select("ul > li:contains(Niveau d\\'expérience) strong").text();
                    String competence = job.select("ul > li:contains(Compétences clés) strong").text();

                                        Job jobObj = new Job(title, niveauEtude, niveauExperience, competence);
                    jobList.add(jobObj);
                }

                                Element nextPageLink = document.selectFirst(".pager-next a");
                if (nextPageLink != null) {
                    currentPage++;
                } else {
                    hasNextPage = false;
                }
            }

                        Gson gson = new Gson();
            String json = gson.toJson(jobList);

                        File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();             }

                        try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                fileWriter.write(json);
                System.out.println("Job data saved to " + outputFilePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



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
