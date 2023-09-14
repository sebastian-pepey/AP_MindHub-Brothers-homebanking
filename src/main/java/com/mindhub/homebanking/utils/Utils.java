package com.mindhub.homebanking.utils;
import java.util.Random;

public final class Utils {

    public Utils() {
    }

    public static String generateRandomCardNumber(){
        Random random = new Random();
        String[] cardNumbers = {String.valueOf(random.nextInt(8999)+1000),String.valueOf(random.nextInt(8999)+1000),String.valueOf(random.nextInt(8999)+1000),String.valueOf(random.nextInt(8999)+1000)};
        return String.join(" ",cardNumbers);
    }

    public static String generateRandomAccountNumber(){
        Random random = new Random();
        return "VIN"+String.format("%8d",random.nextInt(89999999)+10000000);
    }

    public static int generateRandomCvv(){
        Random random = new Random();
        return random.nextInt(1000);
    }

}
