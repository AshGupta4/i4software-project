package Server;

import Course.Course;
import Profile.Account;
import Profile.AccountDatabase;
import Quiz.Quiz;

import static Course.Course.loadCourses;
import static Course.Submission.loadSubmissions;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Server
 *
 * A multithreaded server
 *
 * @author Kyle Harvey, L-14
 * @author Grant Rivera, L-14
 *
 * @version Dec 3, 2021
 */
public class Server {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(2424);
            while (true) {
                ServerLog.write("Waiting");
                Socket socket = serverSocket.accept();
                ServerLog.write("Accepted");
                HandleConnection handleConnection = new HandleConnection(socket);
                new Thread(handleConnection).start();
                ServerLog.write("Thread Started");
            }
        } catch (IOException e) {
            ServerLog.write(Arrays.toString(e.getStackTrace()));
        }

    }

}
