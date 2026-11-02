package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_BANNER = By.cssSelector("h3[data-test='error']");
    private static final By ERROR_CLOSE = By.cssSelector("button.error-button");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/");
        visible(LOGIN_BUTTON);
        return this;
    }

    public LoginPage typeUsername(String username) {
        type(USERNAME, username);
        return this;
    }

    public LoginPage typePassword(String password) {
        type(PASSWORD, password);
        return this;
    }

    public InventoryPage submitValidLogin(String username, String password) {
        typeUsername(username);
        typePassword(password);
        click(LOGIN_BUTTON);
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitUntilLoaded();
        return inventoryPage;
    }

    public LoginPage submitInvalidLogin(String username, String password) {
        typeUsername(username);
        typePassword(password);
        click(LOGIN_BUTTON);
        visible(ERROR_BANNER);
        return this;
    }

    public LoginPage login(String username, String password) {
        return submitInvalidLogin(username, password);
    }

    public LoginPage waitForError() {
        visible(ERROR_BANNER);
        return this;
    }

    public String errorMessage() {
        return text(ERROR_BANNER);
    }

    public boolean isErrorDisplayed() {
        return isPresent(ERROR_BANNER);
    }

    public LoginPage dismissError() {
        click(ERROR_CLOSE);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(ERROR_BANNER));
        return this;
    }

    public boolean isLoginButtonDisplayed() {
        return isPresent(LOGIN_BUTTON);
    }

    public String passwordFieldType() {
        return visible(PASSWORD).getAttribute("type");
    }
}
