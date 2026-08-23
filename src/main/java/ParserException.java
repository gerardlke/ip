/**
 * Indicates that a user command cannot be parsed.
 */
public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }
}
