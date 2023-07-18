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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_Categories
{
    static By category_menu = By.xpath("//*[@id='navigation-menu']//a[@title='קטגוריות']");
    static By category_submenu1 = By.xpath("//*[@class='submenu']//a[@title='לבית']");
    static By category_submenu2 = By.xpath("//*[@class='submenu']//a[@title='מטבח']");
    static By category_submenu3 = By.xpath("//*[@class='submenu']//a[@title='כלי אוכל ושתייה']");
    static By sort_result = By.xpath("//div[@class='fancy-select']/div[@class='trigger']");
    static By ascend = By.xpath("//li[contains(.,'הנמוך קודם')]");
    static By slider_bar_right = By.xpath("//span[contains(@class,'irs-slider to')]");
    By agura_list = By.xpath("//*[@id='results-boxes']//span[@class='cents']");
    By shekel_list = By.xpath("//*[@id='results-boxes']//span[@class='shekels money-sign']");
    By arcosteel = By.xpath("//*[@class='filter-item']//div[contains(.,'ARCOSTEEL')]");
    By title_list = By.xpath("//*[@id='results-boxes']//h4");
    By category_list = By.xpath("//*[@id='home-boxes']//p[@class='phcCategoryText']");
    By category_submenu1_list = By.xpath("//*[@id='HomePageBarComp-menu']//ul[@class='link-row first-level-list']//a");
    By A = By.xpath("//*[@id=\"home-boxes\"]/div/div[2]/section/div/div/section/div/div[3]/a/div[2]/p");
    static ExtentReports extent;
    static ExtentTest Test23;
    static ExtentTest Test24;
    static ExtentTest Test25;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test23 = ExtentManager.createTest("test 23 - Sort and filter", "the purpose of the test to check element sorted by price ascend amd price range");
        Test24 = ExtentManager.createTest("test 24 - Add filter and check title", "the purpose of the test to add another filter by manufacture and check the elements title contains the manufacture name");
        Test25 = ExtentManager.createTest("test 25 - Compare 2 list content", "the purpose of the test to compare 2 lists size&content of category section - list1 its the home page category and list 2 its via menu category");
    }

    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test(priority = 1)
    public void T23_Sort_and_filter_test()
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            Actions action = new Actions(driver);
            enterPage(action);
            sortResult(wait);
            ////-201 שווה ל 20 בסינון לפי מחיר יוצא שטווח המחירים הוא בין 9-20 במקום 9-550
            selectPriceRange(action,wait,-201);
            List<WebElement> aguraList = Functions.elementList(driver,agura_list);
            List<WebElement> shekelList = Functions.elementList(driver,shekel_list);
            List<Double> fullPriceList = getPriceList(aguraList,shekelList);
            int count_compares = checkSort(fullPriceList);
            if (count_compares == fullPriceList.size()-1)
                Test23.pass(MarkupHelper.createLabel("test passed - items sort by price from cheap to expensive as expected", ExtentColor.GREEN));
            else
                Test23.fail("test failed - items not sort properly");
            Test23.info("price list order:");
            Test23.info(MarkupHelper.createUnorderedList(fullPriceList));
            Double min_price = 9.0;
            Double max_price = 20.0;
            if ((fullPriceList.get(0)>=min_price) && (fullPriceList.get(fullPriceList.size()-1)<= max_price))
                Test23.pass(MarkupHelper.createLabel("test passed - min and max prices are in range",ExtentColor.GREEN));
            else
                Test23.fail("test failed - min and max prices not in range");
            Test23.info("expected items price range: "+min_price+"-"+max_price);
            Test23.info("actual items price range: "+fullPriceList.get(0)+"-"+fullPriceList.get(fullPriceList.size()-1));
        }
        catch (Exception e)
        {
            Test23.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    @Test(priority = 2, dependsOnMethods = "T23_Sort_and_filter_test")
    public void T24_Add_filter_and_check_title_test()
    {
        try
        {
            WebElement arcoFilter = Functions.element(driver,arcosteel);
            arcoFilter.click();
            Functions.waitByStalenessOf(arcosteel);
            List<WebElement> titleList = Functions.elementList(driver,title_list);
            List<String> titles = new ArrayList<>();
            int count_compare = 0;
            for (int i = 0; i < titleList.size(); i++)
            {
                String item_title = titleList.get(i).getText();
                titles.add(item_title);
                if (item_title.contains("ARCOSTEEL"))
                    count_compare++;
            }
//            --------------------------מצריך תיקון-----------------------------------------
//            int expectedText = titleList.size();
//            int actualText = count_compare;
//            String testDescription = "אנחנו מצפים שכל המוצרים אחרי סינון יופיעו אם שם היצרן ארקוסטיל";
//            boolean result = Functions.resultPrint(Test02,expectedText,actualText,testDescription);
//            Assert.assertTrue(result);
//            if (count_compare == titleList.size())
//                Test24.pass(MarkupHelper.createLabel("test passed - all items after filter from manufacture ARCOSTEEL",ExtentColor.GREEN));
//            else
//                Test24.fail("test failed - not all items after filter from manufacture ARCOSTEEL");
            Test24.info("items title list:");
            Test24.info(MarkupHelper.createUnorderedList(titles));
        }
        catch (Exception e)
        {
            Functions.ex(Test24, e);
        }
    }
    @Test(priority = 3)
    public void T25_Compare_2_list_content_test()
    {
        try
        {
            driver.get("https://shop.super-pharm.co.il/");
            Functions.waitByVisibility(category_list);
            List<WebElement> list2 = Functions.elementList(driver,category_list);
            WebElement categoryBtn = Functions.element(driver,category_menu);
            categoryBtn.click();
            Functions.waitByVisibility(category_submenu1_list);
            List<WebElement> list1 = Functions.elementList(driver,category_submenu1_list);
            List<String> resultList = new ArrayList<>();
            if (list1.size() <= list2.size())
                compareLists(list1,list2,resultList);
            else
                compareLists(list2,list1,resultList);
            if (resultList.size() == 0)
                Test25.pass(MarkupHelper.createLabel("test passed",ExtentColor.GREEN));
            else
            {
                Test25.fail(MarkupHelper.createLabel("test failed - the failed  compared content is:",ExtentColor.RED));
                Test25.fail(MarkupHelper.createUnorderedList(resultList));
            }
            Test25.info("list 1 size: "+list1.size());
            Test25.info("list 2 size: "+list2.size());
        }
        catch (Exception e)
        {
            Functions.ex(Test25, e);
        }
    }
    public static void enterPage(Actions action)
    {
        Functions.element(driver,category_menu).click();
        action.moveToElement(Functions.element(driver,category_submenu1)).perform();
        action.moveToElement(Functions.element(driver,category_submenu2)).perform();
        Functions.element(driver,category_submenu3).click();
    }
    public static void sortResult(WebDriverWait wait)
    {
        Functions.element(driver,sort_result).click();
        wait.until(ExpectedConditions.elementToBeClickable(ascend));
        Functions.element(driver,ascend).click();
        wait.until(ExpectedConditions.invisibilityOf(Functions.element(driver,ascend)));
    }
    public static void selectPriceRange(Actions action,WebDriverWait wait,int xOffset)
    {
        WebElement sb_right = Functions.element(driver,slider_bar_right);
        //-201 שווה ל 20 בסינון לפי מחיר יוצא שטווח המחירים הוא בין 9-20 במקום 9-550
        action.scrollToElement(sb_right).clickAndHold(sb_right).moveByOffset(xOffset,0).release(sb_right).build().perform();
//        action.release(sb_right).build().perform();
        wait.until(ExpectedConditions.invisibilityOf(Functions.element(driver,slider_bar_right)));
    }
    public static List<Double> getPriceList(List<WebElement> aguraList,List<WebElement> shekelList)
    {
        List<Double> fullPriceList = new ArrayList<>();
        for (int i = 0; i < shekelList.size(); i++)
        {
            String agura = aguraList.get(i).getText();
            agura = agura.replace(""+agura,"."+agura);
            String shekel = shekelList.get(i).getText();
            String full_price = shekel+agura;
            Double Full_Price = Double.parseDouble(full_price);
            fullPriceList.add(Full_Price);
        }
        return fullPriceList;
    }
    public static int checkSort(List<Double> fullPriceList)
    {
        int count_compares = 0;
        for (int i = 0; i < fullPriceList.size(); i++)
        {
            if (i == fullPriceList.size()-1)
                break;
            else if(fullPriceList.get(i) <= fullPriceList.get(i+1))
                count_compares++;
        }
        return count_compares;
    }
    public static List<String> compareLists(List<WebElement> list1,List<WebElement> list2,List<String> resultList)
    {
        int count_compare = 0;
        for (int i = 0; i < list1.size(); i++)
        {
            count_compare = 0;
            String list1_item = list1.get(i).getText();
            for (int j = 0; j < list2.size(); j++)
            {
                String list2_item = list2.get(j).getText();
                if (list1_item.equals(list2_item))
                    break;
                else if (count_compare == list1.size()-1)
                    resultList.add(list1_item);
                else if (count_compare == list2.size()-1)
                    resultList.add(list2_item);
                count_compare++;
            }
        }
        return resultList;
    }
}
