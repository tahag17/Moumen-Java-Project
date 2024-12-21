package scrapers;

import com.google.gson.Gson;
import config.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.io.File;

public class MJob {
    List<Map<String, String>> jobDataList = new ArrayList<>();

    public void scrap() {
        String url = "https://m-job.ma/recherche/informatique";
        List<Job> jobList = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", Config.CHROME_DRIVER_PATH);
        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        String outputDirPath = Config.BASE_PATH;
        String outputFilePath = outputDirPath + "mjob.json";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Open the site
            driver.get(url);
            wait.until((WebDriver d) -> {
                JavascriptExecutor js = (JavascriptExecutor) d;
                String readyState = js.executeScript("return document.readyState").toString();
                return readyState.equals("complete");
            });

            scrollPage(driver);

            // Main loop to iterate through job offers
            List<WebElement> jobOffers = driver.findElements(By.cssSelector(".btn.featured-btn.waves-effect.waves-light"));
            for (int i = 0; i < jobOffers.size(); i++) {
                try {
                    // Refresh job offers list after navigating back
                    jobOffers = driver.findElements(By.cssSelector(".btn.featured-btn.waves-effect.waves-light"));

                    // Interact with the current offer
                    WebElement offer = jobOffers.get(i);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", offer);
                    wait.until(ExpectedConditions.elementToBeClickable(offer));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", offer);

                    // Scrape job details using CSS selectors
                    WebElement entrepriseNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div h3")));
                    String entrepriseName = entrepriseNameElement.getText();

                    WebElement contratElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div ul li:nth-child(2) h3")));
                    String contrat = contratElement.getText();

                    WebElement salaireElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div ul li:nth-child(3) h3")));
                    String salaire = salaireElement.getText();

                    WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div h1")));
                    String title = titleElement.getText();

                    WebElement niveauEtudeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div:nth-child(7)")));
                    String niveauEtude = niveauEtudeElement.getText();

                    WebElement activityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div:nth-child(4)")));
                    String activity = activityElement.getText();

                    WebElement niveauExperienceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div:nth-child(6)")));
                    String niveauExperience = niveauExperienceElement.getText();

                    WebElement metierElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div:nth-child(5)")));
                    String metier = metierElement.getText();

                    WebElement langElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div:nth-child(8)")));
                    String lang = langElement.getText();

                    WebElement regionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section div div div div span")));
                    String region = regionElement.getText();

                    Job jobObj = new Job(url, "MJob", entrepriseName, contrat, salaire, title, niveauEtude, activity, niveauExperience, metier, lang, region);
                    jobList.add(jobObj);

                    // Navigate back to the job list
                    driver.navigate().back();
                    wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".btn.featured-btn.waves-effect.waves-light")));

                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element exception, retrying...");
                    i--; // Retry the same index
                } catch (Exception e) {
                    System.out.println("Error processing job offer: " + e.getMessage());
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

        } catch (Exception e) {
            System.out.println("Error scraping job: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    private void scrollPage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(2000); // Allow time for content to load
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long newHeight = (Long) js.executeScript("return document.body.scrollHeight");
            if (newHeight == lastHeight) {
                break;
            }
            lastHeight = newHeight;
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
        private String teletravail;  // Add missing 'teletravail' field

        // Constructor
        public Job(String url, String site, String title, String desc, String date, String region,
                   String typeC, String activity, String experience, String educationLevel, String function, String teletravail) {
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
            this.teletravail = teletravail;
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

        public String getTeletravail() {
            return teletravail;
        }

        public void setTeletravail(String teletravail) {
            this.teletravail = teletravail;
        }
    }

    public static void main(String[] args) {
        MJob job = new MJob();
        job.scrap();
    }
}
