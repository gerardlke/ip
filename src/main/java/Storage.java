import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Handles loading and saving tasks from/to the storage file.
 */
public class Storage {
    private final Path storagePath;

    public Storage(String filePath) {
        this.storagePath = Path.of(filePath);
    }

    /** 
     * Loads valid saved tasks from the storage file when it exists. 
     * 
     * @return List of loaded tasks.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public List<Task> load() throws IOException {
        List<Task> loadedTasks = new ArrayList<>();
        if (!Files.isRegularFile(storagePath)) {
            return loadedTasks;
        }

        for (String savedTask : Files.readAllLines(storagePath)) {
            String[] parts = savedTask.split(" \\| ", -1);
            Task task = createTask(parts);
            if (task != null) {
                loadedTasks.add(task);
            }
        }
        return loadedTasks;
    }

    /** 
     * Saves the current task list in a structured format to disk. 
     * 
     * @param tasks The TaskList to save.
     * @throws IOException If an I/O error occurs writing to the file.
     */
    public void save(TaskList tasks) throws IOException {
        Path parentDirectory = storagePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }
        List<String> savedTasks = new ArrayList<>();
        for (Task task : tasks) {
            String savedTask = formatTaskForStorage(task);
            if (savedTask != null) {
                savedTasks.add(savedTask);
            }
        }
        Files.write(storagePath, savedTasks);
    }

    /** Creates a task from one valid saved line, or returns null when the line is malformed. */
    private Task createTask(String[] parts) {
        if (parts.length < 3) {
            return null;
        }

        Task task;
        try {
            switch (parts[0]) {
            case "T":
                if (parts.length != 3) {
                    return null;
                }
                task = new Todo(decode(parts[2]));
                break;
            case "D":
                if (parts.length != 4) {
                    return null;
                }
                task = new Deadline(decode(parts[2]), LocalDate.parse(decode(parts[3])));
                break;
            case "E":
                if (parts.length != 5) {
                    return null;
                }
                task = new Event(decode(parts[2]), LocalDate.parse(decode(parts[3])), LocalDate.parse(decode(parts[4])));
                break;
            default:
                return null;
            }
        } catch (IllegalArgumentException | DateTimeException e) {
            return null;
        }

        if (parts[1].equals("1")) {
            task.markDone();
        } else if (!parts[1].equals("0")) {
            return null;
        }
        return task;
    }

    /** Converts a task into one line of the storage format. */
    private String formatTaskForStorage(Task task) {
        String status = task.isCompleted() ? "1" : "0";
        if (task instanceof Todo) {
            return String.format("T | %s | %s", status, encode(task.getName()));
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.format("D | %s | %s | %s", status, encode(task.getName()), encode(deadline.getDeadline().toString()));
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return String.format("E | %s | %s | %s | %s", status, encode(task.getName()),
                    encode(event.getStart().toString()), encode(event.getEnd().toString()));
        }
        return null;
    }

    /** Encodes text as Base64 so it cannot conflict with the storage delimiter. */
    private String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /** Decodes a Base64-encoded storage field as UTF-8 text. */
    private String decode(String encodedField) {
        return new String(Base64.getDecoder().decode(encodedField), StandardCharsets.UTF_8);
    }
}