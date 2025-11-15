package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {
    public static String takeScreenshot(WebDriver driver, String namePrefix) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destDir = Path.of("screenshots");
            Files.createDirectories(destDir);
            String fileName = namePrefix + "_" + System.currentTimeMillis() + ".png";
            Path dest = destDir.resolve(fileName);
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
