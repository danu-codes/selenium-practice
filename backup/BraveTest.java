
package com.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BraveTest {

    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();

        // Your actual Brave Browser location
        String bravePath = System.getenv("LOCALAPPDATA")
                + "\\BraveSoftware\\Brave-Browser"
                + "\\Application\\brave.exe";

        options.setBinary(bravePath);

        WebDriver driver = new ChromeDriver(options);

        try {
            // Open Selenium test website
            driver.get(
                "https://www..ebay.com/"
            );

            
            // Get result
        

            System.out.println("Result: " + result);

            if ("Received!".equals(result)) {
                System.out.println("TEST PASSED!");
            } else {
                throw new AssertionError("TEST FAILED!");
            }

        } finally {
			Thread.sleep(10000);
            // Close Brave
            driver.quit();
        }
    }
}
