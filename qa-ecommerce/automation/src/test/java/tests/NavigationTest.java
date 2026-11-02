package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.InventoryPage;
import pages.LoginPage;

public class NavigationTest extends BaseTest {

    @Test(description = "TC-050 logout ends the session")
    public void logoutReturnsToLoginPage() {
        LoginPage loginPage = loginAsStandardUser().logout();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button should be visible after logout");
        Assert.assertEquals(loginPage.currentUrl(), BASE_URL + "/", "Logout should land on the login page");
    }

    @Test(description = "TC-051 reset app state clears the cart badge")
    public void resetAppStateClearsCart() {
        InventoryPage inventory = loginAsStandardUser().addToCart("sauce-labs-backpack");
        Assert.assertEquals(inventory.cartBadgeCount(), 1, "Precondition: one product in the cart");

        inventory.resetAppState();

        Assert.assertEquals(inventory.cartBadgeCount(), 0, "Badge should disappear after resetting app state");
    }

    @Test(description = "TC-031 protected page redirects to login when no session exists")
    public void protectedPageRedirectsToLogin() {
        driver.get(BASE_URL + "/inventory.html");
        LoginPage loginPage = new LoginPage(driver).waitForError();

        Assert.assertTrue(loginPage.currentUrl().equals(BASE_URL + "/"),
                "Unauthenticated access should redirect to the login page but was " + loginPage.currentUrl());
        Assert.assertTrue(loginPage.errorMessage().contains("You can only access"),
                "Unexpected error text: " + loginPage.errorMessage());
    }
}
