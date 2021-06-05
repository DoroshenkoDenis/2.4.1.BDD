package web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

@Data
public class LoginPage {
    SelenideElement inputLoginError = $("[data-test-id=login] .input__sub");
    SelenideElement inputPasswordError = $("[data-test-id=password] .input__sub");
    SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");
    SelenideElement loginField = $("[data-test-id=login] .input__control");
    SelenideElement passwordField = $("[data-test-id=password] .input__control");
    SelenideElement loginButton = $("[data-test-id=action-login] .button__text");

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

}


