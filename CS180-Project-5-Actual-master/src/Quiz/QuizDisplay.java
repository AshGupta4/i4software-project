package Quiz;

import java.util.ArrayList;

/**
 * QuizDisplay - Takes care of displaying any quiz related data to terminal
 * Example print:
 *  1. Question Head?
 *    a) Answer 1
 *    b) Answer 2
 *    c) Answer 3
 *
 * @author Gabriel I.
 * @version 11/6/2021
 */

public class QuizDisplay {

    /**
     * Generate a question from Question object and returns the string
     * Will randomize order of answers if randomize is true
     *
     * @param question
     * @param questionNum
     * @param randomize
     * @return
     */
    public static String generateMCQuestion(Question question, int questionNum, boolean randomize) {
        String questionStr = String.format("%s. %s\n", questionNum + 1, question.getHead());

        //Randomize by Grant
        ArrayList<String> answers = question.getAnswers();
        if (randomize) {
            ArrayList<Integer> answerOrder = new ArrayList<>();
            ArrayList<Integer> answerShuffle = new ArrayList<>();

            for (int i = 0; i < answers.size(); i++) {
                answerOrder.add(i);
            }

            for (int i = answers.size() - 1; i >= 0; i--) {
                int randomIndex = (int) (Math.random() * (i + 1));
                answerShuffle.add(answerOrder.get(randomIndex));
                answerOrder.remove(randomIndex);
            }

            for (int i : answerShuffle) {
                questionStr += String.format("  %s) %s\n", (char) (i + 97), answers.get(i));
            }
        } else {
            for (int i = 0; i < question.getAnswers().size(); i++) {
                questionStr += String.format("  %s) %s\n", (char) (i + 97), answers.get(i));
            }
        }
        return questionStr;
    }
}