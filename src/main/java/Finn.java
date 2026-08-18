import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Finn {

    // Tools
    private final Scanner scanner = new Scanner(System.in);
    private List<Task> tasks = new ArrayList<>();

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
        String breakline = "____________________________________________________________\n";

        String opening = banner + breakline;
        System.out.println(opening);

        String greeting = "Hello! I'm Finn.\nYour personal AI assistant!\n" + breakline;
        System.out.println(greeting);

        while (true) {
            System.out.print("What can I do for you? ");
            String command = scanner.nextLine().trim();

            System.out.println(breakline);

            // End program if command is "bye"
            if (command.equals("bye")) {
                break;
            }

            // Execute "list" command
            if (command.equals("list")) {
                for (int cur = 1; cur <= tasks.size(); cur++) {
                    Task task = tasks.get(cur - 1);
                    System.out.println(
                        cur + "." + printTaskCompletion(task)
                    );
                }
                System.out.println(breakline);
                continue;
            }

            // Execute "mark" and "unmark" commands
            if (command.contains("mark")) {
                String[] parts = command.split("\\s+", 2);
                command = parts[0];

                int arg = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                Task task = tasks.get(arg - 1);

                if (command.equals("mark")) {
                    task.markDone();
                    System.out.println(
                        String.format("Nice! I've marked this task as done:\n%s", printTaskCompletion(task))
                    );
                }
                if (command.equals("unmark")) {
                    task.markUndone();
                    System.out.println(
                        String.format("OK, I've marked this task as not done yet:\n%s", printTaskCompletion(task))
                    );
                }
                System.out.println(breakline);
                continue;
            }

            System.out.println("Added: " + command);
            tasks.add(new Task(command));

            System.out.println(breakline);
        }

        // Closing text
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }

    public String printTaskCompletion(Task task) {
        return String.format("[%s] %s", task.isCompleted() ? "X" : " ", task.getName());
    }
}
