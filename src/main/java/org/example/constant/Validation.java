package org.example.constant;


import org.example.functional.ValidationPredicate;
import org.example.validation.ValidationResult;

import java.util.function.Supplier;

import static org.example.constant.Color.RESET;
import static org.example.constant.Color.YELLOW;
import static org.example.constant.Config.REGEX_TABLE_OPTION;
import static org.example.constant.Config.printError;
import static org.example.constant.Error.ERROR_INVALID_OPTION;
import static org.example.constant.Error.ERROR_OPTION_EMPTY;


public class Validation {


    /**
     * This method for reusable code to get validated input from user
     * <br>{@code Example:}
     * <pre>{@code String option =
     * getValidatedInput(
     *   scanner::nextLine,
     *   value -> {
     *      if (!value.matches(regex)) {
     *        return new ValidationResult(false, errorMessage);
     *      } else
     *      return new ValidationResult(true, "");
     *   },
     *   "Enter  option: "
     * );
     *}</pre>
     @Param1 : scanner::nextLine
     @Param2 : <pre>{@code
            value -> {
                if (condition) {
                return new ValidationResult(false, errorMessage);
                } else return new ValidationResult(true, "");
            }
     }</pre>
     @Param3 : Message to ask user to input
     */
    public static String getValidatedInput(Supplier<String> input, ValidationPredicate validator, String inputMessage) {
        while (true) {
            System.out.print(YELLOW + inputMessage + RESET);
            String value = input.get();
            ValidationResult result = validator.test(value);
            if (result.isValid()) return value;
            printError(result.getErrorMessage() + " Try again.");
        }
    }

    public static boolean validateMenuOption(String option) {
        if (option.isBlank()) {
            printError(ERROR_OPTION_EMPTY);
            return false;
        } else if (!option.matches(REGEX_TABLE_OPTION)) {
            printError(ERROR_INVALID_OPTION);
            return false;
        }
        return true;
    }

    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z][a-zA-Z0-9 ]*$");
    }

    public static boolean isValidQuantity(String quantity) {
        try {
            int qty = Integer.parseInt(quantity);
            return qty > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPrice(String price) {
        try {
            float pr = Float.parseFloat(price);
            return pr > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
