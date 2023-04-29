package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Favorite_and_No_inventory
{
    By add_favorite_btn = By.xpath("//*[@id='results-boxes']/a[1]//button[@class='add-favorites']");
    By pop_up_error_text = By.xpath("//div[@id='generic-popup-wrap']//*[@id=\"genericPopUpLabel\"]");
    By close_pop_up_btn = By.xpath("//*[@id='generic-popup']//button[@class='close']");
    By add_to_cart_btn_list = By.xpath("//*[@id='results-boxes']//div[@class='add-to-basket']");
    By cart_number = By.id("cart-number");
    static ExtentReports extent;
    static ExtentTest Test26;
    static ExtentTest Test27;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass()
    {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test26 = ExtentManager.createTest("test 26 - Add to favorite", "the purpose of the test to try add item to favorite list");
        Test27 = ExtentManager.createTest("test 27 - Add item to cart ", "the purpose of the test to check item out of stock add to cart btn");
    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test(priority = 1)
    public void T26_Add_to_favorite_test()
    {
        try
        {
            Functions.search_item("נביעות");
            WebElement addToFavorite = Functions.element(driver,add_favorite_btn);
            addToFavorite.click();
            Functions.waitByPresenceOfElementLoc(pop_up_error_text);
            String actual_result = Functions.element(driver,pop_up_error_text).getText();
            String expected_result = "הוספה למועדפים נכשלה";
            WebElement closePopUp = Functions.element(driver,close_pop_up_btn);
            closePopUp.click();
            if(actual_result.equals(expected_result))
                Test26.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test26.fail(MarkupHelper.createLabel("test failed",ExtentColor.RED));
            Test26.info("actual result is: "+actual_result);
            Test26.info("expected result is: "+expected_result);
            Test26.info("we expecting for an error because we cant add without user login to system");
        }
        catch (Exception e)
        {
            Test26.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test(priority = 2,dependsOnMethods = "T26_Add_to_favorite_test")
    public void T27_Add_item_to_cart()
    {
        try
        {
            List<WebElement> list = Functions.elementList(driver,add_to_cart_btn_list);
            int count_true = 0,count_false = 0;
            String cart_num = Functions.element(driver,cart_number).getAttribute("innerHTML");
            for (int i = 0; i < list.size(); i++)
            {
                String item_add_to_cart_btn = list.get(i).getText();
                if(item_add_to_cart_btn.equals("אזל במלאי זמנית"))
                    list.get(i).click();
            }
            String after_click_cart_num = Functions.element(driver,cart_number).getAttribute("innerHTML");
            if (cart_num.equals(after_click_cart_num))
                Test27.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
                Test27.fail(MarkupHelper.createLabel("test failed",ExtentColor.RED));
            Test27.info("cart num before click is: "+cart_num);
            Test27.info("cart num after click is: "+after_click_cart_num);
        }
        catch (Exception e)
        {
            Test27.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
}
