package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

    private static final By CART_ITEMS = By.cssSelector(".cart_item");
    private static final By ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    private static final By ITEM_PRICES = By.cssSelector(".cart_item .inventory_item_price");
    private static final By CHECKOUT_BUTTON = By.cssSelector("button[data-test='checkout']");
    private static final By CONTINUE_SHOPPING = By.cssSelector("button[data-test='continue-shopping']");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
        visible(CHECKOUT_BUTTON);
        return this;
    }

    public int itemCount() {
        return count(CART_ITEMS);
    }

    public List<String> itemNames() {
        return texts(ITEM_NAMES);
    }

    public List<Double> itemPrices() {
        return texts(ITEM_PRICES).stream()
                .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                .toList();
    }

    public CartPage removeItem(String itemSlug) {
        click(By.cssSelector("button[data-test='remove-" + itemSlug + "']"));
        return this;
    }

    public int cartBadgeCount() {
        if (!isPresent(CART_BADGE)) {
            return 0;
        }
        return Integer.parseInt(text(CART_BADGE));
    }

    public CheckoutPage checkout() {
        click(CHECKOUT_BUTTON);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.waitUntilStepOneLoaded();
        return checkoutPage;
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitUntilLoaded();
        return inventoryPage;
    }
}
