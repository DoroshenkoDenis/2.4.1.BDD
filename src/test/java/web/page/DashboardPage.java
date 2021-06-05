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

    public int getBalance(DataHelper.CardLastDigits replenishmentCard) {
        String cardData = $(withText(replenishmentCard.getLastDigits())).toString();
        int start = cardData.indexOf("баланс: ");
        int finish = cardData.indexOf(" р.");
        String value = cardData.substring(start + "баланс: ".length(), finish).trim();
        return Integer.parseInt(value);
    }

    public TransferForm goToTransferForm(DataHelper.CardLastDigits replenishmentCard) {
        $(withText(replenishmentCard.getLastDigits())).$("button").click();
        return new TransferForm();
    }

    //       сброс баланса к начальному - сумма равными частями на картах
    public void resetBalance(DataHelper.CardLastDigits cardLastDigits, DataHelper.CardNumber card1, DataHelper.CardNumber card2) {
        //       рассчёт общей суммы на всех картах
        int sumBalance = 0;
        for (SelenideElement card : cards) {
            int start = card.toString().indexOf("баланс: ");
            int finish = card.toString().indexOf(" р.");
            String value = card.toString().substring(start + "баланс: ".length(), finish).trim();
            int cardBalance = Integer.parseInt(value);
            sumBalance += cardBalance;
        }
        //      стартовый баланс - принимаем, что на картах одинаковые суммы
        int startBalance = sumBalance / cards.size();
        //      для каждой карты: получаем последние 4 цифры номера, баланс и ...
        for (SelenideElement card : cards) {
            int start1 = card.toString().indexOf("**** **** **** ");
            int finish1 = card.toString().indexOf(",");
            String currentCard = card.toString().substring(start1 + "**** **** **** ".length(), finish1).trim();
            int cardDigits = Integer.parseInt(currentCard);

            int start = card.toString().indexOf("баланс: ");
            int finish = card.toString().indexOf(" р.");
            String balance = card.toString().substring(start + "баланс: ".length(), finish).trim();
            int cardBalance = Integer.parseInt(balance);

            // ... проверяем условие - если текущий баланс на карте меньше  startBalance (равноразделённого),
            //      то пополняем эту карту
            if (cardBalance - startBalance < 0) {
                card.$("button").click();
                String transferAmount = String.valueOf((cardBalance - startBalance) * -1);

                transferForm.clearField(transferForm.getAmountInputField());
                transferForm.getAmountInputField().setValue(transferAmount);
                transferForm.clearField(transferForm.getCardInputField());
                //      выбираем карту
                if (!(Integer.parseInt(cardLastDigits.getLastDigits()) == cardDigits)) {
                    transferForm.getCardInputField().setValue(card1.getCardNumber());
                }
                transferForm.getCardInputField().setValue(card2.getCardNumber());
                transferForm.getTransferButton().click();
            }
        }
    }
}


