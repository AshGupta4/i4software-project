package Course;

import Profile.Account;
import Profile.AccountDatabase;
import Quiz.Question;
import Quiz.Quiz;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Submission
 *
 * A Submission of a Quiz that is timestamped, can be graded and viewed, and contains the original Quiz info
 *
 * @author Grant Rivera
 * @version Nov 14, 2021
 */
public class Submission implements Serializable {


    @Serial
    private static final long serialVersionUID = 4153067928139546090L;

    private static Object gate  = new Object();
    private static ArrayList<Submission> submissions = new ArrayList<>();
    private String[] answers;
    private Quiz quiz; // the Quiz that the submission is based on
    private Course course; // the course the Submission belongs to
    private long timestamp; // the time the student submitted the quiz
    private Account student; // the student that submitted the quiz
    private int[] grade; // the grade per question that the quiz has been assigned
    private int totalGrade = -1; // the total grade
    boolean graded = false; // has the quiz been graded yet

    /**
     * For testing purposes only
     */
    public static void main(String[] args) {

        /*
        // Loading submissions check -- WORKING
        Course.loadCourses();
        Course ma261 = Course.findCourse("MA 261");
        Account student1 = new Account("Username1##password##false");
        AccountDatabase.add(student1);
        Account student2 = new Account("Username2##password##false");
        AccountDatabase.add(student2);
        Quiz q = new Quiz(ma261, "Quiz 1");
        Question question1 = new Question("Question 1");
        question1.addAnswer("Answer 1");
        question1.addAnswer("Answer 2");
        q.addQuestion(question1);
        ma261.addQuiz(q);
        loadSubmissions();
        System.out.println("SUBMISSIONS AFTER LOADING");
        System.out.println(submissions);
        System.out.println();
        System.out.println();

        // Grading & finding by student -- WORKING
        Submission s2 = getSubmissionsByStudent("Username1").get(0);
        int[] grade_s2 = {50, 50};
        s2.addGrade(grade_s2);
        System.out.println("SUBMISSIONS AFTER GRADING");
        System.out.println(submissions);
        System.out.println();
        System.out.println();

        // Adding submissions -- WORKING
        String[] answers2 = {"A", "B"};
        new Submission(ma261, q, answers2, student2, 3000);
        // submissions.add(s3);
        System.out.println("SUBMISSIONS AFTER ADDING");
        System.out.println(submissions);

        // Writing submissions -- WORKING
        writeSubmissions();
        */
    }

    /**
     * Loads the submissions from submissions.txt
     */
    public static void loadSubmissions() {

        synchronized (gate) {
            submissions = new ArrayList<>();
            try {
                File file = new File("submissions.txt");

                if (!file.exists()) {
                    boolean createdCourseList = file.createNewFile();
                    if (!createdCourseList) {
                        throw new IOException("Course list not created");
                    }
                }

                FileReader fr = new FileReader(file);
                BufferedReader bfr = new BufferedReader(fr);

                while (true) {
                    String line = bfr.readLine();
                    if (line == null) {
                        break;
                    }

                    // format: CourseName;QuizName;UserName;TimeStamp;[Grade];Answer1,Answer2,...
                    String[] submissionInfo = line.split(";");

                    String courseName = submissionInfo[0];
                    String quizName = submissionInfo[1];
                    String userName = submissionInfo[2];

                    Course c = Course.findCourse(courseName);
                    Quiz q = c.findQuiz(quizName);
                    Account user = AccountDatabase.findUsername(userName);

                    // has it been graded?
                    if (submissionInfo.length == 5) {

                        String timestamp = submissionInfo[3];
                        String[] answers = submissionInfo[4].split(",");
                        new Submission(c, q, answers, user, Long.parseLong(timestamp));
                        // submissions.add(new Submission(c, q, answers, user, Integer.parseInt(timestamp)));

                    } else {

                        String timestamp = submissionInfo[4];
                        String[] grades = submissionInfo[3].split(",");
                        int[] gradeVals = new int[grades.length];
                        for (int i = 0; i < grades.length; i++) {
                            gradeVals[i] = Integer.parseInt(grades[i]);
                        }
                        String[] answers = submissionInfo[5].split(",");
                        new Submission(c, q, answers, user, Long.parseLong(timestamp), gradeVals);
                        // submissions.add(new Submission(c, q, answers, user, Integer.parseInt(timestamp));

                    }

                }

                bfr.close();
                fr.close();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("There was an error loading the submissions");
            }

        }

    }

    /**
     * Clears the submissions.txt file
     */
    public static void clearSubmissions() {
        synchronized (gate) {
            try {

                File submissionList = new File("submissions.txt");
                FileOutputStream fos = new FileOutputStream(submissionList, false);
                PrintWriter pw = new PrintWriter(fos);

                pw.flush();
                pw.close();
                fos.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }

    /**
     * Writes to the submissions.txt file
     */
    public static void writeSubmissions() {

        synchronized (gate) {
            int count = 0;
            try {
                File courseList = new File("submissions.txt");

                if (!courseList.exists()) {
                    boolean createdCourseList = courseList.createNewFile();
                    if (!createdCourseList) {
                        throw new IOException("Course list not created");
                    }
                }

                FileOutputStream fos = new FileOutputStream(courseList, false);
                PrintWriter pw = new PrintWriter(fos);

                // format: CourseName;QuizName;UserName;TimeStamp;[Grade];Answer1,Answer2,...
                for (Submission s : submissions) {
                    // in case the duplication happens again
                    if (count >= 1000) {
                        break;
                    }
                    String courseName = s.course.getName();
                    String quizName = s.quiz.getQuizName();
                    String userName = s.student.getUsername();
                    String timestamp = String.valueOf(s.timestamp);
                    StringBuilder answersString = new StringBuilder();
                    for (String a : s.answers) {
                        answersString.append(a).append(",");
                    }
                    String answers = answersString.toString().substring(0, answersString.length() - 1);

                    if (s.totalGrade != -1) {
                        StringBuilder grade = new StringBuilder();
                        for (int i : s.grade) {
                            grade.append(i + ",");
                        }
                        String gradeString = grade.substring(0, grade.length() - 1);
                        // String grade = Arrays.toString(s.grade);
                        String submissionString = String.format("%s;%s;%s;%s;%s;%s", courseName, quizName, userName,
                                gradeString, timestamp, answers);
                        pw.println(submissionString);
                        pw.flush();
                    } else {
                        String submissionString = String.format("%s;%s;%s;%s;%s", courseName, quizName, userName,
                                timestamp, answers);
                        pw.println(submissionString);
                        pw.flush();
                    }

                    count++;

                }
                pw.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("There was an error saving the submissions!");
            }

        }

    }

    /**
     * Creates a Submission object
     * @param course
     * @param quiz
     * @param answers
     * @param student
     * @param time
     */
    public Submission(Course course, Quiz quiz, String[] answers, Account student, long time) {

        this.course = course;
        this.quiz = quiz;
        this.answers = answers;
        this.student = student;
        this.timestamp = time;
        this.grade = new int[answers.length];
        synchronized (gate) {
            submissions.add(this);
        }

    }

    /**
     * Creates a Submission object
     * @param course
     * @param quiz
     * @param answers
     * @param student
     * @param time
     * @param grade
     */
    public Submission(Course course, Quiz quiz, String[] answers, Account student, long time, int[] grade) {

        this.course = course;
        this.quiz = quiz;
        this.answers = answers;
        this.student = student;
        this.timestamp = time;
        this.grade = grade;
        for (int i : grade) {
            this.totalGrade += i;
        }
        totalGrade++;
        synchronized (gate) {
            submissions.add(this);
        }

    }

    /**
     * Returns the submissions ArrayList
     * @return submissions
     */
    public static ArrayList<Submission> getSubmissions() {
        synchronized (gate) {
            return submissions;
        }
    }

    /**
     * Return submissions that match a course name
     * @param name
     * @return submissions that match a course
     */
    public static ArrayList<Submission> getSubmissionsByCourse(String name) {
        ArrayList<Submission> res = new ArrayList<>();
        synchronized (gate) {
            for (Submission s : submissions) {
                if (s.course.getName().equals(name)) {
                    res.add(s);
                }
            }
        }
        return res;
    }

    /**
     * Submissions by student
     * @param name
     * @return submissions by student
     */
    public static ArrayList<Submission> getSubmissionsByStudent(String name) {
        ArrayList<Submission> res = new ArrayList<>();
        synchronized (gate) {
            for (Submission s : submissions) {
                if (s.student.getUsername().equals(name)) {
                    res.add(s);
                }
            }
        }
        return res;
    }

    /**
     * Submissions by quiz
     * @param name
     * @return submissions by quiz
     */
    public static ArrayList<Submission> getSubmissionsByQuiz(String name) {
        ArrayList<Submission> res = new ArrayList<>();
        synchronized (gate) {
            for (Submission s : submissions) {
                if (s.quiz.getQuizName().equals(name)) {
                    res.add(s);
                }
            }
        }
        return res;
    }

    /**
     * Add grade for all questions
     * @param grade
     */
    public void addGrade(int[] grade) {

        this.grade = grade;
        graded = true;
        if (totalGrade == -1) {
            totalGrade = 0;
        }
        for (int i : grade) {
            totalGrade += i;
        }

    }

    /**
     * Add grade by index
     * @param grade
     * @param index
     */
    public void addGrade(int grade, int index) {

        if (totalGrade == -1) {
            totalGrade = 0;
        }
        graded = true;
        if (index < this.grade.length) {
            this.grade[index] = grade;
            totalGrade += grade;
        }

    }

    public String toString() {

        String gradeReceivedMessage = "Not graded";
        if (totalGrade != -1) {
            gradeReceivedMessage = "" + totalGrade;
        }

        StringBuilder answerString = new StringBuilder();
        for (int i = 0; i < answers.length; i++) {

            answerString.append(answers[i]);
            if (totalGrade != -1) {
                answerString.append(" (" + grade[i] + "), \n");
            }
        }

        return String.format("Submission for quiz %s from course %s\n" +
                        "Student: %s, Time submitted: %d, Total Grade: %s\n" +
                        "Answers: %s", quiz.getQuizName(), course, student.getUsername(), timestamp,
                gradeReceivedMessage, answerString.substring(0, answerString.length()));

    }

    public Account getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int[] getGrade() {return grade;}

}
