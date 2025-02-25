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

public class extentReport {

    private ExtentHtmlReporter htmlReporter;
    protected ExtentReports extent;
    protected ExtentTest test;

    // This method sets up the report at the beginning of the test suite
    @BeforeSuite
    public void reportSetup() {
        // Use user.dir or GITHUB_WORKSPACE depending on the environment
        String reportPath = System.getProperty("user.dir") + "/Reports/framework.html"; // Local setup
        // Or use GITHUB_WORKSPACE for GitHub Actions
        // String reportPath = System.getenv("GITHUB_WORKSPACE") + "/Reports/framework.html";

        // Ensure that the Reports folder exists
        //File reportDir = new File(System.getProperty("user.dir") + "/Reports"); // Local setup
        // Or for GitHub Actions
        File reportDir = new File(System.getenv("GITHUB_WORKSPACE") + "/Reports");

        if (!reportDir.exists()) {
            reportDir.mkdirs();  // Create the Reports folder if it doesn't exist
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
}