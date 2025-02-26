package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Browser {

        public static WebDriver createWebDriver(String browser) throws IOException {
            WebDriver webDriver;

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().clearDriverCache().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("user-data-dir=" + "/tmp/chrome-user-data-" + UUID.randomUUID());
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    webDriver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":

                    // Start Xvfb in the background (to simulate a display in headless mode)
                    //Runtime.getRuntime().exec("Xvfb :90 -screen 0 1280x1024x24 &");

                // Set the DISPLAY variable to the simulated Xvfb display
                    Map<String, String> environment = new HashMap<>();
                    environment.put("DISPLAY", ":90");

                    GeckoDriverService service = new GeckoDriverService.Builder()
                            .usingAnyFreePort()
                            .withEnvironment(environment)
                            .build();

                    WebDriverManager.firefoxdriver().clearDriverCache().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--no-sandbox");
                    firefoxOptions.addArguments("--disable-dev-shm-usage");
                    webDriver = new FirefoxDriver(service, firefoxOptions);



                    break;
                case "edge":
                    WebDriverManager.edgedriver().clearDriverCache().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless");
                    webDriver = new EdgeDriver(edgeOptions);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            return webDriver;
        }


    }


