package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.UUID;

public class Browser {

        public static WebDriver createWebDriver(String browser) {
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
                    WebDriverManager.firefoxdriver().clearDriverCache().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--no-sandbox");
                    firefoxOptions.addArguments("--disable-dev-shm-usage");
                    webDriver = new FirefoxDriver(firefoxOptions);
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


