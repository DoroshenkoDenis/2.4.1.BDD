package web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

@Data
public class VerificationPage {
    SelenideElement inputCodeError = $("[data-test-id=code] .input__sub");
    SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");
    SelenideElement codeField = $("[data-test-id=code] .input__control");
    SelenideElement verifyButton = $("[data-test-id=action-verify] .button__text");

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }

}
