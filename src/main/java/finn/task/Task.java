package finn.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Represents a task with a name and a completion status.
 */
public class Task {
    private String name;
    private Boolean completed;

    /**
     * Creates a new, incomplete task with the given name.
     *
     * @param name The task's description.
     */
    public Task(String name) {
        this.name = name;
        this.completed = false;
    }

    /**
     * Returns this task's name.
     *
     * @return The task's description.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns whether this task has been marked as done.
     *
     * @return {@code true} if the task is completed, {@code false} otherwise.
     */
    public Boolean isCompleted() {
        return this.completed;
    }

    /** Marks this task as done. */
    public void markDone() {
        this.completed = true;
    }

    /** Marks this task as not done. */
    public void markUndone() {
        this.completed = false;
    }

    /**
     * Returns this task's display representation: a status box followed
     * by its name, e.g. {@code "[X] read book"} or {@code "[ ] read book"}.
     *
     * @return The formatted display string.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.completed ? "X" : " ", this.name);
    }
}