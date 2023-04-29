package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class TestNG_RegisterAndLogin
{
    public static WebDriver driver;
    By registration_link = By.xpath("//*[@id='trigger-signin-overlay']/a[1]");
    By registration_fname = By.id("registration-name");
    By registration_lname = By.id("registration-last-name");
    By registration_mail = By.id("registration-email");
    By registration_error_text = By.xpath("//ul[@class='list-unstyled']/li");
    By login_link = By.xpath("//*[@id='trigger-signin-overlay']/a[2]");
    By login_mail = By.id("j_username");
    By login_password = By.id("j_password");
    By err_list = By.className("list-unstyled");
    static WebElement registrationLink;
    static WebElement rFirstName;
    static WebElement rLastName;
    static WebElement rMail;
    static ExtentReports extent;
    static ExtentTest Test01;
    static ExtentTest Test02;
    static ExtentTest Test03;
    static ExtentTest Test04;

//----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test01 = ExtentManager.createTest("test 1 - Registration first name", "the purpose of the test to enter invalid (number) input to first-name field and expected to error msg");
        Test02 = ExtentManager.createTest("test 2 - Registration last name", "the purpose of the test to enter invalid input (number) to last-name field and expected to error msg");
        Test03 = ExtentManager.createTest("test 3 - Registration Email", "the purpose of the test to enter invalid input to mail (double @@) field and expected to error msg");
        Test04 = ExtentManager.createTest("test 4 - Login Email and password", "the purpose of the test to leave the fields mail and password empty and expected error msg");

    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }
//----------------------------------------------------------

    @Test
    public void T01_Registration_first_name_test()
    {
        try
        {
            Functions.waitByClick(registration_link);
            registrationLink = Functions.element(driver,registration_link);
            registrationLink.click();
            rFirstName = Functions.element(driver,registration_fname);
            rFirstName.sendKeys("123");
            String actualText = getErrorMsg();
            String expectedText = "נא להזין שם תקין";
            if (actualText.equals(expectedText))
                Test01.pass(MarkupHelper.createLabel("test passed - we got error as expected", ExtentColor.GREEN));
            else
                Test01.fail("test failed - we didn't get error as expected");
            Test01.info("actual error: "+actualText);
            Test01.info("expected error: "+expectedText);
            rFirstName.clear();
            rFirstName.sendKeys("a");
        }
        catch (Exception e)
        {
            Test01.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test
    public void T02_Registration_last_name_test()
    {
        try
        {
            rLastName = Functions.element(driver,registration_lname);
            rLastName.sendKeys("123");
            String actualText = getErrorMsg();
            String expectedText = "נא להזין שם תקין";
            if (actualText.equals(expectedText))
                Test02.pass(MarkupHelper.createLabel("test passed - we got error as expected", ExtentColor.GREEN));
            else
                Test02.fail("test failed - we didn't get error as expected");
            Test02.info("actual error: "+actualText);
            Test02.info("expected error: "+expectedText);
            rLastName.clear();
            rLastName.sendKeys("a");
        }
        catch (Exception e)
        {
            Test02.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test
    public void T03_Registration_Email_test()
    {
        try
        {
            rMail = Functions.element(driver, registration_mail);
            rMail.sendKeys("e@@e.gmail.com");
            String actualText = getErrorMsg();
            String expectedText = "נא להזין כתובת דוא\"ל תקינה";
            if (actualText.equals(expectedText))
                Test03.pass(MarkupHelper.createLabel("test passed - we got error as expected", ExtentColor.GREEN));
            else
                Test03.fail("test failed - we didn't get error as expected");
            Test03.info("actual error: "+actualText);
            Test03.info("expected error: "+expectedText);
            driver.navigate().refresh();
        }
        catch (Exception e)
        {
            Test03.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test
    public void T04_Login_Email_and_password_test()
    {
        try
        {
            Functions.waitByClick(registration_link);
            Functions.element(driver, login_link).click();
            Functions.element(driver, login_mail).click();
            Functions.element(driver, login_password).click();
            Functions.element(driver, login_mail).click();
            Functions.waitByVisibility(login_password);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(login_password));
            List<WebElement> list = Functions.elementList(driver,err_list);
            String expectedText = "נא למלא שדה זה";
            for (int i = 0; i < list.size(); i++)
            {
                String actualText = list.get(i).getText().trim();
                if(actualText.equals(expectedText))
                    Test04.pass(MarkupHelper.createLabel("test passed - we got error as expected", ExtentColor.GREEN));
                else
                    Test04.fail("test failed - we didn't get error as expected");
                Test04.info("actual error: "+actualText);
                Test04.info("expected error: "+expectedText);
            }
        }
        catch (Exception e)
        {
            Test04.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }

    public String getErrorMsg()
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(registration_error_text));
            String Text = Functions.element(driver,registration_error_text).getText().trim();
            return Text;
        }
        catch (Exception e)
        {
            return "error!!! unable to catch element";
        }
    }
}

