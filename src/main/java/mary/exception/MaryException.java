package mary.exception;

/** Represents an input or command error specific to MARY. */
public class MaryException extends Exception {
    /** Creates an error with a message that can be shown to the user. */
    public MaryException(String message) {
        super(message);
    }
}
