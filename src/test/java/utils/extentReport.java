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

import java.io.File;
import java.lang.reflect.Method;

public class extentReport {

    private ExtentHtmlReporter htmlReporter;
    protected ExtentReports extent;
    protected ExtentTest test;

    // This method sets up the report at the beginning of the test suite
    @BeforeSuite
    public void reportSetup() {
        // Get the report path from the environment variable (if available)
        String reportPath = System.getenv("REPORT_PATH");

        // If the environment variable is not set, use a default path
        if (reportPath == null || reportPath.isEmpty()) {
            reportPath = "Reports/framework.html";  // Default relative path
        }

        // Ensure that the Reports folder exists
        File reportDir = new File(reportPath);
        if (!reportDir.getParentFile().exists()) {
            reportDir.getParentFile().mkdirs();  // Create the Reports folder if it doesn't exist
        }

        // Set up the Extent report
        htmlReporter = new ExtentHtmlReporter(reportPath);
        htmlReporter.config().setDocumentTitle("Web_UI_Testing Automation Report");
        htmlReporter.config().setReportName("Web_UI_Testing Automation Report");
        htmlReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Set system information
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("Tester", "Anneline");
    }

    // Create a test before each method
    @BeforeMethod
    public void startTest(Method method) {
        // Start a test before each method
        test = extent.createTest(method.getName());
    }

    // This method captures the test result after each test execution
    @AfterMethod
    public void endTest(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Test Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Test Skipped");
        }
    }

    // After all tests are done, flush the report data
    @AfterSuite
    public void tearDown() {
        extent.flush();  // Write the report to the file
    }
}
