package Profile;

import Exceptions.CourseNotFoundException;
import Exceptions.DuplicateCourseException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Account.java
 *
 * Represents an account, either a teacher or a student.
 *
 * @author Kyle Harvey, L-14
 *
 * @version 11-14-21
 *
 */
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1976635978949393960L;

    private String username = "";
    private String password = "";
    private ArrayList<String> courseNames = new ArrayList<>();
    private boolean isTeacher;

    /**
     *
     * @param password
     */
    public Account (String username, String password) {
        this.username = username;
        this.password = password;
    }
    public void setPassword(String password) {
        this.password = password;
        try {
            AccountDatabase.outputAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
        try {
            AccountDatabase.outputAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param username
     * @param password
     * @return username.equals(username) && password.equals(password)
     */
    public boolean is(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }

    /**
     *
     * @return isTeacher
     */
    public boolean isTeacher() {
        return isTeacher;
    }

    /**
     *
     * @return !isTeacher
     */
    public boolean isStudent() {
        return !isTeacher;
    }

    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return courseNames
     */
    public ArrayList<String> getCourseNames() {
        return courseNames;
    }

    /**
     *
     * @param courseName
     * @return courseNames.contains(courseName)
     */
    public boolean hasCourse(String courseName) {
        return courseNames.contains(courseName);
    }

    /**
     *
     * @param courseName
     * @throws DuplicateCourseException
     */
    public void addCourse(String courseName) throws DuplicateCourseException {
        if (hasCourse(courseName)) {
            throw (new DuplicateCourseException(courseName + " is already added"));
        }
        courseNames.add(courseName);
        try {
            AccountDatabase.outputAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param courseName
     * @throws CourseNotFoundException
     */
    public void removeCourse(String courseName) throws CourseNotFoundException {
        if (!hasCourse(courseName)) {
            throw (new CourseNotFoundException(courseName + " not found"));
        }
        courseNames.remove(courseName);
        try {
            AccountDatabase.outputAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param username
     * @param password
     * @return username.equals(username) && username.equals(password)
     */
    public boolean checkPassword(String username, String password) {
        return is(username, password);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String string = String.format("%s##%s##%b##", username, password, isTeacher());
        for (int i = 0; i < courseNames.size(); i++) {
            string += courseNames.get(i) + "$$";
        }
        return string;
    }

    /**
     *
     * @param string
     */
    public Account(String string) {
        String[] stringArray = string.split("##");
        username = stringArray[0];
        password = stringArray[1];
        isTeacher = stringArray[2].equals("true");

        try {
            String[] courseNameArray = stringArray[3].split("$$");
            for (int i = 0; i < courseNameArray.length; i++) {
                courseNames.add(courseNameArray[i]);
            }
        } catch (Exception e) {

        }

    }

    /**
     *
     * @param username
     * @param password
     * @param isTeacher
     */
    public Account(String username, String password, boolean isTeacher) {
        this.username = username;
        this.password = password;
        this.isTeacher = isTeacher;
    }
}