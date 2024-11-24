import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class ForceEmploi {
    public void scrap() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Electronic Store\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<Map<String, Object>> jobDataList = new ArrayList<>(); // List to store job details

        try {
            System.out.println("//");
            driver.get("https://www.breizhinterim.com/offres-d-emploi/");

            boolean hasMorePages = true;
            while (hasMorePages) {
                wait.until((WebDriver d) -> {
                    JavascriptExecutor js = (JavascriptExecutor) d;
                    String readyState = js.executeScript("return document.readyState").toString();
                    return readyState.equals("complete");
                });
                System.out.println("Page is fully loaded!");

                scrollPage(driver);

                List<WebElement> jobOffers = driver.findElements(By.className("c-offer__mainlink"));

                for (int i = 0; i < jobOffers.size(); i++) {
                    try {
                        WebElement offer = jobOffers.get(i);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", offer);

                        System.out.println("Attempting to click: " + offer.getText());

                        wait.until(ExpectedConditions.elementToBeClickable(offer));

                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", offer);

                        Map<String, Object> jobDetails = new HashMap<>(); // Map to hold job details

                        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("/html/body/main/header/div[1]/div[2]/div[1]/div/div[2]/h1")));
                        String title = titleElement.getText();
                        jobDetails.put("title", title);

                        WebElement domaineElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("/html/body/main/header/div[1]/div[2]/div[1]/div/div[2]/div[1]/div[3]/span")));
                        String domain = domaineElement.getText();
                        jobDetails.put("domain", domain);

                        List<String> competences = new ArrayList<>();
                        WebElement ulElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Profil du candidat']/following-sibling::ul")));
                        List<WebElement> listItems = ulElement.findElements(By.tagName("li"));
                        for (WebElement listItem : listItems) {
                            WebElement pTag = listItem.findElement(By.tagName("p"));
                            competences.add(pTag.getText());
                        }
                        jobDetails.put("competences", competences);

                        jobDataList.add(jobDetails); // Add job details to the list

                        driver.navigate().back();
                        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("c-offer__mainlink")));
                    } catch (StaleElementReferenceException e) {
                        System.out.println("StaleElementReferenceException encountered. Retrying...");
                        jobOffers = driver.findElements(By.className("c-offer__mainlink"));
                        i--;
                    } catch (Exception e) {
                        System.out.println("Error clicking element: " + e.getMessage());
                    }
                }
                try {
                    WebElement nextLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("/html/body/main/div[1]/div/div[2]/div/div[2]/div/div[2]/nav/ul/li[5]/a")));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextLink);
                    String nextPageUrl = nextLink.getAttribute("href");
                    driver.get(nextPageUrl);
                } catch (Exception e) {
                    System.out.println("No more pages available.");
                    hasMorePages = false;
                }
            }

            // Write job data to a JSON file
            writeToJsonFile(jobDataList);

        } catch (Exception e) {
            e.printStackTrace();
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
                Thread.sleep(2000);
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

    private void writeToJsonFile(List<Map<String, Object>> jobDataList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Define the file path
            File outputFile = new File("C:\\Users\\Electronic Store\\Downloads\\projectos\\projectos\\outputs\\ForceEmploi.json");

            // Ensure the parent directory exists
            File outputDir = outputFile.getParentFile();
            if (!outputDir.exists()) {
                outputDir.mkdirs(); // Create the directory if it doesn't exist
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jobDataList);
            System.out.println("Job data written to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
