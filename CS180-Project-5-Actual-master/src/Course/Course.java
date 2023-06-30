package Course;

import Exceptions.DuplicateCourseException;
import Exceptions.InvalidCourseException;
import Quiz.Quiz;

import java.io.*;
import java.util.ArrayList;

/**
 * Course
 *
 * Courses are stored in the form of a directory (whose name is the Course name) and contain quiz.txt files
 * which can be loaded and accessed by students
 *
 * Make sure to loadCourses() and saveCourses() when starting/ending program
 *
 * @author Grant Rivera
 * @version Nov 14, 2021
 */
public class Course implements Serializable {



    // TODO quiz synchronization
    private static final Object gate = new Object(); // make sure no race conditions for file writing

    @Serial
    private static final long serialVersionUID = 7940820655979312143L;

    private static ArrayList<Course> courses = new ArrayList<>(); // keep track of course names
    private ArrayList<Quiz> quizzes = new ArrayList<>(); // the list of quizzes in the course

    private String name; // course name
    private String path; // path for course directory, should follow ("Courses/(CourseName)")

    /**
     * For testing purposes only
     */

    /*
    public static void main(String[] args) {

        // for testing
        // must start with courses.txt containing:
        // (line 1) MA 261
        // (line 2) CS 180
        // otherwise tests will NOT work

        // Course loading check -- WORKING
        loadCourses();

        // Course creation check -- WORKING
        try {
           new Course("CS 193");
        } catch (InvalidCourseException | DuplicateCourseException ignored) {

        }

        // Course finding check -- WORKING
        Course cs180 = Course.findCourse("CS 180");
        assert cs180 != null;
        Course ma261 = Course.findCourse("MA 261");
        assert ma261 != null;
        Course cs193 = Course.findCourse("CS 193");

        // Adding quiz to course check -- WORKING
        Quiz q = new Quiz(ma261, "Quiz 1");
        Question question1 = new Question("Question 1");
        question1.addAnswer("Answer 1");
        question1.addAnswer("Answer 2");
        q.addQuestion(question1);
        ma261.addQuiz(q);

        // Writing a quiz check -- IMPROPER FORMAT
        q.writeQuiz();

        // Deleting a course check -- WORKING
        assert ma261 != null;
        boolean deleted = deleteCourse(ma261);
        assert deleted;

        assert cs180 != null;
        deleted = deleteCourse(cs180);
        assert deleted;

        // Writing courses check -- WORKING
        Course.writeCourses();

    }
    */

    /**
     * Load Courses from the courses.txt file
     * This should only happen once at beginning, no need to sync
     */
    public static void loadCourses() {

        File courseList = new File("courses.txt");
        File courseFolder = new File("Courses");

        try {

            if (!courseFolder.exists()) {

                boolean created = courseFolder.mkdir();
                if (!created) {
                    throw new IOException();
                }
            } else if (!courseFolder.isDirectory()) {

                throw new IOException("File Course is not a directory!");

            }

            FileReader fr = new FileReader(courseList);
            BufferedReader br = new BufferedReader(fr);

            while (true) {

                String line = br.readLine();
                if (line == null) {
                    break;
                }

                try {
                    new Course(line);
                } catch (DuplicateCourseException | InvalidCourseException ignored) {
                    // Ignore the exceptions and don't add the same course twice
                }

            }

            br.close();
            fr.close();

        } catch (FileNotFoundException nfe) {

            try {
                boolean created = courseList.createNewFile();
                if (!created) {
                    throw new IOException();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Could not create the file 'courses.txt'");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading the courses");
        }

    }

    /**
     * Write all courses to the courses.txt file
     */
    public static void writeCourses() {

            try {
                synchronized (gate) {
                    File courseList = new File("courses.txt");
                    FileOutputStream fos = new FileOutputStream(courseList, false);
                    PrintWriter pw = new PrintWriter(fos);
                    for (Course c : courses) {

                        pw.println(c.getName());

                    }
                    pw.flush();
                    pw.close();
                    fos.close();
                }
            } catch (Exception e) {
                System.out.println("There was an error saving the courses!");
            }

    }

    /**
     * Either loading or creating a Course directory
     * If it has already been created, it will add directory path
     *
     * @param name
     */
    public Course(String name) throws DuplicateCourseException, InvalidCourseException {

        String path = String.format("Courses/%s", name);
        String submissionPath = String.format("%s/Submissions", path);
        String quizPath = String.format("%s/Quizzes", path);
        File courseFolder = new File(path);
        File submissionFolder = new File(submissionPath);
        File quizFolder = new File(quizPath);

        if (courseFolder.exists()) {
            if (courseFolder.isDirectory()) {
                // Course folder exists, make sure course isn't already loaded
                for (Course c : courses) {
                    // Is the course already loaded?
                    if (c.getName().equals(name)) {
                        throw new DuplicateCourseException(String.format("Course %s already exists!", name));
                    }
                    // Is the directory structured properly?
                    if (!(quizFolder.exists() && submissionFolder.exists() &&
                            quizFolder.isDirectory() && submissionFolder.isDirectory())) {
                        throw new InvalidCourseException(String.format("Course %s is an invalid directory!", name));
                    }
                }
                // If no name matches found, create course and add to list
                this.name = name;
                this.path = path;
                synchronized (gate) {
                    courses.add(this);
                }
            } else {
                throw new InvalidCourseException(String.format("Course %s is not a directory!", name));
            }

        } else {

            try {

                // Create the folders
                boolean created = courseFolder.mkdir() && submissionFolder.mkdir() && quizFolder.mkdir();
                if (!created) {
                    throw new IOException();
                } else {
                    this.name = name;
                    this.path = path;
                    synchronized (gate) {
                        courses.add(this);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("There was an error creating the course " + name);
            }

        }
    }

    /**
     * finds a course with the given name
     * @param name
     * @return the Course with the matching name
     */
    public static Course findCourse(String name) {

        synchronized (gate) {
            for (Course c : courses) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }

        // returns null if no courses found with name
        return null;

    }

    /**
     * @return the course arraylist
     */
    public static ArrayList<Course> getCourses() {
        synchronized (gate) {
            return courses;
        }
    }

    /**
     * deletes a course and its contents
     * @param course
     * @return true if successful, false otherwise
     */
    public static boolean deleteCourse(Course course) {

        // Entire thing needs to be synchronized due to file deletion
        synchronized (gate) {
            File courseDirectory = new File(course.getPath());
            boolean deleted = deleteFileRecursively(courseDirectory);
            courses.remove(course);
            /*
            if (deleted) {
                courses.remove(course);
                return true;
            } else {
                return false;
            }
            */
            return true;

        }

    }

    /**
     * deletes a course directory with all contents
     * @param f the file or directory to be deleted
     * @return true if successful, false otherwise
     */
    public static boolean deleteFileRecursively(File f) {

        if (f == null) {
            return false;
        }

        boolean deleted = true;

        synchronized (gate) {

            if (!f.isDirectory()) {
                // deleted = f.delete();
                try {
                    FileOutputStream fos = new FileOutputStream(f, false);
                    PrintWriter pw = new PrintWriter(fos);
                    pw.flush();
                    pw.close();
                    fos.close();
                } catch (IOException ignored) {
                    return false;
                }
            } else {
                if (f.listFiles() != null) {
                    for (File d : f.listFiles()) {
                        deleted = deleted && deleteFileRecursively(d);
                    }
                }
                // deleted = deleted && f.delete();
            }
        }

        // return deleted;
        return true;

    }

    /**
     * @return the course name
     */
    public String getName() {
        return name;
    }

    /**
     * get path
     * @return path of the course directory
     */
    public String getPath() {
        return path;
    }

    /**
     * get quizzes
     * @return quizzes
     */
    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    /**
     * Finds a quiz
     * @param name
     * @return quiz matching the name, or null
     */
    public Quiz findQuiz(String name) {
        synchronized (gate) {
            for (Quiz q : quizzes) {
                if (q.getQuizName().equals(name)) {
                    return q;
                }
            }
        }
        return null;
    }

    /**
     * Adds a quiz to the quizzes arraylist
     * @param quiz
     */
    public void addQuiz(Quiz quiz) {
        synchronized (gate) {
            for (Quiz q : quizzes) {
                if (q.getQuizName().equals(quiz.getQuizName())) {
                    return;
                }
            }
            quizzes.add(quiz);
        }
    }

    /**
     * Writes all quizzes
     */
    public void writeQuizzes() {
        synchronized (gate) {
            for (Quiz q : quizzes) {
                q.writeQuiz();
            }
        }
    }

    /**
     * Removes a quiz from the quiz arraylist
     * @param quiz
     */
    public void removeQuiz(Quiz quiz) {
        synchronized (gate) {
            quizzes.removeIf(q -> q.getQuizName().equals(quiz.getQuizName()));
        }
    }

    /**
     * Clear Quiz file before writing
     */
    public void clearQuizzes() {

        try {
            String path = String.format("%s/Quizzes/Quizzes.txt", getPath());
            FileOutputStream fos = new FileOutputStream(path, false);
            PrintWriter pw = new PrintWriter(fos);
            pw.flush();
            pw.close();
            fos.close();
        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public String toString() {
        return name;
    }

}
