package tests;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Swag Labs E-Commerce")
@Feature("Inventory & Product Management")
public class InventoryTest extends BaseTest {

    @Test(priority = 1, description = "Verify UI elements on the Inventory Page")
    @Story("Inventory Elements Display")
    @Severity(SeverityLevel.CRITICAL)
    public void testInventoryPageElements() {
        LoginPage loginPage = new LoginPage(driver);

        String user = DataDriven.jsonReader("validUser", "username");
        String pass = DataDriven.jsonReader("validUser", "password");

        InventoryPage inventoryPage = loginPage.performLogin(user, pass);

        // 1. Verify Page Title
        Assert.assertEquals(inventoryPage.getPageTitle(), "Swag Labs", "Page title mismatch!");

        // 2. Verify Cart Icon Displayed
        Assert.assertTrue(inventoryPage.isCartIconDisplayed(), "Cart icon is not displayed!");

        // 3. Verify Product Count is 6
        Assert.assertEquals(inventoryPage.getProductCount(), 6, "Products count is not 6!");
    }

    @Test(priority = 2, description = "Verify adding a product updates the cart badge count")
    @Story("Add Product to Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddToCartBadgeUpdate() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.performLogin(
                DataDriven.jsonReader("validUser", "username"),
                DataDriven.jsonReader("validUser", "password")
        );

        inventoryPage.addBackpackToCart();
        Assert.assertEquals(inventoryPage.getCartBadgeText(), "1", "Cart badge count mismatch!");
    }

    @Test(priority = 3, description = "Verify sorting products by price low to high")
    @Story("Sort Products")
    @Severity(SeverityLevel.NORMAL)
    public void testSortProductsByPriceLowToHigh() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.performLogin(
                DataDriven.jsonReader("validUser", "username"),
                DataDriven.jsonReader("validUser", "password")
        );

        inventoryPage.selectSortOption("Price (low to high)");
        List<Double> prices = inventoryPage.getProductPrices();

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Products are not sorted properly by price!");
        }
    }
}