package finn.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import finn.command.AddCommand;
import finn.command.Command;
import finn.command.DeleteCommand;
import finn.command.ExitCommand;
import finn.command.FindCommand;
import finn.command.ListCommand;
import finn.command.MarkCommand;
import finn.exception.ParserException;
import finn.task.Deadline;
import finn.task.Event;
import finn.task.Task;
import finn.task.Todo;


/**
 * Parses raw user input into executable {@link Command} objects.
 */
public class Parser {

    /**
     * Parses one line of user input into a {@link Command}.
     *
     * @param fullInput The raw command line entered by the user.
     * @return The Command corresponding to the input.
     * @throws ParserException If the input is empty, uses an unknown command word,
     *         or does not follow the expected format for its command word.
     */
    public static Command parse(String fullInput) throws ParserException {
        if (fullInput == null || fullInput.trim().isEmpty()) {
            throw new ParserException("Sorry! Please enter a command.");
        }

        String trimmed = fullInput.trim();
        String[] parts = trimmed.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1].trim() : "";

        switch (commandWord) {
            case "bye":
                requireNoDetails("bye", arguments);
                return new ExitCommand();

            case "list":
                requireNoDetails("list", arguments);
                return new ListCommand();

            case "mark":
            case "unmark":
                int markIndex = parseIndex(arguments);
                return new MarkCommand(markIndex, commandWord.equals("mark"));

            case "delete":
                int deleteIndex = parseIndex(arguments);
                return new DeleteCommand(deleteIndex);

            case "find":
                String keyword = parseFindKeyword(arguments);
                return new FindCommand(keyword);

            case "todo":
                Task todo = parseTodo(arguments);
                return new AddCommand(todo);

            case "deadline":
                Task deadline = parseDeadline(arguments);
                return new AddCommand(deadline);

            case "event":
                Task event = parseEvent(arguments);
                return new AddCommand(event);

            default:
                throw new ParserException("Sorry! Unknown task type: " + commandWord);
        }
    }

    /**
     * Ensures a command that takes no arguments was not given any.
     *
     * @param commandWord The command word, used in the error message if validation fails.
     * @param details The argument text following the command word.
     * @throws ParserException If {@code details} is non-empty.
     */
    private static void requireNoDetails(String commandWord, String details) throws ParserException {
        if (!details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: " + commandWord);
        }
    }

    /**
     * Parses a one-based task index from user input into a zero-based index.
     *
     * @param details The raw index text supplied by the user.
     * @return The zero-based index.
     * @throws ParserException If {@code details} is not a valid non-negative integer
     *         after conversion (i.e. the one-based input is not a positive integer).
     */
    private static int parseIndex(String details) throws ParserException {
        try {
            int index = Integer.parseInt(details) - 1;
            if (index < 0) {
                throw new ParserException("Sorry! Invalid task index!");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new ParserException("Sorry! Invalid task index!");
        }
    }

    /**
     * Parses the arguments of a {@code find} command into a search keyword.
     *
     * @param details The keyword text supplied by the user.
     * @return The keyword to search for.
     * @throws ParserException If the keyword is empty.
     */
    private static String parseFindKeyword(String details) throws ParserException {
        if (details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: find KEYWORD");
        }
        return details;
    }

    /**
     * Parses the arguments of a {@code todo} command into a {@link Todo} task.
     *
     * @param details The task description supplied by the user.
     * @return The parsed Todo task.
     * @throws ParserException If the description is empty.
     */
    private static Task parseTodo(String details) throws ParserException {
        if (details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: todo DESCRIPTION");
        }
        return new Todo(details);
    }

    /**
     * Parses the arguments of a {@code deadline} command into a {@link Deadline} task.
     *
     * @param details The task description and date, in the form {@code DESCRIPTION /by DATE}.
     * @return The parsed Deadline task.
     * @throws ParserException If the description or date is missing, or the date
     *         is not in {@code yyyy-MM-dd} format.
     */
    private static Task parseDeadline(String details) throws ParserException {
        String[] parts = details.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: deadline DESCRIPTION /by DATE");
        }
        try {
            LocalDate byDate = LocalDate.parse(parts[1]);
            return new Deadline(parts[0], byDate);
        } catch (DateTimeParseException e) {
            throw new ParserException("Sorry! Please use a valid date in the format yyyy-MM-dd.");
        }
    }

    /**
     * Parses the arguments of an {@code event} command into an {@link Event} task.
     *
     * @param details The task description and dates, in the form
     *         {@code DESCRIPTION /from START /to END}.
     * @return The parsed Event task.
     * @throws ParserException If the description or either date is missing, either
     *         date is not in {@code yyyy-MM-dd} format, or the end date precedes the start date.
     */
    private static Task parseEvent(String details) throws ParserException {
        String[] parts = details.split(" /from ", 2);
        if (parts.length < 2 || parts[0].isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: event DESCRIPTION /from START /to END");
        }
        String description = parts[0];
        String[] timeParts = parts[1].split(" /to ", 2);
        if (timeParts.length < 2 || timeParts[0].isEmpty() || timeParts[1].isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: event DESCRIPTION /from START /to END");
        }
        try {
            LocalDate fromDate = LocalDate.parse(timeParts[0]);
            LocalDate toDate = LocalDate.parse(timeParts[1]);
            if (toDate.isBefore(fromDate)) {
                throw new ParserException("Sorry! The event end date must not be before its start date.");
            }
            return new Event(description, fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new ParserException("Sorry! Please use valid dates in the format yyyy-MM-dd.");
        }
    }
}