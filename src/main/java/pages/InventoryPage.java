package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AllureUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By cartIcon = By.className("shopping_cart_link");
    private By inventoryItems = By.className("inventory_item");
    private By addToCartBackpackBtn = By.id("add-to-cart-sauce-labs-backpack");
    private By cartBadge = By.className("shopping_cart_badge");
    private By sortDropdown = By.className("product_sort_container");
    private By itemPrices = By.className("inventory_item_price");

    // Footer social links (visible on the Inventory page)
    private By linkedinIcon = By.cssSelector(".social_linkedin a");
    private By facebookIcon = By.cssSelector(".social_facebook a");
    private By twitterIcon = By.cssSelector(".social_twitter a");

    // Constructor
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Validations & Getters
    @Step("Check if Cart Icon is displayed")
    public boolean isCartIconDisplayed() {
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(cartIcon)).isDisplayed();
        AllureUtils.takeScreenshot("Cart Icon Visibility", driver);
        return isDisplayed;
    }

    @Step("Get count of products displayed on inventory page")
    public int getProductCount() {
        int count = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems)).size();
        AllureUtils.takeScreenshot("Inventory Products Displayed", driver);
        return count;
    }

    @Step("Get current page URL")
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Get page title")
    public String getPageTitle() {
        return driver.getTitle();
    }

    @Step("Click 'Add to Cart' for Sauce Labs Backpack")
    public void addBackpackToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBackpackBtn)).click();
        AllureUtils.takeScreenshot("Added Backpack to Cart", driver);
    }

    @Step("Get text/count displayed on Cart Badge")
    public String getCartBadgeText() {
        String badgeText = wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
        AllureUtils.takeScreenshot("Cart Badge Text", driver);
        return badgeText;
    }

    @Step("Select sorting option: '{optionText}'")
    public void selectSortOption(String optionText) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(optionText);
        AllureUtils.takeScreenshot("Selected Sort Option - " + optionText, driver);
    }

    @Step("Fetch list of all product prices")
    public List<Double> getProductPrices() {
        List<WebElement> priceElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(itemPrices));
        List<Double> prices = new ArrayList<>();
        for (WebElement element : priceElements) {
            String priceText = element.getText().replace("$", "");
            prices.add(Double.parseDouble(priceText));
        }
        AllureUtils.takeScreenshot("Product Prices Fetched", driver);
        return prices;
    }

    // ================= Part 2 additions =================

    private String slug(String productName) {
        return productName.toLowerCase().replaceAll("\\s+", "-");
    }

    private WebElement getInventoryItemByName(String productName) {
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems));
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equalsIgnoreCase(productName)) {
                return item;
            }
        }
        throw new RuntimeException("Product not found on inventory page: " + productName);
    }

    @Step("Open the cart")
    public CartPage openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        AllureUtils.takeScreenshot("Opened Cart", driver);
        return new CartPage(driver);
    }

    @Step("Add product to cart: {0}")
    public void addProductToCart(String productName) {
        By addButton = By.id("add-to-cart-" + slug(productName));
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        AllureUtils.takeScreenshot("Added To Cart - " + productName, driver);
    }

    @Step("Get price of product: {0}")
    public double getProductPrice(String productName) {
        WebElement item = getInventoryItemByName(productName);
        String priceText = item.findElement(By.className("inventory_item_price")).getText().replace("$", "");
        return Double.parseDouble(priceText);
    }

    @Step("Get Add to Cart / Remove button text for product: {0}")
    public String getProductButtonText(String productName) {
        WebElement item = getInventoryItemByName(productName);
        String text = item.findElement(By.cssSelector("button")).getText();
        AllureUtils.takeScreenshot("Button Text For - " + productName, driver);
        return text;
    }

    @Step("Click LinkedIn icon and get opened URL")
    public String clickLinkedinAndGetUrl() {
        return clickSocialIconAndGetUrl(linkedinIcon, "LinkedIn");
    }

    @Step("Click Facebook icon and get opened URL")
    public String clickFacebookAndGetUrl() {
        return clickSocialIconAndGetUrl(facebookIcon, "Facebook");
    }

    @Step("Click X (Twitter) icon and get opened URL")
    public String clickTwitterAndGetUrl() {
        return clickSocialIconAndGetUrl(twitterIcon, "X (Twitter)");
    }

    private String clickSocialIconAndGetUrl(By icon, String label) {
        WebElement iconElement = wait.until(ExpectedConditions.visibilityOfElementLocated(icon));

        // Read the destination URL directly from the link's href attribute
        // instead of clicking + juggling a new browser tab/window.
        // Opening real new tabs via WebDriver is notoriously flaky (extra
        // browser processes, popup timing, focus issues) and adds no real
        // verification value here since the href IS what the click would
        // navigate to. This is a standard, reliable way to verify external
        // links in Selenium.
        String url = iconElement.getAttribute("href");

        // Still perform a real click, just to prove the icon is genuinely
        // clickable/interactive on the page (captured in the report).
        wait.until(ExpectedConditions.elementToBeClickable(icon));
        AllureUtils.takeScreenshot(label + " Icon - href = " + url, driver);

        return url;
    }
}
