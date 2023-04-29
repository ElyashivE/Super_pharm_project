package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Join_lifestyle
{
    static By join = By.xpath("//*[@title='הצטרפות למועדון']");
    static By click_and_join = By.xpath("//*[@title='בואו נתחיל']");
    static By ID_number = By.id("customerID");
    static By late_visibility_ele = By.xpath("//div[@class='container-flex']/div[contains(.,'מאשרים ומסיימים')]");
    By phone_number = By.id("cellPhoneNumber");
    By phone_error = By.id("error-cellPhoneNumber-minlength");
    By first_name = By.id("hebrewFirstName");
    By first_name_error = By.id("error-hebrewFirstName");
    static ExtentReports extent;
    static ExtentTest Test28;
    static ExtentTest Test29;
    static ExtentTest Test30;


//----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass()
    {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/lifestyle");
        extent = ExtentManager.GetExtent();
        Test28 = ExtentManager.createTest("test 28 - ID field", "the purpose of the test to check the ID field that get only numbers");
        Test29 = ExtentManager.createTest("test 29 - Phone field", "the purpose of the test to check the Phone field get the allowed amount of numbers");
        Test30 = ExtentManager.createTest("test 30 - First name field", "the purpose of the test to check the First name field that get only hebrew letters");
    }

    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

//----------------------------------------------------------
    @Test(priority = 1)
    public void T28_ID_field()
    {
        try
        {
            getToSignUpForm();
            String myInput = "abcde";
            String expectedResult = "";
            String actualResult = sendKeysAndGetValueBack(myInput,ID_number);
            if(actualResult.equals(expectedResult))
                Test28.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test28.fail("test failed");
            Test28.info("input string: "+myInput);
            Test28.info("actual result: "+actualResult);
            Test28.info("expected result: "+expectedResult);
            myInput = "123a";
            expectedResult = "123";
            actualResult = sendKeysAndGetValueBack(myInput,ID_number);
            if(actualResult.equals(expectedResult))
                Test28.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test28.fail("test failed");
            Test28.info("input string: "+myInput);
            Test28.info("actual result: "+actualResult);
            Test28.info("expected result: "+expectedResult);
        }
        catch (Exception e)
        {
            Test28.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test(priority = 2,dependsOnMethods = "T28_ID_field")
    public void T29_Phone_field()
    {
        try
        {
            String myInput = "123456";
            String expectedResult = "הטלפון שהוזן אינו תקין";
            String actualResult = sendKeysAndGetErrorBack(myInput,phone_number,phone_error);
            if(actualResult.equals(expectedResult))
                Test29.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test29.fail("test failed");
            Test29.info("input string: "+myInput);
            Test29.info("actual result: "+actualResult);
            Test29.info("expected result: "+expectedResult);
            myInput = "1234567";
            expectedResult = "";
            actualResult = sendKeysAndGetErrorBack(myInput,phone_number,phone_error);
            if(actualResult.equals(expectedResult))
                Test29.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test29.fail("test failed");
            Test29.info("input string: "+myInput);
            Test29.info("actual result: "+actualResult);
            Test29.info("expected result: "+expectedResult);
        }
        catch (Exception e)
        {
            Test29.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test(priority = 3,dependsOnMethods = "T28_ID_field")
    public void T30_First_name_field()
    {
        try
        {
            String myInput = "123456";
            String expectedResult = "רק אותיות בעברית, בבקשה";
            String actualResult = sendKeysAndGetErrorBack(myInput,first_name,first_name_error);
            if(actualResult.equals(expectedResult))
                Test30.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test30.fail("test failed");
            Test30.info("input string: "+myInput);
            Test30.info("actual result: "+actualResult);
            Test30.info("expected result: "+expectedResult);
            myInput = "abcde";
            actualResult = sendKeysAndGetErrorBack(myInput,first_name,first_name_error);
            if(actualResult.equals(expectedResult))
                Test30.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test30.fail("test failed");
            Test30.info("input string: "+myInput);
            Test30.info("actual result: "+actualResult);
            Test30.info("expected result: "+expectedResult);
            myInput = "ישראל";
            expectedResult = "";
            actualResult = sendKeysAndGetErrorBack(myInput,first_name,first_name_error);
            if(actualResult.equals(expectedResult))
                Test30.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test30.fail("test failed");
            Test30.info("input string: "+myInput);
            Test30.info("actual result: "+actualResult);
            Test30.info("expected result: "+expectedResult);
        }
        catch (Exception e)
        {
            Test30.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    public static void getToSignUpForm()
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Functions.element(driver,join).click();
        Functions.element(driver,click_and_join).click();
        List<String> tabs2 = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(late_visibility_ele));
    }
    public static String sendKeysAndGetValueBack(String myInput,By by)
    {
        Functions.element(driver,by).clear();
        Functions.element(driver,by).sendKeys(myInput);
        return Functions.element(driver,by).getAttribute("value");
    }
    public static String sendKeysAndGetErrorBack(String myInput,By inputField,By errorText)
    {
        Functions.element(driver,inputField).clear();
        Functions.element(driver,inputField).sendKeys(myInput);
        Functions.element(driver,inputField).sendKeys(Keys.ENTER);
        String actualResult;
        try
        {
             actualResult = Functions.element(driver,errorText).getText();
        }
        catch (Exception ElementNotInteractableException)
        {
            actualResult ="";
        }
        return actualResult;
    }
}