package temperature;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import objectRepository.objectsClass;

public class menu {

	//Page Object Factory and Page Object Pattern both are used to improve simplicity and readability of the code
	
	
	WebDriver driver;
	
	String mailid = "perfect@gmail.com";

	@BeforeTest
	@Parameters("browser")
	
	public void setup(String browser) throws Exception {
		
		// function to initialise drivers for respective browsers
		
		if (browser.equalsIgnoreCase("Edge")) {
			String windir = System.getenv("windir");
			String edgeDriverPath = windir + "\\SysWOW64\\MicrosoftWebDriver.exe";
			System.setProperty("webdriver.edge.driver", edgeDriverPath);
			driver = new EdgeDriver();
		} 
		else if (browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		else if (browser.equalsIgnoreCase("Firefox")) {
			System.setProperty("webdriver.gecko.driver", "C:\\selenium\\geckodriver.exe");
			driver = new FirefoxDriver();
		}
		else if (browser.equalsIgnoreCase("IE")) {
			System.setProperty("webdriver.ie.driver", "C:\\selenium\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
		else if (browser.equalsIgnoreCase("Safari")) {
			System.out.println("Safari not supported in Windows. Please recheck.");
		}
		else {
			throw new Exception("Browser is not correct");
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void temperature() throws InterruptedException {
		try {
			
			driver.manage().deleteAllCookies();
			driver.get("http://weathershopper.pythonanywhere.com/");
			Reporter.log("Browser Invoked and navigated to URL", true);

			driver.manage().window().maximize();
			
			objectsClass home = new objectsClass(driver);		//object created for homepage
			
			String temp = home.fetchTemp().getText();			//Fetching the temperature shown on the website
			String[] temp_split = temp.split("[\\s\u00A0]+");	//Partitioning the string with whitespace
			int numb = Integer.parseInt(temp_split[0]);			//Fetching the first digit and converting the string to integer

			if (numb > 34) {									//As per instructions provided
				home.selectSunscreen().click();
				Reporter.log("Sunscreen selected");
				String[] itemsNeeded = { "SPF-30", "SPF-50" };	//As per instructions provided
				mapping(driver, itemsNeeded);
			} else if (numb < 19) {								//As per instructions provided
				home.selectMoisturizer().click();
				Reporter.log("Moisturizer selected");
				String[] itemsNeeded = { "Almond", "Aloe" };	//As per instructions provided
				mapping(driver, itemsNeeded);
			} else {
				Reporter.log("Temperature is normal. Stay hydrated.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mapping(WebDriver driver, String[] items) throws InterruptedException {

		objectsClass mappin = new objectsClass(driver);					//object created for mapping function
		
		mappin.itemsFetch();											//fetching all items available on the menu screen
		mappin.itemspriceFetch();										//fetching prices of all items available on the menu screen
		mappin.selectButtons();											//fetching the add to cart buttons for all the items

		String product_name;
		String product_price;
		int int_product_price;
		int low_price;
																		// Use of HashMap to store Products and Their prices(after conversion to integer)
		
		HashMap<Integer, String> map_final_products = new HashMap<Integer, String>();
		HashMap<Integer, String> map_final_products_pro = new HashMap<Integer, String>();

		ArrayList<String> al = new ArrayList<String>();

		for (int i = 0; i < mappin.itemsFetch().size(); i++) {
			product_name = mappin.itemsFetch().get(i).getText();		//Iterate and fetch product name
			product_price = mappin.itemspriceFetch().get(i).getText();	//Iterate and fetch product price
			product_price = product_price.replaceAll("[^0-9]", "");		//Replace anything with space other than numbers
			int_product_price = Integer.parseInt(product_price);		//Price convertion to Integer
			map_final_products.put(int_product_price, product_name);	//Add product and price in HashMap
		}

		Reporter.log("Product Name and price fetched from UI and saved in HashMap as:" + map_final_products.toString(),true);

		for (int j = 0; j < items.length; j++) {						//checking for match between required and available products
			map_final_products_pro.clear();
			for (int k = 0; k < mappin.itemsFetch().size(); k++) {
				if ((mappin.itemsFetch().get(k).getText().toLowerCase()).contains(items[j].toLowerCase())) {
					map_final_products_pro.put(Integer.parseInt(mappin.itemspriceFetch().get(k).getText().replaceAll("[^0-9]", "")),
					mappin.itemsFetch().get(k).getText());
				}
			}

			Set<Integer> allkeys = map_final_products_pro.keySet();
			ArrayList<Integer> array_list_values_product_prices = new ArrayList<Integer>(allkeys);
			Collections.sort(array_list_values_product_prices);
			low_price = array_list_values_product_prices.get(0);

			al.add(map_final_products_pro.get(low_price));
			al.add(Integer.toString(low_price));

			for (int i = 0; i < mappin.itemsFetch().size(); i++) {
				if (map_final_products_pro.get(low_price).equalsIgnoreCase(mappin.itemsFetch().get(i).getText())) {
					mappin.selectButtons().get(i).click();
				}
			}
		}
		Thread.sleep(5000);
		addToCart(driver, items, al);
	}

	public void addToCart(WebDriver driver, String[] items, ArrayList<String> al) throws InterruptedException {
		
		objectsClass addn = new objectsClass(driver);					//object created for addToCart function
		
		try {											
			WebDriverWait w = new WebDriverWait(driver, 10);			//wait for the 'Add to Cart' button and 'Checkout' button to be visible on the screen
			
			w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.thin-text.nav-link"))).click();
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Checkout')]")));
			
			int k = 0;
			for (int i = 1; i <= items.length; i++) {					//verification of the elements added to cart VS the items displayed in checkout screen
				for (int j = 1; j <= 2; j++) {
					if (addn.verifyTable().getText().toString().equalsIgnoreCase((String) al.toArray()[k])) {
						System.out.println(addn.verifyTable().getText().toString() + " verified");
					}
					k++;
				}
			}
		} 
		catch (Exception e) {
			Reporter.log("catch block - add to cart");
			e.printStackTrace();
		}
		finally {
			cardPayment(driver);
		}
	}

	public void cardPayment(WebDriver driver) throws InterruptedException {
		
		objectsClass pay = new objectsClass(driver);					//object created for cardPayment function
		
		try {	
																	
			WebDriverWait w = new WebDriverWait(driver, 10);			//wait for the stripe screen to be visible on the screen
			w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.stripe-button-el>span"))).click();
			
			driver.switchTo().frame(0);									//switching to the stripe screen
			
			WebDriverWait block = new WebDriverWait(driver, 10);		//wait for the text on stripe screen to be visible on the screen
			WebElement modal = block.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("main.Modal")));

			pay.emailTextFunction().sendKeys(mailid);					//provide the mail id required
			Thread.sleep(4000); //reduced from 9000 to 4000
			stripeScreen(modal);
			
		}
		catch (InvalidSelectorException e) {
			Reporter.log("catch block - card payment");
			e.printStackTrace();
		}
	}

	public void logoutCheck(WebElement modal) throws InterruptedException {
		
		objectsClass check = new objectsClass(driver);					//object created for logoutCheck function
		
		try {
			WebDriverWait w = new WebDriverWait(driver, 10);
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='Header-loggedInEmail']"))).isDisplayed();
					
			check.logoutbtnCheck().click();								//check for logout button
			check.emailTextFunction().sendKeys(mailid);					//provide the mail id required
			Thread.sleep(2000);
			stripeScreen(modal);
		}
		catch (Exception e) {
			check.emailTextFunction().sendKeys(mailid);					//provide the mail id required
			Thread.sleep(2000);
			stripeScreen(modal);
		}
	}

	public void stripeScreen(WebElement modal) throws InterruptedException {
		
		objectsClass strip = new objectsClass(driver);					//object created for stripeScreen function
		
		try {
			if (strip.elementsCheck().size() == 0) {					//check for elements on the stripe screen
				Reporter.log("No element found");
			}
			else {
				Thread.sleep(7000);
				WebDriverWait w = new WebDriverWait(driver, 10);		//wait for the error message to be visible on the screen
				w.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".CodeNotReceived-actionMessage"))).isDisplayed();
				strip.errorMessageFunction().click();
			}
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		finally {
			Thread.sleep(2000);
			
			((JavascriptExecutor)driver).executeScript("window.open()");
		    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(1));
		    driver.get("https://stripe.com/docs/testing#cards");
		    
		    WebDriverWait w = new WebDriverWait(driver, 10);		//wait for the error message to be visible on the screen
			w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"card-numbers\"]"))).isDisplayed();
		   
			WebElement web = strip.cardNumbFunc();
			
		    List<WebElement> givenList = web.findElements(By.cssSelector("td.card-number"));
		    int options = givenList.size();
		    
		    Random random = new Random();
		    int option = random.nextInt(options);
		    String value = givenList.get(option).getText();
		    Thread.sleep(2000);
		    		    
		    String brand = web.findElement(By.xpath("//td[@class='card-number']/following-sibling::td")).getText();
		    
		    driver.switchTo().window(tabs.get(0));
		   
		    Thread.sleep(2000);
		    strip.cardNumberFunction().sendKeys(value);
		    
		    SecureRandom rand= new SecureRandom();
		    
		    //generate a random date/month
		    int month = rand.nextInt(12) + 1;
	        String format_month = String.format("%02d", month);
	        
	        int year = rand.nextInt(10) + 21;
	        String format_year = String.format("%02d", year);
	         
	        strip.monthYearFunction().sendKeys(format_month + "/" + format_year);
			
			//generate a random CVC Number
			
	        int pick_amer = rand.nextInt(9999);
		    int pick = rand.nextInt(999);
		    String format = String.format("%03d", pick);
		    String format_amer = String.format("%04d", pick_amer);
		    if(brand=="American Express") {
		    	strip.CVCFunction().sendKeys(format_amer);
		    }
		    else {
		    	strip.CVCFunction().sendKeys(format);
		    }
		    
		    if(brand.contains("Visa")||brand=="Mastercard"||brand=="Mastercard (debit)"||
		    		brand=="Mastercard (prepaid)"||brand=="American Express"||
		    		brand=="Discover"||brand=="JCB"){
						strip.ZIPCodeFunction().sendKeys("689121");
		    }
			
			Thread.sleep(1000);
			strip.rememberMeFunction().click();
			Thread.sleep(1000);
			
			if(strip.telephoneFunction().isDisplayed())
			{
				strip.telephoneFunction().sendKeys("9876543210");
			}
			
			Thread.sleep(2000);
			strip.submitFunction().click();

			paymentSuccess(driver);
		}
	}

	public void paymentSuccess(WebDriver driver) throws InterruptedException {
		
		objectsClass last = new objectsClass(driver);					//object created for paymentSuccess function
		
		driver.switchTo().defaultContent();
		Thread.sleep(9000);
		if (last.headingFunction().getText() != null) {					//feching text of the last page
			if (last.headingFunction().getText().equalsIgnoreCase("Payment Success")) {
				Reporter.log(last.contentFunction().getText());
				System.out.println("Payment successful");
			} else {
				System.out.println("Oops! Payment Failed");
			}
		}
	}
}
