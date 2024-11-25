package scrapers;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.io.File;
import java.io.IOException;

public class wetech {
    public void scrap() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Electronic Store\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<Map<String, String>> jobDataList = new ArrayList<>();

        try {
            driver.get("https://www.wetech.ma/offres-emploi/");

            boolean hasMorePages = true;

            while (hasMorePages) {
                wait.until((WebDriver d) -> {
                    JavascriptExecutor js = (JavascriptExecutor) d;
                    String readyState = js.executeScript("return document.readyState").toString();
                    return readyState.equals("complete");
                });

                // Scroll the page to load all job offers
                scrollPage(driver);

                // Locate job offers
                List<WebElement> jobOffers = driver.findElements(By.className("offreUrl"));

                // Iterate through job offers
                for (int i = 0; i < jobOffers.size(); i++) {
                    try {
                        WebElement offer = jobOffers.get(i);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", offer);
                        wait.until(ExpectedConditions.elementToBeClickable(offer));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", offer);

                        // Wait for job details page to load
                        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[1]/div[1]/div/div/div/div[2]/h1")));
                        String title = titleElement.getText();

                        WebElement descriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[1]/div[2]/div/div/p")));
                        String description = descriptionElement.getText();

                        WebElement requirementsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div[1]/div[3]/div/div/p")));
                        String requirements = requirementsElement.getText();

                        // Store job data in a map
                        Map<String, String> jobData = Map.of(
                                "title", title,
                                "description", description,
                                "requirements", requirements
                        );

                        jobDataList.add(jobData);

                        // After scraping, navigate back to job listings
                        driver.navigate().back();
                        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("offreUrl")));
                    } catch (StaleElementReferenceException e) {
                        jobOffers = driver.findElements(By.className("offreUrl"));
                        i--; // Retry the same index
                    } catch (Exception e) {
                        System.out.println("Error scraping job: " + e.getMessage());
                    }
                }

                // Move to the next page
                try {
                    WebElement nextLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[1]/div/div[16]/ul/li[7]/a")));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextLink);
                    String nextPageUrl = nextLink.getAttribute("href");
                    driver.get(nextPageUrl);
                } catch (Exception e) {
                    hasMorePages = false; // Exit loop if no more pages
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            writeToJsonFile(jobDataList); // Write data to JSON after scraping
        }
    }

    private void scrollPage(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");

        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(2000); // Wait for page content to load
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

    private void writeToJsonFile(List<Map<String, String>> jobDataList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Define the output file path with the new file name
            File outputFile = new File("C:\\Users\\Electronic Store\\Downloads\\projectos\\projectos\\outputs\\WeTech.json");

            // Ensure the parent directory exists
            File outputDir = outputFile.getParentFile();
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Write the list of job data to the file
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jobDataList);
            System.out.println("Job data written to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
