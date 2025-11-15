package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartRows = By.cssSelector("table.cart tbody tr");
    private final By quantityInput = By.cssSelector("input.qty-input");
    private final By productNameLink = By.cssSelector(".product a");
    private final By unitPrice = By.cssSelector(".unit-price");
    private final By subtotal = By.cssSelector(".product-subtotal");
    private final By checkoutBtn = By.id("checkout");

    private final WaitUtils wait;

    public CartPage(WebDriver driver) {
        super(driver);
        wait = new WaitUtils(driver, 10);
    }

    /** Returns all cart item rows */
    public List<WebElement> getCartRows() {
        wait.waitForElementVisible(cartRows);
        return driver.findElements(cartRows);
    }

    /** Opens the cart page */
    public void openCart() {
        WebElement cartLink = driver.findElement(By.cssSelector("a[href='/cart']"));
        scrollToElement(cartLink);
        clickJS(cartLink);
        wait.waitForElementVisible(cartRows);
    }

    /** Parses a price string into a double */
    public double parsePrice(String priceText) {
        // Handles formats like "$1,234.56" or "₹1,234.56"
        String cleaned = priceText.replaceAll("[^0-9.,]", "").replaceAll(",", "");
        return Double.parseDouble(cleaned);
    }

    /** Proceeds to checkout page */
    public void proceedToCheckout() {
        WebElement btn = wait.waitForElementClickable(checkoutBtn);
        scrollToElement(btn);
        clickJS(btn);
    }

    /** Gets product name from a cart row */
    public String getProductName(WebElement row) {
        return row.findElement(productNameLink).getText().trim();
    }

    /** Gets quantity from a cart row */
    public int getQuantity(WebElement row) {
        try {
            return Integer.parseInt(row.findElement(quantityInput).getAttribute("value").trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /** Gets unit price from a cart row */
    public double getUnitPrice(WebElement row) {
        try {
            String priceText = row.findElement(unitPrice).getText();
            return parsePrice(priceText);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /** Gets subtotal from a cart row */
    public double getSubtotal(WebElement row) {
        try {
            String subtotalText = row.findElement(subtotal).getText();
            return parsePrice(subtotalText);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
