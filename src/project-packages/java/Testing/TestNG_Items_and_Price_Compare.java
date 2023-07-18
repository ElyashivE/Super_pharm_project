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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Items_and_Price_Compare
{
    static By title_list = By.xpath("//*[@id='results-boxes']//h4");
    static By agura_list = By.xpath("//*[@id='results-boxes']//span[@class='cents']");
    static By shekel_list = By.xpath("//*[@id='results-boxes']//span[@class='shekels money-sign']");
    static String splitter;
    static By filter_list = By.xpath("//ul[@class='filter-items']//div[@class='title']");
    static By expend_category_btn = By.xpath("//div[@data-id='category']//span[@class='more-text']");
    static By search_result_title_list = By.xpath("//*[@id='results-boxes']//h4");
    static ExtentReports extent;
    static ExtentTest Test19;
    static ExtentTest Test20;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test19 = ExtentManager.createTest("test 19 - Find the cheapest item", "the purpose of the test to find the cheapest price between 5 items and see if its cheaper then 28");
        Test20 = ExtentManager.createTest("test 20 - Search and Filter match to result box", "the purpose of the test to search and filter and verify the result box show the exact content we searched");
    }

    @AfterClass(alwaysRun = true)
    public static void AfterClass() {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test
    public void T19_find_the_cheapest_item_test()
    {
        try
        {
            splitter = "~~";
            Functions.search_item("משחת שיניים מלבינה");
            String best_item = findCheapPrice();
            String[] best = best_item.split(splitter);
            String best_title = best[0];
            String best_price = best[1];
            Double actual_price = Double.parseDouble(best_price);
            //מבוצעת המרה מ Double ל int ע"י עיגול המספר למס' int הקרוב ביותר בכדי שנוכל להשתמש בפונקציה קיימת ולא נצטרך ליצור חדשה
            int actualPriceInt = (int)Math.round(actual_price);
            int expected_price = 28;
            String testDescription = "אנחנו מצפים למצוא פריט משחת שיניים זול יותר מ 28 שקלים";
            boolean result = Functions.resultPrintInt(Test19,expected_price,actualPriceInt,0,testDescription);
            Test19.info("actual price is: " + actual_price);
            Test19.info("expected price is : " + expected_price);
            Test19.info("the title name is: " + best_title);
            Assert.assertTrue(result);
//            if (actual_price < expected_price)
//                Test19.pass(MarkupHelper.createLabel("test passed - the cheapest price is lower ",ExtentColor.GREEN));
//            else
//                Test19.fail("test failed - the cheapest price is higher");
        }
        catch (Exception e)
        {
            Functions.ex(Test19, e);
        }
    }
    @Test
    public void T20_Search_and_Filter_match_to_result_box_test()
    {
        try
        {
            Functions.search_item("לגבר עור");
            filterResultBy("ארנקים ופאוצ׳ים לגברים");
            List<WebElement> searchResultTitleList = Functions.elementList(driver,search_result_title_list);
            int count_titles = 0;
            List<String> noMatchList = new ArrayList<>();
            for (int i = 0; i < searchResultTitleList.size(); i++)
            {
                String item_title = searchResultTitleList.get(i).getText();
                if (item_title.contains("עור") && item_title.contains("ארנק"))
                    count_titles++;
                else
                    noMatchList.add(item_title);
            }
            String expectedResult = String.valueOf(searchResultTitleList.size());
            String actualResult = String.valueOf(count_titles);
            String testDescription = "אנחנו מצפים שכל הפריטים אחרי סינון לפי קטגוריה יכילו את המילים עור ו- ארנק";
            boolean result = Functions.resultPrint(Test20,expectedResult,actualResult,testDescription);
//            if (count_titles == searchResultTitleList.size())
//                Test20.pass(MarkupHelper.createLabel("test passed - all the result shows the exact content we searched",ExtentColor.GREEN));
//            else
//            {
//                Test20.fail(MarkupHelper.createLabel("test failed - the list below show the result with wrong content:",ExtentColor.RED));
////                Test20.fail(MarkupHelper.createUnorderedList(noMatchList));
//            }
            if (noMatchList.size() != 0)
                Test20.info(MarkupHelper.createUnorderedList(noMatchList));
//            Test20.info("search result count: "+searchResultTitleList.size());
//            Test20.info("matching titles count: "+count_titles);
            Assert.assertTrue(result);
        }
        catch (Exception e)
        {
            Functions.ex(Test20, e);
        }
    }
    public static String findCheapPrice()
    {
        String best_item = "";
        Double cheap = -1.0;
        int location = -1;
        List<WebElement>titleList = Functions.elementList(driver,title_list);
        List<WebElement>aguraList = Functions.elementList(driver,agura_list);
        List<WebElement>shekelList = Functions.elementList(driver,shekel_list);
        //הגבלת הלולאה ל 5 דוגמת רק את ה 5 מוצרים הראשונים ב list
        for (int i = 0; i < 5; i++)
        {
            String price = aguraList.get(i).getText();
            price = price.replace(""+price,"."+price);
            String fullprice = shekelList.get(i).getText();
            fullprice = fullprice+price;
            fullprice = fullprice.replace(",","");
            Double fullPrice = Double.parseDouble(fullprice);
            if (i == 0)
            {
                cheap = fullPrice;
                location = i;
            }
            else
            {
                if (cheap < fullPrice)
                    continue;
                else if (cheap > fullPrice)
                {
                    cheap = fullPrice;
                    location = i;
                }
            }
        }
        switch(location)
        {
            case 0:
                best_item = titleList.get(0).getText()+splitter+cheap;
                break;
            case 1:
                best_item = titleList.get(1).getText()+splitter+cheap;
                break;
            case 2:
                best_item = titleList.get(2).getText()+splitter+cheap;
                break;
            case 3:
                best_item = titleList.get(3).getText()+splitter+cheap;
                break;
            case 4:
                best_item = titleList.get(4).getText()+splitter+cheap;
                break;
        }
        return best_item;
    }
    public static void filterResultBy(String my_filter)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Functions.element(driver,expend_category_btn).click();
        List<WebElement> filterList = Functions.elementList(driver,filter_list);
        for (int i = 0; i < filterList.size(); i++)
        {
            String filter = filterList.get(i).getText().trim();
            if (filter.equals(my_filter))
            {
                filterList.get(i).click();
                //ממתין עד שהרשימה מתרפרשת
                wait.until(ExpectedConditions.stalenessOf(Functions.element(driver,search_result_title_list)));
                break;
            }
        }
    }

}
