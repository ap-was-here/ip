package mary.task;

import java.time.LocalDateTime;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    protected LocalDateTime by;

    /** Creates an unfinished deadline task. */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + formatDateTime(by) + ")";
    }

    @Override
    public String toStorageRecord() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    /** Returns the deadline date/time. */
    public LocalDateTime getBy() { return by; }
}
