package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Shared behaviour for all page objects: an explicit wait plus the small set of
 * helpers that every page needs.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected int count(By locator) {
        return driver.findElements(locator).size();
    }

    protected List<String> texts(By locator) {
        return driver.findElements(locator).stream()
                .map(element -> element.getText().trim())
                .filter(value -> !value.isEmpty())
                .toList();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
