package Exceptions;

/**
 * Exceptions.InvalidCourseException
 *
 * Exception for creating a course whose name is already taken by a non-directory file
 * (For duplicate course names use Exceptions.DuplicateCourseException)
 *
 * @author Grant Rivera
 * @version Nov 10, 2021
 */
public class InvalidCourseException extends Exception {

    public InvalidCourseException(String message) {
        super(message);
    }

}
