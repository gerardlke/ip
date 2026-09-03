package finn.ui;

import java.util.Scanner;

import finn.task.Task;
import finn.task.TaskList;


/**
 * Handles all console input and output for Finn.
 */
public class Ui {
    private static final String BREAKLINE = "____________________________________________________________\n";
    private final Scanner scanner;
    private String lastResponse = "";

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
        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (int index = 0; index < tasks.size(); index++) {
            response.append(index + 1).append(".").append(tasks.get(index)).append("\n");
        }
        lastResponse = response.toString().trim();
        System.out.println(lastResponse);
        System.out.println(BREAKLINE);
    }

    /** Shows the tasks matching a search keyword. */
    public void showMatchingTasks(TaskList matchingTasks) {
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int index = 0; index < matchingTasks.size(); index++) {
            response.append(index + 1).append(".").append(matchingTasks.get(index)).append("\n");
        }
        lastResponse = response.toString().trim();
        System.out.println(lastResponse);
        System.out.println(BREAKLINE);
    }

    /** Shows confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        lastResponse = String.format(
                "Got it. I've added this task:\n    %s\nNow you have %d task(s) in the list.", task, taskCount);
        System.out.println(lastResponse);
        System.out.println(BREAKLINE);
    }

    /** Shows confirmation that a task's completion state changed. */
    public void showTaskMarked(Task task, boolean completed) {
        lastResponse = completed
                ? String.format("Nice! I've marked this task as done:\n    %s", task)
                : String.format("OK, I've marked this task as not done yet:\n    %s", task);
        System.out.println(lastResponse);
        System.out.println(BREAKLINE);
    }

    /** Shows confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        lastResponse = String.format(
                "Oops! I've removed this task:\n    %s\nNow you have %d task(s) in the list.", task, taskCount);
        System.out.println(lastResponse);
        System.out.println(BREAKLINE);
    }

    /** Shows an error message with the standard divider. */
    public void showError(String message) {
        lastResponse = message;
        System.out.println(message);
        System.out.println(BREAKLINE);
    }

    /** Shows Finn's farewell. */
    public void showGoodbye() {
        lastResponse = "Bye. Hope to see you again soon!";
        System.out.println(lastResponse);
    }

    /** Returns the latest message produced by this UI. */
    public String getLastResponse() {
        return lastResponse;
    }
}
