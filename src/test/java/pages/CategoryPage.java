package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import utils.WaitUtils;

import java.util.List;
import java.util.Random;

public class CategoryPage extends BasePage {
    private final By productItems = By.cssSelector(".product-item, .item-box");
    private final WaitUtils wait;
    private final Random random;

    public CategoryPage(WebDriver driver) {
        super(driver);
        wait = new WaitUtils(driver, 20);
        random = new Random();
    }

    public int getProductCount() {
        wait.waitForAllVisible(productItems);
        return driver.findElements(productItems).size();
    }

    public List<WebElement> getProductElements() {
        wait.waitForAllVisible(productItems);
        return driver.findElements(productItems);
    }

    public WebElement getRandomProductElementExcept(int excludeIndex) {
        List<WebElement> elems = getProductElements();
        if (elems.isEmpty()) throw new RuntimeException("No products found in category!");

        int idx;
        if (elems.size() == 1) return elems.get(0); // only one product
        do {
            idx = random.nextInt(elems.size());
        } while (idx == excludeIndex);

        WebElement element = elems.get(idx);

        // Optional: highlight and scroll
        highlight(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);

        return element;
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}
