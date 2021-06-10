package web.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Value;

import java.util.Locale;
import java.util.Random;

@Value
@Data
public class DataHelper {

    public DataHelper() {
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo(String login, String password) {
        return new AuthInfo(login, password);
    }

    public static AuthInfo getBadAuthInfo(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return new AuthInfo(
                faker.name().username(),
                faker.internet().password()
        );
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getVerificationCode(String code) {
        return new VerificationCode(code);
    }

//    public static VerificationCode getBadCode(int a) {
//        Random random = new Random();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < a; i++) {
//            stringBuilder.append(random.nextInt(9));
//        }
//        return new VerificationCode(stringBuilder.toString())
//                ;
//    }

    @Value
    public static class CardNumber {
        String CardNumber;
    }

    public static CardNumber getCardNumber(String cardNumber) {
        return new CardNumber(cardNumber);
    }

    public static CardNumber getBadCardNumber(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return new CardNumber(
                faker.business().creditCardNumber()
        );
    }

    @Value
    public static class CardLastDigits {
        String lastDigits;
    }

    public static CardLastDigits getLastDigits(String digits) {
        return new CardLastDigits(digits);
    }

    @Value
    public static class TransferAmount {
        String amount;
    }

    public static TransferAmount getAmount(String amount) {
        return new TransferAmount(amount);
    }

}
