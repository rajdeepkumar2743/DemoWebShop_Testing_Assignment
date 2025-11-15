package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import utils.WaitUtils;

public class ProductPage extends BasePage {

    private final WaitUtils wait;

    private final By nameLocator = By.cssSelector("div.product-name h1");
    private final By priceLocator = By.cssSelector(".product-price span, span.price.actual-price");
    private final By addToCartButton = By.cssSelector(
            "input[value='Add to cart'], " +
                    "button.add-to-cart-button, " +
                    "#add-to-cart-button-1"
    );
    private final By successNotification = By.cssSelector(".bar-notification.success");
    private final By closeNotification = By.cssSelector(".bar-notification.success .close");

    public ProductPage(WebDriver driver) {
        super(driver);
        this.wait = new WaitUtils(driver, 20);
    }

    // -------------------- PRODUCT INFO --------------------

    public String getProductName() {
        return wait.waitForElementVisible(nameLocator).getText().trim();
    }

    public String getProductPrice() {
        return wait.waitForElementVisible(priceLocator).getText().trim();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // -------------------- ADD TO CART --------------------

    public boolean addToCartIfAvailable() {
        try {
            WebElement button = wait.waitForElementClickable(addToCartButton);
            highlight(button);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
            button.click();

            wait.waitForElementVisible(successNotification);

            // Close notification safely
            if (!driver.findElements(closeNotification).isEmpty()) {
                try {
                    driver.findElement(closeNotification).click();
                } catch (Exception ignored) {}
            }

            wait.waitForElementInvisible(successNotification);

            System.out.println("Product added to cart successfully: " + getProductName());
            return true;

        } catch (Exception e) {
            System.out.println("Skipped: Add to Cart Not Available for " + getProductName() + " | " + e.getMessage());
            return false;
        }
    }
}
