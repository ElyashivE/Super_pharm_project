package Assists;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ExtentManager
{
    public static WebDriver driver;
    public static String folderPath;
    public ExtentManager()
    {
        ExtentManager.driver = driver;
    }
    public static ExtentReports extent;
    static ExtentTest test;
    public static ExtentSparkReporter htmlReporter;
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    static Date today = Calendar.getInstance().getTime();
    public static String reportDate = df.format(today);
    public static String filePath = ".\\My SuperPharm Project - STD + STR Report\\STR Report\\"+reportDate+"\\exReport.html";

    //יצירת דוח report
    public static ExtentReports GetExtent()
    {
        new File(".\\My SuperPharm Project - STD + STR Report\\STR Report\\" + reportDate).mkdirs();
        if (extent != null)
            return extent; //avoid creating new instance of html file
        extent = new ExtentReports();
        extent.attachReporter(getHtmlReporter());
        return extent;
    }

    //יצירת טסט
    public static ExtentTest createTest(String name, String description)
    {
        test = extent.createTest(name, description);
        return test;
    }

    //הגדרות
    private static ExtentSparkReporter getHtmlReporter()
    {
        htmlReporter = new ExtentSparkReporter(filePath);
        htmlReporter.config().setDocumentTitle("QAV automation report");
        htmlReporter.config().setReportName("Regression cycle");
        htmlReporter.config().setEncoding("windows-1255");
        return htmlReporter;
    }

    //צילום מסך
    public static String CaptureScreen(WebDriver driver) throws AWTException, IOException
    {
        LocalDateTime now = LocalDateTime.now();
        String time =now.format(DateTimeFormatter.ofPattern("ddHHmmss"));
        folderPath = (".\\My SuperPharm Project - STD + STR Report\\STR Report\\"+reportDate);
        String imagePath = folderPath +"/pic" +time +".jpg";
        TakesScreenshot oScn = (TakesScreenshot) driver;
        File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
        File oDest = new File(imagePath);
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        Files.copy(scrFile, oDest);
        return imagePath;
    }
}

