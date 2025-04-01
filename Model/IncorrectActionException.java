package Model;

/**
 * Exception thrown when an incorrect or unsupported action is encountered.
 */
public class IncorrectActionException extends Exception {
    
    /**
     * Constructs an IncorrectActionException with the specified detail message.
     *
     * @param message The detail message describing the incorrect action.
     */
    public IncorrectActionException(String message) {
        super(message);
    }
}
