package scrapers;

import config.Config;
import job.JobDetails;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TalentTectra implements scrappersInterface{
    public void scrap() {
        List<Job> jobList = new ArrayList<>();

        String outputDirPath = Config.BASE_PATH;
        String outputFilePath = outputDirPath + "talenttectra_jobs.json";

        // Setting up the ChromeDriver
        System.setProperty("webdriver.chrome.driver", Config.CHROME_DRIVER_PATH);

        // ChromeOptions for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        String url="https://talent-tectra.com/s3/annonces";
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://talent-tectra.com/s3/annonces");

        int maxClicks = 5; // Max number of clicks for loading more jobs
        int clickCount = 0;

        // Loop to click on the "Load More" button
        while (clickCount < maxClicks) {
            try {
                // Find the "Load More" button and click it
                WebElement button = driver.findElement(By.cssSelector("button.btn"));
                button.click();
                TimeUnit.SECONDS.sleep(3); // Wait for the page to load

                clickCount++; // Increment the click count
            } catch (Exception e) {
                System.out.println("The button could not be found or clicked.");
                break; // Exit the loop if the button is not found
            }
        }

        // After loading the data, get the page's HTML
        String pageSource = driver.getPageSource();
        driver.quit(); // Close the browser

        try {
            // Parse the HTML content using Jsoup
            Document doc = Jsoup.parse(pageSource);
            Elements jobs = doc.select(".card-body");

            for (Element job : jobs) {
                String desc = job.select(".card-text").text();
                Element h6Element = job.select("h6.card-subtitle").first();
                String h6Text = h6Element.text(); // Le texte de l'élément <h6> : "17-12-2024 . Casablanca et région"
                String[] parts = h6Text.split(" . ");
                String date = parts[0];
                String region = parts[1];

                Element contractType = job.select("p > span:nth-of-type(2)").first();
                String contract = contractType.text();

                String title = job.select("h5").text(); // Get the job title

                // Extract sector activity (secteur d'activité)
                Element secteurActivity = job.select("p.mb-0").first();
                String sActivity = secteurActivity.text();

                // Extract experience level
                Element experience = secteurActivity.nextElementSibling();
                String expe = experience.text();

                // Extract education level (niveau d'etude)
                Element niveauEtude = experience.nextElementSibling();
                String nEtude = niveauEtude.text();

                // Extract function/role (fonction)
                Element fonction = niveauEtude.nextElementSibling();
                String function = fonction.text();

                Job jobObj = new Job(url,"TalentTectra",title,desc,date,region,contract,sActivity,expe,nEtude,function);
                jobList.add(jobObj);
            }

            // Convert the job list to JSON
            Gson gson = new Gson();
            String json = gson.toJson(jobList);

            // Ensure the directory exists
            File outputDir = new File(outputDirPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();  // Create the directory if it doesn't exist
            }

            // Write the JSON data to a file
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
        private String site;
        private String title;
        private String desc;
        private String date;
        private String region;
        private String experience;
        private String function;
        private String activity;
        private String educationLevel;
        private String typeC;

        // Constructor
        public Job(String url, String site, String title, String desc, String date, String region,
                   String typeC, String activity, String experience, String educationLevel, String function) {
            this.url = url;
            this.site = site;
            this.title = title;
            this.desc = desc;
            this.date = date;
            this.region = region;
            this.experience = experience;
            this.function = function;
            this.activity = activity;
            this.educationLevel = educationLevel;
            this.typeC = typeC;
        }

        // Getters and Setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
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

        public String getTypeC() {
            return typeC;
        }

        public void setTypeC(String typeC) {
            this.typeC = typeC;
        }
    }

    public static void main(String[] args) {
        TalentTectra scraper = new TalentTectra();
        scraper.scrap();
    }
}
