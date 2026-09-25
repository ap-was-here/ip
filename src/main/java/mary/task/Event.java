package mary.task;

import java.time.LocalDateTime;

/** Represents a task with a start time and an end time. */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /** Creates an unfinished event task. */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + formatDateTime(from) + " to: "
                + formatDateTime(to) + ")";
    }

    @Override
    public String toStorageRecord() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    /** Returns the event start date/time. */
    public LocalDateTime getFrom() { return from; }

    /** Returns the event end date/time. */
    public LocalDateTime getTo() { return to; }
}
