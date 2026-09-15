package finn.command;

import finn.storage.Storage;
import finn.task.TaskList;
import finn.ui.Ui;


/**
 * Represents a command that displays all tasks currently in the task list.
 */
public class ListCommand extends Command {
    /** Creates a command that displays all tasks without changing them. */
    public ListCommand() {
    }

    /**
     * Shows the current task list to the user. Does not modify the task
     * list or storage.
     *
     * @param tasks The task list to display.
     * @param ui The UI used to display the task list.
     * @param storage The current storage (unused).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
