import java.util.InputMismatchException;
import java.util.Scanner;
public class Calculator {
    public static void main(String[] args) throws InputMismatchException {
        // получение вводных данных от пользователя
        System.out.println("Введите выражение с двумя числами: римскими или арабскими, " +
                "а также арифметическим действием: + - * или /.");
        Scanner scanner = new Scanner(System.in);
        String userInputInitial = scanner.nextLine();
        String userInputSqueezed = userInputInitial.replaceAll("\\s", "");// удаление из строки всех пробелов
        String userInput = userInputSqueezed.toUpperCase();

        // проверка на корректность ввода знака, нужно исключить, что знак операции является последним символом в строке
        int a = userInput.length()-1;
        if(userInput.charAt(a)=='+'||userInput.charAt(a)=='-'||userInput.charAt(a)=='*'||userInput.charAt(a)=='/'){
            throw new InputMismatchException("Некорректный ввод знака операции.");
        }

        // передача вводных данных в метод calc, вывод результата работы метода calc
        System.out.println(Calculation.calc(userInput));
    }
}
class Conversions{
    // массив нужных для данного калькулятора римских чисел, ненужные - нули;
    static String [] romanNumbers = new String[] {"0","I","II","III","IV","V","VI","VII","VIII","IX","X",
            "XI","XII","XIII","XIV","XV","XVI","XVII","XVIII","XIX","XX","XXI","0","0","XXIV","XXV","0","XXVII","XXVIII","0","XXX",
            "0","XXXII","0","0","XXXV","XXXVI","0","0","0","XL","0","XLII","0","0","XLV","0","0","XLVIII","XLIX","L",
            "0","0","0","LIV","0","LVI","0","0","0","LX","0","0","LXIII","LXIV","0","0","0","0","0","LXX",
            "0","LXXII","0","0","0","0","0","0","0","LXXX","LXXXI","0","0","0","0","0","0","0","0","XC",
            "0","0","0","0","0","0","0","0","0","C"};
    static boolean isitRoman(String value){
        for(int i=0; i< romanNumbers.length;i++){
            if(value.equals(romanNumbers[i])) {
                return true;
            }
        }
        return false;
    }
    static int convertToArabic(String roman){
        for(int i=0; i<romanNumbers.length;i++){
            if(roman.equals(romanNumbers[i])){
                return i;
            }
        }
        return-1;
    }
    static String convertToRoman(int a) {
        return romanNumbers[a];
    }
}
class Correspondence{
    // массив - шаблон, содержащий все возможные корректные строки, подлежащие расчету;
    static String[] sampleArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "I", "II", "III", "IV", "V",
            "VI", "VII", "VIII", "IX", "X"};
    static boolean check(String v){
        for(int i=0; i< sampleArray.length;i++){
            if(v.equals(sampleArray[i])) {
                return true;
            }
        }
        return false;
    }
}
class Calculation{
    static String calc(String userInput) {
        // разделение строки ввода пользователя на 2 части, формирование массива класса String из двух элементов
        String[] numbersToCalculate = userInput.split("[+/*-]");

        // проверка через массив sampleArray, что каждая из введенных строк соответствует строке, подлежащей обработке
        if (!Correspondence.check(numbersToCalculate[0]) || !Correspondence.check(numbersToCalculate[1])) {
            throw new InputMismatchException("Введены некорректные данные или числа вне требуемого диапазона.");
        }
        // проверка, что количество операндов соответствует двум
        if (numbersToCalculate.length != 2)
            throw new InputMismatchException("Количество операндов не соответствует 2.");

        // проверка, 1) римские ли оба числа, 2) арабские ли оба числа, а если ни то и ни другое - выброс исключения;
        boolean isRoman;
        int number1, number2;
        if (Conversions.isitRoman(numbersToCalculate[0]) && Conversions.isitRoman(numbersToCalculate[1])) {
            number1 = Conversions.convertToArabic(numbersToCalculate[0]);
            number2 = Conversions.convertToArabic(numbersToCalculate[1]);
            isRoman = true;
        } else if (!Conversions.isitRoman(numbersToCalculate[0]) && !Conversions.isitRoman(numbersToCalculate[1])) {
            // привидение переменных класса String из массива в тип int
            number1 = Integer.parseInt(numbersToCalculate[0]);
            number2 = Integer.parseInt(numbersToCalculate[1]);
            isRoman = false;
        } else throw new InputMismatchException("Введены числа в разных форматах.");

        // создание массива, содержащего знаки операций
        String[] signs = {"+", "-", "*", "/"};
        // выявление арифметического действия внутри userInput, маркер - индекс элемента массива signs
        int sign = 4;
        for (int i = 0; i < signs.length; i++) {
            if (userInput.contains(signs[i])) {
                sign = i;
                break;
            }
        }
        // вычисление результата
        int arabicResult;
        switch (sign) {
            case 0:
                arabicResult = number1 + number2;
                break;
            case 1:
                arabicResult = number1 - number2;
                break;
            case 2:
                arabicResult = number1 * number2;
                break;
            case 3:
                arabicResult = number1 / number2;
                break;
            default:
                arabicResult = 0;
        }
        // проверка, что при вычислении римских чисел результат - положительный
        if (isRoman && arabicResult < 1)
            throw new InputMismatchException("Результат вычисления римских чисел - меньше 1.");

        // если ввод был римскими, то конвертация результата в римское значение типа String, возврат результата в main
        if (isRoman) {
            String romanResult = (Conversions.convertToRoman(arabicResult));
            userInput = romanResult;
            return userInput;
        }
        // если ввод был арабскими - конвертация арабского результата в тип String, возврат результата в main
        else {
            String arabicResultString = String.valueOf(arabicResult);
            userInput = arabicResultString;
            return userInput;
        }
    }
}