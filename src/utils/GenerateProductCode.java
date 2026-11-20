package utils;

public class GenerateProductCode {

    public static String generateCode(String categoryName, Integer number){
        return categoryName.substring(0, 4).toUpperCase() + number;
    }
}
