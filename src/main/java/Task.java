/**
 * Represents one task and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected String type;
    protected String dateDetails;

    /**
     * Creates a new unfinished task.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this("T", description, "");
    }

    /**
     * Creates a task with a type and optional date/time details.
     *
     * @param type the task type: T, D, or E
     * @param description the text describing the task
     * @param dateDetails the optional deadline or event timing text
     */
    public Task(String type, String description, String dateDetails) {
        this.type = type;
        this.description = description;
        this.dateDetails = dateDetails;
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

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the task type marker.
     *
     * @return T, D, or E
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the task's optional date/time details.
     *
     * @return the date/time details, or an empty string for a todo
     */
    public String getDateDetails() {
        return dateDetails;
    }

    /**
     * Returns the task in the format used by MARY's list command.
     *
     * @return the formatted task
     */
    public String getDisplayText() {
        String timing = dateDetails.isEmpty() ? "" : " " + dateDetails;
        return "[" + type + "][" + getStatusIcon() + "] " + description + timing;
    }
}
