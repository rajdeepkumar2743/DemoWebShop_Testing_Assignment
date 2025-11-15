package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private final By mainCategories = By.xpath("//ul[contains(@class,'top-menu')]/li/a");
    private final WaitUtils wait;

    public HomePage(WebDriver driver) {
        super(driver);
        wait = new WaitUtils(driver, 15);
    }

    /** Returns all main category names */
    public List<String> getAllCategoryNames() {
        forceWaitForMenuLoad();

        List<WebElement> elems = driver.findElements(mainCategories);
        List<String> names = new ArrayList<>();
        for (WebElement e : elems) {
            highlight(e);
            names.add(e.getText().trim());
        }
        return names;
    }

    /** Clicks a random category dynamically */
    public String clickRandomCategory() {
        forceWaitForMenuLoad();
        scrollToTop();

        List<WebElement> elems = driver.findElements(mainCategories);
        if (elems.isEmpty()) throw new RuntimeException("No categories found on homepage!");

        int idx = new Random().nextInt(elems.size());
        WebElement chosen = elems.get(idx);
        String categoryName = chosen.getText().trim();

        try {
            new Actions(driver).moveToElement(chosen).pause(200).perform();
            highlight(chosen);
            chosen.click();
        } catch (Exception ex) {
            super.clickJS(chosen);
        }

        return categoryName;
    }

    /** Scrolls the page to the top */
    private void scrollToTop() {
        js.executeScript("window.scrollTo(0,0)");
    }

    /** Force wait for categories to load */
    private void forceWaitForMenuLoad() {
        try {
            wait.waitForElementVisible(mainCategories);
        } catch (TimeoutException e) {
            // Retry by refreshing page (DemoWebShop quirk)
            driver.navigate().refresh();
            wait.waitForElementVisible(mainCategories);
        }
    }

    /** JS fallback click */
    protected void clickJS(WebElement element)
    {
        js.executeScript("arguments[0].click();", element);
    }
}
