package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InventoryPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By PRODUCT_CARDS = By.cssSelector(".inventory_item");
    private static final By PRODUCT_NAMES = By.cssSelector(".inventory_item_name");
    private static final By PRODUCT_PRICES = By.cssSelector(".inventory_item_price");
    private static final By SORT_DROPDOWN = By.cssSelector("select[data-test='product-sort-container']");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By RESET_LINK = By.id("reset_sidebar_link");
    private static final By CLOSE_MENU_BUTTON = By.id("react-burger-cross-btn");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        visible(PRODUCT_CARDS);
        return this;
    }

    public String pageTitle() {
        return text(PAGE_TITLE);
    }

    public int productCount() {
        return count(PRODUCT_CARDS);
    }

    public List<String> productNames() {
        return texts(PRODUCT_NAMES);
    }

    public List<Double> productPrices() {
        return texts(PRODUCT_PRICES).stream()
                .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                .toList();
    }

    public InventoryPage sortBy(String value) {
        new org.openqa.selenium.support.ui.Select(visible(SORT_DROPDOWN)).selectByValue(value);
        return this;
    }

    public InventoryPage addToCart(String itemSlug) {
        click(By.cssSelector("button[data-test='add-to-cart-" + itemSlug + "']"));
        return this;
    }

    public InventoryPage removeFromCart(String itemSlug) {
        click(By.cssSelector("button[data-test='remove-" + itemSlug + "']"));
        return this;
    }

    public int cartBadgeCount() {
        if (!isPresent(CART_BADGE)) {
            return 0;
        }
        return Integer.parseInt(text(CART_BADGE));
    }

    public CartPage openCart() {
        click(CART_LINK);
        CartPage cartPage = new CartPage(driver);
        cartPage.waitUntilLoaded();
        return cartPage;
    }

    public InventoryPage openMenu() {
        click(MENU_BUTTON);
        visible(LOGOUT_LINK);
        return this;
    }

    public LoginPage logout() {
        openMenu();
        click(LOGOUT_LINK);
        LoginPage loginPage = new LoginPage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        return loginPage;
    }

    public InventoryPage resetAppState() {
        openMenu();
        click(RESET_LINK);
        click(CLOSE_MENU_BUTTON);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(CLOSE_MENU_BUTTON));
        return this;
    }
}
