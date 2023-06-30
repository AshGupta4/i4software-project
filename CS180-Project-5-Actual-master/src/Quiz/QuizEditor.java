package Quiz;

import Course.SubmissionViewer;
import UI.TerminalInteraction;

import java.util.Scanner;

/**
 * QuizEditor - Prompts for adding new questions or editing old questions
 * After entering a question, prompts for a list of answers
 * Saves all information to a Quiz.Quiz object
 *
 * @author Gabriel I.
 * @version 11/6/2021
 */

public class QuizEditor {
    private static String questionOptionsPrompt = "Enter an action: \n 1) Add a question \n 2) Edit a question " +
            "\n 3) Remove a question \n 4) Randomize questions \n 5) Grade submissions \n 6) Exit quiz";
    private static String questionHeadPrompt = "Enter the question: ";
    private static String questionAnswersPrompt = "Enter an answer: ";
    private static String addAnswerPrompt = "Enter an action: \n 1) Add another answer \n 2) Finish question";
    private static String questionEditPrompt = "Enter a question number to edit it: ";
    private static String questionRemovePrompt = "Enter a question number to remove it: ";
    private static String randomizeTogglePrompt = "Enter an action: \n 1) Randomize question and answer order " +
            "\n 2) Keep question and answer order";
    private static String noQuestionsError = "There are no questions, please add one.";
    private static String invalidQuestionError = "Invalid question! Please enter a number from 1 - ";

    private Quiz quiz;

    public QuizEditor(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * Displays all questions and answer choices in quiz
     */
    public String generateQuestions() {
        String questionList = "";
        if (quiz.getQuestions().size() >= 1) {
            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                Question question = quiz.getQuestions().get(i);
                questionList += "\n" + QuizDisplay.generateMCQuestion(question, i, false);
            }
        }
        return questionList;
    }

    /**
     * Loops through the question actions until user quits.
     *
     * @param scanner
     */
    public void openTeacherActions(Scanner scanner) {
        while (chooseQuestionAction(scanner));
    }

    /**
     * Prompts user to choose to create, edit, or delete a course or quit the program.
     * Returns if the user wants to continue or quit.
     * If no questions exist, will automatically prompt user to add one.
     *
     * @param scanner
     * @return
     */
    public boolean chooseQuestionAction(Scanner scanner) {
        if (quiz.getQuestions().size() < 1) {
            quiz.addQuestion(addQuestion(scanner));
        }

        int action = TerminalInteraction.readAction(scanner, questionOptionsPrompt, 6);
        if (action == 1) {
            quiz.addQuestion(addQuestion(scanner));
            quiz.writeQuiz();
            return true;
        } else if (action == 2) {
            editQuestions(scanner);
            quiz.writeQuiz();
            return true;
        } else if (action == 3) {
            removeQuestion(scanner);
            quiz.writeQuiz();
            return true;
        } else if (action == 4) {
            randomizeToggle(scanner);
            quiz.writeQuiz();
            return true;
        } else if (action == 5) {
            SubmissionViewer submissionViewer = new SubmissionViewer(quiz);
            submissionViewer.openGradingActions(scanner);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Prompts user for question head and answer
     * Takes more answers as requested
     * Returns a new question object with head and answers
     *
     * @param scanner
     * @return
     */
    public Question addQuestion(Scanner scanner) {
        System.out.println(questionHeadPrompt);
        String questionHead = scanner.nextLine();
        while (questionHead.equals("")) {
            System.out.println("Please enter a valid question: ");
            questionHead = scanner.nextLine();
        }
        Question question = new Question(questionHead);

        int action = 1;
        while (action == 1) {
            System.out.println(questionAnswersPrompt);
            String answer = scanner.nextLine();
            while (answer.equals("")) {
                System.out.println("Please enter a valid answer: ");
                answer = scanner.nextLine();
            }
            question.addAnswer(answer);

            action = TerminalInteraction.readAction(scanner, addAnswerPrompt, 2);
            System.out.println(action);
        }

        return question;
    }

    /**
     * Displays questions and prompts user to edit a question
     *
     * @param scanner
     */
    public void editQuestions(Scanner scanner) {
        String questionList = generateQuestions();
        if (questionList.equals("")) {
            System.out.println(noQuestionsError);
        } else {
            int action = TerminalInteraction.readAction(scanner, questionEditPrompt + questionList,
                    quiz.getQuestions().size(), invalidQuestionError);
            quiz.changeQuestion(action - 1, addQuestion(scanner));
        }
    }

    /**
     * Prompts user to choose a question from a list of question to delete.
     * If no quiz exists, prompts user to add one.
     *
     * @param scanner
     */
    public void removeQuestion(Scanner scanner) {
        String questionList = generateQuestions();
        if (questionList.equals("")) {
            System.out.println(noQuestionsError);
        } else {
            int action = TerminalInteraction.readAction(scanner, questionRemovePrompt + questionList,
                    quiz.getQuestions().size(), invalidQuestionError);
            quiz.removeQuestion(quiz.getQuestions().get(action - 1));
        }
    }

    /**
     * Prompts user to toggle the randomization of a quiz
     *
     * @param scanner
     */
    public void randomizeToggle(Scanner scanner) {
        int action = TerminalInteraction.readAction(scanner, randomizeTogglePrompt, 2);
        if (action == 1) {
            quiz.setRandomize(true);
        } else if (action == 2) {
            quiz.setRandomize(false);
        }
    }
}
