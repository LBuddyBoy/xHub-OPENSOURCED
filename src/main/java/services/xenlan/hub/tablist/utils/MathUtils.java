package services.xenlan.hub.tablist.utils;

public class MathUtils {

    /**
     * Check if a value is between two other values
     */
    public static Boolean isBetween(Integer input, Integer min, Integer max){
        return input >= min && input <= max;
    }

    /**
     * Convert a value to a positive value
     */
    public static int convertToPositive(int input){
        if (input > 0){
            return input;
        }
        return Math.abs(input);
    }

}
