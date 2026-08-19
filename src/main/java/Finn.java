import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Finn {

    // Tools
    private final Scanner scanner = new Scanner(System.in);
    private List<Task> tasks = new ArrayList<>();
    private String breakline = "____________________________________________________________\n";

    public static void main(String[] args) {
        new Finn().run();
    }
    
    public void run() {
        // Greeting text
        String banner = " ____ ___ _   _ _   _ \n"
                + "|  __|_ _| \\ | | \\ | |\n"
                + "| |_  | ||  \\| |  \\| |\n"
                + "|  _| | || |\\  | |\\  |\n"
                + "|_|  |___|_| \\_|_| \\_|\n";

        String opening = banner + breakline;
        System.out.println(opening);

        String greeting = "Hello! I'm Finn.\nYour personal AI assistant!\n" + breakline;
        System.out.println(greeting);

        while (true) {
            System.out.print("What can I do for you? ");
            String input = scanner.nextLine().trim();
            System.out.println(breakline);

            String[] parts = input.split("\\s+", 2);
            String command = parts[0];
            String details = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "bye":
                    System.out.println("Bye. Hope to see you again soon!");
                    return;

                case "list":
                    listTasks();
                    break;

                case "mark":
                    markTask(details);
                    break;
                    
                case "unmark":
                    unmarkTask(details);
                    break;

                case "todo":
                    addTodo(details);
                    break;

                case "deadline":
                    addDeadline(details);
                    break;

                case "event":
                    addEvent(details);
                    break;

                default:
                    System.out.println("Unknown task type: " + command);
                    System.out.println(breakline);
            }
        }
    }

    private void listTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int cur = 1; cur <= tasks.size(); cur++) {
            Task task = tasks.get(cur - 1);
            System.out.println(cur + "." + task);
        }
        System.out.println(breakline);
    }

    private void markTask(String details) {
        int index = Integer.parseInt(details) - 1;
        Task task = tasks.get(index);
        task.markDone();
        System.out.println(
            String.format("Nice! I've marked this task as done:\n%s", task)
        );
        System.out.println(breakline);
    }

    private void unmarkTask(String details) {
        int index = Integer.parseInt(details) - 1;
        Task task = tasks.get(index);
        task.markUndone();
        System.out.println(
            String.format("OK, I've marked this task as not done yet:\n%s", task)
        );
        System.out.println(breakline);
    }

    private void addTask(Task task) {
        tasks.add(task);
        System.out.println(String.format("Got it. I've added this task:\n   %s\nNow you have %d task(s) in the list.", task, tasks.size()));
        System.out.println(breakline);
    }

    private void addTodo(String details) {
        addTask(new Todo(details));
    }

    private void addDeadline(String details) {
        String[] parts = details.split("\\s+/by\\s+", 2);

        if (parts.length < 2) {
            System.out.println("Format: deadline DESCRIPTION /by DATE");
            return;
        }

        addTask(new Deadline(parts[0], parts[1]));
    }

    private void addEvent(String details) {
        String[] fromParts = details.split("\\s+/from\\s+", 2);

        if (fromParts.length < 2) {
            System.out.println("Format: event DESCRIPTION /from START /to END");
            return;
        }

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);

        if (toParts.length < 2) {
            System.out.println("Format: event DESCRIPTION /from START /to END");
            return;
        }

        addTask(new Event(fromParts[0], toParts[0], toParts[1]));
    }
}
