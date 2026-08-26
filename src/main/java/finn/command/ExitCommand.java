package finn.command;

import finn.storage.Storage;
import finn.task.TaskList;
import finn.ui.Ui;


/**
 * Represents a command that ends the program.
 */
public class ExitCommand extends Command {

    /**
     * Shows the farewell message. Does not modify the task list or storage.
     *
     * @param tasks The current task list (unused).
     * @param ui The UI used to show the farewell message.
     * @param storage The current storage (unused).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    /**
     * Signals that the program's main loop should terminate.
     *
     * @return {@code true}, always.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}