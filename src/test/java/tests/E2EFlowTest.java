package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.CSVUtils;
import utils.ScreenshotUtil;

import java.util.*;

public class E2EFlowTest extends BaseTest {

    @Test(priority = 1)
    public void homepageCategoryAndSelectProducts() {
        try {
            driver.get("https://demowebshop.tricentis.com/");
            HomePage home = new HomePage(driver);

            // --- STEP 1: Get all non-empty categories ---
            List<String> categories = home.getAllCategoryNames();
            categories.removeIf(String::isEmpty);
            report.log("Filtered categories: " + categories);
            Assert.assertTrue(categories.size() > 0, "No valid categories found");

            // --- STEP 2: Click a random category ---
            home.clickRandomCategory();
            CategoryPage categoryPage = new CategoryPage(driver);
            String selectedCategoryTitle = categoryPage.getPageTitle();
            report.log("Selected category page title: " + selectedCategoryTitle);

            // --- STEP 3: Get products in category ---
            List<WebElement> productElements = categoryPage.getProductElements();
            int productCount = productElements.size();
            report.log("Products in category: " + productCount);
            Assert.assertTrue(productCount > 0, "No products found in selected category");

            // --- STEP 4: Pick up to 2 random products ---
            int productsToPick = Math.min(2, productCount);
            Random random = new Random();
            Set<Integer> pickedIndexes = new HashSet<>();
            List<Map<String, Object>> selectedProducts = new ArrayList<>();

            // Read CSV once for Gift Card or user data
            Map<String, String> csv = null;
            try { csv = CSVUtils.readFirstRow("testdata.csv"); }
            catch (Exception e) { report.log("CSV read error, using fallback data"); }

            for (int i = 0; i < productsToPick; i++) {
                int idx;
                do { idx = random.nextInt(productElements.size()); }
                while (pickedIndexes.contains(idx) && pickedIndexes.size() < productElements.size());
                pickedIndexes.add(idx);

                WebElement product = productElements.get(idx);
                WebElement link;
                try { link = product.findElement(By.cssSelector(".product-title a")); }
                catch (Exception e) { link = product.findElement(By.cssSelector("a")); }

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
                try { link.click(); }
                catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link); }

                ProductPage productPage = new ProductPage(driver);
                String name = "", price = "", url = productPage.getCurrentUrl();
                try { name = productPage.getProductName(); } catch (Exception ignored) {}
                try { price = productPage.getProductPrice(); } catch (Exception ignored) {}

                report.log("Selected product: " + name + " | " + price + " | " + url);

                // --- Add to Cart, handling Jewelry/Gift Card automatically ---
                boolean added = productPage.addToCartIfAvailable();
                if (!added) report.log("Skipped: Add to Cart Not Available for " + name);

                Map<String, Object> productInfo = new HashMap<>();
                productInfo.put("name", name);
                productInfo.put("price", price);
                productInfo.put("url", url);
                productInfo.put("added", added);
                selectedProducts.add(productInfo);

                driver.navigate().back();
                productElements = categoryPage.getProductElements(); // refresh list
            }

            // --- STEP 5: Open cart and validate products ---
            CartPage cart = new CartPage(driver);
            cart.openCart();
            List<WebElement> rows = cart.getCartRows();
            report.log("Cart rows found: " + rows.size());

            double totalCalculated = 0.0;
            for (Map<String, Object> product : selectedProducts) {
                boolean added = (Boolean) product.get("added");
                if (!added) {
                    report.log("Skipped product not added: " + product.get("name"));
                    continue;
                }

                String name = (String) product.get("name");
                boolean present = rows.stream().anyMatch(rw -> rw.getText().toLowerCase()
                        .contains(name.toLowerCase().substring(0, Math.min(10, name.length()))));

                if (!present) {
                    report.log("Validation error: product not present in cart - " + name);
                    Assert.fail("Product missing in cart: " + name);
                } else {
                    for (WebElement rw : rows) {
                        if (rw.getText().toLowerCase().contains(name.toLowerCase().substring(0, Math.min(10, name.length())))) {
                            String unitPriceTxt = "";
                            try { unitPriceTxt = rw.findElement(By.cssSelector(".product-unit-price, .unit-price")).getText(); }
                            catch (Exception ignored) {}
                            try { totalCalculated += cart.parsePrice(unitPriceTxt); } catch (Exception ignored) {}
                        }
                    }
                }
            }

            report.log("Cart validation completed. Total calculated: " + totalCalculated);

            // --- STEP 6: Proceed to checkout and register user ---
            cart.proceedToCheckout();
            RegistrationPage reg = new RegistrationPage(driver);
            reg.openRegistrationPage();

            String firstName = csv != null ? csv.getOrDefault("firstName", "John") : "John";
            String lastName = csv != null ? csv.getOrDefault("lastName", "Doe") : "Doe";
            String password = csv != null ? csv.getOrDefault("password", "Password123!") : "Password123!";
            String confirmPassword = csv != null ? csv.getOrDefault("confirmPassword", password) : password;
            String uniqueEmail = csv != null ? csv.getOrDefault("email", "auto+" + System.currentTimeMillis() + "@example.com")
                    : "auto+" + System.currentTimeMillis() + "@example.com";

            reg.register("male", firstName, lastName, uniqueEmail, password, confirmPassword);
            report.log("Positive registration attempted with email: " + uniqueEmail);

            // --- STEP 7: Negative registration test ---
            reg.openRegistrationPage();
            reg.register("male", "", lastName, "invalid@example.com", password, confirmPassword);

            String errorMsg = reg.getErrorText();
            if (errorMsg == null || errorMsg.isEmpty()) {
                String path = ScreenshotUtil.takeScreenshot(driver, "NegativeRegistrationError");
                report.log("No error message found on invalid registration. Screenshot: " + path);
            } else {
                report.log("Negative registration error captured: " + errorMsg);
            }

            report.log("E2E test flow completed successfully (stopped before placing order).");

        } catch (Exception e) {
            report.log("Test error: " + e.getMessage());
            String path = ScreenshotUtil.takeScreenshot(driver, "E2EFlowTest_error");
            report.log("Screenshot on error: " + path);
            Assert.fail(e.getMessage());
        } finally {
            report.writeReport("report.txt");
        }
    }
}
