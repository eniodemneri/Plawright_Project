package Test;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Page.ScreenshotOptions;

public class dashboard {

	private Page page;
	private Playwright playwright;
	private Browser browser;

	@BeforeMethod
	public void setUp() {
		playwright = Playwright.create();
		ArrayList<String> arguments = new ArrayList<>();
		arguments.add("--start-maximized");
		browser = playwright.chromium()
				.launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false).setArgs(arguments));
		BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
		//ScreenshotOptions screenshotOptions = new ScreenshotOptions();
		//page.screenshot(screenshotOptions.setPath(Paths.get("./screenshot/screenshot.png")));
		page = context.newPage();
		page.setDefaultTimeout(30000);
		page.navigate("https://demo.nopcommerce.com/login");

	}

	@Test
	public void dashboard_nav() {

		page.fill(".email", "test.playwright5@mail.com");
		page.fill("#Password", "Test1234!");
		page.locator(".login-button").click();

		page.locator(".notmobile > li:first-child").hover();
		page.click("text=Notebooks");

		page.locator("#products-pagesize").selectOption("9");

		int gridItems = page.locator(".item-grid > .item-box").count();
		assert gridItems == 6;

		page.waitForTimeout(1000);
		page.locator(".filter-content > ul:nth-child(2) > li:nth-child(4) > input").check();

		page.waitForTimeout(2000);
		int filterItem = page.locator(".item-grid > .item-box").count();
		assert filterItem == 1;

		page.locator(".filter-content > ul:nth-child(2) > li:nth-child(4) > input").uncheck();
		page.waitForTimeout(2000);
		int unfilterItems = page.locator(".item-grid > .item-box").count();
		assert unfilterItems == 6;
		page.waitForTimeout(2000);
		page.locator(".item-box:nth-child(2) > div > div.details > div.add-info > div > button.add-to-wishlist-button")
				.click();
		page.waitForTimeout(2000);
		page.locator(".item-box:nth-child(3) > div > div.details > div.add-info > div > button.add-to-wishlist-button")
				.click();
		page.waitForTimeout(2000);
		page.locator(
				".item-box:nth-child(4) > div > div.details > div.add-info > div > button.product-box-add-to-cart-button")
				.click();
		page.waitForTimeout(2000);
		page.locator(
				".item-box:nth-child(5) > div > div.details > div.add-info > div > button.product-box-add-to-cart-button")
				.click();
		page.waitForTimeout(2000);
		page.locator(
				".item-box:nth-child(6) > div > div.details > div.add-info > div > button.product-box-add-to-cart-button")
				.click();
		page.waitForTimeout(2000);

		String wishlistText = page.textContent("span[class='wishlist-qty']");
		int wishlistCount = Integer.parseInt(wishlistText.trim().replaceAll("[(){}]", ""));
		assert wishlistCount == 2;

		String cartText = page.textContent("span[class='cart-qty']");
		int cartCount = Integer.parseInt(cartText.trim().replaceAll("[(){}]", ""));
		page.waitForTimeout(2000);
		assert cartCount == 3;

	}

	@AfterMethod
	public void closeUp() {

		browser.close();
		playwright.close();
	}

}

