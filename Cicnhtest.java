package com.cinch.test;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.cinch.test.ExcelUtil;

public class Cicnhtest {
    private WebDriver driver;
    private WebDriverWait wait;

    public void initializeDriver() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    public List<String> getDynamicSearchResults(String searchQuery, By searchResultsLocator) {
        List<String> results = new ArrayList<>();
        
        // Wait for search results to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(searchResultsLocator));
        
        // Get all search result elements
        List<WebElement> searchElements = driver.findElements(searchResultsLocator);
        
        // Extract text from each result
        for (WebElement element : searchElements) {
            try {
                String resultText = element.getText().trim();
                if (!resultText.isEmpty()) {
                    results.add(resultText);
                }
            } catch (Exception e) {
                // Skip elements that are not visible or have stale references
                continue;
            }
        }
        
        return results;
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
} 