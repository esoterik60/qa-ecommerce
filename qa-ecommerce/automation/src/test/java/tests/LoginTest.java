package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.InventoryPage;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(description = "TC-001 valid login lands on the inventory page")
    public void validUserCanLogin() {
        InventoryPage inventory = loginAsStandardUser();

        Assert.assertTrue(inventory.currentUrl().contains("/inventory.html"),
                "Expected to land on /inventory.html but was " + inventory.currentUrl());
        Assert.assertEquals(inventory.pageTitle(), "Products", "Inventory page title mismatch");
    }

    @Test(description = "TC-002 locked_out_user is rejected")
    public void lockedOutUserIsRejected() {
        LoginPage loginPage = openLoginPage().submitInvalidLogin(LOCKED_OUT_USER, PASSWORD);

        Assert.assertTrue(loginPage.errorMessage().contains("Sorry, this user has been locked out"),
                "Unexpected error text: " + loginPage.errorMessage());
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "User should stay on the login page");
    }

    @Test(description = "TC-003 wrong password is rejected")
    public void wrongPasswordIsRejected() {
        LoginPage loginPage = openLoginPage().submitInvalidLogin(STANDARD_USER, "wrong_password");

        Assert.assertTrue(loginPage.errorMessage().contains("do not match any user in this service"),
                "Unexpected error text: " + loginPage.errorMessage());
    }

    @Test(description = "TC-004 empty username is rejected")
    public void emptyUsernameIsRejected() {
        LoginPage loginPage = openLoginPage().submitInvalidLogin("", PASSWORD);

        Assert.assertTrue(loginPage.errorMessage().contains("Username is required"),
                "Unexpected error text: " + loginPage.errorMessage());
    }

    @Test(description = "TC-005 empty password is rejected")
    public void emptyPasswordIsRejected() {
        LoginPage loginPage = openLoginPage().submitInvalidLogin(STANDARD_USER, "");

        Assert.assertTrue(loginPage.errorMessage().contains("Password is required"),
                "Unexpected error text: " + loginPage.errorMessage());
    }

    @Test(description = "TC-007 error banner can be dismissed")
    public void errorBannerCanBeDismissed() {
        LoginPage loginPage = openLoginPage().submitInvalidLogin(STANDARD_USER, "wrong_password");
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Error banner should be visible first");

        loginPage.dismissError();

        Assert.assertFalse(loginPage.isErrorDisplayed(), "Error banner should be dismissed");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Form should remain usable");
    }

    @Test(description = "TC-008 password input is masked")
    public void passwordFieldIsMasked() {
        LoginPage loginPage = openLoginPage();

        Assert.assertEquals(loginPage.passwordFieldType(), "password", "Password field must not expose text");
    }
}
