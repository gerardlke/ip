import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Parses user commands and creates validated task objects from their details.
 */
public class Parser {
    /** Splits non-blank user input into a command word and optional details. */
    public ParsedCommand parseCommand(String input) throws ParserException {
        if (input.isEmpty()) {
            throw new ParserException("Sorry! Please enter a command.");
        }
        String[] parts = input.split("\\s+", 2);
        String details = parts.length > 1 ? parts[1] : "";
        return new ParsedCommand(parts[0], details);
    }

    /** Ensures that a command has no arguments. */
    public void requireNoDetails(String command, String details) throws ParserException {
        if (!details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: " + command);
        }
    }

    /** Parses a todo command's details into a task. */
    public Todo parseTodo(String details) throws ParserException {
        if (details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: todo DESCRIPTION");
        }
        return new Todo(details);
    }

    /** Parses a deadline command's details into a task. */
    public Deadline parseDeadline(String details) throws ParserException {
        String[] parts = details.split("\\s+/by\\s+", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new ParserException("Sorry! Please follow the format: deadline DESCRIPTION /by DATE");
        }
        try {
            return new Deadline(parts[0], LocalDate.parse(parts[1]));
        } catch (DateTimeException e) {
            throw new ParserException("Sorry! Please use a valid date in the format yyyy-MM-dd.");
        }
    }

    /** Parses an event command's details into a task. */
    public Event parseEvent(String details) throws ParserException {
        String[] fromParts = details.split("\\s+/from\\s+", 2);
        if (fromParts.length < 2 || fromParts[0].isBlank() || fromParts[1].isBlank()) {
            throw new ParserException("Sorry! Please follow the format: event DESCRIPTION /from START /to END");
        }

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);
        if (toParts.length < 2 || toParts[0].isBlank() || toParts[1].isBlank()) {
            throw new ParserException("Sorry! Please follow the format: event DESCRIPTION /from START /to END");
        }

        try {
            LocalDate start = LocalDate.parse(toParts[0]);
            LocalDate end = LocalDate.parse(toParts[1]);
            return new Event(fromParts[0], start, end);
        } catch (DateTimeException e) {
            throw new ParserException("Sorry! Please use valid dates in the format yyyy-MM-dd.");
        } catch (IllegalArgumentException e) {
            throw new ParserException("Sorry! The event end date must not be before its start date.");
        }
    }

    /** Returns a zero-based valid task index. */
    public int parseTaskIndex(String details, int taskCount) throws ParserException {
        try {
            int index = Integer.parseInt(details) - 1;
            if (index >= 0 && index < taskCount) {
                return index;
            }
        } catch (NumberFormatException e) {
            // The shared error message below gives the user the relevant guidance.
        }
        throw new ParserException("Sorry! Invalid task index!");
    }

    /** Represents a command word and its unparsed details. */
    public static class ParsedCommand {
        private final String command;
        private final String details;

        private ParsedCommand(String command, String details) {
            this.command = command;
            this.details = details;
        }

        public String getCommand() {
            return command;
        }

        public String getDetails() {
            return details;
        }
    }
}
