public class Finn {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

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

    public static void main(String[] args) {
        new Finn("./data/Finn.txt").run();
    }

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