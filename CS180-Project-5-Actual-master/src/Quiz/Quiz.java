package Quiz;

import Course.Course;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Quiz
 *
 * A class to read and write Quiz objects from a Course
 *
 * @author Ash Gupta
 * @author Grant Rivera
 * @version Nov 12, 2021
 *
 */
public class Quiz implements Serializable {

    @Serial
    private static final long serialVersionUID = -4314700582012546426L;

    Object gate = new Object();
    private Course course;
    private ArrayList<Question> questions;
    private String quizName;
    private static ArrayList<Quiz> quizzes = new ArrayList<>();
    private boolean randomize;

    /**
     * constructor mainly used by the courses class to store the quiz inside the course
     * @param course
     * @param quizName
     */
    public Quiz(Course course, String quizName) {
        this.course = course;
        questions = new ArrayList<>();
        this.quizName = quizName;
        randomize = false;
        synchronized (gate) {
            quizzes.add(this);
        }
    }

    /**
     * changes a specific question
     * @param index
     * @param question
     */
    public void changeQuestion(int index, Question question) {
        questions.set(index, question);
    }


    /**
     * basic constructor with two parameters to create a quiz object
     * @param questions
     * @param course
     */
    public Quiz(ArrayList<Question> questions, Course course) {
        this.course = course;
        this.questions = questions;
        synchronized (gate) {
            quizzes.add(this);
        }
    }

    /**
     * returns the list of questions
     * @return
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * sets the questions to what we need
     * @param questions
     */
    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    /**
     * adds a question to the already existing questions list
     * @param question
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    /**
     * removes a question from list
     * @param question
     */
    public void removeQuestion(Question question) {

        synchronized (gate) {
            questions.removeIf(q -> q.getHead().equals(question.getHead()));
        }

    }

    /**
     * This is supposed to read the quiz file that is sent
     * @param fileName
     * @throws IOException
     */
    public void readQuiz(String fileName) throws IOException {
        // reading a file and then printing it, not sure if you wanted me to but I made a toString anyways
        File f = new File(fileName);
        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);
        ArrayList<String> output = new ArrayList<String>();

        while (true) {
            String line = bfr.readLine();
            if (line != null) {
                output.add(line);
            } else {
                break;
            }
        }

        toString(output);
    }

    /**
     * This is supposed to write the quiz to a file that is under the course
     */
    public void writeQuiz() {

        try {
            String path = String.format("%s/Quizzes/Quizzes.txt", course.getPath());
            FileOutputStream fos = new FileOutputStream(path, true);
            PrintWriter pw = new PrintWriter(fos);

            StringBuilder quizString = new StringBuilder();
            quizString.append(quizName);
            quizString.append(";");

            // QuizName;Question1,answer1,answer2;Question2,answer1,...
            for (int i = 0; i < questions.size(); i++) {
                quizString.append(questions.get(i).getHead());
                quizString.append(",");
                for (int j = 0; j < questions.get(i).getAnswers().size(); j++) {
                    quizString.append(questions.get(i).getAnswers().get(j));
                    if (j != questions.get(i).getAnswers().size() - 1) {
                        quizString.append(",");
                    }
                }
                quizString.append(";");
            }
            String q = quizString.substring(0, quizString.length() - 1);
            pw.println(q);
            pw.flush();
            // pw.println();
            pw.close();
        } catch (FileNotFoundException nfe) {

            // TODO create quiz file if not found
            nfe.printStackTrace();
        }
    }

    /**
     * toString for reading the file
     * @param output
     */
    public void toString(ArrayList<String> output) {
        // couldn't think of any other way of printing out the read file so this is a temp way
        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i) + "\n");
        }
    }

    /**
     * randomizes the order of the questions inside the quiz
     */
    public void randomizeQuestions() {
        Random random = new Random();
        ArrayList<Question> questions3 = new ArrayList<Question>();

        while (questions.size() > 0) {
            questions3.add(questions.remove(random.nextInt(questions.size())));
        }

        this.questions = questions3;
    }

    /**
     * returns the quiz name
     */
    public String getQuizName() {
        return this.quizName;
    }

    /**
     * sets the quiz values to null
     */
    public void removeQuiz() {
        this.questions = null;
        this.quizName = null;
        quizzes.remove(this);
    }

    /**
     * loads in an existing quiz from the text file that was written earlier
     */
    public static void loadQuizzes(Course course) {

        quizzes = new ArrayList<>();

        try {
            String path = String.format("%s/Quizzes/Quizzes.txt", course.getPath());
            File file = new File(path);

            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    throw new IOException();
                }
            }

            FileReader fr = new FileReader(file);
            BufferedReader bfr = new BufferedReader(fr);

            while (true) {

                String line = bfr.readLine();
                if (line == null) {
                    break;
                }

                String[] vals = line.split(";");
                ArrayList<Question> questions = new ArrayList<>();

                String quizName = vals[0];

                for (int i = 1; i < vals.length; i++) {

                    ArrayList<String> answers = new ArrayList<>();
                    String[] questionVals = vals[i].split(",");
                    String questionName = questionVals[0];
                    for (int j = 1; j < questionVals.length; j++) {
                        answers.add(questionVals[j]);
                    }

                    questions.add(new Question(questionName, answers));

                }

                Quiz q = new Quiz(course, quizName);
                q.setQuestions(questions);
                course.addQuiz(q);

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading quizzes!");
        }

    }

    /**
     * default toString that just prints the quizName
     * @return
     */
    public String toString() {
        return quizName;
    }

    /**
     * returns the boolean value of randomize
     * @return
     */
    public boolean getRandomize() {
        return randomize;
    }

    /**
     * sets the value of randomize
     * @param randomize
     */
    public void setRandomize(boolean randomize) {
        this.randomize = randomize;
    }

    /**
     * returns the course that this quiz is under
     * @return
     */
    public Course getCourse() {
        return course;
    }

    public static ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    /**
     * get a question by its title
     * @param questionHead
     * @return the matching question, or null if none found
     */
    public Question getQuestion(String questionHead) {

        for (Question q : questions) {
            if (questionHead.equals(q.getHead())) {
                return q;
            }
        }

        return null;

    }

}
