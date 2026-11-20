package utils;

import java.util.Random;

public class GenerateRandomNumber {

    public static Integer generateNumber(){
        Random random = new Random();
        return random.nextInt(1000, 9999);
    }
}
