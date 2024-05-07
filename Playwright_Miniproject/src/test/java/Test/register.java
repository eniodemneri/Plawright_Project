package Test;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;

public class register {

	private Page page;
	private Playwright playwright;
	Browser browser;

	@BeforeMethod
	public void setUp() {

		playwright = Playwright.create();
		ArrayList<String> arguments = new ArrayList<>();
		arguments.add("--start-maximized");
		browser = playwright.chromium()
				.launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false).setArgs(arguments));
		BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
		page = context.newPage();
		ScreenshotOptions screenshotOptions = new ScreenshotOptions();
		page.screenshot(screenshotOptions.setPath(Paths.get("./screenshot/screenshot.png")));
		page.navigate("https://demo.nopcommerce.com/");
	}

	@Test(priority = 1)
	public void valid_register() {
		page.click("text=Log in");
		page.locator(".register-button").click();
		System.out.println(page.title());
		page.locator("#gender-male").click();
		page.fill("input[name='FirstName']", "Enio");
		page.fill("input[name='LastName']", "Demneri"); 
		page.locator("select[name='DateOfBirthDay']").selectOption("30");
		page.locator("select[name='DateOfBirthMonth']").selectOption("May");
		page.locator("select[name='DateOfBirthYear']").selectOption("1990");
		page.fill("input[type='Email']", "test.playwright5@mail.com");

		page.fill("#Company", "My Personal Company");

		// checkbox
		page.locator("#Newsletter").uncheck();
		page.locator("#Newsletter").check();
		assert page.locator("#Newsletter").isChecked();

		page.fill("#Password", "Test1234!");
		page.fill("#ConfirmPassword", "Test1234!");
		
		page.waitForTimeout(2000);
		page.locator("#register-button").click();
		assert page.getByText("Your registration completed").isVisible();
		
		page.click("text='Continue'");
		page.locator(".ico-logout").click();
		assert page.locator(".ico-register").isVisible();
	}
	
	@Test(priority=2, dependsOnMethods = {"valid_register"})
	public void login() {
		page.locator(".ico-login").click();
		assert page.getByText("Returning Customer").isVisible(); 

		page.fill(".email", "test.playwright5@mail.com");
		page.fill("#Password", "Test1234!");
		page.locator("#RememberMe").check();
		assert page.locator("#RememberMe").isChecked();

		page.locator(".login-button").click();

		Assert.assertEquals(page.innerText(".topic-block-title > h2"), "Welcome to our store");
		assert page.locator(".ico-logout").isVisible();
		page.click(".ico-logout");
		assert page.locator(".ico-login").isVisible();
	}

	
	
	@AfterMethod
	public void closeUp() {
		browser.close();
		playwright.close();
	}

}

//}