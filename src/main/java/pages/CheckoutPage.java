package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AllureUtils;

import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Step One - Your Information
    private By firstNameInput = By.id("first-name");
    private By lastNameInput = By.id("last-name");
    private By postalCodeInput = By.id("postal-code");
    private By continueButton = By.id("continue");

    // Step Two - Overview
    private By itemTotalLabel = By.className("summary_subtotal_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Fill checkout information and continue")
    public CheckoutPage fillInfoAndContinue(String firstName, String lastName, String postalCode) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(postalCodeInput).sendKeys(postalCode);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        AllureUtils.takeScreenshot("Filled Checkout Info", driver);
        return this;
    }

    @Step("Get Item Total displayed on checkout overview page")
    public double getItemTotal() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(itemTotalLabel)).getText();
        // Text looks like: "Item total: $58.97"
        String amount = text.substring(text.indexOf("$") + 1);
        AllureUtils.takeScreenshot("Item Total Read - " + amount, driver);
        return Double.parseDouble(amount);
    }
}
