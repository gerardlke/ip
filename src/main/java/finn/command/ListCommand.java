package finn.command;

import finn.task.TaskList;
import finn.ui.Ui;
import finn.storage.Storage;


public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}