/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    protected String by;

    /** Creates an unfinished deadline task. */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toStorageRecord() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
