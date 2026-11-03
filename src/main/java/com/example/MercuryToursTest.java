
package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.nio.file.Files;
import java.nio.file.Path;

public class MercuryToursTest {

    public static void main(String[] args) throws Exception {

        // STEP 1: Open Brave Browser
        ChromeOptions options = new ChromeOptions();

        String bravePath = System.getenv("LOCALAPPDATA")
                + "\\BraveSoftware\\Brave-Browser"
                + "\\Application\\brave.exe";

        options.setBinary(bravePath);

        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(
                driver, Duration.ofSeconds(20));

        boolean loginSuccessful = false;

        try {
            // STEP 2: Open Mercury Tours
            driver.get(
                "https://demo.guru99.com/test/newtours/"
            );

            // STEP 3: Maximize window
            driver.manage().window().maximize();

            System.out.println("Website opened");

            // STEP 4: Enter login information
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("userName")
                )
            ).sendKeys("mercury");

            driver.findElement(By.name("password"))
                  .sendKeys("mercury");

            // Click the login button
            driver.findElement(By.name("submit")).click();

            // Check login result
            try {
                WebDriverWait loginWait = new WebDriverWait(
                        driver, Duration.ofSeconds(10));

                loginWait.until(d ->
                    d.getCurrentUrl().contains("login_sucess")
                    || !d.findElements(
                        By.linkText("SIGN-OFF")
                    ).isEmpty()
                );

                loginSuccessful = true;
                System.out.println("LOGIN SUCCESSFUL");

            } catch (TimeoutException e) {

                System.out.println(
                    "LOGIN NOT VERIFIED"
                );

                System.out.println(
                    "Current URL: " + driver.getCurrentUrl()
                );

                System.out.println(
                    "Current title: " + driver.getTitle()
                );

                // Capture login error screenshot
                Files.write(
                    Path.of("login-debug.png"),
                    ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES)
                );
            }

            // STEP 5: Open Flights
            // This demo form is accessible independently
            // of successful login.
            driver.get(
                "https://demo.guru99.com/test/newtours/reservation.php"
            );

            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.name("tripType")
                )
            );

            System.out.println("Flight page opened");

            // STEP 6: Select One Way
            WebElement oneWay = driver.findElement(
                By.cssSelector(
                    "input[name='tripType'][value='oneway']"
                )
            );

            oneWay.click();

            // STEP 7: Number of passengers
            Select passengers = new Select(
                driver.findElement(By.name("passCount"))
            );
            passengers.selectByVisibleText("2");

            // STEP 8: Departing airport
            Select departure = new Select(
                driver.findElement(By.name("fromPort"))
            );
            departure.selectByVisibleText("London");

            // STEP 9: Departing month and date
            Select fromMonth = new Select(
                driver.findElement(By.name("fromMonth"))
            );
            fromMonth.selectByIndex(5);

            Select fromDay = new Select(
                driver.findElement(By.name("fromDay"))
            );
            fromDay.selectByVisibleText("15");

            // STEP 10: Arriving airport
            Select arrival = new Select(
                driver.findElement(By.name("toPort"))
            );
            arrival.selectByVisibleText("Frankfurt");

            // STEP 11: Returning month and date
            Select toMonth = new Select(
                driver.findElement(By.name("toMonth"))
            );
            toMonth.selectByIndex(6);

            Select toDay = new Select(
                driver.findElement(By.name("toDay"))
            );
            toDay.selectByVisibleText("20");

            // STEP 12: Service class
            // STEP 12: Service class
            WebElement businessClass = driver.findElement(
                 By.cssSelector(
                "input[name='servClass'][value='Business']"
               )
            );
            businessClass.click();
            

            // STEP 13: Select airline
            Select airline = new Select(
                driver.findElement(By.name("airline"))
            );
            airline.selectByVisibleText("Unified Airlines");

            // Verify selections
            if (!oneWay.isSelected()
                    || !businessClass.isSelected()) {
                throw new AssertionError(
                    "Flight options were not selected"
                );
            }

            // Print selected values
            System.out.println("--------------------------");
            System.out.println("FLIGHT DETAILS");
            System.out.println("--------------------------");

            System.out.println("Trip: One Way");

            System.out.println("Passengers: " +
                passengers.getFirstSelectedOption().getText());

            System.out.println("From: " +
                departure.getFirstSelectedOption().getText());

            System.out.println("Departure: " +
                fromMonth.getFirstSelectedOption().getText()
                + " " + fromDay.getFirstSelectedOption().getText());

            System.out.println("To: " +
                arrival.getFirstSelectedOption().getText());

            System.out.println("Return: " +
                toMonth.getFirstSelectedOption().getText()
                + " " + toDay.getFirstSelectedOption().getText());

            System.out.println("Class: Business");

            System.out.println("Airline: " +
                airline.getFirstSelectedOption().getText());

            // Save screenshot
            Files.write(
                Path.of("mercury-flight-form.png"),
                ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES)
            );

            System.out.println("FLIGHT FORM TEST PASSED");

            if (!loginSuccessful) {
                System.out.println(
                    "NOTE: Login remains unverified."
                );
            }

            // Keep browser visible for inspection
            Thread.sleep(10000);

        } finally {
            driver.quit();
        }
    }
}
