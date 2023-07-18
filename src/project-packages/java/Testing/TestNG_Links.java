package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Links
{
    static By first_suggest = By.xpath("//*[@class='aa-dataset-products']//div[@class='aa-suggestion']");
    static By header = By.tagName("h1");
    By items_list = By.xpath("//*[@id='results-boxes']//h4");
    By add_to_cart_btn = By.xpath("//*[@id=\"results-boxes\"]//*[contains(@class,'add-to-basket')]");
    By category_menu = By.xpath("//*[@id='navigation-menu']//a[@title='קטגוריות']");
    By category_submenu1 = By.xpath("//*[@class='submenu']//a[@title='מזון ומשקאות']");
    By category_submenu2 = By.xpath("//*[@class='submenu']//a[@title='משקאות ושתיה חמה']");
    By category_submenu3 = By.xpath("//*[@class='submenu']//a[@title='תה']");
    By category_submenu4 = By.xpath("//*[@class='submenu']//a[@title='תה']");
    By category_green_tea_item = By.xpath("//a[@title='תה ירוק']");
    By result_search_elements = By.className("item-box-link");

    static ExtentReports extent;
    static ExtentTest Test16;
    static ExtentTest Test17;
    static ExtentTest Test18;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test16 = ExtentManager.createTest("test 16 - Navigation to item page when item out of stock", "the purpose of the test to try navigate to item page via search quick access when item out of stock");
        Test17 = ExtentManager.createTest("test 17 - Navigation to item page", "the purpose of the test to navigate to item page via search quick access");
        Test18 = ExtentManager.createTest("test 18 - Search item via category submenu", "the purpose of the test to search item via category submenus - search content need to pass 10");

    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test
    public void T16_Navigation_to_item_page_when_item_out_of_stock_test()
    {
        try
        {
            String header_text = "";
            String item_title = "";
            String no_inventory = "";
            String My_search = "שוקולד מריר 70%";
            Functions.search_item("toni's");
            List<WebElement> itemTitleList = Functions.elementList(driver,items_list);
            List<WebElement> itemAddToCartBtnList = Functions.elementList(driver,add_to_cart_btn);
            Functions.waitByPresenceOfElementLoc(items_list);
            for (int i = 0; i < itemTitleList.size(); i++)
            {
             item_title = itemTitleList.get(i).getText().trim();
            if (item_title.contains(My_search))
                 no_inventory = itemAddToCartBtnList.get(i).getText().trim();
            }
            boolean result = Functions.no_inventory_logo_check(no_inventory);
            if (result == false)
            {
                header_text = go_to_item_page_and_get_item_title(My_search);
                if(header_text.contains(My_search))
                {
                    Test16.pass(MarkupHelper.createLabel("test passed - item page found",ExtentColor.GREEN));
                    Test16.info("header text: "+header_text);
                }
                else
                {
                    Test16.fail(MarkupHelper.createLabel("test failed - wrong page found",ExtentColor.RED));
                    Test16.info("header text: "+header_text);
                }
            }
            else
            {
                header_text = go_to_item_page_and_get_item_title(My_search);
                if(header_text.contains(My_search))
                {
                    Test16.pass(MarkupHelper.createLabel("test passed - item page found",ExtentColor.GREEN));
                    Test16.info("header text: "+header_text);
                }
                else
                {
                    Test16.fail(MarkupHelper.createLabel("test failed - wrong page found",ExtentColor.RED));
                    Test16.info("header text: "+header_text);
                }
            }
        }
        catch (Exception e)
        {
            Functions.ex(Test16, e);
        }
    }
    @Test
    public void T17_Navigation_to_item_page_test()
    {
        try
        {
            driver.get("https://shop.super-pharm.co.il/");
            String expected_result = "3OL קמומילו אורל- ג'ל לטיפול בפה אצל תינוקות";
            String actual_result = go_to_item_page_and_get_item_title("קמומילו ג'ל");
            String testDescription = "אנחנו מצפים לניווט נכון לדף מוצר ע''י השוואה של כותרת המוצר לפני כניסה לדף ולאחר כניסה";
            boolean result = Functions.resultPrint(Test17,expected_result,actual_result,testDescription);
            Assert.assertTrue(result);
//            if (actual_result.equals(expected_result))
//                Test17.pass(MarkupHelper.createLabel("test passed - item page found", ExtentColor.GREEN));
//            else {
//                Test17.fail("test failed - item page not found");
//            }
//            Test17.info("actual item title is: " + actual_result);
//            Test17.info("expected item title is: " + expected_result);
        }
        catch (Exception e)
        {
            Functions.ex(Test17, e);
        }
    }
    @Test
    public void T18_Search_item_via_category_submenu()
    {
        try
        {
            driver.get("https://shop.super-pharm.co.il/");
            Actions actions = new Actions(driver);
            Functions.element(driver,category_menu).click();
            actions.moveToElement(Functions.element(driver,category_submenu1)).perform();
            actions.moveToElement(Functions.element(driver,category_submenu2)).perform();
            actions.moveToElement(Functions.element(driver,category_submenu3)).perform();
            actions.moveToElement(Functions.element(driver,category_submenu4)).perform();
            Functions.element(driver,category_green_tea_item).click();
            List<WebElement> list = Functions.elementList(driver,result_search_elements);
            int actual_result = list.size();
            int expected_result = 10;
            String testDescription = "אנחנו מצפים שתוצאות החיפוש יציגו ערך ערך גבוה מהמצופה";
            boolean result = Functions.resultPrintInt(Test18,actual_result,expected_result,0,testDescription);
            Test18.info("Actual Result: "+actual_result);
            Test18.info("Expected Result: "+expected_result);
            Assert.assertTrue(result);
//            if (actual_result > 10)
//                Test18.pass(MarkupHelper.createLabel("test passed - number of elements shown in the result are higher then 10", ExtentColor.GREEN));
//            else
//                Test18.fail("test failed - number of elements shown in the result equal or lower then 10");
//            Test18.info("actual result is: "+actual_result);
//            Test18.info("expected result is: "+expected_result);
        }
        catch (Exception e)
        {
            Functions.ex(Test18, e);
        }

    }
    public static String go_to_item_page_and_get_item_title(String item_name)
    {
        Functions.element(driver,Functions.search_input).sendKeys(Keys.ESCAPE);
        Functions.element(driver,Functions.search_input).clear();
        Functions.element(driver,Functions.search_input).sendKeys(item_name);
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(first_suggest)).click();
        wait.until(ExpectedConditions.visibilityOf(Functions.element(driver,header)));
        String header_text = Functions.element(driver,header).getText().trim();
        return header_text;
    }

}
