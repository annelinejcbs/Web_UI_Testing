package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserListPage {
    private WebDriver driver;
    private static final Logger logger = Logger.getLogger(UserListPage.class.getName()); // Logger for error handling

    // Locators for User List Page
    @FindBy(xpath = "//table[contains(@class, 'smart-table')]//tbody//tr")
    WebElement xpath_tableName;

    @FindBy(xpath = "//button[@class='btn btn-link pull-right'][contains(.,'Add User')]")
    WebElement xpath_AddUserBnt;

    public UserListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize elements annotated with @FindBy
    }

    // Helper method for waiting for visibility of elements
    private void waitForElementToBeVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Method to check if the user list table is visible
    public boolean isUserListTableVisible() {
        try {
            waitForElementToBeVisible(xpath_tableName); // Wait for table visibility
            return xpath_tableName.isDisplayed();
        } catch (Exception e) {
            logger.warning("User list table not visible: " + e.getMessage());
            return false;
        }
    }

    // Click on "Add User" button
    public AddUserPage clickAddUser() {
        waitForElementToBeVisible(xpath_AddUserBnt); // Wait for button to be visible
        xpath_AddUserBnt.click();
        return new AddUserPage(driver);
    }

    // Method to extract all usernames from the table
    public List<String> getUsernamesFromTable() {
        waitForElementToBeVisible(xpath_tableName); // Wait for table rows to be visible

        List<String> usernames = new ArrayList<>();
        List<WebElement> rows = driver.findElements(By.xpath("//table[contains(@class, 'smart-table')]//tbody//tr"));

        // Loop through each row and extract the username (assuming it's in the third column)
        for (WebElement row : rows) {
            try {
                WebElement usernameCell = row.findElement(By.xpath("./td[3]"));  // Adjust index if needed
                String username = usernameCell.getText().trim();

                // Skip empty usernames and add non-empty ones
                if (!username.isEmpty()) {
                    usernames.add(username);
                }
            } catch (Exception e) {
                logger.warning("Error extracting username from row: " + e.getMessage());
            }
        }

        return usernames;
    }

    // Method to verify if a specific user is added to the table
    public boolean isUserAdded(String username) {
        List<String> usernames = getUsernamesFromTable();
        return usernames.contains(username);
    }
}
