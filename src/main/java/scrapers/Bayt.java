package scrapers;

import config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Bayt {

    // Class to hold job details
    static class JobDetails {
        private String title;
        private String date;
        private String description;

        public JobDetails(String title, String date, String description) {
            this.title = title;
            this.date = date;
            this.description = description;
        }
    }

    public void scrap() {
        System.setProperty("webdriver.chrome.driver", Config.CHROME_DRIVER_PATH);

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        // List to store all job details
        List<JobDetails> jobList = new ArrayList<>();

        // Output JSON file path
//        String outputFilePath = "C:\\Users\\HP\\Documents\\GitHub\\Moumen-Java-Project\\outputs\\bayt_jobs.json";
        String outputFilePath = Config.BASE_PATH+"bayt_jobs.json";

        try {
            // Open the website
            driver.get("https://www.bayt.com/en/morocco/jobs/?jobid=5201745&page=2&_gl=1*lq5vlk*_up*MQ.._ga*MTI3NzA0MTI4MC4xNzMxMTE1Mzc4_ga_1NKPLGNKKD*MTczMTExNTM3OC4xLjAuMTczMTExNTM3OC4wLjAuMA..");

            // Create a WebDriverWait instance for handling waits
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            int currentPage = 1; // Start from the first page
            int maxPages = 4; // Limit to 4 pages

            while (currentPage <= maxPages) {
                System.out.println("Scraping page: " + currentPage);

                // Locate job listings
                List<WebElement> jobOffers = driver.findElements(By.className("has-pointer-d"));

                // Iterate over each offer
                for (int i = 0; i < jobOffers.size(); i++) {
                    try {
                        // Refresh jobOffers list to avoid stale elements
                        jobOffers = driver.findElements(By.className("has-pointer-d"));

                        WebElement offer = jobOffers.get(i);

                        // Wait until the element is clickable and click it
                        wait.until(ExpectedConditions.elementToBeClickable(offer));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", offer);

                        // Wait for job details to load
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("jobViewJobTitle")));
                        String title = driver.findElement(By.id("jobViewJobTitle")).getText();

                        // Wait for the job description to appear
                        WebElement descriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//div[contains(@class,'card-content p10t-m')]")));
                        String paragraph = descriptionElement.getText();

                        // Process the paragraph into lines
                        String[] lines = paragraph.split("\\r?\\n");
                        String date = lines[0];
                        StringBuilder description = new StringBuilder();

                        for (int j = 4; j < lines.length; j++) {
                            description.append(lines[j]).append("\n");
                        }

                        // Add job details to the list
                        jobList.add(new JobDetails(title, date, description.toString()));

                        // Go back to the main job list
                        driver.navigate().back();

                        // Wait for the main job list to reload
                        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("has-pointer-d")));
                    } catch (Exception e) {
                        System.out.println("An error occurred while processing the job offer: " + e.getMessage());
                    }
                }

                // Check if there's a next page and increment the page counter
                try {
                    WebElement nextLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//li[contains(@class,'pagination-next')]/a[contains(@class,'jsAjaxLoad')]")));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextLink);
                    String nextPageUrl = nextLink.getAttribute("href");
                    driver.get(nextPageUrl);
                    currentPage++; // Move to the next page
                } catch (Exception e) {
                    System.out.println("No more pages available or unable to navigate to the next page.");
                    break; // Exit the loop if there's no "Next" button
                }
            }

            // Save job data as JSON
            saveToJson(jobList, outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the driver
            driver.quit();
        }
    }

    // Method to save job list to JSON
    private void saveToJson(List<JobDetails> jobList, String filePath) {
        Gson gson = new Gson();
        String json = gson.toJson(jobList);

        // Ensure the output directory exists
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(json);
            System.out.println("Job data saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
