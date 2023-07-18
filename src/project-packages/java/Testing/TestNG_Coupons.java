package Testing;

import Assists.ExtentManager;
import Assists.Functions;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Coupons
{
    By menu_coupons = By.xpath("//a[@title='קופונים']");
    By black_badge = By.className("black-badge");
    static ExtentReports extent;
    static ExtentTest Test15;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test15 = ExtentManager.createTest("test 15 - Black tag compare", "the purpose of the test to compare the black tags , expected : חדש באתר יהיה הכי הרבה");
    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test
    public void T15_Black_tag_compare_test()
    {
        try
        {
            String New1 = "חדש באתר",Only1 = "בלעדי לאתר", Unique1 = "מחיר מיוחד!";
            WebElement goToCoupons = Functions.element(driver,menu_coupons);
            goToCoupons.click();
            List<WebElement> blackBadgeList = Functions.elementList(driver,black_badge);
            int New = black_badge_count(New1,blackBadgeList);
            int Only = black_badge_count(Only1,blackBadgeList);
            int Unique = black_badge_count(Unique1,blackBadgeList);
            String testDescription = "אנחנו מצפים שהתוצאות יכילו הכי הרבה תגית NEW";
            boolean result = Functions.resultPrintInt(Test15,New,Only,Unique,testDescription);
            Test15.info(New1 + " count: "+New);
            Test15.info(Only1 + " count: "+Only);
            Test15.info(Unique1 + " count: "+Unique);
            Assert.assertTrue(result);
//            if (New > Only)
//            {
//                if (New > Unique)
//                    Test15.pass("test passed - מופיע הכי הרבה " + New1);
//                else
//                    Test15.fail("test failed - מופיע הכי הרבה " + Unique1);
//            }
//            else if (Only > Unique)
//                Test15.fail("test failed - מופיע הכי הרבה " + Only1);
//            else
//                Test15.fail(MarkupHelper.createLabel("test failed - מופיע הכי הרבה " + Unique1, ExtentColor.RED));
//            Test15.info(New1 + " count: "+New);
//            Test15.info(Only1 + " count: "+Only);
//            Test15.info(Unique1 + " count: "+Unique);
        }
        catch (Exception e)
        {
            Functions.ex(Test15, e);
        }
    }
    public static int black_badge_count(String text,List<WebElement> blackBadgeList)
    {
        int count = 0;
        for (int i = 0; i < blackBadgeList.size(); i++)
        {
            String bb_name = blackBadgeList.get(i).getText();
            if (bb_name.equals(text))
                count++;
        }
        return count;
    }
}

