package tests;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RozetkaTests {
    WebDriver driver;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void setUpTest() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://rozetka.com.ua/");
    }

    @Test
    public void sumInCartIsLessThan100000UAHTest() throws InterruptedException {
        WebDriverWait waiter = new WebDriverWait(driver, 30);
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//li[@class='menu-categories__item ng-star-inserted']//a[text()='Ноутбуки и компьютеры']")).click();
        waiter.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        driver.findElement(By.xpath("//a[@class='tile-cats__heading tile-cats__heading_type_center ng-star-inserted'][@title='Ноутбуки']")).click();

        List<WebElement> listOfLaptops =
                driver.findElements(By
                        .xpath("//button[@aria-label='Купить']"));
        waiter.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        for (int i = 0; i < 3; i++) {
            if (listOfLaptops.get(i) == null) {
                System.err.println("Something went wrong, the list is empty");
            } else {
                driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
                listOfLaptops.get(i).click();
                driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            }
        }
        driver.findElement(By.xpath("//li[@class='header-actions__item header-actions__item--cart']")).click();
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='modal__holder modal__holder_show_animation modal__holder--large']")));
        waiter.until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        int currentSumInCart = Integer.parseInt(driver.findElement(By.xpath("//div[@class='cart-receipt ng-star-inserted']//div[@class='cart-receipt__sum-price']//span[1]")).getText());
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        boolean isSumInCartLowerThanOneHundredThousands = true;
        if (currentSumInCart >= 100000 || currentSumInCart == 0) {
            isSumInCartLowerThanOneHundredThousands = false;
        }
        Assert.assertTrue(isSumInCartLowerThanOneHundredThousands);
    }

    @AfterMethod
    public void close() {
        driver.close();
    }
}
