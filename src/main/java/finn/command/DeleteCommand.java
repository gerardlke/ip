package finn.command;

import finn.task.Task;
import finn.task.TaskList;
import finn.ui.Ui;
import finn.storage.Storage;
import finn.exception.ParserException;


public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws Exception {
        if (index < 0 || index >= tasks.size()) {
            throw new ParserException("Sorry! Invalid task index!");
        }
        Task removedTask = tasks.remove(index);
        ui.showTaskDeleted(removedTask, tasks.size());
        storage.save(tasks);
    }
}