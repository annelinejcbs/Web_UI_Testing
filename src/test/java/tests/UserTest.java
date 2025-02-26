package tests;

import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.UserListPage;
import pages.AddUserPage;
import utils.DataUtils;
import utils.Browser;
import utils.TakeScreenShot;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.extentReport;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class UserTest extends extentReport {

    private String baseUrl = "http://www.way2automation.com/angularjs-protractor/webtables/";
    private UserListPage userListPage;
    private AddUserPage addUserPage;

    private static final Logger logger = LogManager.getLogger(UserTest.class);
    private static final String USER_DATA_DIR = "user-data-dir-";
    private TakeScreenShot takeScreenShot = new TakeScreenShot();
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();  // ThreadLocal for WebDriver

    @BeforeClass
    public void setUpExtent() {
        extent = new ExtentReports();
    }

    @Parameters("browser")
    @BeforeMethod
    public void setUp(String browser) throws Exception {
        // Create WebDriver using the Browser utility class and set it into ThreadLocal
        WebDriver webDriver = Browser.createWebDriver(browser);
        driver.set(webDriver);  // Set the WebDriver instance in ThreadLocal
        driver.get().get(baseUrl);  // Navigate to the base URL
        driver.get().manage().window().maximize();  // Maximize window

        logger.info("Setting up WebDriver for " + browser);

        // Initialize Page Objects
        userListPage = PageFactory.initElements(driver.get(), UserListPage.class);
        addUserPage = PageFactory.initElements(driver.get(), AddUserPage.class);

        logger.info("WebDriver and application setup complete.");
    }

    @Test
    public void testAddUser() throws Exception {
        logger.info("Test Case 'testAddUser' started.");

        // Load user data
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
            WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(10));  // Fix: driver.get() is passed here
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

    @AfterMethod
    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();  // Quit the driver after the test method
        }
        cleanupTempDirectories();
    }

    private void cleanupTempDirectories() {
        try {
            Path tempDir = Path.of(USER_DATA_DIR + Thread.currentThread().getId());
            Files.walk(tempDir).map(Path::toFile).forEach(file -> {
                if (!file.delete()) {
                    logger.error("Failed to delete file: " + file.getAbsolutePath());
                }
            });
        } catch (Exception e) {
            logger.error("Error cleaning up temporary directories: " + e.getMessage());
        }
    }
}
