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

public class UserListPage {
    private WebDriver driver;

    // Locators for User List Page
    @FindBy(xpath = "//table[contains(@class, 'smart-table')]//tbody//tr")
    WebElement xpath_tableName;

    @FindBy(xpath = "//button[@class='btn btn-link pull-right'][contains(.,'Add User')]")
    WebElement xpath_AddUserBnt;

    public UserListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize elements annotated with @FindBy
    }

    public boolean isUserListTableVisible() {
        try {
            WebElement userListTable = xpath_tableName;
            return userListTable.isDisplayed();  // Returns true if the table is visible
        } catch (Exception e) {
            return false;  // Returns false if the table is not found or not visible
        }
    }



    // Click on "Add User" button
    public AddUserPage clickAddUser() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(xpath_AddUserBnt)); // Wait for the button to be clickable
        xpath_AddUserBnt.click();
        return new AddUserPage(driver);
    }

    // Method to extract all usernames from the table
    public List<String> getUsernamesFromTable() {
        // Wait until the table rows are visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[contains(@class, 'smart-table')]//tbody//tr")));

        // Create a list to store usernames
        List<String> usernames = new ArrayList<>();

        // Locate all rows in the table body (excluding header)
        List<WebElement> rows = driver.findElements(By.xpath("//table[contains(@class, 'smart-table')]//tbody//tr"));

        // Loop through each row and extract the username (assuming username is in the first column)
        for (WebElement row : rows) {
            try {
                // Get the username from the first column (assuming username is in the first column)
                WebElement usernameCell = row.findElement(By.xpath("./td[3]"));  // Adjust index if needed
                String username = usernameCell.getText().trim();

                // Skip empty rows (if any)
                if (!username.isEmpty()) {
                    usernames.add(username);  // Add username to the list
                }
            } catch (Exception e) {
                // Handle the case where the row does not contain the expected structure
                System.out.println("Error extracting username: " + e.getMessage());
            }

        }

        return usernames;
    }


    // Method to verify if a specific user is added to the table
    public boolean isUserAdded(String username) {
        // Get all the usernames from the table
        List<String> usernames = getUsernamesFromTable();

        // Check if the username exists in the list
        return usernames.contains(username);
    }

}
