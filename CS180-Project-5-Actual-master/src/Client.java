import Course.Course;
import GUI.GuiWindow;
import Client.PacketHandler;
import Profile.AccountCreator;
import Profile.AccountDatabase;
import Quiz.Quiz;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static Course.Course.loadCourses;
import static Course.Course.writeCourses;
import static Course.Submission.loadSubmissions;
import static Course.Submission.writeSubmissions;

/**
 * Client - Runs the main method which logs user into account, then allows to create, modify or take a quiz.
 *
 * @author Gabriel I.
 * @version 11/6/2021
 */

public class Client {
    public static void main(String[] args) {

        //Create packet handler
        PacketHandler packetHandler = new PacketHandler();

        //GUI TESTING
        SwingUtilities.invokeLater(new GuiWindow(packetHandler));
    }
}
