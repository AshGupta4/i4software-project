package Exceptions;

/**
 * Invalid MC File Exception
 *
 * @author Benjamin W.
 * @version November 14, 2021
 */
public class InvalidFileImportException extends Exception {
    public InvalidFileImportException(String message) {
        super(message);
    }
}
