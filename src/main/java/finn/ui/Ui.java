package finn.ui;

import finn.task.*;

import java.util.Scanner;


/**
 * Handles all console input and output for Finn.
 */
public class Ui {
    private static final String BREAKLINE = "____________________________________________________________\n";
    private final Scanner scanner;

    /** Creates a Ui that reads user commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Finn's opening banner and greeting. */
    public void showWelcome() {
        String banner = " ____ ___ _   _ _   _ \n"
                + "|  __|_ _| \\ | | \\ | |\n"
                + "| |_  | ||  \\| |  \\| |\n"
                + "|  _| | || |\\  | |\\  |\n"
                + "|_|  |___|_| \\_|_| \\_|\n";
        System.out.println(banner + BREAKLINE);
        System.out.println("Hello! I'm Finn.\nYour personal AI assistant!\n" + BREAKLINE);
    }

    /** Reads one trimmed command, or returns null when input has ended. */
    public String readCommand() {
        System.out.print("What can I do for you? ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String input = scanner.nextLine().trim();
        System.out.println(BREAKLINE);
        return input;
    }

    /** Shows the current task list. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println((index + 1) + "." + tasks.get(index));
        }
        System.out.println(BREAKLINE);
    }

    /** Shows confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(String.format("Got it. I've added this task:\n    %s\nNow you have %d task(s) in the list.", task, taskCount));
        System.out.println(BREAKLINE);
    }

    /** Shows confirmation that a task's completion state changed. */
    public void showTaskMarked(Task task, boolean completed) {
        String message = completed
                ? String.format("Nice! I've marked this task as done:\n    %s", task)
                : String.format("OK, I've marked this task as not done yet:\n    %s", task);
        System.out.println(message);
        System.out.println(BREAKLINE);
    }

    /** Shows confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(String.format("Oops! I've removed this task:\n    %s\nNow you have %d task(s) in the list.", task, taskCount));
        System.out.println(BREAKLINE);
    }

    /** Shows an error message with the standard divider. */
    public void showError(String message) {
        System.out.println(message);
        System.out.println(BREAKLINE);
    }

    /** Shows Finn's farewell. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }
}