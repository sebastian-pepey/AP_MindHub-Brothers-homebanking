package com.mindhub.homebanking.utils;
import java.util.Random;

public class Utils {

    public Utils() {
    }

    public String generateRandomCardNumber(){
        Random random = new Random();
        String[] cardNumbers = {String.valueOf(random.nextInt(8999)+1000),String.valueOf(random.nextInt(8999)+1000),String.valueOf(random.nextInt(8999)+1000),String.valueOf(random.nextInt(8999)+1000)};
        return String.join(" ",cardNumbers);
    }
}
