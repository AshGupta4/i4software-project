package Course;

import Quiz.Quiz;
import UI.TerminalInteraction;

import java.util.ArrayList;
import java.util.Scanner;

public class SubmissionViewer {
    private static String gradeActionPrompt = "Enter an action \n 1) Grade a submission \n 2) Exit grading";
    private static String chooseStudentPrompt = "Enter a student to grade: ";
    private static String gradeAnswerPrompt = "Enter a grade for the following answer: ";
    private static String addAnswerPrompt = "Enter an action: \n 1) Add another answer \n 2) Finish question";
    private static String questionEditPrompt = "Enter a question number to edit it: ";
    private static String questionRemovePrompt = "Enter a question number to remove it: ";
    private static String randomizeTogglePrompt = "Enter an action: \n 1) Randomize question and answer order " +
            "\n 2) Keep question and answer order";
    private static String noSubmissionsError = "There are no submissions, please come back later.";
    private static String invalidSubmissionError = "Invalid student! Please enter a number from 1 - ";

    private Quiz quiz;

    public SubmissionViewer(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * Displays all students in submission
     */
    public String generateStudentNames() {
        String studentNames = "";
        ArrayList<Submission> submissions = Submission.getSubmissionsByQuiz(quiz.getQuizName());

        if (submissions.size() >= 1) {
            for (int i = 0; i < submissions.size(); i++) {
                Submission submission = submissions.get(i);
                studentNames += String.format("\n %s) %s", i + 1, submissions.get(i).getStudent().getUsername());
            }
        }
        return studentNames;
    }

    /**
     * Loops through the teacher actions until quit
     *
     * @param scanner
     */
    public void openGradingActions(Scanner scanner) {
        while (chooseGradingAction(scanner));
    }

    public boolean chooseGradingAction(Scanner scanner) {
        int action = TerminalInteraction.readAction(scanner, gradeActionPrompt, 2);
        if (action == 1) {
            chooseSubmission(scanner);
            return true;
        } else {
            return false;
        }
    }

    public void chooseSubmission(Scanner scanner) {
        String studentNames = generateStudentNames();
        if (studentNames.equals("")) {
            System.out.println(noSubmissionsError);
        } else {
            int action = TerminalInteraction.readAction(scanner, chooseStudentPrompt + studentNames,
                    Submission.getSubmissionsByQuiz(quiz.getQuizName()).size(), invalidSubmissionError);
            gradeSubmission(scanner, action);
        }
    }

    public void gradeSubmission(Scanner scanner, int submissionNum) {
        Submission submission = Submission.getSubmissionsByQuiz(quiz.getQuizName()).get(submissionNum - 1);
        for (int i = 0; i < submission.getQuiz().getQuestions().size(); i++) {
            int grade = TerminalInteraction.readNumber(scanner, gradeAnswerPrompt + "\n Question: " +
                    submission.getQuiz().getQuestions().get(i) + "\n Response: " + submission.getAnswers()[i]);
            submission.addGrade(grade, i);
        }
    }
}
