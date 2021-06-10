package web.test;

import org.junit.jupiter.api.*;
import web.data.DataHelper;
import web.page.DashboardPage;
import web.page.LoginPage;
import web.page.TransferForm;
import web.page.VerificationPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VerificationTest {
    LoginPage loginPage = new LoginPage();
    VerificationPage verificationPage = new VerificationPage();
    DashboardPage dashboardPage = new DashboardPage();
    TransferForm transferForm = new TransferForm();

    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo("vasya", "qwerty123");
    DataHelper.AuthInfo badAuthInfo = DataHelper.getBadAuthInfo("ru");
    DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCode("12345");
    DataHelper.VerificationCode badVerificationCode = DataHelper.getVerificationCode("54321");
    DataHelper.CardNumber card1 = DataHelper.getCardNumber("5559 0000 0000 0001");
    DataHelper.CardNumber card2 = DataHelper.getCardNumber("5559 0000 0000 0002");
    DataHelper.CardNumber badCard = DataHelper.getBadCardNumber("ru");
    DataHelper.CardLastDigits cardLastDigits1 = DataHelper.getLastDigits("0001");
    DataHelper.CardLastDigits cardLastDigits2 = DataHelper.getLastDigits("0002");
    DataHelper.TransferAmount transferAmount1 = DataHelper.getAmount("9500");
    DataHelper.TransferAmount transferAmount2 = DataHelper.getAmount("11000");

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");

    }

    @Test
    @Order(1)
    public void shouldTransferAndReturn() {
        loginPage
                .validLogin(authInfo)
                .verify(verificationCode);
        dashboardPage
                .goToTransferForm(cardLastDigits2)
                .sentTransfer(transferAmount1, card1);
        assertEquals(dashboardPage.getBalance(cardLastDigits2), 10000 + Integer.parseInt(transferAmount1.getAmount()));
        assertEquals(dashboardPage.getBalance(cardLastDigits1), 10000 - Integer.parseInt(transferAmount1.getAmount()));
        assertEquals(dashboardPage.getBalance(cardLastDigits1) + dashboardPage.getBalance(cardLastDigits2), 20000);
        dashboardPage
                .goToTransferForm(cardLastDigits1)
                .sentTransfer(transferAmount1, card2);
        assertEquals(dashboardPage.getBalance(cardLastDigits2), 10000);
        assertEquals(dashboardPage.getBalance(cardLastDigits1), 10000);
        assertEquals(dashboardPage.getBalance(cardLastDigits1) + dashboardPage.getBalance(cardLastDigits2), 20000);
    }

    @Test
    @Order(2)
    public void shouldNotLoginIfBadAuthInfo() {
        loginPage
                .validLogin(badAuthInfo)
                .getErrorNotification().shouldHave(text("Ошибка! \n" +
                "Неверно указан логин или пароль"));
    }

    @Test
    @Order(3)
    public void shouldNotTransferFromBadCard() {
        loginPage
                .validLogin(authInfo)
                .verify(verificationCode);
        dashboardPage
                .goToTransferForm(cardLastDigits2)
                .sentTransfer(transferAmount1, badCard);
        transferForm.getErrorNotification().shouldHave(text("Ошибка! Произошла ошибка"));
    }

    //    Этот тест должен упасть, оформлено issue
    @Test
    @Order(4)
    public void shouldBeErrorIfExcessAmount() {
        loginPage
                .validLogin(authInfo)
                .verify(verificationCode);
        dashboardPage
                .goToTransferForm(cardLastDigits2)
                .sentTransfer(transferAmount2, card1);
        transferForm.getErrorNotification().shouldHave(text("Ошибка! Вы ввели сумму превышающую остаток средств на вашей карте. Пожалуйста, введите другую сумму!"));
    }

    //    Этот тест проверяет блокировку пользователя при многократном вводе неверного SMS кода, должен идти последним в списке, чтобы продолжить тестирование без перезапуска приложения
    @Test
    @Order(5)
    public void ShouldBlocksUserIfVerificationCodeWasWrongThreeTimes() {
        loginPage.validLogin(authInfo)
                .verify(badVerificationCode);
        verificationPage.verify(badVerificationCode);
        verificationPage.verify(badVerificationCode);
        verificationPage.verify(verificationCode);
        verificationPage.getErrorNotification().shouldHave(text("Ошибка! Превышено количество попыток ввода кода!"));
    }

}
