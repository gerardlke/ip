import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate deadline;

    public Deadline(String name, LocalDate deadline) {
        super(name);
        this.deadline = deadline;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.deadline.format(DISPLAY_DATE_FORMAT));
    }
}
