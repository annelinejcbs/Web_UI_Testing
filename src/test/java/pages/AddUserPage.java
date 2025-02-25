package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class AddUserPage {
    private WebDriver driver;

    // Locators for Add User Page
    @FindBy(xpath = "//button[@class='btn btn-link pull-right'][contains(.,'Add User')]")
    WebElement xpath_AddUserBnt;

    @FindBy(xpath = "//input[contains(@name,'FirstName')]")
    WebElement xpath_FirstNameTxtbox;

    @FindBy(xpath = "//input[contains(@name,'LastName')]")
    WebElement xpath_LastNameTxtbox;

    @FindBy(xpath = "//input[contains(@name,'UserName')]")
    WebElement xpath_UserNameTxtbox;

    @FindBy(xpath = "//input[contains(@type,'password')]")
    WebElement xpath_passwordTxtbox;

    @FindBy(xpath = "(//input[contains(@type,'radio')])[1]")
    WebElement xpath_CompanyAAASelect;

    @FindBy(xpath = "(//input[contains(@type,'radio')])[2]")
    WebElement xpath_CompanyBBBSelect;

    @FindBy(xpath = "//select[contains(@name,'RoleId')]")
    WebElement xpath_RoleSelect;

    @FindBy(xpath = "//input[contains(@type,'email')]")
    WebElement xpath_EmailTxtbox;

    @FindBy(xpath = "//input[contains(@name,'Mobilephone')]")
    WebElement xpath_MobilephoneTxtbox;

    @FindBy(xpath = "//button[@ng-click='close()'][contains(.,'Close')]")
    WebElement xpath_CloseBtn;

    @FindBy(xpath = "//button[@ng-click='save(user)'][contains(.,'Save')]")
    WebElement xpath_SaveBtn;

    public AddUserPage(WebDriver driver) {
        this.driver = driver;
    }

    // Method to clear all fields before adding a new user
    public void clearFields() {
        xpath_FirstNameTxtbox.clear();
        xpath_LastNameTxtbox.clear();
        xpath_UserNameTxtbox.clear();
        xpath_passwordTxtbox.clear();
        xpath_EmailTxtbox.clear();
        xpath_MobilephoneTxtbox.clear();
    }

    // Add User Details
    public void EnterFirstName(String firstName) {
        xpath_FirstNameTxtbox.sendKeys(firstName);
    }

    public void EnterLastName(String lastName) {
        xpath_LastNameTxtbox.sendKeys(lastName);
    }

    public void EnterUserName(String username) {
        xpath_UserNameTxtbox.sendKeys(username);
    }

    public void EnterPassword(String password) {
        xpath_passwordTxtbox.sendKeys(password);
    }

    public void SelectCompany(String company) {
        if (company.equals("CompanyAAA")) {
            xpath_CompanyAAASelect.click();
        } else if (company.equals("CompanyBBB")) {
            xpath_CompanyBBBSelect.click();
        }
    }

    public void SelectRole(String role) {
        // Locate the dropdown and select the role
        Select roleDropdownselect = new Select(xpath_RoleSelect);

        // Select the role by visible text (no need to loop)
        roleDropdownselect.selectByVisibleText(role);
    }

    public void EnterEmail(String email) {
        xpath_EmailTxtbox.sendKeys(email);
    }

    public void EnterMobileNumber(String cellPhone) {
        xpath_MobilephoneTxtbox.sendKeys(cellPhone);
    }

    // Click the "Save" button
    public void clickSave() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(xpath_SaveBtn));

        // Scroll the Save button into view and click it
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", xpath_SaveBtn);

        xpath_SaveBtn.click();
    }

    // Click the "Close" button
    public void clickClose() {
        xpath_CloseBtn.click();
    }
}

