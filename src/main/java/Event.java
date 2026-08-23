import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate start;
    private final LocalDate end;

    public Event(String name, LocalDate start, LocalDate end) {
        super(name);
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s, to: %s)", super.toString(),
                this.start.format(DISPLAY_DATE_FORMAT), this.end.format(DISPLAY_DATE_FORMAT));
    }
}
