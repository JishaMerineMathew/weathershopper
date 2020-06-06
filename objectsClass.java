package objectRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class objectsClass {

	WebDriver driver;

	public objectsClass(WebDriver driver) {
		this.driver = driver;
		PageFactory .initElements(driver, this);
	}
	
	@FindBy(id="temperature")
	WebElement temp;
	
	@FindBy(xpath="//button[contains(text(),'Buy sunscreens')]")
	WebElement sunscreen;
	
	@FindBy(xpath="//button[contains(text(),'Buy moisturizers')]")
	WebElement moisturizer;

	By list_of_items = By.cssSelector("p.font-weight-bold.top-space-10");
	By list_of_items_price = By.cssSelector("p.font-weight-bold.top-space-10~p");
	By buttons = By.cssSelector("p.font-weight-bold.top-space-10~p~button");
	
	@FindBy(xpath="//table[@class='table table-striped']//tbody//tr[\" + i + \"]//td[\" + j + \"]")
	WebElement tablePath;
	
	@FindBy(xpath="//input[@placeholder='Email']")
	WebElement emailTextbox;
	
	@FindBy(xpath="//h2[contains(text(),'LogOut')]")
	WebElement logoutBtn;
	
	By screen = By.cssSelector("div.Codebox.can-setfocus.Section-child");
	
	By error = By.cssSelector(".CodeNotReceived-actionMessage");
	
	By cardNumber = By.xpath("//input[@placeholder='Card number']");
	
	By monthYear = By.xpath("//input[@placeholder='MM / YY']");
	
	By CVC = By.xpath("//input[@placeholder='CVC']");
	
	By ZIPcode = By.xpath("//input[@placeholder='ZIP Code']");
	
	By rememberMe = By.cssSelector("label.Fieldset-label");
	
	By telephone = By.xpath("//input[@inputmode='tel']");
	
	By submit = By.xpath("//button[@type='submit']");
	
	By heading = By.xpath("/html[1]/body[1]/div[1]/div[1]/h2[1]");
	
	By content = By.cssSelector("p.text-justify");
	
	By cardNumb = By.xpath("//*[@id=\"card-numbers\"]");
	
	// functions
	public WebElement fetchTemp() {
		return temp;
	}
	public WebElement selectSunscreen() {
		return sunscreen;
	}
	public WebElement selectMoisturizer() {
		return moisturizer;
	}
	public List<WebElement> itemsFetch(){
		return driver.findElements(list_of_items);
	}
	public List<WebElement> itemspriceFetch(){
		return driver.findElements(list_of_items_price);
	}
	public List<WebElement> selectButtons(){
		return driver.findElements(buttons);
	}
	public WebElement verifyTable() {
		return tablePath;
	}
	public WebElement emailTextFunction() {
		return emailTextbox;
	}
	public WebElement logoutbtnCheck() {
		return logoutBtn;
	}
	public List<WebElement> elementsCheck(){
		return driver.findElements(screen);
	}
	public WebElement errorMessageFunction(){
		return driver.findElement(error);
	}
	public WebElement cardNumberFunction(){
		return driver.findElement(cardNumber);
	}
	public WebElement monthYearFunction() {
		return driver.findElement(monthYear);
	}
	public WebElement CVCFunction() {
		return driver.findElement(CVC);
	}
	public WebElement ZIPCodeFunction() {
		return driver.findElement(ZIPcode);
	}
	public WebElement rememberMeFunction() {
		return driver.findElement(rememberMe);
	}
	public WebElement telephoneFunction() {
		return driver.findElement(telephone);
	}
	public WebElement submitFunction() {
		return driver.findElement(submit);
	}
	public WebElement headingFunction() {
		return driver.findElement(heading);
	}
	public WebElement contentFunction() {
		return driver.findElement(content);
	}
	public WebElement cardNumbFunc() {
		return driver.findElement(cardNumb);
	}
}
