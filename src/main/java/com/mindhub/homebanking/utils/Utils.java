package com.mindhub.homebanking.utils;
import java.util.Random;

public class Utils {

    public Utils() {
    }

    public String generateRandomCardNumber(){
        Random random = new Random();
        return String.format("%4s%5s%5s%5s", String.valueOf(Math.abs(random.nextLong())).substring(0,16).split("(?<=\\G....)"));
    }
}
