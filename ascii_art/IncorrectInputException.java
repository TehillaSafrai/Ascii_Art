package ascii_art;

/**
 * The IncorrectInputException class is a custom exception that extends the standard Java Exception class.
 * It is designed to be thrown in case of incorrect input parameters or conditions within the context of
 * the ASCII art processing. This exception provides a way to handle and communicate errors related to
 * input validation in the ASCII art application.
 */
public class IncorrectInputException extends Exception {

    /**
     * Constructs an IncorrectInputException with the specified error message.
     *
     * @param errorMessage A String representing the error message associated with the exception.
     */
    public IncorrectInputException(String errorMessage) {
        super(errorMessage);
    }
}
