package sportapp;

/**
 * Exception thrown when invalid user input is encountered.
 */
public class ExInvalidUserInput extends Exception {

    /**
     * Constructs an ExInvalidUserInput with the specified error message.
     *
     * @param message The error message describing the invalid input.
     */
    public ExInvalidUserInput(String message) {
        super(message);
    }
}