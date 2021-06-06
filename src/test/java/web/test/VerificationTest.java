package web.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.data.DataHelper;
import web.page.DashboardPage;
import web.page.LoginPage;
import web.page.TransferForm;
import web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerificationTest {
    LoginPage loginPage = new LoginPage();
    VerificationPage verificationPage = new VerificationPage();
    DashboardPage dashboardPage = new DashboardPage();
    TransferForm transferForm = new TransferForm();

    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo("vasya", "qwerty123");
    DataHelper.AuthInfo badAuthInfo = DataHelper.getBadAuthInfo("ru");
    DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCode("12345");
    DataHelper.VerificationCode badVerificationCode = DataHelper.getBadCode(5);
    DataHelper.CardNumber card1 = DataHelper.getCardNumber("5559 0000 0000 0001");
    DataHelper.CardNumber card2 = DataHelper.getCardNumber("5559 0000 0000 0002");
    DataHelper.CardNumber badCard = DataHelper.getBadCardNumber("ru");
    DataHelper.CardLastDigits cardLastDigits1 = DataHelper.getLastDigits("0001");
    DataHelper.CardLastDigits cardLastDigits2 = DataHelper.getLastDigits("0002");
    DataHelper.TransferAmount transferAmount1 = DataHelper.getAmount("10500");
    DataHelper.TransferAmount transferAmount2 = DataHelper.getAmount("0");
    DataHelper.TransferAmount transferAmount3 = DataHelper.getAmount("11000");
    DataHelper.TransferAmount transferAmount4 = DataHelper.getAmount("-1000");

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        loginPage
                .validLogin(authInfo);
        sleep(3000);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void ResetCardBalance() {
        dashboardPage
                .resetBalance(cardLastDigits1, card1, card2);
        assertEquals(dashboardPage.getBalance(cardLastDigits1), dashboardPage.getBalance(cardLastDigits2));
    }

    @Test
    public void shouldTransfer() {
        dashboardPage
                .goToTransferForm(cardLastDigits1);
        transferForm.sentTransfer(transferAmount1, card2);

    }

//    @Test
//    public void SettingsTest() {
//        loginPage
//                .validLogin(authInfo);
//        sleep(3000);
//        verificationPage.validVerify(verificationCode);
//        dashboardPage
//                .goToTransferForm(cardLastDigits1);
//        transferForm.sentTransfer(transferAmount1, card2);
//        dashboardPage.resetBalance(cardLastDigits1, card1, card2);
//    }
}
