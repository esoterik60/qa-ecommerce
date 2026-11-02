package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.CartPage;
import pages.InventoryPage;

public class CartTest extends BaseTest {

    @Test(description = "TC-025 cart lists the added product with its price")
    public void cartListsAddedProduct() {
        InventoryPage inventory = loginAsStandardUser().addToCart("sauce-labs-backpack");
        List<Double> listingPrices = inventory.productPrices();

        CartPage cart = inventory.openCart();

        Assert.assertEquals(cart.itemCount(), 1, "Cart should contain one item");
        Assert.assertEquals(cart.itemNames().get(0), "Sauce Labs Backpack", "Wrong item in cart");
        Assert.assertTrue(listingPrices.contains(cart.itemPrices().get(0)),
                "Cart price should match one of the listing prices");
    }

    @Test(description = "TC-027 removing an item from the cart page")
    public void removeItemFromCartPage() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .addToCart("sauce-labs-bike-light");
        CartPage cart = inventory.openCart();

        cart.removeItem("sauce-labs-backpack");

        Assert.assertEquals(cart.itemCount(), 1, "One item should remain");
        Assert.assertEquals(cart.itemNames().get(0), "Sauce Labs Bike Light", "Wrong item was removed");
    }

    @Test(description = "TC-028 badge decrements after removal")
    public void badgeDecrementsAfterRemoval() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .addToCart("sauce-labs-bike-light");
        CartPage cart = inventory.openCart();

        cart.removeItem("sauce-labs-backpack");

        Assert.assertEquals(cart.cartBadgeCount(), 1, "Badge should show 1");
    }

    @Test(description = "TC-029 empty cart shows no badge")
    public void emptyCartHasNoBadge() {
        CartPage cart = loginAsStandardUser().openCart();

        Assert.assertEquals(cart.itemCount(), 0, "Cart should be empty on a fresh session");
        Assert.assertEquals(cart.cartBadgeCount(), 0, "No badge should be displayed for an empty cart");
    }

    @Test(description = "TC-030 continue shopping returns to the inventory")
    public void continueShoppingReturnsToInventory() {
        InventoryPage inventory = loginAsStandardUser().openCart().continueShopping();

        Assert.assertTrue(inventory.currentUrl().contains("/inventory.html"),
                "Continue Shopping should return to the inventory page");
    }
}
