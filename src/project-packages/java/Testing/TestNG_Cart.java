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
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;


public class TestNG_Cart
{
    By registration_link = By.xpath("//*[@id='trigger-signin-overlay']/a[1]");
    By water_1Liter_bottle_price_agura = By.xpath("//*[@id='results-boxes']/a[1]//div[2]//span[1]");
    By water_1Liter_bottle_price_shekel = By.xpath("//*[@id='results-boxes']/a[1]//div[2]//span[2]");
    By water_1Liter_bottle_add_to_cart = By.xpath("//div[@data-position='1' and @data-list='Desktop Search Results']/div[1]");
    By cart_total_price = By.id("total-side");
    By item_more_btn = By.xpath("//div[@class='item-details']//div[@class='more']");
    By item_less_btn = By.xpath("//div[@class='item-details']//div[contains(@class,'less')]");
    By item_amount_text = By.xpath("//div[@class='item-details']//div[@class='count']");
    By item_price_per_unit = By.xpath("//span[@class='price-per-unit money-sign']");
    By cart_float_num = By.id("cart-number");

    static ExtentReports extent;
    static ExtentTest Test05;
    static ExtentTest Test06;
    static ExtentTest Test07;
    static ExtentTest Test08;
    static ExtentTest Test09;

//----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException
    {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/");
        extent = ExtentManager.GetExtent();
        Test05 = ExtentManager.createTest("test 5 - Check item price", "the purpose of the test to add item to cart and check the price added correctly");
        Test06 = ExtentManager.createTest("test 6 - Check item price updated", "the purpose of the test to check calc after changing item amount");
        Test07 = ExtentManager.createTest("test 7 - Check cart float number", "the purpose of the test to check the cart float number with the items amount in the cart");
        Test08 = ExtentManager.createTest("test 8 - Add and remove item by less btn", "the purpose of the test to remove item from cart by amount less button");
        Test09 = ExtentManager.createTest("test 9 - Remove item by x btn", "the purpose of the test to remove item from cart by (x) button");
    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }
//----------------------------------------------------------

    @Test(priority = 1)
    public void T05_Check_item_price_test()
    {
        try
        {
            Functions.search_item("נביעות");
            Functions.waitByClick(water_1Liter_bottle_add_to_cart);
            WebElement eleAgura = Functions.element(driver,water_1Liter_bottle_price_agura);
            WebElement eleShekel =  Functions.element(driver,water_1Liter_bottle_price_shekel);
            WebElement addToCart = Functions.element(driver,water_1Liter_bottle_add_to_cart);
            String agura = eleAgura.getText();
            String shekel = eleShekel.getText();
            String totalPriceSt = Functions.getPrice(agura,shekel);
            Functions.waitByClick(water_1Liter_bottle_add_to_cart);
            addToCart.click();
            Functions.waitByVisibility(cart_float_num);
            Functions.goToCartAndCloseAd();
            Functions.waitByVisibility(cart_total_price);
            WebElement cartTotalPrice = Functions.element(driver,cart_total_price);
            String cartTotalPriceSt = cartTotalPrice.getText();
            String testDescription = "אנחנו משווים מחיר של מוצר לפני ואחרי הוספתו לסל שהוא זהה";
            boolean result = Functions.resultPrint(Test05,cartTotalPriceSt,totalPriceSt,testDescription);
            Assert.assertTrue(result);
        }
        catch (Exception e)
        {
            Functions.ex(Test05, e);
        }
        Functions.clear_cart(driver);
    }
    @Test(priority = 2)
    public void T06_Check_item_price_updated_test()
    {
        try
        {
            Functions.search_item("נביעות");
            Functions.waitByClick(water_1Liter_bottle_add_to_cart);
            WebElement addToCart = Functions.element(driver,water_1Liter_bottle_add_to_cart);
            addToCart.click();
            Functions.waitByVisibility(cart_float_num);
            Functions.goToCartAndCloseAd();
            Functions.waitByPresenceOfElementLoc(item_more_btn);
            WebElement moreBtn = Functions.element(driver,item_more_btn);
            moreBtn.click();
            Functions.waitByTextToBe(item_amount_text,"2");
            String price = Functions.element(driver,item_price_per_unit).getText();
            String amount_text = Functions.element(driver,item_amount_text).getText();
            String Result = price_calc(amount_text,price);
            String cartTotalPrice = Functions.element(driver,cart_total_price).getText();
            String testDescription = "אנחנו בודקים את המחיר הסופי לאחר עדכון כמות ל 2 במקום 1";
            boolean result = Functions.resultPrint(Test06,Result,cartTotalPrice,testDescription);
            Assert.assertTrue(result);
        }
        catch (Exception e)
        {
            Functions.ex(Test06, e);
        }
    }
    @Test(priority = 3, dependsOnMethods = "T06_Check_item_price_updated_test")
    public void T07_Check_cart_float_number_test()
    {
        try
        {
            List<WebElement> list = Functions.elementList(driver,item_amount_text);
            int items_amount_sum = 0;
            for (int i = 0; i < list.size(); i++)
            {
                items_amount_sum = Integer.parseInt(list.get(i).getText());
            }
            String floatNum = Functions.element(driver,cart_float_num).getText();
            String itemsAmountSum = String.valueOf(items_amount_sum);
            String testDescription = "אנחנו משווים בין הערך המס' בעגלה לבין הכמות הנצברת בעגלה";
            boolean result = Functions.resultPrint(Test07,floatNum,itemsAmountSum,testDescription);
            Assert.assertTrue(result);
        }
        catch (Exception e)
        {
            Functions.ex(Test07, e);
        }
        Functions.clear_cart(driver);
    }
    @Test(priority = 4)
    public void T08_Add_remove_item_by_less_btn_test()
    {
        try
        {
            Functions.search_item("נביעות");
            WebElement addToCart = Functions.element(driver,water_1Liter_bottle_add_to_cart);
            addToCart.click();
            Functions.waitByVisibility(cart_float_num);
            Functions.goToCartAndCloseAd();
            Functions.waitByClick(item_less_btn);
            WebElement lessBtn = Functions.element(driver,item_less_btn);
            lessBtn.click();
            Functions.waitByVisibility(Functions.empty_cart);
            String Empty_cart = Functions.element(driver,Functions.empty_cart).getText();
            String testDescription = "אנחנו מוודאים קבלת טקסט עגלה ריקה לאחר הסרה של הפריטים";
            boolean result = Functions.resultPrint(Test08,"סל הקניות שלך ריק",Empty_cart,testDescription);
            Assert.assertTrue(result);
        }
        catch (Exception e)
        {
            Functions.ex(Test08, e);
        }
    }
    @Test(priority = 5)
    public void T09_Remove_item_by_x_btn_test()
    {
        try
        {
            Functions.search_item("נביעות");
            WebElement addToCart = Functions.element(driver,water_1Liter_bottle_add_to_cart);
            addToCart.click();
            Functions.waitByVisibility(cart_float_num);
            Functions.goToCartAndCloseAd();
            Functions.clear_cart(driver);
            Functions.waitByVisibility(Functions.empty_cart);
            String Empty_cart = Functions.element(driver,Functions.empty_cart).getText();
            String testDescription = "אנחנו מוודאים קבלת טקסט עגלה ריקה לאחר הסרה של הפריטים";
            boolean result = Functions.resultPrint(Test09,"סל הקניות שלך ריק",Empty_cart,testDescription);
            Assert.assertTrue(result);
        }
        catch (Exception e)
        {
            Functions.ex(Test09, e);
        }
    }
    public static String price_calc(String amount_text,String price)
    {
        double Price = Double.parseDouble(price);
        double Amount_text = Double.parseDouble(amount_text);
        Price = Amount_text*Price;
        String SPrice = String.valueOf(Price);
        return SPrice+"0";
    }
}