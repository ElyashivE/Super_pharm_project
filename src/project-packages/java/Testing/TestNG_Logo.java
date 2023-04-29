package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Logo
{
    By logo_btn = By.id("logo-top-img");
    By logo_size = By.xpath("//*[@id=\"logo-top-img\"]/div/a/img");
    static ExtentReports extent;
    static ExtentTest Test21;
    static ExtentTest Test22;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test21 = ExtentManager.createTest("test 21 - Logo home page", "the purpose of the test to check logo btn navigate to home page");
        Test22 = ExtentManager.createTest("test 22 - Logo size", "the purpose of the test to check the the size is affected by css values and not the default values (JSE)");
    }

    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test
    public void T21_Logo_home_page_test()
    {
        try
        {
            String expected_url = driver.getCurrentUrl();
            Functions.goToCartAndCloseAd();
            if (driver.getCurrentUrl().equals("https://shop.super-pharm.co.il/cart"))
            {
                Test21.info("navigate to cart pass ok");
                Functions.element(driver,logo_btn).click();
                String actual_url = driver.getCurrentUrl();
                if (actual_url.equals(expected_url))
                {
                    Test21.info("navigate back to home page pass ok");
                    Test21.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
                }
            }
            else
                Test21.fail("test failed");
        }
        catch (Exception e)
        {
            Test21.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test
    public void T22_Logo_size_test()
    {
        try
        {
            WebElement logo = Functions.element(driver,logo_btn);
            WebElement size = Functions.element(driver,logo_size);
//        int actual_w = 1370;
//        int actual_h = 467;
            int actual_w = logo.getRect().getWidth();
            int actual_h = logo.getRect().getHeight();
            String CSS_width = logo.getCssValue("width");
            String CSS_height = logo.getCssValue("height");
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            String JSE_width = (String) jsExecutor.executeScript("return arguments[0].getAttribute('width');", size);
            String JSE_height = (String) jsExecutor.executeScript("return arguments[0].getAttribute('height');", size);
            int css_width = string_to_int(CSS_width);
            int css_height = string_to_int(CSS_height);
            int jse_width = string_to_int(JSE_width);
            int jse_height = string_to_int(JSE_height);
            if ((actual_w == css_width) && (actual_h == css_height))
                Test22.pass(MarkupHelper.createLabel("test passed - width & height affected by css",ExtentColor.GREEN));
            else if((actual_w == jse_width) && (actual_h == jse_height))
                Test22.fail(MarkupHelper.createLabel("test failed - width & height should affected by css",ExtentColor.RED));
            Test22.info("actual width: "+actual_w+" | actual height: "+actual_h);
            Test22.info("width CSS value: "+css_width+" | height CSS value: "+css_height);
            Test22.info("width JSE value: "+jse_width+" | height JSE value: "+jse_height);
        }
        catch (Exception e)
        {
            Test22.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    public static int string_to_int(String input)
    {
        int INPUT = Integer.parseInt(input.replace("px",""));
        return INPUT;
    }
}
