package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.BasePage;
import utils.ReportUtil;
import utils.WebDriverFactory;

public class BaseTest {
    protected WebDriver driver;
    protected ReportUtil report = new ReportUtil();

    @BeforeClass
    public void setUp() {
        driver = WebDriverFactory.createDriver();
    }

    @AfterClass
    public void tearDown() {
        try {
            report.writeReport("report.txt");
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
