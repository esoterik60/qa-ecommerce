package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.InventoryPage;

public class InventoryTest extends BaseTest {

    @Test(description = "TC-013 six products are listed")
    public void productListShowsSixProducts() {
        InventoryPage inventory = loginAsStandardUser();

        Assert.assertEquals(inventory.productCount(), 6, "Expected 6 product cards");
        Assert.assertEquals(inventory.productNames().size(), 6, "Every card should render a name");
    }

    @Test(description = "TC-015 sort by price low to high")
    public void sortByPriceLowToHigh() {
        InventoryPage inventory = loginAsStandardUser().sortBy("lohi");

        List<Double> prices = inventory.productPrices();
        List<Double> sorted = new ArrayList<>(prices);
        Collections.sort(sorted);

        Assert.assertEquals(prices, sorted, "Prices should be ascending");
        Assert.assertEquals(prices.get(0), 7.99, 0.001, "Cheapest product price mismatch");
    }

    @Test(description = "TC-016 sort by price high to low")
    public void sortByPriceHighToLow() {
        InventoryPage inventory = loginAsStandardUser().sortBy("hilo");

        List<Double> prices = inventory.productPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Collections.reverseOrder());

        Assert.assertEquals(prices, sorted, "Prices should be descending");
    }

    @Test(description = "TC-017 sort by name A to Z")
    public void sortByNameAToZ() {
        InventoryPage inventory = loginAsStandardUser().sortBy("az");

        List<String> names = inventory.productNames();
        List<String> sorted = new ArrayList<>(names);
        Collections.sort(sorted);

        Assert.assertEquals(names, sorted, "Names should be alphabetical ascending");
    }

    @Test(description = "TC-020 adding one product updates the cart badge")
    public void addProductUpdatesCartBadge() {
        InventoryPage inventory = loginAsStandardUser().addToCart("sauce-labs-backpack");

        Assert.assertEquals(inventory.cartBadgeCount(), 1, "Badge should show 1 after adding one product");
    }

    @Test(description = "TC-021 adding two products updates the cart badge")
    public void addTwoProductsUpdatesCartBadge() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .addToCart("sauce-labs-bike-light");

        Assert.assertEquals(inventory.cartBadgeCount(), 2, "Badge should show 2 after adding two products");
    }

    @Test(description = "TC-022 removing a product clears the badge")
    public void removeProductClearsCartBadge() {
        InventoryPage inventory = loginAsStandardUser().addToCart("sauce-labs-backpack");
        Assert.assertEquals(inventory.cartBadgeCount(), 1, "Precondition: badge shows 1");

        inventory.removeFromCart("sauce-labs-backpack");

        Assert.assertEquals(inventory.cartBadgeCount(), 0, "Badge should disappear after removal");
    }

    @Test(description = "TC-064 performance_glitch_user can log in")
    public void performanceGlitchUserCanLogin() {
        InventoryPage inventory = loginAs(PERFORMANCE_USER);

        Assert.assertTrue(inventory.currentUrl().contains("/inventory.html"),
                "performance_glitch_user should reach the inventory page");
    }
}
