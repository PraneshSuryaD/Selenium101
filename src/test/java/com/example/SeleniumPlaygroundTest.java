package com.example;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SeleniumPlaygroundTest
{
    WebDriver driver;
    WebDriverWait wait;

    String BASE_URL = "https://www.testmuai.com/selenium-playground/";

    @BeforeMethod
    public void setUp()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterMethod
    public void tearDown()
    {
        if(driver != null)
        {
            driver.quit();
        }
    }

    @Test
    public void testScenario1_SimpleFormDemo()
    {
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Simple Form Demo"))).click();

        wait.until(ExpectedConditions.urlContains("simple-form-demo"));

        Assert.assertTrue(driver.getCurrentUrl().contains("simple-form-demo"));

        String message = "Welcome to TestMu AI";

        WebElement messageBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("user-message")));

        messageBox.sendKeys(message);

        driver.findElement(By.id("showInput")).click();

        String actualMessage = messageBox.getAttribute("value");

        Assert.assertEquals(actualMessage, message);
    }

    @Test
    public void testScenario2_DragAndDropSliders()
    {
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Drag & Drop Sliders"))).click();

        WebElement slider = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#slider3 input[type='range']")));

        int width = slider.getSize().getWidth();

        int min = Integer.parseInt(slider.getAttribute("min"));
        int max = Integer.parseInt(slider.getAttribute("max"));
        int currentValue = Integer.parseInt(slider.getAttribute("value"));

        int targetValue = 95;

        double pixelsPerUnit =
                (double) width / (max - min);

        int currentOffset =
                (int) ((currentValue - min) * pixelsPerUnit)
                - (width / 2);

        int targetOffset =
                (int) ((targetValue - min) * pixelsPerUnit)
                - (width / 2);

        Actions actions = new Actions(driver);

        actions.moveToElement(slider, currentOffset, 0)
               .clickAndHold()
               .moveToElement(slider, targetOffset, 0)
               .release()
               .perform();

        WebElement rangeValue = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("rangeSuccess")));

        int actual = Integer.parseInt(rangeValue.getText());

        if(actual != targetValue)
        {
            int difference = targetValue - actual;

            Keys direction;

            if(difference > 0)
            {
                direction = Keys.ARROW_RIGHT;
            }
            else
            {
                direction = Keys.ARROW_LEFT;
            }

            for(int i = 0; i < Math.abs(difference); i++)
            {
                slider.sendKeys(direction);
            }
        }

        Assert.assertEquals(rangeValue.getText(), "95");
    }

    @Test
    public void testScenario3_InputFormSubmit()
    {
        driver.get(BASE_URL);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Input Form Submit"))).click();

        WebElement submit = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#seleniumform button[type='submit']")));

        submit.click();

        WebElement name = driver.findElement(By.id("name"));

        String validationMessage =
                name.getAttribute("validationMessage");

        Assert.assertTrue(validationMessage.contains("Please fill"));

        driver.findElement(By.id("name"))
                .sendKeys("John Doe");

        driver.findElement(By.id("inputEmail4"))
                .sendKeys("john.doe@gmail.com");

        driver.findElement(By.id("inputPassword4"))
                .sendKeys("password123");

        driver.findElement(By.id("company"))
                .sendKeys("TestMu AI");

        driver.findElement(By.name("website"))
                .sendKeys("https://example.com");

        driver.findElement(By.id("inputCity"))
                .sendKeys("New York");

        driver.findElement(By.id("inputAddress1"))
                .sendKeys("123 Main Street");

        driver.findElement(By.id("inputAddress2"))
                .sendKeys("Apartment 4B");

        driver.findElement(By.id("inputState"))
                .sendKeys("New York");

        driver.findElement(By.id("inputZip"))
                .sendKeys("10001");

        Select country = new Select(
                driver.findElement(By.name("country")));

        country.selectByVisibleText("United States");

        submit.click();

        WebElement successMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".success-msg")));

        Assert.assertEquals(
                successMessage.getText(),
                "Thanks for contacting us, we will get back to you shortly.");
    }
}