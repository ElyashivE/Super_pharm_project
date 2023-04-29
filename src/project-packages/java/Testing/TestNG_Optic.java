package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Optic
{
    static By menu_optic = By.xpath("//*[@id='navigation-menu']//a[@title='OPTIC']");
    By sunglasses = By.xpath("//*[@id='comp_10132010-menu']//a[@title='משקפי שמש']");
    static By filter_list = By.xpath("//*[@id='filters']//ul[@class='filter-items']//*[@class='title']");
    static By slider_bar_right = By.xpath("//span[@class='irs-slider to']");
    By item_list = By.xpath("//*[@id='results-boxes']//div[@class='price-row']/span[@class='shekels money-sign']");
    By contact_lenses = By.xpath("//*[@id='comp_10132010-menu']//a[@title='עדשות מגע ותמיסות']");
    By image_less = By.className("fallback");
    static ExtentReports extent;
    static ExtentTest Test13;
    static ExtentTest Test14;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test13 = ExtentManager.createTest("test 13 - Verify proper filtering", "the purpose of the test to verify proper elements shown after filter results");
        Test14 = ExtentManager.createTest("test 14 - Verify that all elements got picture", "the purpose of the test to find element without picture");
    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test
    public void T13_verify_proper_filtering()
    {
        try
        {
            navigate_to_optic_menu_and_submenu(sunglasses);
            set_filtering();
            List<WebElement> itemlist = Functions.elementList(driver,item_list);
            int actual_result = compare_list_items_price_in_range(itemlist);
            int expected_result = itemlist.size();
            if(actual_result == expected_result)
                Test13.pass(MarkupHelper.createLabel("test passed - all the elements after filter are in the price range allowed",ExtentColor.GREEN));
            else
                Test13.fail("test failed - not all elements after filter are in the price range allowed");
            Test13.info("actual result: "+actual_result);
            Test13.info("expected result: "+expected_result);
        }
        catch (Exception e)
        {
            Test13.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test
    public void T14_verify_that_all_elements_got_picture()
    {
        try
        {
            navigate_to_optic_menu_and_submenu(contact_lenses);
            List<WebElement> imageLessList = Functions.elementList(driver,image_less);
            List<String> imageLessTitleList = new ArrayList<>();
            for (int i = 0; i < imageLessList.size(); i++)
            {
                //לוקח את השם של הפריט מהערך alt
                imageLessTitleList.add((imageLessList.get(i).getAttribute("alt")));
            }
            int actual_result = imageLessList.size();
            //הכוונה ל 0 זה שלכל האלמנטים יש תמונה
            int expected_result = 0;
            if(actual_result == expected_result)
                Test14.pass("test passed - all the elements got picture");
            else
            {
                Test14.fail(MarkupHelper.createLabel("test failed - see below elements with no picture", ExtentColor.RED));
                Test14.fail(MarkupHelper.createUnorderedList(imageLessTitleList));
            }
            Test14.info("actual result: "+actual_result);
            Test14.info("expected result: "+expected_result);
        }
        catch (Exception e)
        {
            Test14.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    public static void navigate_to_optic_menu_and_submenu(By by)
    {
        Functions.element(driver,menu_optic).click();
        Functions.element(driver,by).click();
    }
    public static void set_filtering()
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        List<WebElement> filterlist = Functions.elementList(driver,filter_list);
        for (int i = 0; i < filterlist.size(); i++)
        {
            if (filterlist.get(i).getText().equals("מוצרים במבצע") || filterlist.get(i).getText().equals("משקפי שמש יוניסקס"))
            {
                filterlist.get(i).click();
                wait.until(ExpectedConditions.stalenessOf(filterlist.get(i)));
            }
            filterlist = Functions.elementList(driver,filter_list);
        }
        Actions action = new Actions(driver);
        WebElement sb_right = Functions.element(driver,slider_bar_right);
        action.clickAndHold(sb_right).moveByOffset(-165,0).release(sb_right).perform();
        wait.until(ExpectedConditions.stalenessOf(sb_right));
    }
    public static int compare_list_items_price_in_range(List<WebElement> itemlist)
    {
        int actual_result =0;
        for (int i = 0; i < itemlist.size(); i++)
        {
            int price = Integer.parseInt(itemlist.get(i).getText());
            if (price>=299 && price<=416)
                actual_result++;
        }
        return actual_result;
    }
}
