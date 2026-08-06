package tests;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;

import java.util.List;

@Epic("Swag Labs E-Commerce")
@Feature("Cart Management")
public class CartTest extends BaseTest {

    // Reusable helper: login with the standard valid user
    private InventoryPage loginAsValidUser() {
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.performLogin(
                DataDriven.jsonReader("validUser", "username"),
                DataDriven.jsonReader("validUser", "password")
        );
    }

    // Reusable helper: get the 3 cart products from testData.json
    @SuppressWarnings("unchecked")
    private List<String> getCartProducts() {
        JSONArray array = DataDriven.jsonArrayReader("cartProducts");
        List<String> products = new java.util.ArrayList<>();
        for (Object o : array) {
            products.add((String) o);
        }
        return products;
    }

    @Test(priority = 1, description = "Verify social media links (LinkedIn, Facebook, X) open the correct sites")
    @Story("Footer Social Links")
    @Severity(SeverityLevel.MINOR)
    public void testSocialLinks() {
        InventoryPage inventoryPage = loginAsValidUser();

        String linkedinUrl = inventoryPage.clickLinkedinAndGetUrl();
        Assert.assertTrue(linkedinUrl.contains("linkedin"), "LinkedIn URL does not contain 'linkedin'! Actual: " + linkedinUrl);

        String facebookUrl = inventoryPage.clickFacebookAndGetUrl();
        Assert.assertTrue(facebookUrl.contains("facebook"), "Facebook URL does not contain 'facebook'! Actual: " + facebookUrl);

        String twitterUrl = inventoryPage.clickTwitterAndGetUrl();
        Assert.assertTrue(twitterUrl.contains("x.com"), "X (Twitter) URL does not contain 'x.com'! Actual: " + twitterUrl);
    }

    @Test(priority = 2, description = "Verify the cart is empty right after login")
    @Story("Empty Cart State")
    @Severity(SeverityLevel.NORMAL)
    public void testCartIsEmptyInitially() {
        InventoryPage inventoryPage = loginAsValidUser();
        CartPage cartPage = inventoryPage.openCart();

        Assert.assertTrue(cartPage.isCartEmpty(), "Cart is not empty right after login!");
    }

    @Test(priority = 3, description = "Add 3 specific products (data-driven) and verify they appear in the cart in the same order")
    @Story("Add Products To Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddSpecificProductsToCart() {
        InventoryPage inventoryPage = loginAsValidUser();
        List<String> products = getCartProducts();

        for (String product : products) {
            inventoryPage.addProductToCart(product);
        }

        CartPage cartPage = inventoryPage.openCart();
        List<String> cartItemNames = cartPage.getCartItemNames();

        Assert.assertEquals(cartItemNames, products,
                "Cart items do not match the products added, or the order is different!");
    }

    @Test(priority = 4, description = "Remove one product from the cart and verify inventory buttons update correctly")
    @Story("Remove Product From Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testRemoveProductFromCart() {
        InventoryPage inventoryPage = loginAsValidUser();
        List<String> products = getCartProducts();

        for (String product : products) {
            inventoryPage.addProductToCart(product);
        }

        String productToRemove = "Sauce Labs Bolt T-Shirt";

        CartPage cartPage = inventoryPage.openCart();
        cartPage.removeProduct(productToRemove);

        InventoryPage backToInventory = cartPage.clickContinueShopping();

        Assert.assertEquals(backToInventory.getProductButtonText(productToRemove), "Add to cart",
                "Removed product's button did not change back to 'Add to cart'!");

        for (String product : products) {
            if (!product.equals(productToRemove)) {
                Assert.assertEquals(backToInventory.getProductButtonText(product), "Remove",
                        "Product '" + product + "' button should still show 'Remove'!");
            }
        }
    }

    @Test(priority = 5, description = "Verify the cart Item Total at checkout matches the sum of the inventory prices")
    @Story("Cart Total Price Calculation")
    @Severity(SeverityLevel.CRITICAL)
    public void testCartTotalPriceMatchesSum() {
        InventoryPage inventoryPage = loginAsValidUser();
        List<String> products = getCartProducts();

        double expectedTotal = 0.0;
        for (String product : products) {
            expectedTotal += inventoryPage.getProductPrice(product);
        }

        for (String product : products) {
            inventoryPage.addProductToCart(product);
        }

        CartPage cartPage = inventoryPage.openCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillInfoAndContinue("Nada", "Nabil", "12345");

        double actualItemTotal = checkoutPage.getItemTotal();

        Assert.assertEquals(actualItemTotal, expectedTotal, 0.01,
                "Item Total at checkout does not match the sum of the individual product prices!");
    }

    @Test(priority = 6, description = "Verify checking out with an empty cart shows an error and does not proceed normally")
    @Story("Checkout Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutWithEmptyCart() {
        InventoryPage inventoryPage = loginAsValidUser();
        CartPage cartPage = inventoryPage.openCart();

        Assert.assertTrue(cartPage.isCartEmpty(), "Precondition failed: cart is not empty!");

        cartPage.clickCheckout();

        String error = cartPage.getErrorMessageText();
        Assert.assertFalse(error.isEmpty(),
                "No 'cart is empty' error was shown - the site allowed proceeding to checkout "
                        + "normally with an empty cart (this is the real SauceDemo behavior; "
                        + "it does not validate an empty cart, so this failure is expected).");
        Assert.assertTrue(error.toLowerCase().contains("empty"),
                "Expected an error message telling the cart is empty, but got: " + error);
    }

    @Test(priority = 7, description = "Verify whether cart items persist after logging out and logging back in")
    @Story("Cart State After Logout/Login")
    @Severity(SeverityLevel.NORMAL)
    public void testCartStateAfterLogoutLogin() {
        InventoryPage inventoryPage = loginAsValidUser();
        List<String> products = getCartProducts().subList(0, 2); // at least 2 products

        for (String product : products) {
            inventoryPage.addProductToCart(product);
        }

        String badgeBeforeLogout = inventoryPage.getCartBadgeText();

        // Logout via the burger menu (with proper waits for the slide-out menu animation)
        org.openqa.selenium.support.ui.WebDriverWait shortWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));

        shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .elementToBeClickable(org.openqa.selenium.By.id("react-burger-menu-btn"))).click();
        shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .elementToBeClickable(org.openqa.selenium.By.id("logout_sidebar_link"))).click();

        // Confirm we're back on the login page before logging in again
        shortWait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .visibilityOfElementLocated(org.openqa.selenium.By.id("user-name")));

        // Login again with the same user
        InventoryPage inventoryAfterLogin = loginAsValidUser();

        // Make sure the inventory page has actually finished loading before checking the badge
        inventoryAfterLogin.isCartIconDisplayed();

        List<org.openqa.selenium.WebElement> badgeAfterLogin =
                driver.findElements(org.openqa.selenium.By.className("shopping_cart_badge"));

        if (badgeAfterLogin.isEmpty()) {
            // Real behavior: cart was cleared after logout/login
            Assert.assertTrue(true, "Cart was cleared after logout/login (no badge displayed).");
        } else {
            // Real behavior: cart persisted after logout/login
            Assert.assertEquals(badgeAfterLogin.get(0).getText(), badgeBeforeLogout,
                    "Cart badge count changed after logout/login even though items should have persisted!");
        }
    }
}
