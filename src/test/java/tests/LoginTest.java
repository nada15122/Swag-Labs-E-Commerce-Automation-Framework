package tests;

import base.BaseTest;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        String user = DataDriven.jsonReader("validUser", "username");
        String pass = DataDriven.jsonReader("validUser", "password");

        InventoryPage inventoryPage = loginPage.performLogin(user, pass);

        Assert.assertTrue(inventoryPage.getCurrentUrl().contains("/inventory.html"),
                "URL does not contain /inventory.html");
    }

    @Test(priority = 2)
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(driver);

        String user = DataDriven.jsonReader("invalidUser", "username");
        String pass = DataDriven.jsonReader("invalidUser", "password");

        loginPage.performLogin(user, pass);

        String error = loginPage.getErrorMessageText();
        Assert.assertTrue(error.contains("Username and password do not match"),
                "Error message is not as expected!");
    }

    @Test(priority = 3)
    public void testLoginWithoutPassword() {
        LoginPage loginPage = new LoginPage(driver);

        String user = DataDriven.jsonReader("noPasswordUser", "username");
        String pass = DataDriven.jsonReader("noPasswordUser", "password");

        loginPage.performLogin(user, pass);

        String error = loginPage.getErrorMessageText();
        Assert.assertTrue(error.contains("Password is required"),
                "Error message is not as expected!");
    }

    @Test(priority = 4)
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(driver);

        String user = DataDriven.jsonReader("lockedUser", "username");
        String pass = DataDriven.jsonReader("lockedUser", "password");

        loginPage.performLogin(user, pass);

        String error = loginPage.getErrorMessageText();
        Assert.assertTrue(error.contains("Sorry, this user has been locked out"),
                "Locked out user error message mismatch!");
    }
}