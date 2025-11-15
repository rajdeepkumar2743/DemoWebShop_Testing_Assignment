package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver, int seconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    // -------------------- BASIC SMART WAITS ---------------------------- //

    public WebElement waitForElementVisible(By locator) {
        return retryOnStale(() -> wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return retryOnStale(() -> wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator)));
    }

    public WebElement waitForElementClickable(By locator) {
        return retryOnStale(() -> wait.until(ExpectedConditions.elementToBeClickable(locator)));
    }

    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForTitleContains(String text) {
        return wait.until(ExpectedConditions.titleContains(text));
    }

    // ------------------ SEND KEYS WITH CLEANING ------------------------ //

    public void typeSafely(By locator, String text) {
        WebElement el = waitForElementVisible(locator);
        highlight(el);
        el.clear();
        el.sendKeys(text);
    }

    // ------------------ JS SCROLLING ---------------------------------- //

    public void scrollToElement(By locator) {
        WebElement el = waitForElementVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
    }

    // ------------------ WAIT FOR PAGE LOAD ----------------------------- //

    public void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                (ExpectedCondition<Boolean>) wd ->
                        ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete")
        );
    }

    // ------------------ RETRY ON STALE ELEMENT ------------------------- //

    private <T> T retryOnStale(Supplier<T> action) {
        int attempts = 3;
        while (attempts > 0) {
            try {
                return action.get();
            } catch (StaleElementReferenceException e) {
                attempts--;
                sleep(300);
            }
        }
        throw new RuntimeException("Stale Element Retry Failed After 3 Attempts");
    }

    // ------------------ HIGHLIGHT ELEMENT ------------------------------- //

    private void highlight(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='2px solid red'", element);
        } catch (Exception ignored) {}
    }

    // ------------------ SLEEP HELPER ---------------------------------- //

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    // ------------------ CUSTOM CONDITION WAIT -------------------------- //

    /**
     * Wait until a custom condition returns true or timeout is reached.
     * @param driver WebDriver instance
     * @param seconds max wait time in seconds
     * @param condition lambda returning Boolean
     * @return true if condition met, false if timeout
     */
    public static boolean waitForCondition(WebDriver driver, int seconds, Function<WebDriver, Boolean> condition) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            return wait.until(condition);
        } catch (TimeoutException e) {
            return false;
        }
    }
}
