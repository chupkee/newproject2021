package tests;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.FBHomepage;
import pages.FBLoginpage;
import utilities.TestUtility;

public class FBLoginTest 
{
	//Declare Global properties
	TestUtility tu;
	RemoteWebDriver driver;
	WebDriverWait wait;
	FBLoginpage fblp;
	FBHomepage fbhp;
	
	@Test(priority=1)
	@Parameters({"bname"})
	public void launchSite(String x) throws Exception
	{
		//Create object to utility class
		tu=new TestUtility();
		//RemoteWebDriver driver=tu.launchChromeBrowser();
		driver=tu.launchBrowser(x);
		//Activate properties file
		Properties pro=tu.accessProperties();
		//Launch site
		tu.launchSite(pro.getProperty("url"));
		//Create wait object
		int w=Integer.parseInt(pro.getProperty("maxwait"));
		wait=new WebDriverWait(driver,w);
		//Create page classes
		fblp=new FBLoginpage(driver);
		fbhp=new FBHomepage(driver);
		wait.until(ExpectedConditions.visibilityOf(fblp.emailaddress));
	}
	
	@Test(priority=2)
	@Parameters({"eadd","eaddc","pass","passc"})
	public void loginTest(String x,String y,String z,String w) throws Exception
	{
		fblp.emailAddressFill(x);
		wait.until(ExpectedConditions.visibilityOf(fblp.pass));
		fblp.passFill(z);
		wait.until(ExpectedConditions.elementToBeClickable(fblp.loginbtn));
		fblp.loginbtnClick();
		Thread.sleep(6000);
		//Validations
		try
		{
			if(x.length()==0 && w.equalsIgnoreCase("valid") && fblp.blank_and_nondomain_email_address_err.isDisplayed())
			{
				Reporter.log("Blank/Nondomain email address test passed");
				Assert.assertTrue(true);
			}
			else if(y.equalsIgnoreCase("invalid") && w.equalsIgnoreCase("valid") && fblp.invalid_email_address_err.isDisplayed())
			{
				Reporter.log("Invalid email address test passed");
				Assert.assertTrue(true);
			}
			else if(y.equalsIgnoreCase("valid") && w.equalsIgnoreCase("blank") && fblp.blank_and_invalid_pass.isDisplayed())
			{
				Reporter.log("Blank/Invalid password test passed");
				Assert.assertTrue(true);
			}
			else if(y.equalsIgnoreCase("valid") && w.equalsIgnoreCase("invalid") && fblp.blank_and_invalid_pass.isDisplayed())
			{
				Reporter.log("Blank/Invalid password test passed");
				Assert.assertTrue(true);
			}
			else if(y.equalsIgnoreCase("valid") && w.equalsIgnoreCase("valid") && fbhp.profile_pic.isDisplayed())
			{
				Reporter.log("Login test passed");
				Assert.assertTrue(true);
				//Do logout
				fbhp.profilePicClick();
				wait.until(ExpectedConditions.visibilityOf(fbhp.logout));
				fbhp.logoutClick();
				wait.until(ExpectedConditions.visibilityOf(fblp.pass));
			}
			else
			{
				String ssname=tu.screenshot();
				Reporter.log("Login test failed");
				//String code="<img src=\"file:///"+ssname+"\" alt=\"\"/>";
				String code="<a href=\""+ssname+"\"><img src=\""+ssname+"\" height=\"100\" width=\"100\"/></a>";
				Reporter.log(code);
				Assert.assertTrue(false);
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}	
	}
	
	@Test(priority=3)
	public void closeSite()
	{
		//Close site
		tu.closeSite();
	}
	
	//Automating Results file
	//@AfterSuite
	public void openResults()
	{
		WebDriverManager.chromedriver().setup();
		RemoteWebDriver driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("E:\\Automation\\AutomationNested\\com.tddtestng.gui.facebook\\test-output\\index.html");
		driver.findElement(By.xpath("//*[text()='Reporter output']")).click();
	}
}
