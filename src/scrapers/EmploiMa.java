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
                String url = baseUrl + "?page=" + currentPage;//add to data base
                Document document = Jsoup.connect(url).get();
                String siteName = "EmploiMa";

                System.out.println("Scraping Page: " + currentPage);

                Elements jobs = document.select(".card-job-detail");

                for (Element job : jobs) {
                    String title = job.select("h3 > a").text();

                    String niveauEtude = job.select("ul > li:contains(Niveau d\\´études requis) strong").text();
                    String niveauExperience = job.select("ul > li:contains(Niveau d\\'expérience) strong").text();
                    String competence = job.select("ul > li:contains(Compétences clés) strong").text();//ou activité
                    String datPub=job.select("ul > time").text();
                    String siteEntreprise = job.select("a[title][href]").attr("href");
                    String nomEntreprise =job.select("a[title]").text();
                    String description=job.select(".card-job-description > p").text();
                    String region=job.select("ul > li:contains(Région de) strong").text();
                    String typeContrat =job.select("ul > li:contains(Contrat proposé) strong").text();
                    Job jobObj = new Job(title, niveauEtude, niveauExperience, competence,datPub, siteEntreprise, nomEntreprise,description, region, typeContrat,url,"EmploiMa");
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



    public class Job {
        private String url;
        private String webSite;
        private String title;
        private String niveauEtude;
        private String niveauExperience;
        private String competence;
        private String datPub;
        private String siteEntreprise;
        private String nomEntreprise;
        private String description;
        private String region;
        private String typeContrat;

        // Constructor
        public Job(String title, String niveauEtude, String niveauExperience, String competence,
                   String datPub, String siteEntreprise, String nomEntreprise,
                   String description, String region, String typeContrat, String url, String webSite) {
            this.title = title;
            this.niveauEtude = niveauEtude;
            this.niveauExperience = niveauExperience;
            this.competence = competence;
            this.datPub = datPub;
            this.siteEntreprise = siteEntreprise;
            this.nomEntreprise = nomEntreprise;
            this.description = description;
            this.region = region;
            this.typeContrat = typeContrat;
            this.url = url;
            this.webSite = webSite;
        }

        // Getters and Setters
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

        public String getDatPub() {
            return datPub;
        }

        public void setDatPub(String datPub) {
            this.datPub = datPub;
        }

        public String getSiteEntreprise() {
            return siteEntreprise;
        }

        public void setSiteEntreprise(String siteEntreprise) {
            this.siteEntreprise = siteEntreprise;
        }

        public String getNomEntreprise() {
            return nomEntreprise;
        }

        public void setNomEntreprise(String nomEntreprise) {
            this.nomEntreprise = nomEntreprise;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getTypeContrat() {
            return typeContrat;
        }

        public void setTypeContrat(String typeContrat) {
            this.typeContrat = typeContrat;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWebSite() {
            return webSite;
        }

        public void setWebSite(String webSite) {
            this.webSite = webSite;
        }
    }
    public static void main(String[] args) {
        EmploiMa m = new EmploiMa();
        m.scrap();
    }
}
