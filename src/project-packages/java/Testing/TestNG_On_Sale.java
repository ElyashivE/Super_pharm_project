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
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class TestNG_On_Sale
{
    By first_item = By.xpath("//*[@id='results-boxes']/a[1]/div");
    By item_content_add_btn = By.xpath("//*[@id='salePopUp']//button[contains(@class,'btn')]");
    By item_content_error_msg = By.className("low-validation");
    By item_content_more_btn = By.xpath("//div[@class='item-action']//div[@class='more']");
    By item_amount_text = By.xpath("//div[@class='item-details']//div[@class='count']");
    static By discount_list = By.xpath("//div[contains(@class,'promotion-text') and contains(.,'%') and not(contains(.,'שני'))]");
    By cart_before_discount_shekel = By.xpath("//*[@class='cart-item-wrapper']//div[@class='old-price price-row']/span[2]");
    By cart_before_discount_agura = By.xpath("//*[@class='cart-item-wrapper']//div[@class='old-price price-row']/span[1]");
    By cart_after_discount_shekel = By.xpath("//*[@class='cart-item-wrapper']//div[@class='price-row']/span[2]");
    By cart_after_discount_agura = By.xpath("//*[@class='cart-item-wrapper']//div[@class='price-row']/span[1]");
    static By reload_more_on_sale_btn = By.xpath("//*[@id=\"results\"]//button[@title='למבצעים נוספים']");
    By add_to_card_content_win = By.xpath("//*[@class='modal-content']//button[contains(.,'הוספה לסל')]");
    static ExtentReports extent;
    static ExtentTest Test10;
    static ExtentTest Test11;
    static ExtentTest Test12;

    //----------------------------------------------------------

    @BeforeClass(alwaysRun = true)
    public static void BeforeClass() throws InterruptedException {
        Functions.chromeInit();
        driver.get("https://shop.super-pharm.co.il/promotions");
        extent = ExtentManager.GetExtent();
        Test10 = ExtentManager.createTest("test 10 - Item content error test", "the purpose of the test to get error msg when try to add item to cart with 0 amount via item content window");
        Test11 = ExtentManager.createTest("test 11 - Add item to cart via content window", "the purpose of the test to add item to cart via item content window");
        Test12 = ExtentManager.createTest("test 12 - Discount check test", "the purpose of the test if the discount is calc correctly");

    }
    @AfterClass(alwaysRun = true)
    public static void AfterClass()
    {
        driver.quit();
        extent.flush();
    }

    //----------------------------------------------------------

    @Test
    public void T10_Item_content_error_test()
    {
        try
        {
            WebElement firstItem = Functions.element(driver,first_item);
            firstItem.click();
            Functions.waitByClick(item_content_add_btn);
            WebElement addToCartViaContentWin = Functions.element(driver,item_content_add_btn);
            addToCartViaContentWin.click();
            String actual_text = Functions.element(driver,item_content_error_msg).getAttribute("innerHTML").trim();
            String expected_text = "נא להוסיף מוצר נוסף למימוש המבצע";
            if (actual_text.equals(expected_text))
                Test10.pass(MarkupHelper.createLabel("test passed - we got error as expected", ExtentColor.GREEN));
            else
                Test10.fail("test failed - we didn't get error as expected");
            Test10.info("actual result: "+actual_text);
            Test10.info("expected result: "+expected_text);
        }
        catch (Exception e)
        {
            Test10.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
        driver.navigate().refresh();
    }
    @Test
    public void T11_Add_item_to_cart_via_content_window_test() throws InterruptedException
    {
        try
        {
            WebElement firstItem = Functions.element(driver,first_item);
            firstItem.click();
            Functions.waitByClick(item_content_add_btn);
            WebElement moreBtn = Functions.element(driver,item_content_more_btn);
            WebElement addToCart = Functions.element(driver,add_to_card_content_win);
            moreBtn.click();
            moreBtn.click();
            addToCart.click();
            Functions.goToCartAndCloseAd();
            String actual_text = Functions.element(driver,item_amount_text).getText().trim();
            String expected_text = "2";
            if (actual_text.equals(expected_text))
                Test11.pass(MarkupHelper.createLabel("test passed - the compared amount are the same", ExtentColor.GREEN));
            else
                Test11.fail("test failed - the compared amount are odd");
            Test11.info("actual result: "+actual_text);
            Test11.info("expected result: "+expected_text);
        }
        catch (Exception e)
        {
            Test11.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
        Functions.clear_cart(driver);
    }
    @Test
    public void T12_Discount_check_test() throws InterruptedException
    {
        try
        {
            driver.get("https://shop.super-pharm.co.il/promotions");
            WebElement discount_first_element = locate_first_element_with_discount();
            String discount_num = clear_discount_text_to_get_num(discount_first_element);
            goToElementAndClick(discount_first_element);
            Functions.waitByClick(item_content_add_btn);
            WebElement moreBtn = Functions.element(driver,item_content_more_btn);
            WebElement addToCart = Functions.element(driver,item_content_add_btn);
            moreBtn.click();
            addToCart.click();
            Functions.goToCartAndCloseAd();
            String shekel = Functions.element(driver,cart_before_discount_shekel).getText();
            String agura = Functions.element(driver,cart_before_discount_agura).getText();
            String price_before = Functions.getPrice(agura,shekel);
            shekel = Functions.element(driver,cart_after_discount_shekel).getText();
            agura = Functions.element(driver,cart_after_discount_agura).getText();
            String price_after = Functions.getPrice(agura,shekel);
            double discount = Double.parseDouble(discount_num);
            double before = Double.parseDouble(price_before);
            double after = Double.parseDouble(price_after);
            double calc = (before - (before*(discount/100)));
            if (String.format("%.1f",calc).equals(String.format("%.1f",after)))
                Test12.pass(MarkupHelper.createLabel("test passed - the discount is ok", ExtentColor.GREEN));
            else
                Test12.fail("test failed - the discount is wrong");
            Test12.info("values: discount- "+discount+ " old-price- "+before+" new-price- "+after);
            Test12.info("actual result: "+ String.format("%.1f",calc));
            Test12.info("expected result: "+ String.format("%.1f",after));
        }
        catch (Exception e)
        {
            Test12.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        }
        Functions.clear_cart(driver);
    }

    public static String clear_discount_text_to_get_num(WebElement discount_first_element)
    {
        String discount_num = discount_first_element.getText();
        discount_num = discount_num.replace("הנחה","").replace("%","").trim();
        return discount_num;
    }

    public static WebElement locate_first_element_with_discount() throws InterruptedException
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        int x = 0;
        List<WebElement> discountList;
        do
        {
            discountList = Functions.elementList(driver, discount_list);
            WebElement show_more_btn = Functions.element(driver, reload_more_on_sale_btn);
            if (discountList.size() == 0)
            {
                new Actions(driver).scrollToElement(show_more_btn).perform();
                wait.until(ExpectedConditions.elementToBeClickable(show_more_btn));
                show_more_btn.click();
            }
            else
                x++;
        }
        while (x == 0);
        return discountList.get(0);
    }
    public static void goToElementAndClick(WebElement ele)
    {
        Actions actions = new Actions(driver);
        actions.moveToElement(ele, 0, 0).click().build().perform();
    }
}
