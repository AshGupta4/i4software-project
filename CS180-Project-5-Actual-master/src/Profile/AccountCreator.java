package Profile;

import Course.AllCourseViewer;

import java.util.Scanner;

/**
 * AccountCreator - Displays terminal interaction for creating an account
 * Option to sign-up or log-in. Account is stored in account variable
 *
 * @author Gabriel Iskandar
 * @version 11/11/2021
 */
public class AccountCreator {
    private static String welcomeMessage = "Welcome to Better Than Brightspace!";
    private static String connectPrompt = "Would you like to: \n (1) Sign-up\n (2) Log-in";
    private static String signUpMessage = "\nSign Up";
    private static String usernamePrompt = "Username:";
    private static String passwordPrompt = "Password:";
    private static String invalidUsernameError = "Invalid username! Username must be 8-20 characters " +
            "and cannot contain whitespace.";
    private static String usernameTakenError = "Username taken! Please choose a different username.";
    private static String invalidPasswordError = "Invalid password! Username must be 4 characters or longer " +
            "and cannot contain whitespace.";
    private static String rolePrompt = "Select an account role: \n (1) Teacher\n (2) Student";
    private static String signUpSuccess = "Successfully signed up!";
    private static String usernameNotFoundError = "Username not found! Please re-enter your username.";
    private static String incorrectPasswordError = "Incorrect password! Please retry.";
    private static String logInSuccess = "Successfully logged in!";
    private static String quitMessage = "Thanks for using Better Than Brightspace! See you next time.";

    private Account account;

    /**
     * Welcomes user and asks for sign-up or log-in
     *
     * @param scanner
     */
    public void userConnect(Scanner scanner) {
        System.out.println(welcomeMessage);
        System.out.println(connectPrompt);
        String response = scanner.nextLine();
        while (!response.equals("1") && !response.equals("2")) {
            System.out.println("Invalid response.");
            System.out.println(connectPrompt);
            response = scanner.nextLine();
        }
        if (response.equals("1")) {
            signUp(scanner);
        } else if (response.equals("2")) {
            logIn(scanner);
        }

        if (account.isStudent()) {
            AllCourseViewer allCourseViewer = new AllCourseViewer(account);
            allCourseViewer.openStudent(scanner);
        } else if (account.isTeacher()) {
            AllCourseViewer allCourseViewer = new AllCourseViewer(account);
            allCourseViewer.openTeacher(scanner);
        }
        System.out.println(quitMessage);
    }

    /**
     * Prompts for a new username, password, and role
     * Checks for valid username and password
     * Creates new account and assigns to account
     *
     * @param scanner
     */
    public void signUp(Scanner scanner) {
        System.out.println(signUpMessage);

        System.out.println(usernamePrompt);
        String username = scanner.nextLine();
        boolean validName = isValidUsername(username);
        while (!validName || AccountDatabase.findUsername(username) != null) {
            if (!validName) {
                System.out.println(invalidUsernameError);
            } else {
                System.out.println(usernameTakenError);
            }
            System.out.println(usernamePrompt);
            username = scanner.nextLine();
            validName = isValidUsername(username);
        }

        System.out.println(passwordPrompt);
        String password = scanner.nextLine();
        boolean validPass = isValidPassword(password);
        while (!validPass) {
            System.out.println(invalidPasswordError);
            System.out.println(passwordPrompt);
            password = scanner.nextLine();
            validPass = isValidPassword(password);
        }

        System.out.println(rolePrompt);
        String role = scanner.nextLine();
        while (!role.equals("1") && !role.equals("2")) {
            System.out.println("Invalid response.");
            System.out.println(rolePrompt);
            role = scanner.nextLine();
        }
        if (role.equals("1")) {
            account = new Account(username, password, true);
        } else if (role.equals("2")) {
            account = new Account(username, password, false);
        }

        AccountDatabase.add(account);

        System.out.println(signUpSuccess);
    }

    /**
     * Prompts for username and password
     * Looks for account with Account, if found assigned to account
     *
     * @param scanner
     */
    public void logIn(Scanner scanner) {
        System.out.println(usernamePrompt);
        String username = scanner.nextLine();
        while (AccountDatabase.findUsername(username) == null) {
            System.out.println(usernameNotFoundError);
            System.out.println(usernamePrompt);
            username = scanner.nextLine();
        }

        System.out.println(passwordPrompt);
        String password = scanner.nextLine();
        while (!AccountDatabase.checkPassword(username, password)) {
            System.out.println(incorrectPasswordError);
            System.out.println(passwordPrompt);
            password = scanner.nextLine();
        }

        account = AccountDatabase.findUsername(username);

        System.out.println(logInSuccess);
    }

    /**
     * Returns if username is valid
     *
     * @param username
     * @return
     */
    public static boolean isValidUsername(String username) {
        return !username.contains(" ") && username.length() >= 8 && username.length() <= 20;
    }

    /**
     * Returns if password is valid
     *
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        return !password.contains(" ") && password.length() >= 4;
    }
}
