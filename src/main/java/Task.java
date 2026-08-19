public class Task {
    private String name;
    private Boolean completed;

    public Task(String name) {
        this.name = name;
        this.completed = false;
    }

    public String getName() {
        return this.name;
    }

    public Boolean isCompleted() {
        return this.completed;
    }

    public void markDone() {
        this.completed = true;
    }

    public void markUndone() {
        this.completed = false;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.completed ? "X" : " ", this.name);
    }
}