/** Base class for all tasks. */
public class Task {
    protected String description;
    protected boolean isDone;

    /** Creates an unfinished task with the given description. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as unfinished. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the symbol used to display this task's status.
     *
     * @return {@code X} for a completed task, otherwise a space
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns the task with its completion status. */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
