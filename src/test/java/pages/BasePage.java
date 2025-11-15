package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasePage {
    protected final WebDriver driver;
    protected final JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Highlights a WebElement with a red border for better visibility during execution.
     * @param element The WebElement to highlight
     */
    protected void highlight(WebElement element) {
        try {
            js.executeScript("arguments[0].style.border='3px solid red'", element);
        } catch (Exception ignored) {}
    }

    /**
     * Clicks an element using JavaScriptExecutor
     * @param element The WebElement to click
     */
    protected void clickJS(WebElement element) {
        try {
            js.executeScript("arguments[0].click();", element);
        } catch (Exception ignored) {}
    }

    /**
     * Scrolls an element into view using JavaScriptExecutor
     * @param element The WebElement to scroll into view
     */
    protected void scrollToElement(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        } catch (Exception ignored) {}
    }
}
