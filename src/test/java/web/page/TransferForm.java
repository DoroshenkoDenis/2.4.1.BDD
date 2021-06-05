package web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import org.openqa.selenium.Keys;
import web.data.DataHelper;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

@Data
public class TransferForm {
    private SelenideElement heading = $(withText("Пополнение карты"));
    private SelenideElement amountInputField = $("[data-test-id=amount] input");
    private SelenideElement cardInputField = $("[data-test-id=from] input");
    private SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");

    public DashboardPage transferCancel() {
        cancelButton.click();
        return new DashboardPage();
    }

    public void clearField(SelenideElement field) {
        field.sendKeys(Keys.CONTROL, "a", Keys.DELETE);
    }

    public DashboardPage sentTransfer(DataHelper.TransferAmount amount, DataHelper.CardNumber cardNumber) {
        clearField(amountInputField);
        amountInputField.setValue(amount.getAmount());
        clearField(cardInputField);
        cardInputField.setValue(cardNumber.getCardNumber());
        transferButton.click();
        return new DashboardPage();
    }
}
