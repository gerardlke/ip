package finn.command;

import finn.task.Task;
import finn.task.TaskList;
import finn.ui.Ui;
import finn.storage.Storage;


public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}