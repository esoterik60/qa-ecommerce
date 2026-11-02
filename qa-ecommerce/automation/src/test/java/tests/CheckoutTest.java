package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;

public class CheckoutTest extends BaseTest {

    @Test(description = "TC-033/037/038/039/042/043/044 full checkout flow")
    public void fullCheckoutFlowCompletesAnOrder() {
        InventoryPage inventory = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .addToCart("sauce-labs-bike-light");
        CartPage cart = inventory.openCart();
        List<Double> cartPrices = cart.itemPrices();

        CheckoutPage checkout = cart.checkout().continueWithInfo("Jovan", "Vojinovic", "35000")
                .waitUntilOverviewLoaded();

        Assert.assertEquals(checkout.summaryItemNames().size(), 2, "Overview should list both cart items");
        double expectedItemTotal = cartPrices.get(0) + cartPrices.get(1);
        Assert.assertEquals(checkout.itemTotal(), expectedItemTotal, 0.01, "Item total should equal the cart sum");
        Assert.assertEquals(checkout.total(), checkout.itemTotal() + checkout.tax(), 0.01,
                "Total should equal item total plus tax");

        checkout.finish();
        Assert.assertEquals(checkout.confirmationHeader(), "Thank you for your order!",
                "Confirmation page text mismatch");

        InventoryPage backOnInventory = checkout.backHome();
        Assert.assertEquals(backOnInventory.cartBadgeCount(), 0, "Cart should be empty after a completed order");
    }

    @Test(description = "TC-034 first name is required")
    public void firstNameIsRequired() {
        CheckoutPage checkout = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .openCart()
                .checkout()
                .fillInfo("", "Vojinovic", "35000")
                .clickContinueExpectingError();

        Assert.assertTrue(checkout.errorMessage().contains("First Name is required"),
                "Unexpected error text: " + checkout.errorMessage());
    }

    @Test(description = "TC-036 postal code is required")
    public void postalCodeIsRequired() {
        CheckoutPage checkout = loginAsStandardUser()
                .addToCart("sauce-labs-backpack")
                .openCart()
                .checkout()
                .fillInfo("Jovan", "Vojinovic", "")
                .clickContinueExpectingError();

        Assert.assertTrue(checkout.errorMessage().contains("Postal Code is required"),
                "Unexpected error text: " + checkout.errorMessage());
    }

    @Test(description = "TC-040 cancel on step one returns to the cart unchanged")
    public void cancelReturnsToCart() {
        CartPage cart = loginAsStandardUser().addToCart("sauce-labs-backpack").openCart();

        CartPage cartAfterCancel = cart.checkout().cancel();

        Assert.assertTrue(cartAfterCancel.currentUrl().contains("cart.html"), "Cancel should return to the cart");
        Assert.assertEquals(cartAfterCancel.itemCount(), 1, "Cart should be unchanged after cancelling");
    }
}
