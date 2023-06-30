package Quiz;

import Course.Course;
import Exceptions.InvalidFileImportException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Quiz File Import - Can import quizzes from files for teachers or quiz answer
 * files for students
 *
 * @author Benjamin W.
 * @version November 14, 2021
 */
public class QuizFileImporter {

    private static final String importedQuizFormatError = "Error! The imported file does not have the correct format.\n"
            + "Make sure all questions have a '?' and the first question is on the second line";
    private final String importedMCAnswerError = "Error! The answer choice in the imported file did not match any of the answer choices.";

    public static String readQuizQuestionFile(String filepath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String fileString = "";
            String curLine = br.readLine();
            while (curLine != null) {
                fileString += curLine + "/n/";
                curLine = br.readLine();
            }
            return fileString;
        } catch (FileNotFoundException e) {
            return "Invalid filepath!";
        } catch (IOException e) {
            return "Error reading the file!";
        }
    }

    public static String parseQuizQuestionString(String fileString, Course course) {
        String[] fileLines = fileString.split("/n/");

        System.out.println(fileString);
        System.out.println(fileLines.toString());

        String quizName = fileLines[0];
        Quiz quiz = new Quiz(course, quizName);

        String currLine;
        int currQuestion = 0;
        for (int i = 1; i < fileLines.length; i++) {
            currLine = fileLines[i];
            System.out.println(currLine);
            System.out.println(currLine.contains("?"));
            if (currLine.contains("?")) {
                quiz.addQuestion(new Question(currLine));
                currQuestion++;
            } else if (currQuestion == 0) {
                return "Invalid format!";
            } else {
                quiz.getQuestions().get(currQuestion - 1).addAnswer(currLine);
            }
        }

        course.addQuiz(quiz);
        return "success";
    }

    /**
     * Takes in the filepath for a file that a teacher would like to import as a Quiz
     * then creates and adds that quiz to that teacher's desired course's quiz list
     *
     * @param filepath
     * @param course
     */
    public static void readQuizQuestionFile(String filepath, Course course) throws InvalidFileImportException, FileNotFoundException, IOException {
            File quizFile = new File(filepath);
            BufferedReader br = new BufferedReader(new FileReader(quizFile));

            String quizName = br.readLine();
            Quiz quiz = new Quiz(course, quizName);

            ArrayList<Question> quizQuestions = new ArrayList<>(0);
            String currLine;
            int currQuestion = 0;
            while ((currLine = br.readLine()) != null) {
                if (currLine.contains("?")) {
                    quiz.addQuestion(new Question(currLine));
                    currQuestion++;
                } else if (currQuestion == 0) {
                    throw new InvalidFileImportException(importedQuizFormatError);
                } else {
                    quiz.getQuestions().get(currQuestion - 1).addAnswer(currLine);
                }
            }
            quiz.setQuestions(quizQuestions);
            System.out.println("The Quiz was successfully imported from the file!");
    }

    /**
     * Allows a student to import a Multiple Choice answer in a .txt file. Throws exceptions
     * depending on if the file is found or not and if the file contains a valid answer choice.
     *
     * @param scanner
     * @param answers
     * @return
     * @throws IOException
     * @throws InvalidFileImportException
     */
    public static char readQuizAnswerFile(Scanner scanner, ArrayList<String> answers) throws IOException, InvalidFileImportException {
        System.out.println("Enter the path for the .txt file you want to import:");
        String filepath = scanner.nextLine();
        File quizFile = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(quizFile));
        String answerFileLine = br.readLine();
        char mcLetter = Character.toLowerCase(answerFileLine.charAt(0));
        if (answerFileLine.length() == 1 && (int) mcLetter >= 97 && (int) mcLetter < 97 + answers.size()) {
            return mcLetter;
        } else {
            throw new InvalidFileImportException("The answer choice in the imported file did not match any of the answer choices.");
        }
    }
}
