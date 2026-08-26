package finn.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Maintains the ordered collection of tasks used by Finn.
 */
public class TaskList implements Iterable<Task> {
    private final List<Task> tasks;

    /** Builds an existing task list. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at a zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Removes and returns the task at a zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns an iterator that cannot modify the task list. */
    @Override
    public Iterator<Task> iterator() {
        return Collections.unmodifiableList(tasks).iterator();
    }

    /**
     * Returns a new TaskList containing only the tasks whose name contains
     * the given keyword, case-insensitively, in the same relative order.
     *
     * @param keyword The keyword to search for within task names.
     * @return A TaskList of matching tasks.
     */
    public TaskList find(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getName().toLowerCase().contains(lowerCaseKeyword)) {
                matches.add(task);
            }
        }
        return new TaskList(matches);
    }
}