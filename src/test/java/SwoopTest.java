import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

public class SwoopTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10); // Initialize WebDriverWait
    }

    @Test
    public void swoopTest() throws InterruptedException {
        // Navigate to the swoop.ge
        String url = "https://www.swoop.ge/";
        driver.get(url);

        // Go to 'კინო'
        driver.findElement(By.linkText("კინო")).click();

        // Select the first movie in the returned list and click on ‘ყიდვა’ button
        WebElement movies = driver.findElement(By.xpath("//*[@id=\"body\"]/div[9]"));
        List<WebElement> moviesList = movies.findElements(By.xpath("//*[@id=\"body\"]/div[9]/div[1]"));
        WebElement firstMovie = moviesList.get(0);
        Actions actions = new Actions(driver);
        actions.moveToElement(firstMovie).perform();
        driver.findElement(By.xpath("(//p[contains(text(),'ყიდვა')])")).click();

        // Scroll vertically (if necessary), and horizontally and choose ‘კავეა ისთ ფოინთი’
        //WebElement caveaIslandPoint = driver.findElement(By.id("ui-id-6"));
        //caveaIslandPoint.click();
        // Scroll into view and click the element
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 500)");

        wait = new WebDriverWait(driver, 10);
        WebElement onlyCavea = wait.until((ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"body\"]/section[1]/div[2]/div[1]/ul/li[3]"))));
        if (onlyCavea.getText().equals("კავეა ისთ ფოინთი")) {
            System.out.println("passed");
        } else {
            System.out.println("failed");
        }



        //           - Check that only ‘კავეა ისთ ფოინთი’ options are returned
        WebElement eastPoint = driver.findElement(By.id("ui-id-6"));
        Actions caveaEastPointSection = new Actions(driver);
        caveaEastPointSection.moveToElement(eastPoint).click().build().perform();

        wait = new WebDriverWait(driver, 10);
        WebElement checkCavea = wait.until((ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ui-id-6\"]"))));
        System.out.println(checkCavea.getText());
        if (checkCavea.getText().equals("კავეა ისთ ფოინთი")) {
            System.out.println("pased");
        }else{
            System.out.println("failed");
        }



//        - Click on last date
        WebElement daysListElement = driver.findElement(By.xpath("//*[@id=\"384933\"]/div"));

        WebElement lastDayList = daysListElement.findElements(By.tagName("li")).get(daysListElement.findElements(By.tagName("li")).size() - 1);
        lastDayList.click();

//        and then click on last option

        WebElement date = lastDayList.findElement(By.xpath("//div[@id='384933']//div[@class='calendar-tabs ui-tabs ui-widget ui-widget-content ui-corner-all']"));
        WebElement lastSession = date.findElement(By.xpath("div[last()]"));
        lastSession.click();

//           - Check in opened popup that movie name, cinema and datetime is valid

        /*
           String popupTitle = driver.findElement(By.xpath("//p[@class='movie-title']")).getText();
        List<WebElement> popupContent = driver.findElements(By.xpath("//p[@class='movie-cinema']"));
        String popupCinema = popupContent.get(0).getText();
        String popupTime = popupContent.get(1).getText();

        String actualTitle = driver.findElement(By.xpath("//p[@class='name']")).getText();
        String actualCinema = driver.findElement(By.cssSelector("#ui-id-7")).getText();
        WebElement lastDay = null;
        String actualDay = lastDay.getText();
        WebElement lastSession = null;
        String actualTime = lastSession.getText();
        if (!actualTitle.equals(popupTitle) && actualCinema.equals(popupCinema) && (actualDay + actualTime).equals(popupTime)) {
            System.out.println("fail");
        }

         */

        // damgalaaaaaa

        String popupBoxText = (((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-header']")))).toString());


        String titleMovie = driver.findElement(By.className("movie-title")).getText();
        String cinemaName = driver.findElement(By.xpath("//p[@class='movie-cinema'][text()='კავეა ისთ ფოინთი']")).getText();
        String dateTime = driver.findElement(By.xpath("//p[@class='movie-cinema'][last()]")).getText();

        Assert.assertTrue(popupBoxText.contains(titleMovie));
        Assert.assertTrue(popupBoxText.contains(cinemaName));
        Assert.assertTrue(popupBoxText.contains(dateTime));

//         - Choose any vacant place (Click the first free seat which is anable)

        WebElement popup = driver.findElement(By.cssSelector(".corn-cinema-tabs.ui-tabs.ui-widget.ui-widget-content.ui-corner-all"));
        List<WebElement> seatList = popup.findElements(By.cssSelector(".seance"));
        WebElement chosenSeat = seatList.get(0); // pirvelive elementi
        chosenSeat.click();



//        - Register for a new account

        WebElement registerButton =driver.findElement(By.xpath("//p[@class='register']"));
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();

        driver.findElement(By.id("pFirstName")).sendKeys("liku");
        driver.findElement(By.id("pLastName")).sendKeys("khokhi");
        driver.findElement(By.id("pEmail")).sendKeys("likusukvarslobio.com");
        driver.findElement(By.id("pPhone")).sendKeys("555-55-55-55");

        driver.findElement(By.id("pDateBirth")).sendKeys("10", "8", "2002");

        WebElement gender = driver.findElement(By.id("pGender"));
        Select genderSelect = new Select(gender);
        genderSelect.selectByValue("2");

        WebElement errorMessage = driver.findElement(By.id("physicalInfoMassage"));
        String expectedErrorMessage = "მეილის ფორმატი არასწორია!";
        if (errorMessage.getText().equals(expectedErrorMessage)) {
            System.out.println("Error message is displayed correctly.");
        } else {
            System.out.println("Error message is not displayed correctly.");
        }


        /*
        WebElement errorMessage = driver.findElement(By.id("physicalInfoMassage")); // Check the correct @mail here
        Assert.assertEquals(errorMessage.getText(), "მეილის ფორმატი არასწორია!");
         */

    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
