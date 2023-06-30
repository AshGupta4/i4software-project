package Server;

import Exceptions.CourseNotFoundException;
import Exceptions.DuplicateCourseException;
import Exceptions.InvalidCourseException;
import Profile.Account;
import Profile.AccountDatabase;
import Course.Course;
import Course.Submission;
import Quiz.Quiz;
import Quiz.Question;
import Quiz.QuizFileImporter;

import java.io.*;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Arrays;

import static Course.Course.loadCourses;
import static Course.Submission.loadSubmissions;

/**
 * Connection
 *
 * An instance of a client connection to the server
 *
 * @author Kyle Harvey, L-14
 * @author Grant Rivera, L-14
 * @version Nov 29, 2021
 */
public class HandleConnection implements Runnable {

    Socket socket;
    Object gate = new Object();

    public HandleConnection(Socket socket) {

        this.socket = socket;

    }

    @Override
    public void run() {

        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream());
            boolean done = false;

            while(!done) {

                synchronized (gate) {
                    // Loading in accounts, quizzes, courses, submissions
                    try {
                        AccountDatabase.loadAccounts();
                    } catch (FileNotFoundException e) {
                        File file = new File(AccountDatabase.filename);
                        try {
                            if (!file.createNewFile()) {
                                throw new IOException();
                            }
                            ;
                        } catch (IOException ioe) {
                            // File name could already be taken
                            // ServerLog.write(e.getStackTrace().toString());
                        }
                    }

                    loadCourses();
                    for (Course c : Course.getCourses()) {
                        Quiz.loadQuizzes(c);
                    }
                    loadSubmissions();
                }

                String[] info = input.readLine().split(",");

                if(info.length == 0) {
                    break;
                }

                if(info[0].equals("done")) {

                    done = true;

                } else if(info[0].equals("create account")) {

                    if(info.length != 4) {
                        writeFormatError(output);
                        continue;
                    }
                    boolean isTeacher = Boolean.parseBoolean(info[1]);
                    String user = info[2];
                    String pw = info[3];
                    if(AccountDatabase.findUsername(user) == null) {
                        Account account = new Account(user, pw, isTeacher);
                        AccountDatabase.add(account);
                        output.write("success");
                    } else {
                        output.write("duplicate error");
                    }
                    output.println();
                    output.flush();

                } else if(info[0].equals("delete account")) {

                    if(info.length != 2) {
                        writeFormatError(output);
                        continue;
                    }

                    String user = info[1];

                    if(AccountDatabase.findUsername(user) != null) {
                        AccountDatabase.remove(user);
                        output.write("success");
                    } else {
                        output.write("account not found error");
                    }
                    output.println();
                    output.flush();

                } else if(info[0].equals("find account")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    String user = info[1];
                    String pw = info[2];

                    if(AccountDatabase.findUsername(user) == null) {
                        output.write("Invalid username. Use teacher or student");
                    } else if(AccountDatabase.findUsername(user).isTeacher()) {
                        if(AccountDatabase.checkPassword(user, pw)) {
                            output.write("successTeacher");
                        } else {
                            output.write("invalid password");
                        }
                    } else if(AccountDatabase.findUsername(user).isStudent()) {
                        if(AccountDatabase.checkPassword(user, pw)) {
                            output.write("successStudent");
                        } else {
                            output.write("invalid password");
                        }
                    }
                    output.println();
                    output.flush();

                } else if(info[0].equals("create course")) {

                    if(info.length != 2) {
                        writeFormatError(output);
                        continue;
                    }
                    String courseName = info[1];
                    try {
                        new Course(courseName);
                        output.write("success");
                    } catch (DuplicateCourseException e) {
                        output.write("duplicate error");
                    } catch (InvalidCourseException e) {
                        output.write("invalid error 1");
                    } finally {
                        output.println();
                        output.flush();
                    }

                } else if(info[0].equals("get courses")) {

                    if(info.length != 1) {
                        writeFormatError(output);
                        continue;
                    }
                    StringBuilder courseList = new StringBuilder();
                    ArrayList<Course> courses = Course.getCourses();
                    for(Course c : courses) {
                        courseList.append(c.getName());
                        courseList.append(",");
                    }
                    courseList.delete(courseList.length(), courseList.length() + 1);
                    if(courseList.length() == 0) {
                        output.write("No courses");
                    } else {
                        output.write(courseList.toString());
                    }
                    output.println();
                    output.flush();

                } else if(info[0].equals("delete course")) {

                    if(info.length != 2) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 2");
                        output.println();
                        output.flush();
                        continue;
                    }

                    boolean success = Course.deleteCourse(c);
                    if(success) {
                        output.write("success");
                    } else {
                        output.write("failed");
                    }

                    output.println();
                    output.flush();

                } else if (info[0].equals("create quiz")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 3");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = new Quiz(c, info[2]);
                    c.addQuiz(q);
                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("import quiz")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 4");
                        output.println();
                        output.flush();
                        continue;
                    }

                    // TODO file import quiz
                    String status = QuizFileImporter.parseQuizQuestionString(info[2], c);

                    output.write(status);
                    output.println();
                    output.flush();

                } else if(info[0].equals("delete quiz")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 5");
                        output.println();
                        output.flush();
                        continue;
                    }

                    c.removeQuiz(c.findQuiz(info[2]));
                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("get quizzes")) {

                    if(info.length != 2) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 6");
                        output.println();
                        output.flush();
                        continue;
                    }

                    ArrayList<Quiz> quizzes = c.getQuizzes();
                    StringBuilder quizString = new StringBuilder();
                    for(Quiz q : quizzes) {
                        quizString.append(q.getQuizName());
                        quizString.append(",");
                    }
                    if(quizString.length() == 0) {
                        output.write("No quizzes");
                    } else {
                        output.write(quizString.substring(0, quizString.length() - 1));
                    }
                    output.println();
                    output.flush();

                } else if(info[0].equals("get quiz randomized")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 7");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null) {
                        output.write("invalid error 8");
                        output.println();
                        output.flush();
                        continue;
                    }

                    if(q.getRandomize()) {
                        output.write("random");
                    } else {
                        output.write("in order");
                    }
                    output.println();
                    output.flush();

                } else if(info[0].equals("quiz set randomized")) {

                    if(info.length != 4) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 9");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null) {
                        output.write("invalid error 10");
                        output.println();
                        output.flush();
                        continue;
                    }

                    if(info[3].equals("true")) {
                        q.setRandomize(true);
                    } else {
                        q.setRandomize(false);
                    }

                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("create question")) {

                    if(info.length != 5) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 11");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null || info[3] == null || info[4] == null) {
                        output.write("invalid error 12");
                        output.println();
                        output.flush();
                        continue;
                    }

                    ArrayList<String> answers = new ArrayList<>();
                    for(String s : info[4].split(";")) {
                        answers.add(s);
                    }

                    Question question = new Question(info[3], answers);
                    q.addQuestion(question);

                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("modify question")) {

                    if(info.length != 6) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 13");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null || info[3] == null || info[4] == null) {
                        output.write("invalid error 14");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Question question = q.getQuestion(info[3]);
                    ArrayList<String> answers = question.getAnswers();
                    q.removeQuestion(question);
                    q.addQuestion(new Question(info[4], answers));

                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("delete question")) {

                    if(info.length != 4) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 15");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null || info[3] == null) {
                        output.write("invalid error 16");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Question question = q.getQuestion(info[3]);
                    q.removeQuestion(question);

                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("list questions")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 17");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null) {
                        output.write("invalid error 18");
                        output.println();
                        output.flush();
                        continue;
                    }

                    StringBuilder questions = new StringBuilder();

                    for(Question question : q.getQuestions()) {

                        questions.append(question.getHead());
                        questions.append(":");
                        for(String s : question.getAnswers()) {
                            questions.append(s);
                            questions.append(";");
                        }
                        questions.deleteCharAt(questions.length() - 1);
                        questions.append(",");

                    }

                    if(questions.length() == 0) {
                        output.write("no questions");
                    } else {
                        output.write(questions.substring(0, questions.length() - 1));
                    }

                    output.println();
                    output.flush();

                } else if(info[0].equals("submit quiz")) {

                    if(info.length != 5) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 19");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null) {
                        output.write("invalid error 20");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Account a = AccountDatabase.findUsername(info[3]);
                    if(a == null) {
                        output.write("invalid error 21");
                        output.println();
                        output.flush();
                        continue;
                    }

                    String[] answerInfo = info[4].split(";");
                    new Submission(c, q, answerInfo, a, System.currentTimeMillis());

                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("get submissions by quiz")) {

                    if(info.length != 3) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 22");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null) {
                        output.write("invalid error 23");
                        output.println();
                        output.flush();
                        continue;
                    }

                    StringBuilder res = new StringBuilder();
                    ArrayList<Submission> submissions = Submission.getSubmissionsByQuiz(q.getQuizName());

                    if(submissions.size() == 0) {
                        output.write("no submissions");
                    } else {
                        for (Submission s : submissions) {

                            res.append(s.getStudent().getUsername());
                            res.append(":");

                            for (String a : s.getAnswers()) {

                                res.append(a);
                                res.append(",");

                            }

                            res.deleteCharAt(res.length() - 1);
                            res.append(";");

                        }
                    }
                    output.write(res.substring(0, res.length() - 1));
                    output.println();
                    output.flush();

                } else if(info[0].equals("add grades")) {

                    if(info.length != 5) {
                        writeFormatError(output);
                        continue;
                    }

                    Course c = Course.findCourse(info[1]);
                    if(c == null) {
                        output.write("invalid error 26");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Quiz q = c.findQuiz(info[2]);
                    if(q == null) {
                        output.write("invalid error 27");
                        output.println();
                        output.flush();
                        continue;
                    }

                    Account a = AccountDatabase.findUsername(info[3]);
                    if(a == null) {
                        output.write("invalid error 28");
                        output.println();
                        output.flush();
                        continue;
                    }

                    ArrayList<Submission> submissions = Submission.getSubmissionsByStudent(info[3]);
                    String[] gradeString = info[4].split(";");

                    for(Submission s : submissions) {
                        if(s.getQuiz().getQuizName().equals(info[2]) && s.getCourse().getName().equals(info[1])) {
                            for(int i = 0; i < s.getAnswers().length; i++) {
                                try {
                                    s.addGrade(Integer.parseInt(gradeString[i]), i);
                                } catch (NumberFormatException nfe) {
                                    s.addGrade(0, i);
                                }
                            }
                        }
                    }

                    output.write("success");
                    output.println();
                    output.flush();

                } else if(info[0].equals("get grades")) {

                    if(info.length != 2) {
                        writeFormatError(output);
                        continue;
                    }

                    Account a = AccountDatabase.findUsername(info[1]);
                    if(a == null) {
                        output.write("invalid error 28");
                        output.println();
                        output.flush();
                        continue;
                    }

                    ArrayList<Submission> submissions = Submission.getSubmissionsByStudent(a.getUsername());
                    if(submissions.size() == 0) {
                        output.write("null");
                        output.println();
                        output.flush();
                        continue;
                    }
                    StringBuilder gradeString = new StringBuilder();

                    for(Submission s : submissions) {

                        gradeString.append(s.getQuiz().getQuizName());
                        gradeString.append(",");
                        for(int i : s.getGrade()) {
                            gradeString.append(i);
                            gradeString.append(",");
                        }
                        gradeString.deleteCharAt(gradeString.length() - 1);
                        gradeString.append(";");

                    }

                    String gradeInfo = gradeString.substring(0, gradeString.length() - 1);
                    output.write(gradeInfo);
                    output.println();
                    output.flush();

                }

                synchronized (gate) {
                    AccountDatabase.outputAccounts();
                    Course.writeCourses();
                    for(Course c : Course.getCourses()) {
                        c.clearQuizzes();
                        c.writeQuizzes();
                    }
                    Submission.clearSubmissions();
                    Submission.writeSubmissions();
                }
            }

            // FOR TESTING
            /*
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream());

            String username = input.readLine();
            String password = input.readLine();
            System.out.println("Account details received");

            if (AccountDatabase.checkPassword(username, password)) {
                output.write("true");
            } else {
                output.write("false");
            }

            output.println();
            output.flush();
            output.close();
            input.close();
            */

        } catch (IOException ioe) {

            // ServerLog.write("Error receiving information from client");
            // ServerLog.write(ioe.getStackTrace().toString());

        }

    }

    private void writeFormatError(PrintWriter output) {

        output.write("format error");
        output.println();
        output.flush();

    }

}
