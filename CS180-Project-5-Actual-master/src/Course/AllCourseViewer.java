package Course;

import Exceptions.DuplicateCourseException;
import Exceptions.InvalidCourseException;
import Profile.Account;
import Profile.AccountDatabase;
import UI.TerminalInteraction;

import java.util.Scanner;

/**
 * AllCourseViewer - Prints terminal UI to view available courses to create, open, or delete courses
 *
 * @author Gabriel Iskandar
 * @version 11/12/2021
 */
public class AllCourseViewer {
    private static String courseStudentOptionsPrompt = "Enter an action: \n 1) Open a course \n 2) Delete account \n " +
            "3) View Grades \n 4) Quit program";
    private static String courseOpenPrompt = "Enter a course number to open it: ";
    private static String noCourseError = "There are no courses available, please quit and return later.";
    private static String invalidCourseError = "Invalid course! Please enter a number from 1 - ";
    private static String courseTeacherOptionsPrompt = "Enter an action: \n 1) Create a course \n " +
            "2) Edit a course \n 3) Delete a course \n 4) Delete account \n 5) Quit program";
    private static String courseCreatePrompt = "Enter a course name to create: ";
    private static String courseEditPrompt = "Enter a course number to edit it: ";
    private static String courseDeletePrompt = "Enter a course number to delete it: ";
    private static String noCourseTeacherError = "There are no courses available, please create one.";

    private Account account;

    public AllCourseViewer(Account account) {
        this.account = account;
    }

    /**
     * Prints a list of courses.
     * Returns if no courses exist.
     *
     * @return
     */
    public static String generateCourseList() {
        String courseList = "";
        if (Course.getCourses().size() >= 1) {
            for (int i = 0; i < Course.getCourses().size(); i++) {
                courseList += "\n" + String.format(" %s) %s", i + 1, Course.getCourses().get(i));
            }
        }
        return courseList;
    }

    /**
     * Loops through the student actions until user quits.
     *
     * @param scanner
     */
    public void openStudent(Scanner scanner) {
        while (chooseStudentAction(scanner));
    }

    /**
     * Prompts user to choose to open a course or quit the program.
     * Returns if the user wants to continue or quit.
     *
     * @param scanner
     * @return
     */
    public boolean chooseStudentAction(Scanner scanner) {
        int action = TerminalInteraction.readAction(scanner, courseStudentOptionsPrompt, 4);
        if (action == 1) {
            viewStudentCourses(scanner);
            return true;
        } else if (action == 2) {
            AccountDatabase.remove(account);
            return false;
        } else if (action == 3) {
            viewGrades(scanner);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prompts user to choose a course from a list of courses.
     * If no course exists, prompts user to quit.
     *
     * @param scanner
     */
    public void viewStudentCourses(Scanner scanner) {
        String courseList = generateCourseList();
        if (courseList.equals("")) {
            System.out.println(noCourseError);
        } else {
            int action = TerminalInteraction.readAction(scanner, courseOpenPrompt + generateCourseList(),
                    Course.getCourses().size(), invalidCourseError);
            CourseQuizViewer courseQuizViewer = new CourseQuizViewer(Course.getCourses().get(action - 1), account);
            courseQuizViewer.openStudentCourse(scanner);
        }
    }

    public void viewGrades(Scanner scanner) {
        System.out.println(Submission.getSubmissionsByStudent(account.getUsername()));
        System.out.println("Press enter to continue.");
        scanner.nextLine();
    }

    /**
     * Loops through teacher actions until user quits.
     *
     * @param scanner
     */
    public void openTeacher(Scanner scanner) {
        while (chooseTeacherAction(scanner));
    }

    /**
     * Prompts user to choose to create, edit, or delete a course or quit the program.
     * Returns if the user wants to continue or quit.
     *
     * @param scanner
     * @return
     */
    public boolean chooseTeacherAction(Scanner scanner) {
        int action = TerminalInteraction.readAction(scanner, courseTeacherOptionsPrompt, 5);
        if (action == 1) {
            createCourse(scanner);
            return true;
        } else if (action == 2) {
            viewTeacherCourses(scanner);
            return true;
        } else if (action == 3) {
            deleteCourse(scanner);
            return true;
        } else if (action == 4) {
            AccountDatabase.remove(account);
            return false;
        } else {
            return false;
        }
    }

    /**
     * Prompts user for a name for new course.
     * Invalid or taken names will prompt the user again.
     *
     * @param scanner
     */
    public void createCourse(Scanner scanner) {
        System.out.println(courseCreatePrompt);
        String courseName = scanner.nextLine();

        while (courseName.equals("") || Course.findCourse(courseName) != null) {
            if (courseName.equals("")) {
                System.out.println("Invalid course name! Please enter a valid course name:");
            } else {
                System.out.println("Course name already taken! Please choose another course name:");
            }
            courseName = scanner.nextLine();
        }

        try {
            CourseQuizViewer courseQuizViewer = new CourseQuizViewer(new Course(courseName), account);
            courseQuizViewer.openTeacherCourse(scanner);
        } catch (DuplicateCourseException e) {
            System.out.println(e.getMessage());
        } catch (InvalidCourseException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prompts user to choose a course from a list of courses.
     * If no course exists, prompts user to quit.
     *
     * @param scanner
     */
    public void viewTeacherCourses(Scanner scanner) {
        String courseList = generateCourseList();
        if (courseList.equals("")) {
            System.out.println(noCourseTeacherError);
        } else {
            int action = TerminalInteraction.readAction(scanner, courseEditPrompt + generateCourseList(),
                    Course.getCourses().size(), invalidCourseError);
            CourseQuizViewer courseQuizViewer = new CourseQuizViewer(Course.getCourses().get(action - 1), account);
            courseQuizViewer.openTeacherCourse(scanner);
        }
    }

    /**
     * Prompts user to choose a course from a list of courses to delete.
     * If no course exists, prompts user to quit.
     *
     * @param scanner
     */
    public void deleteCourse(Scanner scanner) {
        String courseList = generateCourseList();
        if (courseList.equals("")) {
            System.out.println(noCourseTeacherError);
        } else {
            int action = TerminalInteraction.readAction(scanner, courseDeletePrompt + generateCourseList(),
                    Course.getCourses().size(), invalidCourseError);
            Course.deleteCourse(Course.getCourses().get(action - 1));
        }
    }
}
