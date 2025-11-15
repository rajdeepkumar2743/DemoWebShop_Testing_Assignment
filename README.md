# Demo Web Shop E2E Test Automation

This project contains end-to-end (E2E) automated tests for the [Demo Web Shop](https://demowebshop.tricentis.com/) website using **Selenium WebDriver** with **TestNG** and **Maven**.  

The tests cover product selection, cart validation, and user registration flows including handling of jewelry products and gift cards.

---

## **Project Structure**

```

DemoWebShop_Testing_Assignment/
│
├── src/
│   ├── main/java/pages/       # Page Object Model classes
│   ├── test/java/tests/       # Test classes
│   └── test/java/utils/       # Utility classes (CSV reading, waits, screenshots, etc.)
│
├── testdata.csv               # Sample data for registration & gift card tests
├── pom.xml                    # Maven project configuration
└── README.md                  # Project documentation

````

---

## **Prerequisites**

- Java 21 or higher
- Maven 3.6+
- Chrome browser (version compatible with ChromeDriver)
- Internet connection to access the Demo Web Shop

---

## **Installation & Setup**

1. **Clone the repository:**
   ```bash
   git clone https://github.com/rajdeepkumar2743/DemoWebShop_Testing_Assignment.git
   cd DemoWebShop_Testing_Assignment
````

2. **Install dependencies and compile project:**

   ```bash
   mvn clean install
   ```

3. **Update test data if needed:**

   * Open `testdata.csv` and update sample user and gift card details.

---

## **Running Tests**

1. **Run all tests via Maven:**

   ```bash
   mvn test
   ```

2. **Run a specific test class via Maven:**

   ```bash
   mvn -Dtest=E2EFlowTest test
   ```

3. **Test Reports & Screenshots:**

   * Test results are saved in `target/surefire-reports/`.
   * Screenshots on failure are saved in `screenshots/`.
   * A simple execution log is generated as `report.txt`.

---

## **What is Covered**

* Homepage category validation
* Random product selection (up to 2 products)
* Jewelry product handling (random length input)
* Gift card handling (fields populated from CSV)
* Cart validation (products and total price)
* Checkout flow and user registration
* Negative registration scenario (error validation)
* Screenshots on failure
* Logging of test steps

---

## **Known Issues**

* **CDP Version Warning:** You may see warnings about CDP version mismatch; they are harmless unless causing failures.
* **Dynamic Product Links:** Some products may be unavailable or their links point to a category page → skipped automatically.
* **Slow page load:** If the product page takes longer than 30 seconds to load, test may fail. Increase explicit wait if needed.

---

## **Notes**

* The test does **not place real orders**; checkout stops before payment.
* Test email addresses are dynamically generated using timestamp to avoid conflicts.
* Make sure the ChromeDriver version matches your Chrome browser version.

---

## **Author**

**Rajdeep Kumar**
Demo Web Shop Automation Project

---

