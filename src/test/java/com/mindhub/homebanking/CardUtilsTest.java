package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTest {
    @Test
    public void cardNumberIsCreated() {
        String cardNumber = Utils.generateRandomCardNumber();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void accountNumberBeginsWithVIN() {
        String accountNumber = Utils.generateRandomAccountNumber();
        assertThat(accountNumber, startsWith("VIN"));
    }

    @Test
    public void ccvIsThreeDigitsLong() {
        int cvv = Utils.generateRandomCvv();
        assertThat(cvv, is(greaterThan(99)));
    }
}
