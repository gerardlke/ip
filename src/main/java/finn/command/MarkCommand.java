package finn.command;

import finn.task.Task;
import finn.task.TaskList;
import finn.ui.Ui;
import finn.storage.Storage;
import finn.exception.ParserException;


public class MarkCommand extends Command {
    private final int index;
    private final boolean isDone;

    public MarkCommand(int index, boolean isDone) {
        this.index = index;
        this.isDone = isDone;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws Exception {
        if (index < 0 || index >= tasks.size()) {
            throw new ParserException("Sorry! Invalid task index!");
        }
        Task task = tasks.get(index);
        if (isDone) {
            task.markDone();
        } else {
            task.markUndone();
        }
        ui.showTaskMarked(task, isDone);
        storage.save(tasks);
    }
}