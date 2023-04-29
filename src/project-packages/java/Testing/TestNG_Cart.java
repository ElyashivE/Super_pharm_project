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
            if (cartTotalPriceSt.equals(totalPriceSt))
                Test05.pass(MarkupHelper.createLabel("test passed - the compared prices are the same", ExtentColor.GREEN));
            else
                Test05.fail("test failed - the compared prices are odd");
            Test05.info("the compared price before cart is: "+totalPriceSt);
            Test05.info("the compared price after cart is: "+cartTotalPriceSt);

        }
        catch (Exception e)
        {
            Test05.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
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
            Double result = price_calc(amount_text,price);
            if(result == Double.parseDouble(Functions.element(driver,cart_total_price).getText()))
                Test06.pass(MarkupHelper.createLabel("test passed - the compared prices are the same", ExtentColor.GREEN));
            else
                Test06.fail("test failed - the compared prices are the odd");
            Test06.info("the compared prices are: "+result+" | "+Functions.element(driver,cart_total_price).getText());
        }
        catch (Exception e)
        {
            Test06.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
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
            int Cart_float_num = Integer.parseInt(Functions.element(driver,cart_float_num).getText());
            if(Cart_float_num == items_amount_sum)
                Test07.pass(MarkupHelper.createLabel("test passed - the compared numbers are the same", ExtentColor.GREEN));
            else
                Test07.fail("test failed - the compared numbers are the odd");
            Test07.info("cart float num is: "+Cart_float_num);
            Test07.info("sum of items amount is: "+items_amount_sum);
        }
        catch (Exception e)
        {
            Test07.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
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
            if(Empty_cart.equals("סל הקניות שלך ריק"))
                Test08.pass(MarkupHelper.createLabel("test passed - item removed from cart", ExtentColor.GREEN));
            else
                Test08.fail("test failed - item didn't removed from cart");
            Test08.info("empty cart text is: "+Empty_cart);
        }
        catch (Exception e)
        {
            Test08.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
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
            if(Empty_cart.equals("סל הקניות שלך ריק"))
                Test09.pass(MarkupHelper.createLabel("test passed - item removed from cart", ExtentColor.GREEN));
            else
                Test09.fail("test failed - item didn't removed from cart");
            Test09.info("empty cart text is: "+Empty_cart);
        }
        catch (Exception e)
        {
            Test09.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
    }
    public static Double price_calc(String amount_text,String price)
    {
        double Price = Double.parseDouble(price);
        double Amount_text = Double.parseDouble(amount_text);
        return Amount_text*Price;
    }
}