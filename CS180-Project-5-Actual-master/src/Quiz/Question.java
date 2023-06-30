package Quiz;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Question
 *
 * A class to make the question objects
 *
 * @author Ash Gupta
 * @version Nov 12, 2021
 *
 */
public class Question implements Serializable {

    @Serial
    private static final long serialVersionUID = 2598625892672253807L;

    private String head;
    private ArrayList<String> answers;

    /**
     * basic constructor for creating the object class
     * @param head
     */
    public Question(String head) {
        this.head = head;
        answers = new ArrayList<String>();
    }

    public Question(String head, ArrayList<String> answers) {
        this.head = head;
        this.answers = answers;
    }

    /**
     * returns the head value
     * @return
     */
    public String getHead() {
        return head;
    }

    /**
     * returns the answers array list
     * @return
     */
    public ArrayList<String> getAnswers() {
        return answers;
    }

    /**
     * sets the head value to the parameter
     * @param head
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * adds an answer to the array list of the answers
     * @param answer
     */
    public void addAnswer(String answer) {
        answers.add(answer);
    }

    /**
     * default toString for a question object
     * @return
     */
    public String toString() {
        String finalString = "";
        finalString += head + "\n";
        for (int i = 0; i < answers.size(); i++) {
            finalString += answers.get(i) + "\n";
        }
        return finalString;
    }
}
