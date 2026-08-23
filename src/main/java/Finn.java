import java.io.IOException;


public class Finn {
    private final Storage storage;
    private final Parser parser;
    private final TaskList tasks;
    private final Ui ui;

    public Finn(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks from file. Starting with an empty list.");
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    public static void main(String[] args) {
        new Finn("./data/Finn.txt").run();
    }
    
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                break;
            }

            try {
                Parser.ParsedCommand parsedCommand = parser.parseCommand(input);
                String command = parsedCommand.getCommand();
                String details = parsedCommand.getDetails();

                switch (command) {
                case "bye":
                    parser.requireNoDetails("bye", details);
                    ui.showGoodbye();
                    return;

                case "list":
                    parser.requireNoDetails("list", details);
                    ui.showTaskList(tasks);
                    break;

                case "mark":
                case "unmark":
                    boolean isDone = command.equals("mark");
                    int markIndex = parser.parseTaskIndex(details, tasks.size());
                    Task taskToMark = tasks.get(markIndex);
                    if (isDone) {
                        taskToMark.markDone();
                    } else {
                        taskToMark.markUndone();
                    }
                    ui.showTaskMarked(taskToMark, isDone);
                    saveTasks();
                    break;

                case "delete":
                    int deleteIndex = parser.parseTaskIndex(details, tasks.size());
                    Task removedTask = tasks.remove(deleteIndex);
                    ui.showTaskDeleted(removedTask, tasks.size());
                    saveTasks();
                    break;

                case "todo":
                    Task todo = parser.parseTodo(details);
                    addTask(todo);
                    break;

                case "deadline":
                    Task deadline = parser.parseDeadline(details);
                    addTask(deadline);
                    break;

                case "event":
                    Task event = parser.parseEvent(details);
                    addTask(event);
                    break;

                default:
                    throw new ParserException("Sorry! Unknown task type: " + command);
                }
            } catch (ParserException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        saveTasks();
    }

    private void saveTasks() {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            ui.showError("Unable to save tasks: " + e.getMessage());
        }
    }
}