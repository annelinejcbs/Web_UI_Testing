package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.UserListPage;
import pages.AddUserPage;
import utils.DataUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import utils.TakeScreenShot;
import utils.extentReport;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class UserTest extends extentReport {
    private String baseUrl = "http://www.way2automation.com/angularjs-protractor/webtables/";
    private UserListPage userListPage;
    private AddUserPage addUserPage;

    // Logger instance for this class
    private static final Logger logger = LogManager.getLogger(UserTest.class);

    TakeScreenShot takeScreenShot = new TakeScreenShot();

    @BeforeClass
    public void setUpExtent(){
        extent = new ExtentReports();
    }

    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Parameters("browser")
    @BeforeMethod
    public void setUp(String browser) throws Exception {
        WebDriver webDriver;

        // Create unique temporary user data directories for each session
        Path tempDir = Files.createTempDirectory("chrome-user-data-" + Thread.currentThread().getId());
        Path tempDirEdge = Files.createTempDirectory("edge-user-data-" + Thread.currentThread().getId());

        if (browser.equalsIgnoreCase("chrome")) {
            // Set up for Chrome
            WebDriverManager.chromedriver().clearDriverCache().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-data-dir=" + tempDir.toString());
            options.addArguments("--headless");  // Remove if not needed for debugging
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            webDriver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            // Set up for Firefox
            WebDriverManager.firefoxdriver().clearDriverCache().setup();
            webDriver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            // Set up for Edge
            WebDriverManager.edgedriver().clearDriverCache().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("user-data-dir=" + tempDirEdge.toString());
            webDriver = new EdgeDriver(edgeOptions);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.set(webDriver);
        driver.get().get(baseUrl);  // Navigate to the base URL
        driver.get().manage().window().maximize();

        logger.info("Setting up WebDriver for " + browser);

        // Initialize page objects
        userListPage = PageFactory.initElements(driver.get(), UserListPage.class);
        addUserPage = PageFactory.initElements(driver.get(), AddUserPage.class);

        logger.info("WebDriver and application setup complete.");
    }

    @AfterMethod
    public void tearDown() {
        // Clean up WebDriver and temporary directories
        logger.info("Test completed. Closing the browser.");

        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }

        // Cleanup temporary user data directories
        cleanupTempDirectories();

        logger.info("Temporary directories cleaned up.");
    }

    private void cleanupTempDirectories() {
        try {
            Path tempDir = Path.of("chrome-user-data-" + Thread.currentThread().getId());
            Path tempDirEdge = Path.of("edge-user-data-" + Thread.currentThread().getId());

            Files.walk(tempDir).map(Path::toFile).forEach(file -> {
                if (!file.delete()) {
                    logger.error("Failed to delete file: " + file.getAbsolutePath());
                }
            });

            Files.walk(tempDirEdge).map(Path::toFile).forEach(file -> {
                if (!file.delete()) {
                    logger.error("Failed to delete file: " + file.getAbsolutePath());
                }
            });
        } catch (Exception e) {
            logger.error("Error cleaning up temporary directories: " + e.getMessage());
        }
    }

    @Test
    public void testAddUser() throws Exception {
        logger.info("Test Case 'testAddUser' started.");

        JsonNode testData = DataUtils.loadTestData("src/data/testdata.json");
        JsonNode users = testData.get("users");

        test = extent.createTest("Verify User List Table Testcase");
        test.log(Status.INFO, "Verify User List Table Testcase started");

        // Step 1: Validate the User List Table visibility
        Assert.assertTrue(userListPage.isUserListTableVisible(), "User List Table is not visible.");
        logger.info("User List Table visibility validated.");
        takeScreenShot.takeSnapshot(driver.get(), "UserListTableValidation");

        test.log(Status.INFO, "User List Table Testcase completed");

        // Step 2: Add users from the JSON data
        test = extent.createTest("Add User Test Case");
        test.log(Status.INFO, "Add User Test Case started");

        for (int i = 0; i < users.size(); i++) {
            JsonNode user = users.get(i);

            String firstName = user.has("firstName") ? user.get("firstName").asText() : "";
            String lastName = user.has("lastName") ? user.get("lastName").asText() : "";
            String username = user.has("username") ? user.get("username").asText() + System.currentTimeMillis() : "";
            String password = user.has("password") ? user.get("password").asText() : "";
            String company = user.has("company") ? user.get("company").asText() : "";
            String role = user.has("role") ? user.get("role").asText() : "";
            String email = user.has("email") ? user.get("email").asText() : "";
            String cellPhone = user.has("cellPhone") ? user.get("cellPhone").asText() : "";

            logger.debug("Filling out details for user: {}", username);

            // Step 3: Click "Add User" and wait for the form to load
            userListPage.clickAddUser();
            WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@name,'FirstName')]")));

            // Step 4: Fill out the user form
            addUserPage.clearFields();
            addUserPage.EnterFirstName(firstName);
            addUserPage.EnterLastName(lastName);
            addUserPage.EnterUserName(username);
            addUserPage.EnterPassword(password);
            addUserPage.SelectCompany(company);
            addUserPage.SelectRole(role);
            addUserPage.EnterEmail(email);
            addUserPage.EnterMobileNumber(cellPhone);
            takeScreenShot.takeSnapshot(driver.get(), "UserDetails_" + username);

            // Save the user
            addUserPage.clickSave();
            logger.info("User {} saved successfully.", username);

            // Step 5: Verify the user was added
            test = extent.createTest("Verify User Added Test Case: " + username);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[contains(text(),'" + username + "')]")));
            Assert.assertTrue(userListPage.isUserAdded(username), "User " + username + " was not added successfully.");
            logger.info("User {} was added successfully.", username);
            takeScreenShot.takeSnapshot(driver.get(), "UserAdded_" + username);
        }

        test.log(Status.INFO, "Add User Test Case completed");
    }
}
