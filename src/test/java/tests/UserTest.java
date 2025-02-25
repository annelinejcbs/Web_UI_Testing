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

    // Create a logger instance for this class
    private static final Logger logger = LogManager.getLogger(UserTest.class);

    TakeScreenShot takeScreenShot = new TakeScreenShot();

    @BeforeClass
    public void setUpExtent(){
        extent = new ExtentReports();
    }

    // ThreadLocal ensures each test method gets its own WebDriver instance.
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Parameters("browser")
    @BeforeMethod
    public void setUp(String browser) throws Exception {
        WebDriver webDriver;

        // Create a unique temporary user data directory for each session
        Path tempDir = Files.createTempDirectory("chrome-user-data");

        if (browser.equalsIgnoreCase("chrome")) {
            // Set up for Chrome
            WebDriverManager.chromedriver().clearDriverCache().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-data-dir=" + tempDir.toString());
            options.addArguments("--headless");
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
            edgeOptions.addArguments("user-data-dir=" + tempDir.toString());
            webDriver = new EdgeDriver(edgeOptions);
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.set(webDriver);  // Set the WebDriver to ThreadLocal
        driver.get().get(baseUrl);  // Use driver.get() to access the WebDriver instance
        driver.get().manage().window().maximize();  // Maximize the window

        // Log the start of the setup
        logger.info("Setting up the WebDriver and launching the application." + " " + browser);

        userListPage = PageFactory.initElements(driver.get(), UserListPage.class);
        addUserPage = PageFactory.initElements(driver.get(), AddUserPage.class);

        logger.info("WebDriver and application setup complete.");
    }

    @AfterMethod
    public void tearDown() {
        // Log the end of the test
        logger.info("Test completed. Closing the browser.");

        // Clean up WebDriver and delete the temp directory
        if (driver.get() != null) {
            driver.get().quit();  // Clean up after the test
            driver.remove();
        }

        logger.info("Browser closed.");
    }

    @Test
    public void testAddUser() throws Exception {
        logger.info("Test Case 'testAddUser' started.");

        JsonNode testData = DataUtils.loadTestData("src/data/testdata.json");
        JsonNode users = testData.get("users");

        logger.debug("Navigating to User List Page.");
        test = extent.createTest("Verify User List Table Testcase");
        test.log(Status.INFO, "Verify User List Table Testcase started");

        // Step 2: Validate that we are on the User List Table page
        Assert.assertTrue(userListPage.isUserListTableVisible(), "User List Table is not visible.");
        logger.info("Successfully validated User List Table visibility.");
        takeScreenShot.takeSnapshot(driver.get(), "Verify User List Table Testcast");

        test.log(Status.INFO, "Verify User List Table Testcase ended");

        // Step 3: Add users from the JSON file
        test = extent.createTest("Add User Test Case");
        test.log(Status.INFO, "Add User Test Case started");
        logger.debug("Entering user details from test data.");

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

            // Step 3.1: Click "Add User" and wait for the form to appear
            userListPage.clickAddUser();
            WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@name,'FirstName')]")));

            // Step 3.2: Clear Textboxes
            addUserPage.clearFields();
            logger.debug("Cleared input fields for user.");

            // Step 3.3: Fill out the form with user data
            addUserPage.EnterFirstName(firstName);
            addUserPage.EnterLastName(lastName);
            addUserPage.EnterUserName(username);
            addUserPage.EnterPassword(password);
            addUserPage.SelectCompany(company);
            addUserPage.SelectRole(role);
            addUserPage.EnterEmail(email);
            addUserPage.EnterMobileNumber(cellPhone);
            takeScreenShot.takeSnapshot(driver.get(), "User details");

            // Save the user
            addUserPage.clickSave();

            logger.info("User {} saved successfully.", username);
            test.log(Status.INFO, "Add User Test Case ended");

            // Step 4: Verify that the added user appears in the user list
            test = extent.createTest("Verify User was added Test case" + " " + username);
            test.log(Status.INFO, "Verify User was added Test case" + " " + username + " started");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//td[contains(text(),'" + username + "')]")));
            Assert.assertTrue(userListPage.isUserAdded(username), "User " + username + " was not added successfully.");
            logger.info("User {} was added successfully.", username);
            takeScreenShot.takeSnapshot(driver.get(), "Users added to User List Table");
            test.log(Status.INFO, "Verify User was added Test case" + " " + username + " ended");
        }
    }
}
