package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AllureUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By cartItems = By.className("cart_item");
    private By cartItemNames = By.className("inventory_item_name");
    private By checkoutButton = By.id("checkout");
    private By continueShoppingButton = By.id("continue-shopping");
    private By errorMessage = By.cssSelector("h3[data-test='error']");

    // A label that is ALWAYS present on cart.html (even when the cart is empty),
    // used to reliably confirm we've actually finished navigating to the Cart page
    // before reading anything from it.
    private By cartPageLoadedMarker = By.className("cart_quantity_label");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Make sure we're really on cart.html before doing anything else.
        // Without this, code right after openCart() could still read the
        // previous (Inventory) page's DOM for a split second.
        wait.until(ExpectedConditions.urlContains("cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartPageLoadedMarker));
    }

    private String slug(String productName) {
        return productName.toLowerCase().replaceAll("\\s+", "-");
    }

    @Step("Check if cart is empty")
    public boolean isCartEmpty() {
        List<WebElement> items = driver.findElements(cartItems);
        AllureUtils.takeScreenshot("Cart Empty Check", driver);
        return items.isEmpty();
    }

    @Step("Get names of products currently in the cart, in order")
    public List<String> getCartItemNames() {
        List<WebElement> nameElements = driver.findElements(cartItemNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : nameElements) {
            names.add(el.getText());
        }
        AllureUtils.takeScreenshot("Cart Item Names", driver);
        return names;
    }

    @Step("Remove product from cart: {0}")
    public void removeProduct(String productName) {
        By removeButton = By.id("remove-" + slug(productName));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        AllureUtils.takeScreenshot("Removed From Cart - " + productName, driver);
    }

    @Step("Click Continue Shopping (back to Inventory)")
    public InventoryPage clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
        return new InventoryPage(driver);
    }

    @Step("Click Checkout")
    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        AllureUtils.takeScreenshot("Clicked Checkout", driver);
        return new CheckoutPage(driver);
    }

    @Step("Get error message text after invalid checkout attempt")
    public String getErrorMessageText() {
        // NOTE: real SauceDemo behavior allows proceeding to checkout with an
        // empty cart (no validation error is shown). This wait is expected to
        // time out in that case -- which is exactly the "real behavior vs.
        // assumption" trap the assignment describes for this scenario.
        try {
            String error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
            AllureUtils.takeScreenshot("Cart Error Message Displayed", driver);
            return error;
        } catch (org.openqa.selenium.TimeoutException e) {
            AllureUtils.takeScreenshot("No Error Message Appeared - Site Allowed Empty Checkout", driver);
            return "";
        }
    }
}
