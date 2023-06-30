package Client;

import Quiz.QuizFileImporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * PacketHandler
 *
 * Backend of the UI/client that can send and receive packets from the server
 *
 * @author Gabriel Iskandar, L-14
 * @author Grant Rivera, L-14
 * @version Dec 1, 2021
 */
public class PacketHandler {

    Socket socket;
    BufferedReader input;
    PrintWriter output;

    /**
     * PacketHandler constructor
     */
    public PacketHandler() {

        try {
            socket = new Socket("localhost", 2424);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection could not be established");
            ioe.printStackTrace();
        }

    }

    /**
     * Returns String depending on success, 'success' or any errors
     *
     * @param username
     * @param password
     * @param isTeacher
     * @return
     */
    public String createAccount(String username, String password, boolean isTeacher) {
        String allInput = username + password;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("create account,%s,%s,%s", isTeacher, username, password);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }
        return result;
    }

    /**
     * Returns 'success' if deletes account
     *
     * @param username
     * @param password
     * @return
     */
    public String deleteAccount(String username, String password, boolean isTeacher) {
        String allInput = username + password;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("delete account,%s",username);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }
        return result;
    }

    /**
     * Returns if successful and the role of account, 'successTeacher', 'successStudent', or any errors
     *
     * @param username
     * @param password
     * @return
     */
    public String findAccount(String username, String password) {
        String allInput = username + password;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("find account,%s,%s",username,password);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }
        return result;

    }

    /**
     * Returns whether successfully creates course
     * Throws exceptions for connection or creating course
     *
     * @param courseName
     * @return
     */
    public String createCourse(String courseName) {
        String allInput = courseName;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("create course,%s", courseName);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }
        return result;
    }

    /**
     * Returns whether successfully deletes course
     *
     * @param courseName
     * @return
     */
    public String deleteCourse(String courseName) {
        String allInput = courseName;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("delete course,%s", courseName);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }
        return result;

    }

    /**
     * Returns all published courses or 'No Courses'
     *
     * @return
     */
    public ArrayList<String> listCourses() {

        String info = String.format("get courses");
        output.write(info);
        output.println();
        output.flush();
        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }
        String[] courseList = result.split(",");
        ArrayList<String> courses = new ArrayList<>();
        courses.addAll(Arrays.asList(courseList));
        return courses;

    }

    /**
     * Returns 'success' if successfully created quiz
     *
     * @param courseName
     * @param quizName
     * @return
     */
    public String createQuiz(String courseName, String quizName) {
        String allInput = courseName + quizName;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("create quiz,%s,%s", courseName, quizName);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns 'success' if successfully imports quiz
     *
     * @param courseName
     * @param fileName
     * @return
     */
    public String importQuiz(String courseName, String fileName) {

        String fileString = QuizFileImporter.readQuizQuestionFile(fileName);
        if (fileString.equals("Invalid filepath!") || fileString.equals("Error reading the file!") ||
                fileString.contains(",") || fileString.contains(":") || fileString.contains(";")) {
            return fileString;
        }

        String info = String.format("import quiz,%s,%s", courseName, fileString);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns 'success' if successfully deleted quiz
     *
     * @param courseName
     * @param quizName
     * @return
     */
    public String deleteQuiz(String courseName, String quizName) {
        String allInput = courseName + quizName;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("delete quiz,%s,%s", courseName, quizName);
        
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns any quiz names in the course
     *
     * @param courseName
     * @return
     */
    public ArrayList<String> listQuizzes(String courseName) {

        String info = String.format("get quizzes,%s", courseName);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        String[] quizList = result.split(",");
        ArrayList<String> quizzes = new ArrayList<>();
        quizzes.addAll(Arrays.asList(quizList));
        return quizzes;

    }

    /**
     * Returns 'random' or 'in order'
     *
     * @param courseName
     * @param quizName
     * @return
     */
    public String getQuizRandomize(String courseName, String quizName) {
        String info = String.format("get quiz randomized,%s,%s", courseName, quizName);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns 'success' if changes quiz
     *
     * @param courseName
     * @param quizName
     * @param randomize
     * @return
     */
    public String setQuizRandomize(String courseName, String quizName, boolean randomize) {
        String allInput = courseName + quizName;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("quiz set randomized,%s,%s,%s", courseName, quizName, randomize);

        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;

    }

    /**
     * Returns 'success' if creates question
     *
     * @param courseName
     * @param quizName
     * @param questionHead
     * @param answers
     * @return
     */
    public String createQuestion(String courseName, String quizName, String questionHead, ArrayList<String> answers) {
        String allInput = courseName + quizName + questionHead;
        for (String answer: answers) {
            allInput += answer;
        }
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        StringBuilder info = new StringBuilder();
        info.append(String.format("create question,%s,%s,%s,", courseName, quizName, questionHead));
        for (String s : answers) {
            info.append(s);
            info.append(";");
        }

        output.write(info.substring(0, info.length() - 1));
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;

    }

    /**
     * Returns 'success' if modifies question, change oldQuestionHead with questionHead and answers
     *
     * @param courseName
     * @param quizName
     * @param oldQuestionHead
     * @param questionHead
     * @param answers
     * @return
     */
    public String modifyQuestion(String courseName, String quizName, String oldQuestionHead, String questionHead, ArrayList<String> answers) {
        String allInput = courseName + quizName + oldQuestionHead + questionHead;
        for (String answer: answers) {
            allInput += answer;
        }
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        StringBuilder info = new StringBuilder();
        info.append(String.format("modify question,%s,%s,%s,%s,", courseName, quizName, oldQuestionHead, questionHead));
        for (String s : answers) {
            info.append(s);
            info.append(";");
        }

        output.write(info.substring(0, info.length() - 1));
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;

    }

    /**
     * Returns 'success' if deletes question
     *
     * @param courseName
     * @param quizName
     * @param questionHead
     * @return
     */
    public String deleteQuestion(String courseName, String quizName, String questionHead) {
        String allInput = courseName + quizName + questionHead;
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        String info = String.format("delete question,%s,%s,%s", courseName, quizName, questionHead);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns arraylist of questions and answers
     * Each question and its answers are stored in a nested arraylist
     *
     * @param courseName
     * @param quizName
     * @return
     */
    public ArrayList<ArrayList<String>> listQuestions(String courseName, String quizName) {

        String info = String.format("list questions,%s,%s", courseName, quizName);

        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        if (result.equals("no questions")) {
            return null;
        }

        String[] questionArray = result.split(",");
        ArrayList<ArrayList<String>> questions = new ArrayList<>();

        for (String s : questionArray) {

            ArrayList<String> question = new ArrayList<>();
            if (s.contains(":")) {
                question.add(s.substring(0, s.indexOf(":")));
            } else {
                question.add(s);
            }

            String answerString = s.substring(s.indexOf(":") + 1);
            String[] answerArray = answerString.split(";");

            for (String t : answerArray) {

                question.add(t);

            }

            questions.add(question);

        }

        return questions;
    }

    /**
     * Returns 'success' if submits quiz
     *
     * @param courseName
     * @param quizName
     * @param answers
     * @return
     */
    public String submitQuiz(String courseName, String quizName, ArrayList<String> answers, String accountName) {
        String allInput = courseName + quizName + accountName;
        for (String answer: answers) {
            allInput += answer;
        }
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        StringBuilder info = new StringBuilder();
        info.append(String.format("submit quiz,%s,%s,%s,", courseName, quizName, accountName));
        for (String s : answers) {
            info.append(s);
            info.append(";");
        }

        output.write(info.substring(0, info.length() - 1));
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns the total submissions for the quiz
     *
     * @param courseName
     * @param quizName
     * @return
     */
    public ArrayList<ArrayList<String>> getSubmissions(String courseName, String quizName) {

        // by quiz
        String info = String.format("get submissions by quiz,%s,%s", courseName, quizName);
        output.write(info);
        output.println();
        output.flush();

        ArrayList<ArrayList<String>> res = new ArrayList<>();
        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        String[] userSubmissions = result.split(";");
        for (String s : userSubmissions) {
            ArrayList<String> individualSubmission = new ArrayList<>();
            String studentUsername = s.substring(0, s.indexOf(":"));
            individualSubmission.add(studentUsername);
            String submissionInfo = s.substring(s.indexOf(":") + 1);
            for (String a : submissionInfo.split(",")) {
                individualSubmission.add(a);
            }
            res.add(individualSubmission);
        }

        return res;

    }

    public String submitSubmission(String courseName, String quizName, ArrayList<String> submission, String studentName) {
        String allInput = courseName + quizName + studentName;
        for (String sub: submission) {
            allInput += sub;
        }
        if (allInput.contains(";") || allInput.contains(":") || allInput.contains(",")) {
            return "Please use alphanumeric!";
        }

        StringBuilder info = new StringBuilder();
        info.append("add grades,").append(courseName).append(",");
        info.append(quizName).append(",").append(studentName).append(",");
        for (String s : submission) {
            info.append(s);
            info.append(";");
        }
        output.write(info.substring(0, info.length() - 1));
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            result = "Server connection failed.";
        }

        return result;
    }

    /**
     * Returns the grades of the respective student
     *
     * @param studentName
     * @return
     */
    public ArrayList<ArrayList<String>> getGrades(String studentName) {

        String info = String.format("get grades,%s", studentName);
        output.write(info);
        output.println();
        output.flush();

        String result;
        try {
            result = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            result = "Server connection failed.";
        }

        if (result.equals("null")) {
            return null;
        }

        ArrayList<ArrayList<String>> res = new ArrayList<>();
        String[] quizGrades = result.split(";");
        for (String s : quizGrades) {

            String[] values = s.split(",");
            ArrayList<String> valueList = new ArrayList<>();
            valueList.addAll(Arrays.asList(values));
            res.add(valueList);

        }

        return res;

    }
}
