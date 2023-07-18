package Assists;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static Testing.TestNG_RegisterAndLogin.driver;

public class Functions
{
    public static By search_input = By.id("search-input");
    static By search_btn = By.className("magnifierContainer");
    public static By empty_cart = By.className("empty-headline");
    static By delete_item_by_x = By.className("delete-item");
    static By popup_ad = By.className("dy-lb-close");

    public static void chromeInit()
    {
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
        options.addArguments("disable-infobars");
        options.addArguments("disable-extensions");
        options.addArguments("start-maximized");
        options.addArguments("zoom=75%");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(capabilities);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }
    public static WebElement element(WebDriver driver, By by)
    {
        return driver.findElement(by);
    }
    public static List<WebElement> elementList(WebDriver driver, By by)
    {
        return driver.findElements(by);
    }
    public static void search_item(String item)
    {
        Functions.element(driver,search_input).sendKeys(Keys.ESCAPE);
        Functions.element(driver,search_input).clear();
        Functions.element(driver,search_input).sendKeys(item);
        Functions.element(driver,search_btn).click();
    }
    public static void clear_cart(WebDriver driver)
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.elementToBeClickable(popup_ad));
            Functions.element(driver,popup_ad).click();
            wait.until(ExpectedConditions.elementToBeClickable(delete_item_by_x));
        }
        catch (Exception e)
        {
            //משאיר ריק אין צורך בזה - או שמצליח לסגור את ההודעה במידה וקיימת ואם לא ממשיך רגיל בקוד
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> list = elementList(driver,delete_item_by_x);
        for (int i = 0; i < list.size(); i++)
        {
            wait.until(ExpectedConditions.elementToBeClickable(delete_item_by_x));
            Functions.element(driver,delete_item_by_x).click();
//            Thread.sleep(1000);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(empty_cart));
    }
    public static void goToCartAndCloseAd()
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(popup_ad));
            Functions.element(driver,popup_ad).click();
        }
        catch (Exception e)
        {

        }
        driver.get("https://shop.super-pharm.co.il/cart");
        try
        {
            //לסגור הודעה שקופצת לפעמים
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(popup_ad));
            Functions.element(driver,popup_ad).click();
        }
        catch (Exception e)
        {
            //משאיר ריק אין צורך בזה - או שמצליח לסגור את ההודעה במידה וקיימת ואם לא ממשיך רגיל בקוד
        }
    }
    public static String getPrice(String agura, String shekel)
    {
        agura = agura.replace(""+agura,"."+agura);
        return shekel+agura;
    }
    public static Boolean no_inventory_logo_check(String no_inventory)
    {
        if(no_inventory.equals("אזל במלאי זמנית"))
            return true;
        else
            return false;
    }
    public static void waitByClick(By by)
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }
    public static void waitByVisibility(By by)
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    public static void waitByPresenceOfElementLoc(By by)
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
    public static void waitByTextToBe(By by,String num)
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.textToBe(by,num));
    }
    public static void waitByStalenessOf(By by)
    {
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
        wait.until(ExpectedConditions.stalenessOf(Functions.element(driver,by)));
    }
    public static boolean resultPrint(ExtentTest testNum,String expectedResult, String actualResult,String testDescription)
    {
        boolean isEqual = actualResult.equals(expectedResult);
        if(isEqual)
            testNum.pass(MarkupHelper.createLabel("test passed - "+testDescription, ExtentColor.GREEN));
        else
            testNum.fail(MarkupHelper.createLabel("test failed - "+testDescription,ExtentColor.RED));
        testNum.info("Actual Result: "+actualResult);
        testNum.info("Expected Result: "+expectedResult);
        return isEqual;
    }
    public static boolean resultPrintInt(ExtentTest testNum,int value1Bigger, int value2, int value3, String testDescription)
    {
        boolean isBigger = (value1Bigger > value2) && (value1Bigger > value3);
        if((value1Bigger > value2) && (value1Bigger > value3))
            testNum.pass(MarkupHelper.createLabel("test passed - "+testDescription, ExtentColor.GREEN));
        else
            testNum.fail(MarkupHelper.createLabel("test failed - "+testDescription,ExtentColor.RED));
        return isBigger;
    }
    public static boolean resultPrintNotEqual(ExtentTest testNum,String expectedResult, String actualResult,String testDescription)
    {
        boolean isEqual = !actualResult.equals(expectedResult);
        if (isEqual)
            testNum.pass(MarkupHelper.createLabel("test passed - " + testDescription, ExtentColor.GREEN));
        else
            testNum.fail(MarkupHelper.createLabel("test failed - " + testDescription, ExtentColor.RED));
        testNum.info("Actual Result: " + actualResult);
        testNum.info("Expected Result: " + expectedResult);
        return isEqual;
    }
    public static void ex(ExtentTest testNum, Exception e)
    {
        testNum.fail(MarkupHelper.createLabel("error!!! exception",ExtentColor.YELLOW));
        testNum.fail(e);
    }
}
