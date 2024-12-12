package scrapers;
import com.google.gson.Gson;
import config.Config;
import job.JobDetails;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.io.File;
import java.io.IOException;
public class MJob {
    List<Map<String, String>> jobDataList = new ArrayList<>();
    public void scrap() {
        List<JobDetails> jobList = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", Config.CHROME_DRIVER_PATH);
        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        String outputDirPath = Config.BASE_PATH;
        String outputFilePath = outputDirPath + "mjob.json";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            // Open the site
            driver.get("https://m-job.ma/recherche/informatique");
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

                    // Scrape job details
                    WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[2]/div/div/div[1]/div[2]/h1")));
                    String title = titleElement.getText();

                    WebElement niveauEtudeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[2]/div/div/div[2]/div[7]")));
                    String niveauEtude = niveauEtudeElement.getText();

                    WebElement activityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[2]/div/div/div[2]/div[4]")));
                    String activity = activityElement.getText();

                    WebElement niveauExperienceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[2]/div/div/div[2]/div[6]")));
                    String niveauExperience = niveauExperienceElement.getText();

                    System.out.println(title + " | " + niveauEtude + " | " + activity + " | " + niveauExperience);

                    // Save job details (if needed)
                     JobDetails jobObj = new JobDetails(niveauExperience, title, activity, niveauEtude);
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

    public static void main(String[] args) {
        MJob job = new MJob();
        job.scrap();
    }

}


