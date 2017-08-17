package tests;

import java.util.regex.Pattern;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PetStoreAppTests {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		URL url = getClass().getClassLoader().getResource("resource/geckodriver.exe");
		System.out.println("path is: " + url.getPath());
		System.setProperty("webdriver.gecko.driver", url.getPath());
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		baseUrl = "https://qa-petstore.herokuapp.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test

	public void testDateBannerTests() throws Exception {
		driver.get(baseUrl + "/");
		// Retrieve x y coordinates of banner.
		WebElement banner = driver.findElement(By.cssSelector("span.banner-date"));

		// confirm date banner exists on page
		try {
			assertTrue(isElementPresent(By.cssSelector("span.banner-date")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		// confirm format of the displayed date
		try {
			Date today = new Date();
			SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
			String todayStr = dt1.format(today);
			System.out.println(todayStr);

			assertEquals(todayStr, driver.findElement(By.cssSelector("span.banner-date")).getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		// get the background colour of date banner
		try {
			WebElement element = driver.findElement(By.cssSelector("span.banner-date"));
			String backgroundColor = element.getCssValue("background-color");
			System.out.println(backgroundColor);
			assertEquals(backgroundColor,
					driver.findElement(By.cssSelector("span.banner-date")).getCssValue("background-color"));
		} catch (Error e) {
			verificationErrors.append(e.toString());

		}

		// Get x and y coordinates of banner.
		Point point = banner.getLocation();
		int xcord = point.getX();
		System.out.println("Banner's Position from left side Is " + xcord + " pixels.");
		int ycord = point.getY();
		System.out.println("Banner's Position from top side Is " + ycord + " pixels.");

	}

	@Test

	public void testDataTableTests() throws Exception {
		driver.get(baseUrl + "/");

		// confirm table of pets exists on page
		assertTrue(isElementPresent(By.cssSelector("div.pet-list")));

		List<WebElement> rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate no of rows in the table (less header row).
		int rows_count = rows_table.size() - 1;
		System.out.println("Number of rows: " + rows_count);

	}

	@Test

	public void testPetnameMandatoryFieldTest() throws Exception {
		driver.get(baseUrl + "/");

		// wait for page to load
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-create")));

		List<WebElement> rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate number of rows in table (less header row).
		int rows_count = rows_table.size() - 1;
		System.out.println("Initial number of rows: " + rows_count);

		// check Petname field is mandatory by submitting field
		driver.findElement(By.cssSelector("input.form-control.pet-name")).clear();
		driver.findElement(By.id("btn-create")).click();

		List<WebElement> new_rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate new number of rows in table (less header row).
		int new_rows_count = new_rows_table.size() - 1;
		int added_rows_count = new_rows_count - rows_count;
		System.out.println(added_rows_count + " new rows added");

		try {
			// test fails if a new row is successfully added with no pet name
			// (which implies mandatory checks not implemented)
			assertEquals("Pet name mandatory field failed", rows_count, new_rows_count);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

	}

	@Test
	public void testPetStatusMandatoryFieldTest() throws Exception {
		driver.get(baseUrl + "/");

		// wait for page to load
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-create")));

		List<WebElement> rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate number of rows in table (less header row).
		int rows_count = rows_table.size() - 1;
		System.out.println("Initial number of rows: " + rows_count);

		// check Pet status field is mandatory by submitting field
		driver.findElement(By.cssSelector("input.form-control.pet-status")).clear();
		driver.findElement(By.id("btn-create")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-create")));

		List<WebElement> new_rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate new number of rows in table (less header row).
		int new_rows_count = new_rows_table.size() - 1;
		int added_rows_count = new_rows_count - rows_count;
		System.out.println(added_rows_count + " new rows added");

		try {
			// test fails if a new row is successfully added with no pet status
			// (which implies mandatory checks not implemented)
			assertEquals("Pet Status mandatory field failed", rows_count, new_rows_count);
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

	}

	@Test

	public void newEntryButtonClickTest() throws Exception {
		driver.get(baseUrl + "/");
		assertTrue(isElementPresent(By.cssSelector("div.pet-list")));

		List<WebElement> rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate number of rows In table (less header row).
		int rows_count = rows_table.size() - 1;
		System.out.println("Number of rows: " + rows_count);

		// new pet name and status
		String petname1 = "Bingo";
		String petstatus1 = "Available";

		// make new entry by clicking Create button
		WebElement petStatusField = driver.findElement(By.cssSelector("input.form-control.pet-status"));
		petStatusField.click();
		petStatusField.clear();
		petStatusField.sendKeys(petstatus1);
		driver.findElement(By.cssSelector("input.form-control.pet-name")).click();
		driver.findElement(By.cssSelector("input.form-control.pet-name")).clear();
		driver.findElement(By.cssSelector("input.form-control.pet-name")).sendKeys(petname1);
		driver.findElement(By.id("btn-create")).click();

		List<WebElement> new_rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate new number of rows in table (less header row).
		int new_rows_count = new_rows_table.size() - 1;
		int added_rows_count = new_rows_count - rows_count;
		System.out.println(added_rows_count + " new rows added");

	}

	@Test

	public void newEntryEnterKeyTest() throws Exception {
		driver.get(baseUrl + "/");
		assertTrue(isElementPresent(By.cssSelector("div.pet-list")));

		List<WebElement> rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate number of rows in table (less header row).
		int rows_count = rows_table.size() - 1;
		System.out.println("Number of rows: " + rows_count);

		// new pet name and status
		String petname2 = "Jack";
		String petstatus2 = "Out of stock";

		// make new entry by using the RETURN key
		WebElement petStatusField = driver.findElement(By.cssSelector("input.form-control.pet-status"));
		petStatusField.click();
		petStatusField.clear();
		petStatusField.sendKeys(petstatus2);
		driver.findElement(By.cssSelector("input.form-control.pet-name")).click();
		driver.findElement(By.cssSelector("input.form-control.pet-name")).clear();
		driver.findElement(By.cssSelector("input.form-control.pet-name")).sendKeys(petname2);
		WebElement currentElement = driver.findElement(By.id("btn-create"));
		currentElement.sendKeys(Keys.RETURN);

		List<WebElement> new_rows_table = driver.findElements(By.xpath("//tr"));
		// Calculate new number of rows in table (less header row).
		int new_rows_count = new_rows_table.size() - 1;
		int added_rows_count = new_rows_count - rows_count;
		System.out.println(added_rows_count + " new rows added");

	}

	@Test

	public void pageTabPetNameTest() throws Exception {
		driver.get(baseUrl + "/");
		assertTrue(isElementPresent(By.cssSelector("div.pet-list")));

		// give Pet Name field focus
		driver.findElement(By.cssSelector("input.form-control.pet-name")).click();
		WebElement startElement = driver.switchTo().activeElement();
		System.out.println(startElement);

		// tab to next web element
		startElement.sendKeys(Keys.TAB);

		// confirm web element with focus
		WebElement nextElement = driver.switchTo().activeElement();
		System.out.println(nextElement);

		// tab to next web element
		nextElement.sendKeys(Keys.TAB);

		// confirm web element with focus
		WebElement newElement = driver.switchTo().activeElement();
		System.out.println(newElement);
	}

	@Test

	public void pageTabPetStatusTest() throws Exception {
		driver.get(baseUrl + "/");
		assertTrue(isElementPresent(By.cssSelector("div.pet-list")));

		// give Pet Status field focus
		driver.findElement(By.cssSelector("input.form-control.pet-status")).click();
		WebElement startElement = driver.switchTo().activeElement();
		System.out.println(startElement);

		// shift tab to previous web element
		String shiftTab = Keys.chord(Keys.SHIFT, Keys.TAB);
		driver.findElement(By.tagName("html")).sendKeys(shiftTab);

		// confirm web element with focus
		WebElement previousElement = driver.switchTo().activeElement();
		System.out.println(previousElement);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
