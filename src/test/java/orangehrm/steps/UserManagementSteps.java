package orangehrm.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import static org.assertj.core.api.Assertions.assertThat;

public class UserManagementSteps extends PageObject {

    @Given("Maria navigates to OrangeHRM login page")
    public void navigateToLoginPage() {
        openUrl("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        $("//h5[text()='Login']").waitUntilVisible();
    }

    @When("she logs in with username {string} and password {string}")
    public void login(String username, String password) {
        $("//input[@name='username']").waitUntilVisible().type(username);
        $("//input[@name='password']").waitUntilVisible().type(password);
        $("//button[@type='submit']").click();
        $(By.className("oxd-userdropdown")).waitUntilVisible();
    }

    @When("she navigates to Admin User Management section")
    public void navigateToUserManagement() {
        $("//span[text()='Admin']").waitUntilClickable().click();
        $("//label[text()='Username']").waitUntilVisible();
    }

    @When("she searches for user {string}")
    public void searchForUser(String username) {
        $("//label[text()='Username']/../..//input").waitUntilEnabled().type(username);
        if ($("//button[.//span[text()='Search']]").isPresent()) {
            $("//button[.//span[text()='Search']]").waitUntilClickable().click();
        } else {
            $("//button[@type='submit']").waitUntilClickable().click();
        }
        try {
            $("//div[contains(@class,'oxd-table-body')]").waitUntilVisible();
            String userXpath = String.format("//div[contains(@class,'oxd-table-body')]//div[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]", username.toLowerCase());
            waitFor(10).seconds();
            $(userXpath).waitUntilVisible();
        } catch (Exception e) {
            try {
                String pageSource = getDriver().getPageSource();
                java.nio.file.Path out = java.nio.file.Paths.get("build", "debug-page-source.html");
                java.nio.file.Files.createDirectories(out.getParent());
                java.nio.file.Files.writeString(out, pageSource);
            } catch (Exception ioe) {
                Serenity.recordReportData()
                    .withTitle("Page Source Error")
                    .andContents(ioe.getMessage());
            }
            throw e;
        }
    }

    @When("she updates the username to {string}")
    public void updateUsername(String newUsername) {
        String searched = $("//label[text()='Username']/../..//input").getValue();
        if (searched == null || searched.isEmpty()) {
            $("//div[contains(@class,'oxd-table-body')]//button[contains(@class,'oxd-icon-button')]").waitUntilClickable().click();
        } else {
            String rowXpath = String.format("//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-row')][.//div[normalize-space(text())='%s']]", searched);
            WebElementFacade row = $(rowXpath);
            row.waitUntilVisible();
            WebElementFacade editBtn = row.find(By.xpath(".//button[.//i[contains(@class,'bi-pencil') or contains(@class,'bi-pencil-fill')]]"));
            try {
                editBtn.waitUntilClickable().click();
            } catch (Exception clickEx) {
                try {
                    ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", editBtn);
                } catch (Exception jsEx) {
                    throw new RuntimeException("Failed to click Edit button", jsEx);
                }
            }
        }

        try {
            new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), java.time.Duration.ofSeconds(15))
                    .until(d -> d.findElements(By.xpath("//h6[text()='Edit User']")).size() > 0 && d.findElement(By.xpath("//h6[text()='Edit User']")).isDisplayed());
        } catch (Exception we) {
            try {
                String pageSource = getDriver().getPageSource();
                java.nio.file.Path out = java.nio.file.Paths.get("build", "debug-page-source.html");
                java.nio.file.Files.createDirectories(out.getParent());
                java.nio.file.Files.writeString(out, pageSource);
            } catch (Exception ioe) {
                Serenity.recordReportData()
                    .withTitle("Page Source Error")
                    .andContents(ioe.getMessage());
            }
            throw we;
        }

        WebElementFacade usernameField = $("//label[text()='Username']/../..//input");
        usernameField.waitUntilEnabled();
        usernameField.clear();
        usernameField.type(newUsername);
        $("//button[@type='submit']").waitUntilClickable().click();
    }

    @Then("she should see a success message")
    public void verifySuccessMessage() {
        $(By.className("oxd-toast")).waitUntilVisible();
        String toastMessage = $(".oxd-toast").getText();
        assertThat(toastMessage).containsIgnoringCase("Success");
    }
}
