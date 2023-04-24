package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
    private static final String FIRST_NAME = "hau";
    private static final String LAST_NAME = "huynh";
    private static final String USER_NAME = "admin";
    private static final String PASS_WORD = "admin123";
    private static final String NOTE_TITLE = "test title";
    private static final String NOTE_DESCRIPTION = "test description";
    private static final String CRED_URL = "example.com";

    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void loginPage() {
        driver.get(LOCAL_HOST + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void signupPage() {
        driver.get(LOCAL_HOST + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void unauthorizedHomePage() {
        driver.get(LOCAL_HOST + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void unauthorizedResultPage() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        driver.get(LOCAL_HOST + this.port + "/result");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void newUserAccessTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        // signup
        driver.get(LOCAL_HOST + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(FIRST_NAME);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(LAST_NAME);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();

        //login
        driver.get(LOCAL_HOST + this.port + "/login");
        inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        //logout
        WebElement logoutButton = driver.findElement(By.id("logout"));
        logoutButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
        Assertions.assertEquals("Login", driver.getTitle());

        //Try accessing homepage
        driver.get(LOCAL_HOST + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void noteCreateTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get(LOCAL_HOST + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(FIRST_NAME);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(LAST_NAME);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();
		boolean created = false;
        driver.get(LOCAL_HOST + this.port + "/login");
        WebElement inputUsername1 = driver.findElement(By.id("inputUsername"));
        inputUsername1.sendKeys(USER_NAME);
        WebElement inputPassword1 = driver.findElement(By.id("inputPassword"));
        inputPassword1.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newNote = driver.findElement(By.id("newnote"));
        wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(NOTE_TITLE);
        WebElement notedescription = driver.findElement(By.id("note-description"));
        notedescription.sendKeys(NOTE_DESCRIPTION);
        WebElement savechanges = driver.findElement(By.id("save-changes"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());

        driver.get(LOCAL_HOST + this.port + "/home");
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(NOTE_TITLE)) {
                created = true;
                break;
            }
        }
        Assertions.assertTrue(created);
    }

    @Test
    public void noteUpdationTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String newNoteTitle = "new note title";
        driver.get(LOCAL_HOST + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(FIRST_NAME);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(LAST_NAME);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();
        driver.get(LOCAL_HOST + this.port + "/login");
        WebElement inputUsername1 = driver.findElement(By.id("inputUsername"));
        inputUsername1.sendKeys(USER_NAME);
        WebElement inputPassword1 = driver.findElement(By.id("inputPassword"));
        inputPassword1.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement notesTab1 = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab1);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newNote = driver.findElement(By.id("newnote"));
        wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(NOTE_TITLE);
        WebElement notedescription = driver.findElement(By.id("note-description"));
        notedescription.sendKeys(NOTE_DESCRIPTION);
        WebElement savechanges = driver.findElement(By.id("save-changes"));
        savechanges.click();
        WebElement click = driver.findElement(By.xpath("/html/body/div/div/span/a"));
        jse.executeScript("arguments[0].click()", click);
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
        WebElement editElement = null;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            editElement = element.findElement(By.name("edit"));
            if (editElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
        WebElement notetitle = driver.findElement(By.id("note-title"));
        wait.until(ExpectedConditions.elementToBeClickable(notetitle));
        notetitle.clear();
        notetitle.sendKeys(newNoteTitle);
        WebElement savechanges1 = driver.findElement(By.id("save-changes"));
        savechanges1.click();
        Assertions.assertEquals("Result", driver.getTitle());

        //check the updated note
        driver.get(LOCAL_HOST + this.port + "/home");
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        notesTable = driver.findElement(By.id("userTable"));
        notesList = notesTable.findElements(By.tagName("th"));
        Boolean edited = false;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(newNoteTitle)) {
                edited = true;
                break;
            }
        }
        Assertions.assertTrue(edited);
    }

    @Test
    public void noteDeleteTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get(LOCAL_HOST + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(FIRST_NAME);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(LAST_NAME);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();
        //login
        driver.get(LOCAL_HOST + this.port + "/login");
        WebElement inputUsername1 = driver.findElement(By.id("inputUsername"));
        inputUsername1.sendKeys(USER_NAME);
        WebElement inputPassword1 = driver.findElement(By.id("inputPassword"));
        inputPassword1.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newNote = driver.findElement(By.id("newnote"));
        wait.until(ExpectedConditions.elementToBeClickable(newNote)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(NOTE_TITLE);
        WebElement notedescription = driver.findElement(By.id("note-description"));
        notedescription.sendKeys(NOTE_DESCRIPTION);
        WebElement savechanges = driver.findElement(By.id("save-changes"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());

        driver.get(LOCAL_HOST + this.port + "/home");
        notesTab = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab);
        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(NOTE_TITLE)) {
                break;
            }
        }

        WebElement notesTab1 = driver.findElement(By.id("nav-notes-tab"));
        jse.executeScript("arguments[0].click()", notesTab1);
        WebElement notesTable1 = driver.findElement(By.id("userTable"));
        List<WebElement> notesList1 = notesTable1.findElements(By.tagName("td"));
        WebElement deleteElement = null;
        for (int i = 0; i < notesList1.size(); i++) {
            WebElement element = notesList1.get(i);
            deleteElement = element.findElement(By.name("delete"));
            if (deleteElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
        Assertions.assertEquals("Result", driver.getTitle());
    }

    @Test
    public void credentialCreateTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get(LOCAL_HOST + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(FIRST_NAME);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(LAST_NAME);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();
        driver.get(LOCAL_HOST + this.port + "/login");
        WebElement inputUsername1 = driver.findElement(By.id("inputUsername"));
        inputUsername1.sendKeys(USER_NAME);
        WebElement inputPassword1 = driver.findElement(By.id("inputPassword"));
        inputPassword1.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newCred = driver.findElement(By.id("newcred"));
        wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(CRED_URL);
        WebElement credUsername = driver.findElement(By.id("credential-username"));
        credUsername.sendKeys(USER_NAME);
        WebElement credPassword = driver.findElement(By.id("credential-password"));
        credPassword.sendKeys(PASS_WORD);
        WebElement submit = driver.findElement(By.id("save-credential"));
        submit.click();
        Assertions.assertEquals("Result", driver.getTitle());

        driver.get(LOCAL_HOST + this.port + "/home");
        credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        WebElement credsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        Boolean created = false;
        for (int i = 0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            if (element.getAttribute("innerHTML").equals(USER_NAME)) {
                created = true;
                break;
            }
        }
        Assertions.assertTrue(created);
    }

    @Test
    public void credentialUpdateTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String newCredUsername = "newUser";
        driver.get(LOCAL_HOST + this.port + "/signup");
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.sendKeys(FIRST_NAME);
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.sendKeys(LAST_NAME);
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement signUpButton = driver.findElement(By.id("signup"));
        signUpButton.click();
        driver.get(LOCAL_HOST + this.port + "/login");
        WebElement inputUsername1 = driver.findElement(By.id("inputUsername"));
        inputUsername1.sendKeys(USER_NAME);
        WebElement inputPassword1 = driver.findElement(By.id("inputPassword"));
        inputPassword1.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        wait.withTimeout(Duration.ofSeconds(30));
        WebElement newCred = driver.findElement(By.id("newcred"));
        wait.until(ExpectedConditions.elementToBeClickable(newCred)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(CRED_URL);
        WebElement credUsername = driver.findElement(By.id("credential-username"));
        credUsername.sendKeys(USER_NAME);
        WebElement credPassword = driver.findElement(By.id("credential-password"));
        credPassword.sendKeys(PASS_WORD);
        WebElement submit = driver.findElement(By.id("save-credential"));
        submit.click();
        Assertions.assertEquals("Result", driver.getTitle());

        driver.get(LOCAL_HOST + this.port + "/home");
        credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        WebElement credsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        Boolean created = false;
        for (int i = 0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            if (element.getAttribute("innerHTML").equals(USER_NAME)) {
                created = true;
                break;
            }
        }

        WebElement credTab1 = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab1);
        WebElement credsTable1 = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList1 = credsTable1.findElements(By.tagName("td"));
        WebElement editElement = null;
        for (int i = 0; i < credsList1.size(); i++) {
            WebElement element = credsList1.get(i);
            editElement = element.findElement(By.name("editCred"));
            if (editElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
        WebElement credUsername1 = driver.findElement(By.id("credential-username"));
        wait.until(ExpectedConditions.elementToBeClickable(credUsername1));
        credUsername1.clear();
        credUsername1.sendKeys(newCredUsername);
        WebElement savechanges = driver.findElement(By.id("save-credential"));
        savechanges.click();
        Assertions.assertEquals("Result", driver.getTitle());

        driver.get(LOCAL_HOST + this.port + "/home");
        credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        credsTable = driver.findElement(By.id("credentialTable"));
        credsList = credsTable.findElements(By.tagName("td"));
        Boolean edited = false;
        for (int i = 0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            if (element.getAttribute("innerHTML").equals(newCredUsername)) {
                edited = true;
                break;
            }
        }
        Assertions.assertTrue(edited);
    }

    @Test
    public void credentialDeleteTest() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        driver.get(LOCAL_HOST + this.port + "/login");
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.sendKeys(USER_NAME);
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.sendKeys(PASS_WORD);
        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement credTab = driver.findElement(By.id("nav-credentials-tab"));
        jse.executeScript("arguments[0].click()", credTab);
        WebElement credsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        WebElement deleteElement = null;
        for (int i = 0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            deleteElement = element.findElement(By.name("delete"));
            if (deleteElement != null) {
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
        Assertions.assertEquals("Result", driver.getTitle());
    }
}