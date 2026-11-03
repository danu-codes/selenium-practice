
package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class EbayTest {

    public static void main(String[] args)
            throws InterruptedException {

        // 1. Configure Brave
        ChromeOptions options = new ChromeOptions();

        String bravePath = System.getenv("LOCALAPPDATA")
                + "\\BraveSoftware\\Brave-Browser"
                + "\\Application\\brave.exe";

        options.setBinary(bravePath);
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(25));

        try {
            // 2. Open eBay
            driver.get("https://www.ebay.com/");

            System.out.println("Title: " + driver.getTitle());

            // 3. Find search box
            WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("_nkw")
                )
            );

            // 4. Enter search keyword
            searchBox.sendKeys("Dell Laptop");

            // 5. Press Enter
            searchBox.sendKeys(Keys.ENTER);

            // 6. Wait for product results
            wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(
                        ".s-card__title, .s-item__title"
                    )
                )
            );

            System.out.println("URL: " +
                    driver.getCurrentUrl());

            // 7. Get listing cards
            List<WebElement> products =
                driver.findElements(
                    By.cssSelector("li.s-card, li.s-item")
                );

            int count = 0;

            for (WebElement product : products) {

                List<WebElement> titles =
                    product.findElements(
                        By.cssSelector(
                            ".s-card__title, .s-item__title"
                        )
                    );

                if (titles.isEmpty()) continue;

                String name = titles.get(0).getText();

                if (name.isBlank()) continue;

                List<WebElement> prices =
                    product.findElements(
                        By.cssSelector(
                            ".s-card__price, .s-item__price"
                        )
                    );

                String price = prices.isEmpty()
                    ? "Price unavailable"
                    : prices.get(0).getText();

                count++;

                System.out.println(
                    count + ". " + name
                );
                System.out.println("   Price: " + price);

                if (count == 5) break;
            }

            // 8. Verify results
            if (count > 0) {
                System.out.println("TEST PASSED!");
            } else {
                throw new AssertionError(
                    "No product results found."
                );
            }

            // 9. Keep browser open for observation
            Thread.sleep(10000);

        } finally {
            // 10. Close Brave
            driver.quit();
        }
    }
}
