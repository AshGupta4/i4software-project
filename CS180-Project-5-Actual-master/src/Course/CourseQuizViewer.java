package Course;

import Exceptions.InvalidFileImportException;
import Profile.Account;
import Quiz.*;
import UI.TerminalInteraction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * CourseQuizViewer - View all quizzes in a course and create, open, or delete a quiz.
 *
 * @author Gabriel Iskandar, Benjamin W.
 * @version 11/13/2021
 */
public class CourseQuizViewer {
    private static String quizStudentOptionsPrompt = "Enter an action: \n 1) Take a quiz \n " +
            "2) Exit course";
    private static String quizOpenPrompt = "Enter a quiz number to take it: ";
    private static String noQuizError = "There are no quizzes available, please quit and return later.";
    private static String invalidQuizError = "Invalid quiz! Please enter a number from 1 - ";
    private static String quizTeacherOptionsPrompt = "Enter an action: \n 1) Create a quiz \n " +
            "2) Edit a quiz \n 3) Delete a quiz \n 4) Exit course";
    private static String quizCreatePrompt = "Enter a quiz name to create or enter '0' to import a quiz from a file: ";
    private static String quizPathnamePrompt = "Enter the pathname for the quiz you want to import: ";
    private static String quizEditPrompt = "Enter a quiz number to edit it: ";
    private static String quizDeletePrompt = "Enter a quiz number to delete it: ";
    private static String noQuizTeacherError = "There are no quizzes available, please create one.";

    private Course course;
    private Account account;

    /**
     * Creates a new CourseQuizViewer with a course
     *
     * @param course
     */
    public CourseQuizViewer(Course course, Account account) {
        this.course = course;
        this.account = account;
    }

    /**
     * Prints a list of quizzes in the course.
     * Returns if any quizzes exist in the course.
     *
     * @return
     */
    public String generateQuizList() {
        String quizList = "";
        if (course.getQuizzes().size() >= 1) {
            for (int i = 0; i < course.getQuizzes().size(); i++) {
                quizList += "\n" + String.format(" %s) %s", i+1, course.getQuizzes().get(i));
            }
        }
        return quizList;
    }

    /**
     * Loops through the student actions until user quits.
     *
     * @param scanner
     */
    public void openStudentCourse(Scanner scanner) {
        while (chooseStudentAction(scanner));
    }

    /**
     * Prompts user to choose to open a quiz or quit the program.
     * Returns if the user wants to continue or quit.
     *
     * @param scanner
     * @return
     */
    public boolean chooseStudentAction(Scanner scanner) {
        System.out.println(course.getName());
        int action = TerminalInteraction.readAction(scanner, quizStudentOptionsPrompt, 2);
        if (action == 1) {
            viewStudentQuizzes(scanner);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prompts user to choose a quiz from a list of quizzes.
     * If no quiz exists, prompts user to quit.
     *
     * @param scanner
     */
    public void viewStudentQuizzes(Scanner scanner) {
        String quizList = generateQuizList();
        if (quizList.equals("")) {
            System.out.println(noQuizError);
        } else {
            int action = TerminalInteraction.readAction(scanner, quizOpenPrompt + quizList,
                    course.getQuizzes().size(), invalidQuizError);
            QuizTaker quizTaker = new QuizTaker(course.getQuizzes().get(action - 1), account);
            quizTaker.takeQuiz(scanner);
        }
    }

    /**
     * Loops through teacher actions until user quits.
     *
     * @param scanner
     */
    public void openTeacherCourse(Scanner scanner) {
        while (chooseTeacherAction(scanner));
    }

    /**
     * Prompts user to choose to create, edit, or delete a quiz or quit the program.
     * Returns if the user wants to continue or quit.
     *
     * @param scanner
     * @return
     */
    public boolean chooseTeacherAction(Scanner scanner) {
        int action = TerminalInteraction.readAction(scanner, quizTeacherOptionsPrompt, 4);
        if (action == 1) {
            createQuiz(scanner);
            return true;
        } else if (action == 2) {
            viewTeacherQuizzes(scanner);
            return true;
        } else if (action == 3) {
            deleteQuiz(scanner);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prompts user for a name for new quiz.
     * Invalid or taken names will prompt the user again.
     *
     * @param scanner
     */
    public void createQuiz(Scanner scanner) {
        System.out.println(quizCreatePrompt);
        String quizName = scanner.nextLine();

        while (quizName.equals("") || course.findQuiz(quizName) != null) {
            if (quizName.equals("")) {
                System.out.println("Invalid quiz name! Please enter a valid quiz name:");
            } else {
                System.out.println("Quiz name already taken! Please choose another quiz name:");
            }
            quizName = scanner.nextLine();
        }

        if (quizName.length() == 1 && quizName.charAt(0) == '0') {
            System.out.println(quizPathnamePrompt);
            String pathname = scanner.nextLine();
            while (pathname.equals("") || pathname.equals(null)) {
                System.out.println("Invalid pathname! Please enter a valid pathname:");
                pathname = scanner.nextLine();
            }
            try{
                QuizFileImporter.readQuizQuestionFile(pathname, course);
            } catch (InvalidFileImportException e) {
                createQuiz(scanner);
            } catch (FileNotFoundException e) {
                System.out.println("Error! A file was not found from that pathname.");
                createQuiz(scanner);
            } catch (IOException e) {
                System.out.println("There was an error when reading from the file.");
                createQuiz(scanner);
            }
        } else {
            Quiz quiz = new Quiz(course, quizName);
            course.addQuiz(quiz);
            QuizEditor quizEditor = new QuizEditor(quiz);
            quizEditor.openTeacherActions(scanner);
        }
    }

    /**
     * Prompts user to choose a quiz from a list of quizzes.
     * If no quiz exists, prompts user to quit.
     *
     * @param scanner
     */
    public void viewTeacherQuizzes(Scanner scanner) {
        String quizList = generateQuizList();
        if (quizList.equals("")) {
            System.out.println(noQuizTeacherError);
        } else {
            int action = TerminalInteraction.readAction(scanner, quizEditPrompt + quizList,
                    course.getQuizzes().size(), invalidQuizError);
            QuizEditor quizEditor = new QuizEditor(course.getQuizzes().get(action - 1));
            quizEditor.openTeacherActions(scanner);
        }
    }

    /**
     * Prompts user to choose a quiz from a list of quizzes to delete.
     * If no quiz exists, prompts user to quit.
     *
     * @param scanner
     */
    public void deleteQuiz(Scanner scanner) {
        String quizList = generateQuizList();
        if (quizList.equals("")) {
            System.out.println(noQuizTeacherError);
        } else {
            int action = TerminalInteraction.readAction(scanner, quizDeletePrompt + quizList,
                    course.getQuizzes().size(), invalidQuizError);
            course.removeQuiz(course.getQuizzes().get(action - 1));
        }
    }
}
