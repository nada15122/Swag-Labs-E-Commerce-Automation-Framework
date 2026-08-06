package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AllureUtils;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By usernameInput = By.id("user-name");
    private By passwordInput = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).sendKeys(username);
        AllureUtils.takeScreenshot("Entered Username", driver);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).sendKeys(password);
        AllureUtils.takeScreenshot("Entered Password", driver);
    }

    @Step("Click Login Button")
    public void clickLogin() {
        AllureUtils.takeScreenshot("Before Login Click", driver);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    @Step("Perform Login Action")
    public InventoryPage performLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new InventoryPage(driver);
    }

    @Step("Get Error Message Text")
    public String getErrorMessageText() {
        String error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        AllureUtils.takeScreenshot("Error Message Displayed", driver);
        return error;
    }
}