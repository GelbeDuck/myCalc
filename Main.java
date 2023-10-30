import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input: ");
        String input = scanner.nextLine();
        String result = calc(input);
        System.out.println("Output: " + result);
    }
    public static String calc(String input) {
        // Разделение введенной строки
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *) через пробел");
        }

        String operand1 = parts[0];
        String operator = parts[1];
        String operand2 = parts[2];

        // Проверка ввода на соответствие требованиям
        boolean isRoman = isRomanNumeral(operand1) && isRomanNumeral(operand2);
        boolean isArabic = isArabicNumeral(operand1) && isArabicNumeral(operand2);
        if (!isRoman && !isArabic) {
            throw new IllegalArgumentException("используются одновременно разные системы счисления или входные параметры за пределами от 0 до 10");
        }

        int num1, num2;
        if (isRoman) {
            num1 = convertRomanToArabic(operand1);
            num2 = convertRomanToArabic(operand2);
        } else {
            num1 = Integer.parseInt(operand1);
            num2 = Integer.parseInt(operand2);
        }

        // Вычисление результата
        int result = switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> {
                if (num2 == 0) {
                    throw new ArithmeticException("Деление на ноль недопустимо");
                }
                yield num1 / num2;
            }
            default -> throw new IllegalArgumentException("Неверная арифметическая операция");
        };

        // Возврат результата в нужном формате (арабские или римские цифры)
        if (isRoman) {
            return convertArabicToRoman(result);
        } else {
            return Integer.toString(result);
        }
    }

    private static boolean isRomanNumeral(String input) {
        return input.matches("^(I)+$|^(IV)$|^(V)$|^(VI)$|^(VII)$|^(VIII)$|^(IX)$|^(X)$");
    }

    private static boolean isArabicNumeral(String input) {
        return input.matches("^[1-9]$|^(10)$");
    }

    private static int convertRomanToArabic(String input) {
        int result = 0;
        int prevValue = 0;

        for (int i = input.length() - 1; i >= 0; i--) {
            char currentChar = Character.toUpperCase(input.charAt(i));
            int currentValue = getRomanNumeralValue(currentChar);

            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }

    private static String convertArabicToRoman(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("т.к. в римской системе нет отрицательных чисел и нуля");
        }

        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Integer> entry : ROMAN_NUMERALS.entrySet()) {
            String romanSymbol = entry.getKey();
            int arabicValue = entry.getValue();

            while (number >= arabicValue) {
                result.append(romanSymbol);
                number -= arabicValue;
            }

            if (number == 0) {
                break;
            }
        }

        return result.toString();
    }

    private static final Map<String, Integer> ROMAN_NUMERALS = new LinkedHashMap<>();
    static {

        ROMAN_NUMERALS.put("C", 100);
        ROMAN_NUMERALS.put("XC", 90);
        ROMAN_NUMERALS.put("L", 50);
        ROMAN_NUMERALS.put("XL", 40);
        ROMAN_NUMERALS.put("X", 10);
        ROMAN_NUMERALS.put("IX", 9);
        ROMAN_NUMERALS.put("V", 5);
        ROMAN_NUMERALS.put("IV", 4);
        ROMAN_NUMERALS.put("I", 1);
    }
    private static int getRomanNumeralValue(char romanSymbol) {
        String symbol = String.valueOf(romanSymbol);
        if (!ROMAN_NUMERALS.containsKey(symbol)) {
            throw new IllegalArgumentException("Недопустимый символ римской цифры: " + romanSymbol);
        }
        return ROMAN_NUMERALS.get(symbol);
    }
}
