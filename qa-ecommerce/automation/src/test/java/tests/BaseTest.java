package tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import pages.InventoryPage;
import pages.LoginPage;

/**
 * Driver lifecycle for every test: a fresh headless Chrome session per method,
 * always closed afterwards. The base URL can be overridden with -DbaseUrl=...
 */
public abstract class BaseTest {

    protected static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com");
    protected static final String PASSWORD = "secret_sauce";
    protected static final String STANDARD_USER = "standard_user";
    protected static final String LOCKED_OUT_USER = "locked_out_user";
    protected static final String PROBLEM_USER = "problem_user";
    protected static final String PERFORMANCE_USER = "performance_glitch_user";

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--window-size=1440,900");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected LoginPage openLoginPage() {
        return new LoginPage(driver).open(BASE_URL);
    }

    protected InventoryPage loginAs(String username) {
        return openLoginPage().submitValidLogin(username, PASSWORD);
    }

    protected InventoryPage loginAsStandardUser() {
        return loginAs(STANDARD_USER);
    }
}
