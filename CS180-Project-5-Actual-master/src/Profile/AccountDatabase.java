package Profile;

import java.io.*;
import java.util.ArrayList;

/**
 * AccountDatabase.java
 *
 * Represents an account database, containing teachers and students. You need to load at the beginning, but
 * everything automatically saves
 *
 * @author Kyle Harvey, L-14
 *
 * @version 11-14-21
 *
 */
public class AccountDatabase {
    private static ArrayList<Account> accounts = new ArrayList<>();
    public static String filename = "Accounts.txt";

    /**
     *
     * @param account
     */
    public static void add(Account account) {
        synchronized (accounts) {
            accounts.add(account);
            try {
                outputAccounts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     *
     * @param account
     */
    public static void remove(Account account) {
        synchronized (accounts) {
            accounts.remove(account);

            try {
                outputAccounts();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public static boolean checkPassword(String username, String password) {
        synchronized (accounts) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).checkPassword(username, password)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     *
     * @param usernameString
     */
    public static void remove(String usernameString) {
        synchronized (accounts) {
            remove(findUsername(usernameString));
        }
    }

    /**
     *
     * @param username
     * @return
     */
    public static Account findUsername(String username) {
        synchronized (accounts) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getUsername().equals(username)) {
                    return accounts.get(i);
                }
            }
            return null;
        }
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public static void loadAccounts() throws FileNotFoundException {
        synchronized (accounts) {
            ArrayList<String> lines = new ArrayList<>();

            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            try {
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = bufferedReader.readLine();

                }
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            loadAccounts(lines);
        }
    }

    /**
     *
     * @param strings
     */
    public static void loadAccounts(ArrayList<String> strings) {
        synchronized (accounts) {
            for (int i = 0; i < strings.size(); i++) {
                accounts.add(new Account(strings.get(i)));
            }
        }
    }

    /**
     *
     * @return
     */
    public static ArrayList<String> arrayListOutputAccounts() {
        synchronized (accounts) {
            ArrayList<String> accountStrings = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                accountStrings.add(accounts.get(i).toString());
            }
            return accountStrings;
        }
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public static void outputAccounts() throws FileNotFoundException {
        synchronized (accounts) {
            File file = new File(filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            //ArrayList<String> lines = arrayListOutputAccounts();
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String> accountsList = arrayListOutputAccounts();
            for (int i = 0; i < accountsList.size(); i++) {
                if (!lines.contains(accountsList.get(i))) {
                    lines.add(accountsList.get(i));
                }
            }

            try {

                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                for (int i = 0; i < lines.size(); i++) {
                    printWriter.println(lines.get(i));
                }
                printWriter.close();
                fileOutputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
