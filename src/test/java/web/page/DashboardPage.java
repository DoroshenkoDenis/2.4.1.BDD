package web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import web.data.DataHelper;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

@Data
public class DashboardPage {
    TransferForm transferForm = new TransferForm();
    ElementsCollection cards = $$(".list__item");
    SelenideElement depositButton = $("[data-test-id=action-deposit] .button__text");

    public int getBalance(DataHelper.CardLastDigits digits) {
        String cardData = $(withText(digits.getLastDigits())).toString();
        int start = cardData.indexOf("баланс: ");
        int finish = cardData.indexOf(" р.");
        String value = cardData.substring(start + "баланс: ".length(), finish).trim();
        return Integer.parseInt(value);
    }

    public TransferForm goToTransferForm(DataHelper.CardLastDigits replenishmentCard) {
        $(withText(replenishmentCard.getLastDigits())).$("button").click();
        return new TransferForm();
    }
}


