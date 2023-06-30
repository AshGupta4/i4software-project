package Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * ServerLog
 *
 * logs server activity
 *
 * @author Kyle Harvey, L-14
 * @version December 12, 2021
 */
public class ServerLog {
    private static String filename = "serverLog.txt";


    public static void write(String message) {
        try {
            File file = new File(filename);
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.write(message);
            printWriter.close();
            fileOutputStream.close();

        } catch (Exception e) {

        }
    }
}

