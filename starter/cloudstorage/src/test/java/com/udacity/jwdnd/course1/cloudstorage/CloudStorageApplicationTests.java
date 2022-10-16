package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		//System.out.println("getLoginPage");
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password) {
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * <p>
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		//System.out.println("testRedirection");
		// Create a test account
		doMockSignUp("Redirection", "Test", "RT", "123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * <p>
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 * <p>
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		//System.out.println("testBadUrl");
		// Create a test account
		doMockSignUp("URL", "Test", "UT", "123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 * <p>
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 * <p>
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		//System.out.println("testLargeUpload");
		// Create a test account
		doMockSignUp("Large File", "Test", "LFT", "123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}


	// Test signup and login flow

	//// Write a Selenium test that verifies that the home page is not accessible without logging in.

	@Test
	public void testHomePageNoLogin() {
		//System.out.println("testHomePageNoLogin");
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	//// Write a Selenium test that signs up a new user, logs that user in, verifies that they can access
	//// the home page, then logs out and verifies that the home page is no longer accessible.

	@Test
	public void testHomePageLoginThenLogout() {
		//System.out.println("testHomePageLoginThenLogout");

		String userName = "LTL";
		String password = "123";


		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

	}

	// Test adding, editing, and deleting notes

	//// Write a Selenium test that logs in an existing user, creates a note and verifies
	//// that the note details are visible in the note list.

	@Test
	public void testCreateAndVerifyNote() {
		String userName = "CVN";
		String password = "123";

		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-button")));
		WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
		newNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		noteTitleInput.sendKeys("Test title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescInput = driver.findElement(By.id("note-description"));
		noteDescInput.sendKeys("This is a test description.");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary")));
		WebElement saveButton = driver.findElement(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ create a note ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th")));
		WebElement noteTitle = driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th"));
		Assertions.assertEquals("Test title", noteTitle.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(3)")));
		WebElement noteDesc = driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(3)"));
		Assertions.assertEquals("This is a test description.", noteDesc.getText());

		//^^^ verify that the note is created ^^^

	}

	//// Write a Selenium test that logs in an existing user with existing notes, clicks the edit note
	//// button on an existing note, changes the note data, saves the changes, and verifies that the changes appear in the note list.

	@Test
	public void testUpdateAndVerifyNote() {

		String userName = "UVN";
		String password = "123";

		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-button")));
		WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
		newNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		noteTitleInput.sendKeys("Test title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescInput = driver.findElement(By.id("note-description"));
		noteDescInput.sendKeys("This is a test description.");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary")));
		WebElement saveButton = driver.findElement(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ create a note ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th")));
		WebElement noteTitle = driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th"));
		Assertions.assertEquals("Test title", noteTitle.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(3)")));
		WebElement noteDesc = driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(3)"));
		Assertions.assertEquals("This is a test description.", noteDesc.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > button")));
		WebElement editButton = driver.findElement(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > button"));
		editButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#note-title")));
		noteTitleInput = driver.findElement(By.cssSelector("#note-title"));
		noteTitleInput.clear();
		noteTitleInput.sendKeys("Test title updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#note-description")));
		noteDescInput = driver.findElement(By.cssSelector("#note-description"));
		noteDescInput.clear();
		noteDescInput.sendKeys("This is a test description updated.");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary")));
		saveButton = driver.findElement(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ update the note ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th")));
		noteTitle = driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > th"));
		Assertions.assertEquals("Test title updated", noteTitle.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(3)")));
		noteDesc = driver.findElement(By.cssSelector("#userTable > tbody > tr:nth-child(1) > td:nth-child(3)"));
		Assertions.assertEquals("This is a test description updated.", noteDesc.getText());

		//^^^ verify that the note updated ^^^
	}

	//// Write a Selenium test that logs in an existing user with existing notes,
	//// clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.

	@Test
	public void testDeleteNote() {

		String userName = "DN";
		String password = "123";

		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-button")));
		WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
		newNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		noteTitleInput.sendKeys("Test title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescInput = driver.findElement(By.id("note-description"));
		noteDescInput.sendKeys("This is a test description.");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary")));
		WebElement saveButton = driver.findElement(By.cssSelector("#noteModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ create a note ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > a")));
		WebElement deleteButton = driver.findElement(By.cssSelector("#userTable > tbody > tr > td:nth-child(1) > a"));
		deleteButton.click();

		//^^^ delete it ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes-tab")));
		notesTab = driver.findElement(By.cssSelector("#nav-notes-tab"));
		notesTab.click();

		assertThrows(TimeoutException.class, () -> {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#userTable > tbody > tr")));
		});

		//^^^ Verify it's gone ^^^

	}

	// Test adding, editing and deleting credentials

	//// Write a Selenium test that logs in an existing user, creates a credential and
	//// verifies that the credential details are visible in the credential list.

	@Test
	public void testCreateAndVerifyCredential() {
		String userName = "CVC";
		String password = "123";

		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		WebElement credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials > button")));
		WebElement newNoteButton = driver.findElement(By.cssSelector("#nav-credentials > button"));
		newNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-url")));
		WebElement urlInput = driver.findElement(By.cssSelector("#credential-url"));
		urlInput.sendKeys("Test url");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-username")));
		WebElement userNameInput = driver.findElement(By.cssSelector("#credential-username"));
		userNameInput.sendKeys("test-user");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-password")));
		WebElement passwordInput = driver.findElement(By.cssSelector("#credential-password"));
		passwordInput.sendKeys("test-password");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary")));
		WebElement saveButton = driver.findElement(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ create a credential ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > th")));
		WebElement credentialUrl = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > th"));
		Assertions.assertEquals("Test url", credentialUrl.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)")));
		WebElement credentialUserName = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)"));
		Assertions.assertEquals("test-user", credentialUserName.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button")));
		WebElement credentialPassword = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button"));
		Assertions.assertTrue(credentialPassword.getAttribute("onclick").contains("test-password"));

		//^^^ Verify that it is created ^^^

	}


	//// Write a Selenium test that logs in an existing user with existing credentials,
	//// clicks the edit credential button on an existing credential, changes the credential data,
	//// saves the changes, and verifies that the changes appear in the credential list.

	@Test
	public void testUpdateAndVerifyCredential() {
		String userName = "UVC";
		String password = "123";

		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		WebElement credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials > button")));
		WebElement newNoteButton = driver.findElement(By.cssSelector("#nav-credentials > button"));
		newNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-url")));
		WebElement urlInput = driver.findElement(By.cssSelector("#credential-url"));
		urlInput.sendKeys("Test url");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-username")));
		WebElement userNameInput = driver.findElement(By.cssSelector("#credential-username"));
		userNameInput.sendKeys("test-user");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-password")));
		WebElement passwordInput = driver.findElement(By.cssSelector("#credential-password"));
		passwordInput.sendKeys("test-password");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary")));
		WebElement saveButton = driver.findElement(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ create a credential ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > th")));
		WebElement credentialUrl = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > th"));
		Assertions.assertEquals("Test url", credentialUrl.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)")));
		WebElement credentialUserName = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)"));
		Assertions.assertEquals("test-user", credentialUserName.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button")));
		WebElement credentialPassword = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button"));
		Assertions.assertTrue(credentialPassword.getAttribute("onclick").contains("test-password"));

		//^^^ Verify that it is created ^^^

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button")));
		WebElement editButton = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button"));
		editButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-url")));
		WebElement credentialUrlInput = driver.findElement(By.cssSelector("#credential-url"));
		credentialUrlInput.clear();
		credentialUrlInput.sendKeys("Test url updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-username")));
		WebElement credentialUserNameInput = driver.findElement(By.cssSelector("#credential-username"));
		credentialUserNameInput.clear();
		credentialUserNameInput.sendKeys("test-user updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-password")));
		WebElement credentialPasswordInput = driver.findElement(By.cssSelector("#credential-password"));
		credentialPasswordInput.clear();
		credentialPasswordInput.sendKeys("test-password updated");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary")));
		saveButton = driver.findElement(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ update the note ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > th")));
		credentialUrl = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > th"));
		Assertions.assertEquals("Test url updated", credentialUrl.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)")));
		credentialUserName = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(3)"));
		Assertions.assertEquals("test-user updated", credentialUserName.getText());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button")));
		credentialPassword = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > button"));
		Assertions.assertTrue(credentialPassword.getAttribute("onclick").contains("test-password updated"));

		//^^^ verify that the note updated ^^^

	}


	//// Write a Selenium test that logs in an existing user with existing credentials, clicks the delete
	//// credential button on an existing credential, and verifies that the credential no longer appears in the credential list.

	@Test
	public void testDeleteCredential() {
		String userName = "DC";
		String password = "123";

		doMockSignUp("URL", "Test", userName, password);
		doLogIn(userName, password);

		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		WebElement credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials > button")));
		WebElement newNoteButton = driver.findElement(By.cssSelector("#nav-credentials > button"));
		newNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-url")));
		WebElement urlInput = driver.findElement(By.cssSelector("#credential-url"));
		urlInput.sendKeys("Test url");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-username")));
		WebElement userNameInput = driver.findElement(By.cssSelector("#credential-username"));
		userNameInput.sendKeys("test-user");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credential-password")));
		WebElement passwordInput = driver.findElement(By.cssSelector("#credential-password"));
		passwordInput.sendKeys("test-password");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary")));
		WebElement saveButton = driver.findElement(By.cssSelector("#credentialModal > div > div > div.modal-footer > button.btn.btn-primary"));
		saveButton.click();

		//^^^ create a credential ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > a")));
		WebElement deleteButton = driver.findElement(By.cssSelector("#credentialTable > tbody > tr > td:nth-child(1) > a"));
		deleteButton.click();

		//^^^ Delete it ^^^

		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials-tab")));
		credentialsTab = driver.findElement(By.cssSelector("#nav-credentials-tab"));
		credentialsTab.click();

		assertThrows(TimeoutException.class, () -> {
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#credentialTable > tbody > tr")));
		});

		//^^^ Verify it's gone ^^^

	}

}
