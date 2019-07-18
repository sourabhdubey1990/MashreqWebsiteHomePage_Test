package MashreqWebsiteHomePage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

public class MashreqWebsiteHomePageTests {
	
				
		WebDriver driver;
	

	@Test(priority = 1, description = "This step will launch the application")
	public void launch_WebSite() throws IOException {
		
		// Chrome Options to start window maximized
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");

		// Chrome properties are set
		System.setProperty("webdriver.chrome.driver",getObjectFromRepository("CROMEDRIVERPATH"));
		
		// Chrome Driver Initialization
		driver = new ChromeDriver(options);
		driver.get(getObjectFromRepository("URL"));
		
	}

	@Test(dependsOnMethods = "launch_WebSite", description = "This step will verify 9 items on homepage..Corporate, Business, International etc.")
	public void varifyHeaderItems() throws IOException {
		String items[] = getObjectFromRepository("ITEMS").split(",");
		System.out.println(items);

		int i = 0;
		java.util.List<WebElement> listOfHeaderItems = driver.findElements(By.xpath(getObjectFromRepository("HEADEITEMSLIST")));
		for (WebElement webElement : listOfHeaderItems) {
			
			highlightElement(webElement);
			Assert.assertTrue(webElement.getText().trim().contains(items[i]));
			System.out.println("Item '" + webElement.getText() + "' is desplayed on homepage!!!");
			i++;

		}

	}

	@Test(dependsOnMethods = "varifyHeaderItems", description = "This method verifies Mashreq News on home page")
	public void verify_MashreqNews() throws Exception {

		scrollWindow(driver, 700);
		WebElement element=driver.findElement(By.xpath(getObjectFromRepository("MASHREQNEWS")));
		highlightElement(element);
		if (element.isDisplayed())
			System.out.println("The Mashreq News displayed on the homepage successfully!!");

	}

	@Test(dependsOnMethods = "verify_MashreqNews", description = "This method verifies Contact Us Link")
	public void verify_ContactUs_Link() throws IOException, InterruptedException {
		scrollWindow(driver, 370);
		
		WebElement contactuslink=driver.findElement(By.xpath(getObjectFromRepository("CONTACTUSLINK")));
		highlightElement(contactuslink);
		if (contactuslink.isDisplayed())
			System.out.println("�Contact Us Link� displayed on the homepage!!");

		// Click on contact us link and wait for 'Form Page' to load
		
		driver.findElement(By.xpath(getObjectFromRepository("CONTACTUSLINK"))).click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
	}

	@Test(dependsOnMethods = "verify_ContactUs_Link", description = "This method validate Contact Form Scenarios")
	public void verify_ContactFormPageCases() throws IOException, InterruptedException {
		
		//click on submit button without entering values
		scrollWindow(driver, 500);
		WebElement submitButton=driver.findElement(By.xpath(getObjectFromRepository("SUBMITBUTTON")));
		highlightElement(submitButton);
		submitButton.click();
		WebElement i_Am_Looking_To = driver
				.findElement(By.xpath(getObjectFromRepository("I_AM_LOOKING_TO_DROPDOWN")));
		highlightElement(i_Am_Looking_To);

		if (i_Am_Looking_To.isDisplayed())
			System.out.println("Navigated To Required Field To Be Filled Before Submit :" );

	}

	@Test(dependsOnMethods = "verify_ContactFormPageCases", description = "This method Verify dropdown having 4 values")
	public void verify_DropDown_IAmLookingTo() throws IOException {
		
		//To Click on dropdown, taking values in list and verifing number of dropdown values
		
		driver.findElement(By.xpath(getObjectFromRepository("I_AM_LOOKING_TO_DROPDOWN"))).click();
		java.util.List<WebElement> dropdownItems = driver
				.findElements(By.xpath(getObjectFromRepository("I_AM_LOOKING_TO_DROPDOWN_VALUES")));
		if (dropdownItems.size() == 5)
			System.out.println("I am looking to dropdown is having total 4 items");
		for (WebElement dropDownValue : dropdownItems) {
			System.out.println("DropDown Values are :" + dropDownValue.getText());
		}
	}

	@Test(dependsOnMethods = "verify_DropDown_IAmLookingTo", description = "This method Verifies Sub Product dropdown and values")
	public void verify_SubProduct_DropDown() throws IOException {
		
		// To Verify SubProduct_DropDown is empty initially
		driver.findElement(By.xpath(getObjectFromRepository("SUBPRODUCT_DROPDOWN"))).click();
		if (driver.findElements(By.xpath(getObjectFromRepository("SUBPRODUCT_DROPDOWN_VALUES"))).size() == 1)
			System.out.println("Verified SubProduct_DropDown is empty initially");
		
		//To select Loans product from product dropdown
		WebElement productDropdown=driver.findElement(By.xpath(getObjectFromRepository("PRODUCT_DROPDOWN")));
		highlightElement(productDropdown);
		Select product = new Select(productDropdown);
		product.selectByVisibleText("Loans");
		
		//To Verify number of items in subProduct dropdown
		driver.findElement(By.xpath(getObjectFromRepository("SUBPRODUCT_DROPDOWN"))).click();
		java.util.List<WebElement> dropdownItems = driver.findElements(By.xpath(getObjectFromRepository("SUBPRODUCT_DROPDOWN_VALUES")));
		if (dropdownItems.size()== 7)
			System.out.println("I am looking to dropdown is having total 6 items");

		//To Verify 'Home Loan UAE Resident' value in the sub product dropdown
		for (WebElement subProductValue : dropdownItems) {
			if (subProductValue.getText().equals("Home Loan UAE Resident"))
				System.out.println("Verified one of the value is 'Home Loan UAE Resident'");
		}

	}

	@Test(dependsOnMethods = "verify_SubProduct_DropDown", description = "This method verifies 7 digit Mobile Number")
	public void verify_MobileNumber() throws IOException {
		
		//Initial check with 6 digit mobile number
		WebElement mobileTextbox=driver.findElement(By.xpath(getObjectFromRepository("MOBILE_TEXTBOX")));
		highlightElement(mobileTextbox);
		mobileTextbox.clear();
		mobileTextbox.sendKeys("123456");
		WebElement submitButton=driver.findElement(By.xpath(getObjectFromRepository("SUBMITBUTTON")));
		highlightElement(submitButton);
		submitButton.click();
		WebElement erroMessage = driver
				.findElement(By.xpath(getObjectFromRepository("ERROR_MESSAGE")));
		
		highlightElement(erroMessage);
		if (erroMessage.isDisplayed())
			System.out.println("Verified ErrorMessage successully :" + erroMessage.getText());
		
		//Final Check with 7 digit mobile number
		highlightElement(mobileTextbox);
		mobileTextbox.clear();
		mobileTextbox.sendKeys("1234567");
		submitButton.click();

		if (!erroMessage.isDisplayed())
			System.out.println("Verified incorrect mobile digit ErrorMessage is not present");
	}

	@org.testng.annotations.AfterClass
	public void AfterClass() {
		driver.quit();
	}
	
	//Generic method to perform scroll in the application
	public void scrollWindow(WebDriver driver, int value) throws InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0," + value + ")");
		Thread.sleep(2000);

	}
	
	public String getObjectFromRepository(String key) throws IOException
	{
		//Initialize Object Repository To Read All Objects
		File objectRepo=new File("./Repository/ObjectRepository.properties");
		FileInputStream fis=new FileInputStream(objectRepo);
		Properties property= new Properties();
		property.load(fis);
		
		//Read Objects Value String From Repo
		String value=property.getProperty(key);
		return value;
		
	}
	
	  public  void highlightElement(WebElement element) {
	        for (int i = 0; i <2; i++) {
	            JavascriptExecutor js = (JavascriptExecutor) driver;
	            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 3px solid red;");
	            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
	            }
	        }
	

}