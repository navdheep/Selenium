package package1;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.awt.AWTException;
import java.awt.Robot;	
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestNG {
	
public WebDriver driver;
public String baseurl ="https://courses.letskodeit.com/practice";
public Robot robot;
public ExtentReports extentReports;
public ExtentHtmlReporter htmlReporter;
public ExtentTest testcase;
public WebElement iframe;
public String screenshotLocation = "/Users/naveen_na/eclipse-workspace/mvn_testNG_Selenium_4_Extent/Screenshots/";
public FileInputStream fis;
public Properties prop;

	@Test(priority = 1)
	public void browserSetup() throws IOException {
		
		FileInputStream fis = new FileInputStream("/Users/naveen_na/eclipse-workspace/mvn_testNG_Selenium_4_Extent/PropertyFile/config.properties");
		Properties prop = new Properties();
		prop.load(fis);
		
		testcase = extentReports.createTest("Launching Base URL");
		testcase.log(Status.INFO, "Launching Base URL");
		//System.setProperty("webdriver.chrome.driver", "/Users/naveen_na/eclipse-workspace/mvn_testNG_Selenium_4_Extent/Drivers/chromedriver");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(baseurl);
		if (baseurl.length()!=0) {
			testcase.log(Status.PASS, "Browser Setup Successful");
		} else {
			testcase.log(Status.FAIL, "Browser Setup NOT Successful");
			File scrnshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyDirectory(scrnshotFile, new File(screenshotLocation+"1.png"));
			testcase.addScreenCaptureFromPath(screenshotLocation+"1.png");
		}
	}
	@Test(priority = 2, dependsOnMethods = "browserSetup")
	public void performActions() throws AWTException, InterruptedException, IOException {
		
		testcase = extentReports.createTest("performActions");
		testcase.log(Status.INFO, "performActions");
		driver.findElement(By.xpath(prop.getProperty("xpath1"))).click();
		driver.findElement(By.xpath(prop.getProperty("xpath2"))).click();
		driver.findElement(By.xpath(".//option[@value='orange']")).click();
		Thread.sleep(6000);
		Robot robot = new Robot(); 
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);	
		robot.keyRelease(KeyEvent.VK_A);	
		robot.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(6000);
		if (driver.getCurrentUrl().length()==0) {
			testcase.log(Status.PASS, "performActions Successful");
		} else {
			testcase.log(Status.FAIL, "performActions NOT Successful");
			TakesScreenshot screenshot = (TakesScreenshot) driver;
			File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
			File desFile = new File(screenshotLocation+"2.png");
            FileUtils.copyFile(srcFile, desFile);
			testcase.addScreenCaptureFromPath(screenshotLocation + "2.png");
		}
	}
	@Test(priority = 3)
	public void newWindow() throws InterruptedException {
		testcase = extentReports.createTest("newWindow");
		testcase.log(Status.INFO, "newWindow");
		String parentWindow = driver.getWindowHandle();
		driver.findElement(By.xpath(".//button[@id='openwindow']")).click();
		for (String childWindow : driver.getWindowHandles()) {
			driver.switchTo().window(childWindow);
		}
		//code
		driver.findElement(By.xpath(".//option[@value='2022']")).click();
		driver.findElement(By.xpath(".//input[@id='search']")).sendKeys("Selenium");
		Thread.sleep(6000);
		//close
		driver.close();
		driver.switchTo().window(parentWindow);
		if (baseurl.length()!=0) {
			testcase.log(Status.PASS, "newWindow Successful");
		} else {
			testcase.log(Status.FAIL, "newWindow NOT Successful");
		}
	}
	@Test(priority = 4)
	public void mouseOverAndClick() throws InterruptedException {
		testcase = extentReports.createTest("mouseOverAndClick");
		testcase.log(Status.INFO, "mouseOverAndClick");
		Thread.sleep(6000);
		
		Actions actions = new Actions(driver);
    	WebElement menuOption = driver.findElement(By.xpath(".//button[@id='mousehover']"));
    	actions.moveToElement(menuOption).perform();
    	
    	driver.findElement(By.xpath(".//a[text()='Reload']")).click();
		Thread.sleep(6000);
		if (baseurl.length()!=0) {
			testcase.log(Status.PASS, "mouseOverAndClick Successful");
		} else {
			testcase.log(Status.FAIL, "mouseOverAndClick NOT Successful");
		}
	}
	
	@Test(priority = 5)
	public void iFrameHandle() throws InterruptedException {
		testcase = extentReports.createTest("iFrameHandle");
		testcase.log(Status.INFO, "iFrameHandle");
		
		
		WebElement iframe = driver.findElement(By.xpath(".//iframe[@name='iframe-name']"));
		driver.switchTo().frame("iframe-name");
		
		driver.findElement(By.xpath(".//select/option[@value='2022']")).click();
		
		driver.switchTo().defaultContent();
		
		if (baseurl.length()!=0) {
			testcase.log(Status.PASS, "iFrameHandle Successful");
		} else {
			testcase.log(Status.FAIL, "iFrameHandle NOT Successful");
		}
	}
	
	@BeforeSuite
	public void beforeSuite() {
		extentReports = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("/Users/naveen_na/eclipse-workspace/mvn_testNG_Selenium_4_Extent/Extent.html");
		extentReports.attachReporter(htmlReporter);
	}
	@AfterSuite
	public void afterSuite() {
		driver.quit();
		extentReports.flush();
	}

	
}
