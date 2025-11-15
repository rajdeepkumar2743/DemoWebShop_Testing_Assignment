package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;

public class WebDriverFactory {
    public static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--start-maximized");
        //opts.addArguments("--headless=new"); // uncomment to run headless
        return new ChromeDriver(opts);
    }
}
