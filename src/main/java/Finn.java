import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Finn {
    private static final Path STORAGE_PATH = Path.of(System.getProperty("finn.storage.path", "./data/Finn.txt"));

    // Tools
    private final Scanner scanner = new Scanner(System.in);
    private final List<Task> tasks = new ArrayList<>();
    private final String breakline = "____________________________________________________________\n";

    public static void main(String[] args) {
        new Finn().run();
    }
    
    public void run() {
        readFile();

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
            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine().trim();
            System.out.println(breakline);

            if (input.isEmpty()) {
                printError("Sorry! Please enter a command.");
                continue;
            }

            String[] parts = input.split("\\s+", 2);
            String command = parts[0];
            String details = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "bye":
                    if (!details.isEmpty()) {
                        printError("Sorry! Please follow the format: bye");
                        break;
                    }
                    System.out.println("Bye. Hope to see you again soon!");
                    return;

                case "list":
                    if (details.isEmpty()) {
                        listTasks();
                    } else {
                        printError("Sorry! Please follow the format: list");
                    }
                    break;

                case "mark":
                    markTask(details, true);
                    break;
                    
                case "unmark":
                    markTask(details, false);
                    break;

                case "delete":
                    deleteTask(details);
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
                    printError("Sorry! Unknown task type: " + command);
            }
        }
    }

    /** Loads valid saved tasks from the storage file when it exists. */
    private void readFile() {
        try {
            if (!Files.isRegularFile(STORAGE_PATH)) {
                return;
            }
            for (String savedTask : Files.readAllLines(STORAGE_PATH)) {
                String[] parts = savedTask.split(" \\| ", -1);
                Task task = createTask(parts);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException | SecurityException e) {
            System.err.println("Unable to load tasks: " + e.getMessage());
        }
    }

    /** Creates a task from one valid saved line, or returns null when the line is malformed. */
    private Task createTask(String[] parts) {
        if (parts.length < 3) {
            return null;
        }

        Task task;
        try {
            switch (parts[0]) {
            case "T":
                if (parts.length != 3) {
                    return null;
                }
                task = new Todo(decode(parts[2]));
                break;
            case "D":
                if (parts.length != 4) {
                    return null;
                }
                task = new Deadline(decode(parts[2]), LocalDate.parse(decode(parts[3])));
                break;
            case "E":
                if (parts.length != 5) {
                    return null;
                }
                task = new Event(decode(parts[2]), LocalDate.parse(decode(parts[3])), LocalDate.parse(decode(parts[4])));
                break;
            default:
                return null;
            }
        } catch (IllegalArgumentException | DateTimeException e) {
            return null;
        }

        if (parts[1].equals("1")) {
            task.markDone();
        } else if (!parts[1].equals("0")) {
            return null;
        }
        return task;
    }

    /** Encodes text as Base64 so it cannot conflict with the storage delimiter. */
    private String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /** Decodes a Base64-encoded storage field as UTF-8 text. */
    private String decode(String encodedField) {
        return new String(Base64.getDecoder().decode(encodedField), StandardCharsets.UTF_8);
    }

    /** Saves the current task list in a structured format for later loading. */
    private void writeFile() {
        try {
            Path parentDirectory = STORAGE_PATH.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            List<String> savedTasks = new ArrayList<>();
            for (Task task : tasks) {
                String savedTask = formatTaskForStorage(task);
                if (savedTask != null) {
                    savedTasks.add(savedTask);
                }
            }
            Files.write(STORAGE_PATH, savedTasks);
        } catch (IOException | SecurityException e) {
            System.err.println("Unable to save tasks: " + e.getMessage());
        }
    }

    /** Converts a task into one line of the storage format. */
    private String formatTaskForStorage(Task task) {
        String status = task.isCompleted() ? "1" : "0";
        if (task instanceof Todo) {
            return String.format("T | %s | %s", status, encode(task.getName()));
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("D | %s | %s | %s", status, encode(task.getName()), encode(deadline.getDeadline().toString()));
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("E | %s | %s | %s | %s", status, encode(task.getName()),
                    encode(event.getStart().toString()), encode(event.getEnd().toString()));
        }
        return null;
    }

    private void listTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int cur = 1; cur <= tasks.size(); cur++) {
            Task task = tasks.get(cur - 1);
            System.out.println(cur + "." + task);
        }
        System.out.println(breakline);
    }

    private void markTask(String details, boolean completion) {
        int index = getTaskIndex(details);
        if (index < 0) {
            return;
        }

        Task task = tasks.get(index);
        if (completion) {
            task.markDone();
            System.out.println(
                String.format("Nice! I've marked this task as done:\n%s", task)
            );
        } else {
            task.markUndone();
            System.out.println(
                String.format("OK, I've marked this task as not done yet:\n%s", task)
            );
        }
        writeFile();
        System.out.println(breakline);
    }

    private void deleteTask(String details) {
        int index = getTaskIndex(details);
        if (index < 0) {
            return;
        }

        Task task = tasks.remove(index);
        writeFile();
        System.out.println(String.format("Oops! I've removed this task:\n   %s\nNow you have %d task(s) in the list.", task, tasks.size()));
        System.out.println(breakline);
    }

    private void addTask(Task task) {
        tasks.add(task);
        writeFile();
        System.out.println(String.format("Got it. I've added this task:\n   %s\nNow you have %d task(s) in the list.", task, tasks.size()));
        System.out.println(breakline);
    }

    private void addTodo(String details) {
        if (details.length() < 1) {
            System.out.println("Sorry! Please follow the format: todo DESCRIPTION");
            System.out.println(breakline);
            return;
        }
        addTask(new Todo(details));
    }

    private void addDeadline(String details) {
        String[] parts = details.split("\\s+/by\\s+", 2);

        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            System.out.println("Sorry! Please follow the format: deadline DESCRIPTION /by DATE");
            System.out.println(breakline);
            return;
        }
        try {
            addTask(new Deadline(parts[0], LocalDate.parse(parts[1])));
        } catch (DateTimeException e) {
            printError("Sorry! Please use a valid date in the format yyyy-MM-dd.");
        }
    }

    private void addEvent(String details) {
        String[] fromParts = details.split("\\s+/from\\s+", 2);

        if (fromParts.length < 2 || fromParts[0].isBlank() || fromParts[1].isBlank()) {
            System.out.println("Sorry! Please follow the format: event DESCRIPTION /from START /to END");
            System.out.println(breakline);
            return;
        }

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);

        if (toParts.length < 2 || toParts[0].isBlank() || toParts[1].isBlank()) {
            System.out.println("Sorry! Please follow the format: event DESCRIPTION /from START /to END");
            System.out.println(breakline);
            return;
        }

        try {
            LocalDate start = LocalDate.parse(toParts[0]);
            LocalDate end = LocalDate.parse(toParts[1]);
            if (end.isBefore(start)) {
                printError("Sorry! The event end date must not be before its start date.");
                return;
            }
            addTask(new Event(fromParts[0], start, end));
        } catch (DateTimeException e) {
            printError("Sorry! Please use valid dates in the format yyyy-MM-dd.");
        }
    }

    /** Returns a zero-based valid task index, or -1 after printing an error. */
    private int getTaskIndex(String details) {
        try {
            int index = Integer.parseInt(details) - 1;
            if (index >= 0 && index < tasks.size()) {
                return index;
            }
        } catch (NumberFormatException e) {
            // Invalid numbers are handled by the shared error message below.
        }
        printError("Sorry! Invalid task index!");
        return -1;
    }

    /** Displays an error message followed by the standard divider. */
    private void printError(String message) {
        System.out.println(message);
        System.out.println(breakline);
    }
}
