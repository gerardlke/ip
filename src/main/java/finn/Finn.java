package finn;

import finn.command.Command;
import finn.storage.Storage;
import finn.parser.Parser;
import finn.task.TaskList;
import finn.ui.Ui;


/**
 * Entry point and main control loop for the Finn task management app.
 */
public class Finn {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Finn instance backed by the given storage file, loading
     * any previously saved tasks. If loading fails, an error is shown and
     * the app starts with an empty task list instead of failing to start.
     *
     * @param filePath The path of the file used to load and save tasks.
     */
    public Finn(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks from file. Starting with an empty list.");
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Runs Finn, using {@code ./data/Finn.txt} (relative to the current
     * working directory) as the storage file.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Finn("./data/Finn.txt").run();
    }

    /**
     * Runs the main command loop: greets the user, then repeatedly reads,
     * parses, and executes commands until an exit command is given or
     * input ends. Errors from parsing or execution are shown to the user
     * without terminating the loop.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                if (fullCommand == null) {
                    break;
                }
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage());
            }
        }
    }
}