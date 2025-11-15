package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;

public class RegistrationPage extends BasePage {

    private final WaitUtils wait;

    // Locators
    private final By genderMale = By.id("gender-male");
    private final By genderFemale = By.id("gender-female");
    private final By firstName = By.id("FirstName");
    private final By lastName = By.id("LastName");
    private final By email = By.id("Email");
    private final By password = By.id("Password");
    private final By confirmPassword = By.id("ConfirmPassword");
    private final By registerBtn = By.id("register-button");

    private final By validationErrors = By.cssSelector(".field-validation-error");
    private final By summaryErrors = By.cssSelector("div.validation-summary-errors > ul > li");
    private final By registrationCompleted = By.cssSelector("div.result");

    public RegistrationPage(WebDriver driver) {
        super(driver);
        this.wait = new WaitUtils(driver, 20);
    }

    // Open registration page
    public void openRegistrationPage() {
        driver.get("https://demowebshop.tricentis.com/register");
        wait.waitForPageToLoad();
    }

    // Select gender
    public void selectGender(String gender) {
        if ("male".equalsIgnoreCase(gender)) {
            wait.waitForElementClickable(genderMale).click();
        } else if ("female".equalsIgnoreCase(gender)) {
            wait.waitForElementClickable(genderFemale).click();
        }
    }

    // Type safely
    public void typeFirstName(String fname) { wait.typeSafely(firstName, fname); }
    public void typeLastName(String lname) { wait.typeSafely(lastName, lname); }
    public void typeEmail(String mail) { wait.typeSafely(email, mail); }
    public void typePassword(String pass) { wait.typeSafely(password, pass); }
    public void typeConfirmPassword(String cpass) { wait.typeSafely(confirmPassword, cpass); }

    // Click register
    public void clickRegister() {
        WebElement btn = wait.waitForElementClickable(registerBtn);
        highlight(btn);
        btn.click();
        wait.waitForPageToLoad();
    }

    // Main registration method for E2E test
    public void register(String gender, String fname, String lname, String mail, String pass, String cpass) {
        try {
            selectGender(gender);
            typeFirstName(fname);
            typeLastName(lname);
            typeEmail(mail);
            typePassword(pass);
            typeConfirmPassword(cpass);
            clickRegister();
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // Get success message
    public String getSuccessMessage() {
        try {
            return wait.waitForElementVisible(registrationCompleted).getText().trim();
        } catch (Exception e) { return null; }
    }

    // Get first available error text
    public String getErrorText() {
        try {
            List<WebElement> summary = driver.findElements(summaryErrors);
            if (!summary.isEmpty()) {
                WebElement err = wait.waitForElementVisible(summaryErrors);
                highlight(err);
                return err.getText().trim();
            }

            List<WebElement> fieldErrors = driver.findElements(validationErrors);
            if (!fieldErrors.isEmpty()) {
                WebElement err = wait.waitForElementVisible(validationErrors);
                highlight(err);
                return err.getText().trim();
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
