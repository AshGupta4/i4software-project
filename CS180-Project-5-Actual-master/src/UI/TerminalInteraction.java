package UI;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * TerminalInteraction - Takes care of common terminal interactions
 *
 * @author Gabriel Iskandar
 * @version 11/13/2021
 */
public class TerminalInteraction {
    private static String invalidActionError = "Invalid action! Please enter a number from 1 - ";
    private static String invalidNumberError = "Invalid number! Please enter a number.";

    /**
     * Reads a number action and returns it.
     * Has a default error message.
     *
     * @param scanner
     * @param actionPrompt
     * @param upperRange
     * @return
     */
    public static int readAction(Scanner scanner, String actionPrompt, int upperRange) {
        System.out.println(actionPrompt);
        int action = -1;
        try {
            action = scanner.nextInt();
        } catch (InputMismatchException exception) {
            action = -1;
            scanner.nextLine();
        }
        while (action < 1 || action > upperRange) {
            System.out.println(invalidActionError + upperRange);
            System.out.println(actionPrompt);
            try {
                action = scanner.nextInt();
            } catch (InputMismatchException exception) {
                action = -1;
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return action;
    }

    /**
     * Reads a number action and returns it.
     * Has on overridden error message.
     *
     * @param scanner
     * @param actionPrompt
     * @param upperRange
     * @param error
     * @return
     */
    public static int readAction(Scanner scanner, String actionPrompt, int upperRange, String error) {
        System.out.println(actionPrompt);
        int action = -1;
        try {
            action = scanner.nextInt();
        } catch (InputMismatchException exception) {
            action = -1;
            scanner.nextLine();
        }
        while (action < 1 || action > upperRange) {
            System.out.println(error + upperRange);
            System.out.println(actionPrompt);
            try {
                action = scanner.nextInt();
            } catch (InputMismatchException exception) {
                action = -1;
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return action;
    }

    public static int readNumber(Scanner scanner, String prompt) {
        System.out.println(prompt);
        int number = 0;
        boolean invalidNumber = true;
        while (invalidNumber) {
            System.out.println(prompt);
            try {
                number = scanner.nextInt();
                invalidNumber = false;
            } catch (InputMismatchException exception) {
                System.out.println(invalidNumberError);
                scanner.nextLine();
            }
        }
        scanner.nextLine();
        return number;
    }
}
