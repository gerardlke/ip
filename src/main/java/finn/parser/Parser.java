package finn.parser;

import finn.command.*;
import finn.exception.ParserException;
import finn.task.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class Parser {

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
            int markIdx = parseIndex(arguments);
            return new MarkCommand(markIdx, commandWord.equals("mark"));

        case "delete":
            int deleteIdx = parseIndex(arguments);
            return new DeleteCommand(deleteIdx);

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

    private static void requireNoDetails(String commandWord, String details) throws ParserException {
        if (!details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: " + commandWord);
        }
    }

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

    private static Task parseTodo(String details) throws ParserException {
        if (details.isEmpty()) {
            throw new ParserException("Sorry! Please follow the format: todo DESCRIPTION");
        }
        return new Todo(details);
    }

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