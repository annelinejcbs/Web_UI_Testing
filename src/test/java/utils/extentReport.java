package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;

public class extentReport {

    private ExtentHtmlReporter htmlReporter;
    protected ExtentReports extent;
    protected ExtentTest test;

    // This method sets up the report at the beginning of the test suite
    @BeforeSuite
    public void reportSetup() {
        htmlReporter = new ExtentHtmlReporter(System.getProperty(System.getProperty("user.dir") + "/Reports/framework.html"));
        htmlReporter.config().setDocumentTitle("Web_UI_Testing Automation Report");
        htmlReporter.config().setReportName("Web_UI_Testing Automation Report");
        htmlReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Tester", "Anneline");
    }

    // Before each test method, create a new ExtentTest instance
    @BeforeMethod
    public void beforeTestMethod(ITestResult result) {
        // Create a new test for each test method
        test = extent.createTest(result.getMethod().getMethodName());
    }

    // This method is used to log the results of the test method
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "Test Case Failed is : " + result.getName());
            test.log(Status.FAIL, "Test Case Failed is : " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test Case Skipped is : " + result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Test Case Passed is : " + result.getName());
        }
    }

    // This method flushes the report after the test suite is completed
    @AfterSuite
    public void reportTearDown() {
        extent.flush();
    }
}
