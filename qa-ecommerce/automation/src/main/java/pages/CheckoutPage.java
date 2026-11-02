package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.cssSelector("input[data-test='continue']");
    private static final By CANCEL = By.cssSelector("button[data-test='cancel']");
    private static final By FINISH = By.cssSelector("button[data-test='finish']");
    private static final By ERROR = By.cssSelector("h3[data-test='error']");
    private static final By SUMMARY_ITEMS = By.cssSelector(".cart_item .inventory_item_name");
    private static final By SUBTOTAL_LABEL = By.cssSelector(".summary_subtotal_label");
    private static final By TAX_LABEL = By.cssSelector(".summary_tax_label");
    private static final By TOTAL_LABEL = By.cssSelector(".summary_total_label");
    private static final By COMPLETE_HEADER = By.cssSelector(".complete-header");
    private static final By BACK_HOME = By.cssSelector("button[data-test='back-to-products']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage waitUntilStepOneLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        visible(FIRST_NAME);
        return this;
    }

    public CheckoutPage waitUntilOverviewLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        visible(FINISH);
        return this;
    }

    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        click(CONTINUE);
        return this;
    }

    public CheckoutPage continueWithInfo(String firstName, String lastName, String postalCode) {
        fillInfo(firstName, lastName, postalCode);
        clickContinue();
        return this;
    }

    public CheckoutPage clickContinueExpectingError() {
        click(CONTINUE);
        visible(ERROR);
        return this;
    }

    public String errorMessage() {
        return text(ERROR);
    }

    public CartPage cancel() {
        click(CANCEL);
        CartPage cartPage = new CartPage(driver);
        cartPage.waitUntilLoaded();
        return cartPage;
    }

    public List<String> summaryItemNames() {
        return texts(SUMMARY_ITEMS);
    }

    public double itemTotal() {
        return money(SUBTOTAL_LABEL);
    }

    public double tax() {
        return money(TAX_LABEL);
    }

    public double total() {
        return money(TOTAL_LABEL);
    }

    public CheckoutPage finish() {
        click(FINISH);
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
        visible(COMPLETE_HEADER);
        return this;
    }

    public String confirmationHeader() {
        return text(COMPLETE_HEADER);
    }

    public InventoryPage backHome() {
        click(BACK_HOME);
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitUntilLoaded();
        return inventoryPage;
    }

    private double money(By label) {
        String value = text(label);
        return Double.parseDouble(value.substring(value.indexOf('$') + 1).trim());
    }
}
