import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Cinchtest
{
    WebDriver driver;

    @Test(priority=0)
    public void testm() throws InterruptedException
    {
        driver=new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get("https://fb.cinch-project.com/");

        WebElement uname = driver.findElement(By.name("FormEmail"));
        uname.sendKeys("kc");

        WebElement pwd = driver.findElement(By.name("password"));
        pwd.sendKeys("Pass@123");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(5000);

        driver.findElement(By.cssSelector("i[class=\"fa fa-calendar-o\"]")).click();

        driver.findElement(By.xpath("//i[@title='New Patient']")).click();

    }

    @Test(priority=1, dataProvider = "patientData",enabled = false)
    public void fillPatientForm(String Lastname,String Firstname,String MI,String address,String city,String zip,String cell, String email,String DOB,String SSN,String TA) throws InterruptedException {
        driver.findElement(By.xpath("//input[@formcontrolname='lastName']")).sendKeys(Lastname);
        driver.findElement(By.xpath("//input[@formcontrolname='firstName']")).sendKeys(Firstname);
        driver.findElement(By.xpath("//input[@formcontrolname='middleInitial']")).sendKeys(MI);
        driver.findElement(By.xpath("//input[@formcontrolname='address1']")).sendKeys(address);
        driver.findElement(By.xpath("//input[@formcontrolname='city']")).sendKeys(city);

        WebElement dropdownElement = driver.findElement(By.id("state-select"));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByVisibleText(" Colorado ");

        driver.findElement(By.xpath("//input[@formcontrolname='zip']")).sendKeys(zip);
        driver.findElement(By.xpath("//input[@formcontrolname='homePhone']")).sendKeys(cell);
        driver.findElement(By.xpath("//input[@formcontrolname='email']")).sendKeys(email);
        driver.findElement(By.xpath("//input[@formcontrolname='dateOfBirth']")).sendKeys(DOB);

        WebElement dropdownElement3 = driver.findElement(By.id("suffix-select"));
        Select dropdown3 = new Select(dropdownElement3);
        dropdown3.selectByVisibleText(" Jr. ");

        WebElement dropdownElement1 = driver.findElement(By.id("genderSelect"));
        Select dropdown1 = new Select(dropdownElement1);
        dropdown1.selectByVisibleText("Female");

        driver.findElement(By.xpath("//input[@formcontrolname='ssn']")).sendKeys(SSN);
        driver.findElement(By.xpath("//input[@formcontrolname='tribalAffiliation']")).sendKeys(TA);

        driver.findElement(By.xpath("//div[@id='vital_card-header']//button[text()='Add']")).click();

        Thread.sleep(2000);
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='w-100 table table-bordered']//tbody//tr"));
        for (WebElement row : rows) {
            String language = row.findElement(By.xpath("./td[3]")).getText();
            if (language.startsWith("Au")) {
                row.click();
                System.out.println(language);
            }
        }
        driver.findElement(By.xpath("//div[contains(@class,'modal-content')]//button[text()='Select' and ancestor::div[.//label[text()='Language']]]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[contains(@class,'modal-content')]//button[text()='Select' and ancestor::div[.//label[text()='Race']]]")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[contains(@class,'modal-content')]//button[text()='Select' and ancestor::div[.//label[text()='Ethnicity']]]")).click();

        driver.findElement(By.xpath("//button[text()='Save' and ancestor::div[.//button[text()=' S.O./G.I. ']]]")).click();

        driver.findElement(By.xpath("//button[@type='submit' and ancestor::form]")).click();
    }

    @Test(priority = 2, dataProvider = "nameData")
    public void searchAndPickPerson(String Firstname, String Lastname) throws InterruptedException {
        String fullName = Lastname + ", " + Firstname;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Locate and type in the search box
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='search']")));
        searchBox.clear();
        searchBox.sendKeys(fullName);
        Thread.sleep(3000);
        searchBox.sendKeys(Keys.ENTER);

        Thread.sleep(3000); // Wait for search results to load

        // Wait for results to be present and get fresh reference
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='patientsListDD']")));
        
        // Find all matching search results
        List<WebElement> results = driver.findElements(By.xpath("//div[@id='patientsListDD']"));

        if (results.size() == 0) {
            System.out.println("No person found with name: " + fullName);
        } else {
            try {
                // Wait for and get fresh reference to the patient name element
                WebElement patientName = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='patientsListDD']//span[contains(@class,'search-patient-name') and contains(@style,'pointer')]")
                ));

                // Log the text for debugging
                System.out.println("Patient name found: " + patientName.getText());

                // Click the patient name
                patientName.click();
                //System.out.println("Clicked on patient name: " + patientName.getText());
            } catch (StaleElementReferenceException e) {
                // If stale, wait and try again with fresh reference
                Thread.sleep(2000);
                WebElement patientName = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='patientsListDD']//span[contains(@class,'search-patient-name') and contains(@style,'pointer')]")
                ));
                patientName.click();
                System.out.println("Clicked on patient name after stale element refresh");
            }
        }
    }

    @Test(priority = 3,enabled = false)
    public void Vitals() throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@id='vital_card-header']/div/div/button[text()='Add']")).click();
        driver.findElement(By.xpath("//input[@formcontrolname='bp1Upper']")).sendKeys("43");
        driver.findElement(By.xpath("//input[@formcontrolname='bp1Lower']")).sendKeys("130");

        WebElement bpdropdown1 = driver.findElement(By.id("bp1measure"));
        Select bpdrpdown1 = new Select(bpdropdown1);
        bpdrpdown1.selectByVisibleText(" manual");

        driver.findElement(By.xpath("//input[@formcontrolname='bp2Upper']")).sendKeys("45");
        driver.findElement(By.xpath("//input[@formcontrolname='bp2Lower']")).sendKeys("130");

        WebElement bpdropdown2 = driver.findElement(By.id("bp2measure"));
        Select bpdrpdown2 = new Select(bpdropdown2);
        bpdrpdown2.selectByVisibleText(" manual");

        driver.findElement(By.xpath("//input[@formcontrolname='temp1']")).sendKeys("38");
        driver.findElement(By.xpath("//input[@formcontrolname='temp2']")).sendKeys("38");
        driver.findElement(By.xpath("//input[@formcontrolname='height']")).sendKeys("138");
        driver.findElement(By.xpath("//input[@formcontrolname='weight']")).sendKeys("39");
        driver.findElement(By.id("heartRate")).sendKeys("79");
        driver.findElement(By.id("respiratory_Rate")).sendKeys("89");
        driver.findElement(By.xpath("//input[@formcontrolname='pulseOximetry']")).sendKeys("77");
        driver.findElement(By.xpath("//input[@formcontrolname='inhaledOxygenConcentration']")).sendKeys("87");
        driver.findElement(By.id("head_circumference")).sendKeys("3");
        driver.findElement(By.id("saveBtn")).click();
    }

    @Test(priority = 4,enabled = false)
    public void Allergies() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@id='allergies']//div[contains(@class,'emrcard-header')]//button[text()='Add']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@class='searchbar']/input[@type='search']")).sendKeys("ser");
        Thread.sleep(2000);

        String medicineName = " Seradex   ";
        String xpath = "//li[.//text()[contains(., '" + medicineName + "')]]//i[@title='Add to patient']";
        WebElement addButton = driver.findElement(By.xpath(xpath));
        addButton.click();

        WebElement allerdropdown = driver.findElement(By.xpath("//select[@formcontrolname='status']"));
        Select alldrpdown = new Select(allerdropdown);
        alldrpdown.selectByVisibleText(" Active ");

        driver.findElement(By.xpath("//input[@formcontrolname='startDate']")).sendKeys("1/13/2021");

        WebElement reactndropdown = driver.findElement(By.xpath("//select[@formcontrolname='reactionId']"));
        Select reactdrpdown = new Select(reactndropdown);
        reactdrpdown.selectByVisibleText(" Cough ");

        WebElement severdropdown = driver.findElement(By.xpath("//select[@formcontrolname='severityId']"));
        Select severdrpdown = new Select(severdropdown);
        severdrpdown.selectByVisibleText(" Severe ");

        WebElement alcatdropdown = driver.findElement(By.xpath("//select[@formcontrolname='allergyCategory']"));
        Select alcatdrpdown = new Select(alcatdropdown);
        alcatdrpdown.selectByVisibleText(" environment ");

        driver.findElement(By.id("textAreaDropdown-comments")).sendKeys("Allergy");

        driver.findElement(By.xpath("//button[text()=' Save ']")).click();
    }

    @Test(priority = 5)
    public void Medication() throws InterruptedException {
        Thread.sleep(4000);
        driver.findElement(By.xpath("//div[@id='medications']//div[contains(@class,'emrcard-header')]//button[text()='Add']")).click();

        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@class='searchbar']/input[@type='search']")).sendKeys("ser");
        Thread.sleep(2000);

        String medicineName = " Seraqua topical liquid (51552136805)  ";
        String xpath = "//li[.//text()[contains(., '" + medicineName + "')]]//i[@title='Add to patient']";
        WebElement addButton = driver.findElement(By.xpath(xpath));
        addButton.click();

        WebElement medstatusdrp = driver.findElement(By.xpath("//select[@formcontrolname='folderId']"));
        Select medsdrpdown = new Select(medstatusdrp);
        medsdrpdown.selectByVisibleText(" Active");

        driver.findElement(By.xpath("//input[@formcontrolname='dosage']")).sendKeys("10");

        driver.findElement(By.xpath("//input[@formcontrolname='unitofDosage']")).sendKeys("mg");

        driver.findElement(By.xpath("//input[@formcontrolname='comment']")).sendKeys("test medication");

        driver.findElement(By.xpath("//button[text()='Save']")).click();

        System.out.println("test");
        System.out.println("test1");
    }

    @DataProvider(name = "patientData")
    public Object[][] getPatientData() throws Exception {
        System.out.println("Inside Data provider");
        Thread.sleep(2000);
        return ExcelUtil.getTestData("C:\\Users\\TTPL-LNVE15-078\\IdeaProjects\\Cinch\\src\\test\\java\\CinchP (16).xlsx", "Sheet1");

    }

    @DataProvider(name = "nameData")
    public Object[][] getNames() {
        return ExcelUtil.getSelectedNameData("C:\\Users\\TTPL-LNVE15-078\\IdeaProjects\\Cinch\\src\\test\\java\\CinchP (16).xlsx", "Sheet1");
    }

}
