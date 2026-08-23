package finn.command;

import finn.task.TaskList;
import finn.ui.Ui;
import finn.storage.Storage;


public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws Exception;

    public boolean isExit() {
        return false;
    }
}