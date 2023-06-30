package Quiz;

import Course.Submission;
import Exceptions.InvalidFileImportException;
import Profile.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * QuizTaker - Reads data from a Quiz.Quiz object and displays the quiz to the user
 *  Provides opportunity to read answers and write back to Quiz.Quiz object
 *
 * @author Gabriel I., Benjamin W.
 * @version 11/6/2021
 */

public class QuizTaker {
    private Quiz quiz;
    private Account account;

    public QuizTaker(Quiz quiz, Account account) {
        this.quiz = quiz;
        this.account = account;
    }

    /**
     * Prints the questions of the quiz.
     * If quiz in randomizes, changes the order of the quiz.
     *
     * @param scanner
     */
    public void takeQuiz(Scanner scanner) {

        // original order
        String[] answers = new String[quiz.getQuestions().size()];
        int len = answers.length;

        if (quiz.getRandomize()) {

            // generate new order
            ArrayList<Integer> questionOrder = new ArrayList<>();
            ArrayList<Integer> questionShuffle = new ArrayList<>();

            for(int i = 0; i < len; i++) {
                questionOrder.add(i);
            }

            for(int i = len - 1; i >= 0; i--) {
                int idx = (int) (Math.random() * (i + 1));
                questionShuffle.add(questionOrder.get(idx));
                questionOrder.remove(idx);
            }

            for(int i : questionShuffle) {
                showMCQuestion(i);
                answers[i] = readMCAnswer(i, scanner);
            }
            new Submission(quiz.getCourse(), quiz, answers, account, System.currentTimeMillis());

        } else {
            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                showMCQuestion(i);
                answers[i] = readMCAnswer(i, scanner);
            }
            new Submission(quiz.getCourse(), quiz, answers, account, System.currentTimeMillis());
        }
    }

    /**
     * Prints a question in terminal based on questionNum
     *
     * @param questionNum
     */
    public void showMCQuestion(int questionNum) {
        Question question = quiz.getQuestions().get(questionNum);
        System.out.println(QuizDisplay.generateMCQuestion(question, questionNum, quiz.getRandomize()));
        System.out.println("Enter your response: (Type '0' to import .txt file");
    }

    /**
     * Reads the response to a question based on questionNum
     * If response is invalid, asks for response again
     * Returns a character corresponding to the response
     *
     * @param questionNum
     * @param scanner
     * @return
     */
    public String readMCAnswer(int questionNum, Scanner scanner) {
        ArrayList<String> answers = quiz.getQuestions().get(questionNum).getAnswers();
        String response = scanner.nextLine();
        char mcLetter = Character.toLowerCase(response.charAt(0));
        if (response.length() == 1 && (int) mcLetter >= 97 && (int) mcLetter < 97 + answers.size()) {
            //Valid response
            return answers.get((int) mcLetter - 97);
        } else if ((int) mcLetter == 48) {
            // Response was "0"
            char fileAnswer;
            try {
                fileAnswer = QuizFileImporter.readQuizAnswerFile(scanner, answers);
                return answers.get((int) fileAnswer - 97);
            } catch (IOException | InvalidFileImportException e) {
                System.out.println("Please enter a valid .txt File with a valid answer choice.");
                return readMCAnswer(questionNum, scanner);
            }
        } else {
            //Invalid response
            System.out.println("Please enter a valid letter corresponding to an answer choice.");
            showMCQuestion(questionNum);
            return readMCAnswer(questionNum, scanner);
        }
    }
}
